package com.guhecloud.cube.srv.gen.appraisals.server.integration;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import instance.command.ProcessStartedMessageAttrCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalFormIntegrationService {
    private final ProcessInstanceClient processInstanceClient;

    /**
     * 获取指定表单在考核周期内的所有实例
     *
     * @param businessType 业务类型（表单 ID）
     * @param period       考核周期
     *
     * @return 表单实例列表
     */
    public List<ProcessStartedMessageAttrCommand> listFormInstances(String businessType, Period period) {
        // 初始化结果列表
        List<ProcessStartedMessageAttrCommand> result = new ArrayList<>();
        // 构建分页查询参数
        ProcessInstancePageQuery query = new ProcessInstancePageQuery();
        query.setStatus(FlowResult.FINISHED.name());
        // 设置业务类型（表单 ID）
        query.setBusinessType(businessType);
        // 设置提交时间范围（考核周期）
        query.setSubmitTime(period);
        // 设置每页大小（1000 条）
        query.setPageSize(1000);
        // 设置起始页码
        query.setPageNumber(1);
        // 查询第一页数据
        Pagination<ProcessInstanceDTO> pagination = processInstanceClient.allPage(query);
        // 如果第一页没有数据，直接返回空列表
        if (pagination.getRecords().isEmpty()) {
            return result;
        }
        // 收集第一页的实例数据
        collectPage(result, pagination.getRecords());
        // 遍历剩余页码，继续收集数据
        for (int pageNo = 2; pageNo <= pagination.getPages(); pageNo++) {
            // 设置当前页码
            query.setPageNumber(pageNo);
            // 查询当前页并收集数据
            collectPage(result, processInstanceClient.allPage(query).getRecords());
        }
        // 返回完整的实例列表
        return result;
    }

    /**
     * 从表单实例数据中提取指定字段的数值
     *
     * @param formInfo 表单实例信息
     * @param fieldKey 字段标识
     *
     * @return 数值类型的字段值，无法提取则返回 null
     */
    public BigDecimal extractNumericValue(ProcessStartedMessageAttrCommand formInfo, String fieldKey) {
        // 参数校验：formInfo、fieldKey 不能为 null，fieldKey 不能为空字符串，表单数据不能为 null
        if (formInfo == null || fieldKey == null || fieldKey.isBlank() || formInfo.getProcessVariables() == null) {
            return null;
        }
        // 解析表单数据的 JSON，获取指定字段的值
        var data = formInfo.getProcessVariables().getOrDefault("internal_data", "");
        var value = JsonUtils.getNodeStringLoopValue(String.valueOf(data), fieldKey);
        // 如果字段值为 null，返回 null
        if (StringUtils.isBlank(value)) {
            return null;
        }
        // 尝试将值转换为 BigDecimal
        try {
            return new BigDecimal(value.trim());
        } catch (Exception ignored) {
            // 转换失败，返回 null
            log.error("extractNumericValue无法将字段值转换为 BigDecimal：{}", value);
            return null;
        }
    }

    /**
     * 收集分页数据到结果列表
     *
     * @param result  结果列表
     * @param records 当前页的实例记录
     */
    private void collectPage(List<ProcessStartedMessageAttrCommand> result, List<ProcessInstanceDTO> records) {
        // 遍历当前页的所有记录
        for (ProcessInstanceDTO record : records) {
            // 根据流程实例 ID 获取表单详细信息，并添加到结果列表
            result.add(processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId()));
        }
    }
}

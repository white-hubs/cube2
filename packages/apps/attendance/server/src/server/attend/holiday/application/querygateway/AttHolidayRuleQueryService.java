package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.querygateway;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.KeyValueResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttLegalHolidayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRulePageResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 假期规则查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayRuleQueryService extends ServiceImpl<AttHolidayRuleMapper, AttHolidayRulePO> {

    /**
     * 假期规则分页列表
     *
     * @param param 列表条件入参对象
     *
     * @return 分页列表
     */
    public Pagination<AttHolidayRulePageResult> pageHolidayRule(AttHolidayRulePageParam param) {
        Page<AttHolidayRulePO> page = this.page(PagingUtils.convert(param), Wrappers.<AttHolidayRulePO>lambdaQuery()
                                                        .like(param != null && StringUtils.isNotBlank(param.getName()), AttHolidayRulePO::getName, param.getName())
                                                        .orderByDesc(AttHolidayRulePO::getUpdateTime)
                                               );

        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(po -> AttHolidayRulePageResult.builder()
                .id(po.getId())
                .name(po.getName())
                .calculationWay(po.getCalculationWay())
                .notLimit(po.getNotLimit())
                .minAskUnit(po.getMinAskUnit())
                .updateTime(po.getUpdateTime())
                .build()).collect(Collectors.toList())
        );
    }

    /**
     * 查询假期规则下拉列表(假期类型
     *
     * @return 列表
     */
    public List<AttHolidayRuleListResult> listHolidayRuleList() {
        return listHolidayRuleList(new AttHolidayRulePageParam());
    }

    /**
     * 假期规则详情
     *
     * @param id id
     *
     * @return 详情
     */
    public AttHolidayRuleDetailResult detailHolidayRule(Long id) {
        AttHolidayRuleDetailResult result = new AttHolidayRuleDetailResult();
        AttHolidayRulePO po = this.getById(id);
        if (po == null) {
            return result;
        }
        BeanUtils.copyProperties(po, result);
        return result;
    }

    /**
     * 获取法定假期类型下拉
     *
     * @return 列表
     */
    public List<KeyValueResult> legalHolidayList() {
        List<KeyValueResult> resultList = new ArrayList<>();
        for (AttLegalHolidayEnum legalHoliday : AttLegalHolidayEnum.values()) {
            KeyValueResult keyValueResult = new KeyValueResult(legalHoliday.name(), legalHoliday.getValue());
            resultList.add(keyValueResult);
        }
        return resultList;
    }

    /**
     * 查询假期规则下拉列表(假期类型
     *
     * @return 列表
     */
    public List<AttHolidayRuleListResult> listHolidayRuleList(AttHolidayRulePageParam param) {
        List<AttHolidayRuleListResult> resultList = new ArrayList<>();
        List<AttHolidayRulePO> rulePOList = this.list(Wrappers.<AttHolidayRulePO>lambdaQuery()
                                                              .eq(StringUtils.isNotBlank(param.getHolidayType()), AttHolidayRulePO::getHolidayType, param.getHolidayType())
                                                              .eq(param.getNotLimit() !=
                                                                  null, AttHolidayRulePO::getNotLimit, param.getNotLimit())
                                                              .eq(param.getIntoAbsence() !=
                                                                  null, AttHolidayRulePO::getIntoAbsence, param.getIntoAbsence())
                                                              .like(StringUtils.isNotBlank(param.getName()), AttHolidayRulePO::getName, param.getName())
                                                     );
        if (rulePOList != null && !rulePOList.isEmpty()) {
            for (AttHolidayRulePO rulePO : rulePOList) {
                AttHolidayRuleListResult resultTemp = new AttHolidayRuleListResult();
                resultTemp.setId(rulePO.getId());
                resultTemp.setName(rulePO.getName());
                resultTemp.setNotLimit(rulePO.getNotLimit());
                String unit = AttConstants.DAYS;
                if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                    unit = AttConstants.HOUR;
                }
                resultTemp.setUnit(unit);
                resultList.add(resultTemp);
            }
        }
        return resultList;
    }

}

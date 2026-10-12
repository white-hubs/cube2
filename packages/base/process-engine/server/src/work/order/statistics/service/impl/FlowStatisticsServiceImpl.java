package com.guhecloud.cube.base.flow.work.order.statistics.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.flow.work.order.statistics.dao.FlowStatisticsMapper;
import com.guhecloud.cube.base.flow.work.order.statistics.dto.WordOrderStatisticsDTO;
import com.guhecloud.cube.base.flow.work.order.statistics.dto.WorkOrderStatisticsDTO1;
import com.guhecloud.cube.base.flow.work.order.statistics.service.FlowStatisticsService;
import com.guhecloud.cube.base.flow.work.order.statistics.vo.WordOrderStatisticsVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  14:59
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("IdentityBinaryExpression")
public class FlowStatisticsServiceImpl implements FlowStatisticsService {
    private final FlowStatisticsMapper flowStatisticsMapper;
    private final UserClient userClient;

    @Override
    public Pagination<WordOrderStatisticsDTO> getWordOrderStatisticsByUser(WordOrderStatisticsVO parame) {
        this.condition(parame);
        if (ObjectUtils.isNotEmpty(parame.getOrganizationId()) && ObjectUtils.isEmpty(parame.getUserIds())) {
            return Pagination.empty();
        }
        Page<WordOrderStatisticsDTO> page = new Page<>(parame.getPageNumber(), parame.getPageSize());
        page = flowStatisticsMapper.getWordOrderStatisticsByUser(page, parame);
        this.getExtend(page.getRecords());
        return PagingUtils.convert(page);
    }

    @Override
    public void exportWordOrderStatisticsByUser(WordOrderStatisticsVO parame) {
        this.condition(parame);
        Assert.isNotFound(
                ObjectUtils.isNotEmpty(parame.getOrganizationId()) && ObjectUtils.isEmpty(parame.getUserIds()), "暂无数据，无法导出");
        List<WordOrderStatisticsDTO> list = flowStatisticsMapper.exportWordOrderStatisticsByUser(parame);
        Assert.isNotFound(ObjectUtils.isEmpty(list), "暂无数据，无法导出");
        this.getExtend(list);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = null;
        try {
            excelName = URLEncoder.encode("个人工单统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), WordOrderStatisticsDTO.class)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .sheet("个人工单统计")
                    .doWrite(list);
        } catch (Exception e) {
            log.debug("flow:个人工单导出异常：", e);
            throw new ServiceException("下载异常");
        }
    }

    @Override
    public Pagination<WorkOrderStatisticsDTO1> getNumberGroupByBusinessType(WordOrderStatisticsVO query) {
        this.fillBusinessTypeFilter(query);
        Page<WorkOrderStatisticsDTO1> pageRecord = flowStatisticsMapper.selectNumberGroupByBusinessType(PagingUtils.convert(query), query);
        return PagingUtils.convert(pageRecord);
    }

    @Override
    public void getNumberGroupByBusinessTypeExport(WordOrderStatisticsVO query, HttpServletResponse response) {
        this.fillBusinessTypeFilter(query);
        List<WorkOrderStatisticsDTO1> statisticsDTO1List = flowStatisticsMapper.selectNumberGroupByBusinessType(query);
        Assert.isNotFound(CollectionUtils.isEmpty(statisticsDTO1List), "暂无数据，无法导出");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = null;
        try {
            excelName = URLEncoder.encode("工单中心统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), WorkOrderStatisticsDTO1.class)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .sheet("工单中心统计")
                    .doWrite(statisticsDTO1List);
        } catch (Exception e) {
            log.debug("flow:工单统中心计导出异常：", e);
            throw new ServiceException("下载异常");
        }

    }

    @Override
    public List<WorkOrderStatisticsDTO1> getNumberGroupByBusinessTypeTotal(WordOrderStatisticsVO query) {
        this.fillBusinessTypeFilter(query);
        return flowStatisticsMapper.selectNumberGroupByBusinessType(query);
    }

    /**
     * 整理业务类型查询条件：
     * 1. 拆分 businessTypes 字符串到 businessTypeList；
     * 2. 若传入了工单分组 ID，则取该分组（结合应用编码）下的业务类型；
     *    当用户未显式传入业务类型时，使用分组下的业务类型作为筛选条件。
     */
    private void fillBusinessTypeFilter(WordOrderStatisticsVO query) {
        if (StringUtils.isNotBlank(query.getBusinessTypes())) {
            query.setBusinessTypeList(Arrays.asList(query.getBusinessTypes().split(",")));
        }
        if (ObjectUtils.isNotEmpty(query.getFormGroupId())) {
            List<String> groupBusinessTypes = flowStatisticsMapper.selectBusinessTypesByFormGroup(
                    query.getFormGroupId(), query.getApplicationCode());
            if (CollectionUtils.isEmpty(query.getBusinessTypeList())) {
                query.setBusinessTypeList(groupBusinessTypes);
            }
        }
    }

    private void condition(WordOrderStatisticsVO parame) {
        List<String> userIds = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(parame.getOrganizationId())) {
            List<UserDTO> userList = userClient.organizationUsers(parame.getOrganizationId());
            if (ObjectUtils.isNotEmpty(userList)) {
                // 如果用户名包含关键字，则只查询包含关键字的用户
                if (ObjectUtils.isNotEmpty(parame.getKeyword())) {
                    userIds = userList.stream().filter(ex -> ex.getName().contains(parame.getKeyword()))
                            .map(user -> user.getId().toString()).collect(Collectors.toList());
                } else {
                    userIds = userList.stream().map(ex -> ex.getId().toString()).collect(Collectors.toList());
                }
            }
        } else if (ObjectUtils.isNotEmpty(parame.getKeyword())) {
            List<UserDTO> userList = userClient.listUsers(parame.getKeyword());
            if (ObjectUtils.isNotEmpty(userList) && ObjectUtils.isNotEmpty(userList)) {
                userIds = userList.stream().map(ex -> ex.getId().toString()).collect(Collectors.toList());
            }
        }
        parame.setUserIds(userIds);
    }

    private void getExtend(List<WordOrderStatisticsDTO> data) {
        List<UserDTO> userList = userClient.list(new UserQuery());
        Map<String, UserDTO> map = new HashMap<>();
        userList.forEach(item -> {
            map.put(item.getId().toString(), item);
        });
        data.stream().forEach(ex -> {
            UserDTO user = map.get(ex.getUserId());
            if (ObjectUtils.isNotEmpty(user)) {
                ex.setUserName(map.get(ex.getUserId()).getName());
                ex.setOrganizationName(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getName());
            }
            ex.getExtend();
        });
    }
}

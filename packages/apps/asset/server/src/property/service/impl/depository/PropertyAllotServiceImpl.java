package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.AllotExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotUnitExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.OrganizationClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyAllotMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyAllotRelationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyAllot;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.HandleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyAllotService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyHistoryService;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import instance.event.ProcessCompletedMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产派发记录;(outbound)表服务实现类
 *
 * @author : lijie
 * @date : 2023-3-9
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyAllotServiceImpl extends ServiceImpl<PropertyAllotMapper, PropertyAllot> implements PropertyAllotService {
    private final PropertyAllotRelationMapper propertyAllotRelationMapper;
    private final UserClientService userClientService;
    private final FileClient fileClient;
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final PropertyHistoryService propertyHistoryService;
    private final OrganizationClientService organizationClientService;

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    public AllotPageVO queryById(Long id) {
        AllotPageVO vo = this.baseMapper.queryById(id);
        vo.setReceiveUserName(userClientService.getUser(vo.getReceiveUserId()));
        vo.setCreateByName(userClientService.getCreateByName(vo.getCreateBy(), vo.getCreatorId(), vo.getCreatorType()));
        for (PropertyDetailedVO detailed : vo.getPropertyDetailed()) {
            detailed.setAdminName(userClientService.getUser(detailed.getAdminId()));
            detailed.setReceiveDepName(vo.getReceiveDepName());
            detailed.setReceiveUserName(vo.getReceiveUserName());
            if (ObjectUtils.isNotEmpty(detailed.getImgsStr())) {
                List<Long> fileIdds = Arrays.stream(detailed.getImgsStr().split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
                List<FileVO> file = fileClient.getByIds(fileIdds);
                if (ObjectUtils.isNotEmpty(file)) {
                    detailed.setImgs(file);
                }
            }
        }
        return vo;
    }

    /**
     * 通过资产id查询使用情况
     */
    public AllotPageVO queryByPropertyId(Long propertyId) {
        return baseMapper.queryByPropertyId(propertyId);
    }

    /**
     * 分页查询派发、调拨
     *
     * @param param 筛选条件
     *
     * @return 派发、调拨列表
     */
    public Pagination<AllotPageVO> getAllotPage(AllotPageParam param) {
        Page<AllotPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<AllotPageVO> pageData = baseMapper.getAllotPage(page, param);
        for (AllotPageVO ex : pageData.getRecords()) {
            ex.setPropertyDetailed(baseMapper.getAllotDetails(ex.getId(), param));
            ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserId()));
            ex.setCreateByName(userClientService.getCreateByName(ex.getCreateBy(), ex.getCreatorId(), ActorTypeEnum.safeValueOf(ex.getCreateByName())));
        }
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    /**
     * 新增派发、调拨
     *
     * @param param 资产信息
     *
     * @return 派发、调拨结果
     */
    public Long insert(AllotParam param) {
        log.info("新增资产出库操作", param.getType());
        PropertyAllot allot = this.getPropertyAllot(param);
        allot.setReceiveDepName(organizationClientService.getOrganizationsById(allot.getReceiveDepId()));
        allot.setOverDepName(organizationClientService.getOrganizationsById(allot.getOverDepId()));
        Boolean bool = this.baseMapper.insert(allot) > 0;
        if (bool) {
            log.info("插入资产出库记录：{}", allot.getAllotNumbers());
            bool = propertyAllotRelationMapper.insertBatch(allot.getId(), allot.getAllotNumbers(), param.getPropertyIds()) > 0;
            //            log.info("新增资产履历，共{}条数据:", param.getPropertyIds().size());
            //            HandleTypeEnum handleTypeEnum=OperateType.ALLOT_PERSONAL.name().equals(param.getType())?HandleTypeEnum.DISTRIBUTION:HandleTypeEnum.OUTBOUND;
            //            propertyHistoryService.insertByIds(param.getPropertyIds(),handleTypeEnum, OperateType.ALLOT_PERSONAL.name().equals(param.getType()) ? "领用了" : "调拨了");
        }
        return allot.getId();
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    public Boolean deleteById(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param) {
        Map<String, Map<String, String>> title = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();
        String[] arry = param.getIds().split(",");
        String templateName = "固定资产调拨单.xlsx";
        if (OperateType.ALLOT_UNIT.name().equals(param.getType())) {
            for (String id : arry) {
                PropertyAllot allot = baseMapper.selectById(Long.parseLong(id));
                Map<String, String> map = new HashMap<>();
                List<AllotUnitExcelParam> detailedExcel = baseMapper.getAllotUnitExcel(id);
                map.put("allotNumbers", allot.getAllotNumbers());
                String date = DateUtils.formatDate(allot.getReceiveTime());
                map.put("date", date);
                map.put("receiveDepName", allot.getReceiveDepName());
                map.put("overDepName", allot.getOverDepName());
                title.put(id, map);
                data.put(id, detailedExcel);
            }
        } else {
            templateName = "固定资产领用（出库）登记.xlsx";
            for (String id : arry) {
                PropertyAllot allot = baseMapper.selectById(Long.parseLong(id));
                Map<String, String> map = new HashMap<>();
                List<AllotExcelParam> detailedExcel = baseMapper.getExcel(id);
                for (AllotExcelParam ex : detailedExcel) {
                    ex.setReceiveUserName(userClientService.getUser(ex.getReceiveUserName()));
                }
                map.put("allotNumbers", allot.getAllotNumbers());
                title.put(id, map);
                data.put(id, detailedExcel);
            }
        }
        try {
            log.info("----------------------开始导出固定资产领用登记----------------------");
            ExcelUtile.uploadExcelList(response, data, title, templateName);
        } catch (Exception exception) {
            log.error("固定资产领用（出库）登记导出--{}", exception.getLocalizedMessage());
        }
    }

    @Override
    public Boolean update(WorkflowRelevance param) {
        PropertyAllot allot = baseMapper.selectById(param.getPropertyId());
        allot.setProcessInstanceId(param.getProcessInstanceId());
        return baseMapper.updateById(allot) > 0;
    }

    @Override
    public Boolean submit(ProcessCompletedMessage message) {
        PropertyAllot allot = baseMapper.selectOne(new LambdaQueryWrapper<PropertyAllot>()
                                                           .eq(PropertyAllot::getProcessInstanceId, message.getProcessInstanceId()));
        allot.setStatus(message.getResult().name());
        baseMapper.updateById(allot);
        // 修改资产状态、修改资产使用使用人、使用部门等信息
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            String status = OperateType.ALLOT_PERSONAL.name().equals(allot.getType()) ? PropertyStatusEnum.LEAVE_UNUSED.name()
                                                                                      : PropertyStatusEnum.MAKE_USE_OF.name();
            List<Long> propertyIds = propertyAllotRelationMapper.getPropertyIds(allot.getAllotNumbers());
            propertyDetailedMapper.updateByAllotNumbers(allot, propertyIds, PropertyStatusEnum.MAKE_USE_OF.name(), status);
            log.info("新增派发、调拨资产履历");
            HandleTypeEnum handleTypeEnum =
                    OperateType.ALLOT_PERSONAL.name().equals(allot.getType()) ? HandleTypeEnum.DISTRIBUTION : HandleTypeEnum.OUTBOUND;
            propertyHistoryService.insertByIds(baseMapper.getPropertyDetailedIds(allot.getAllotNumbers()), handleTypeEnum,
                                               OperateType.ALLOT_PERSONAL.name().equals(allot.getType()) ? "领用了" : "调拨了");
        }
        return true;
    }

    public PropertyAllot getPropertyAllot(AllotParam param) {
        PropertyAllot allot = new PropertyAllot();
        allot.setReceiveTime(param.getReceiveTime());
        allot.setType(param.getType());
        allot.setAreaId(param.getAreaId());
        allot.setRemarks(param.getRemarks());
        allot.setPlace(param.getPlace());
        if (ObjectUtils.isNotEmpty(param.getOverDepId()) && !param.getOverDepId().isEmpty()) {
            Object depId = param.getOverDepId().getFirst();
            allot.setOverDepId(Long.parseLong(depId.toString()));
        }
        if (ObjectUtils.isNotEmpty(param.getReceiveDepId()) && !param.getReceiveDepId().isEmpty()) {
            Object depId = param.getReceiveDepId().getFirst();
            allot.setReceiveDepId(Long.parseLong(depId.toString()));
        }
        if (ObjectUtils.isNotEmpty(param.getReceiveUserId()) && !param.getReceiveUserId().isEmpty()) {
            Object userId = param.getReceiveUserId().getFirst();
            allot.setReceiveUserId(Long.parseLong(userId.toString()));
        }
        allot.setStatus(FlowResult.UNDER.name());
        // 调拨需要移交部门
        if (OperateType.ALLOT_UNIT.name().equals(param.getType()) && ObjectUtils.isEmpty(param.getOverDepId())) {
            throw new RuntimeException("移交部门不能为空");
        }
        allot.setAllotNumbers(OperateType.getNumbers(
                OperateType.ALLOT_UNIT.name().equals(param.getType()) ? OperateType.ALLOT_UNIT : OperateType.ALLOT_PERSONAL));
        return allot;
    }
}
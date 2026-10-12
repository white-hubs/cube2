package com.guhecloud.cube.srv.gen.work.permit.server.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.work.permit.server.convert.WorkPermitTypeConfigConvert;
import com.guhecloud.cube.srv.gen.work.permit.server.dao.WorkPermitAssociatedItemMapper;
import com.guhecloud.cube.srv.gen.work.permit.server.dao.WorkPermitTypeConfigMapper;
import com.guhecloud.cube.srv.gen.work.permit.server.entity.WorkPermitAssociatedItem;
import com.guhecloud.cube.srv.gen.work.permit.server.entity.WorkPermitTypeConfig;
import com.guhecloud.cube.srv.gen.work.permit.server.enumeration.WorkPermitAssociatedItemTypeEnum;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitAssociatedItemParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitAssociatedItemQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.service.WorkPermitAssociatedItemService;
import com.guhecloud.cube.srv.gen.work.permit.server.service.WorkPermitTypeConfigService;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitAssociatedItemPageVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigDetailVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigExportVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigNameVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 作业类型配置服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPermitTypeConfigServiceImpl extends ServiceImpl<WorkPermitTypeConfigMapper, WorkPermitTypeConfig> implements WorkPermitTypeConfigService {

    private final WorkPermitAssociatedItemMapper itemMapper;
    private final WorkPermitAssociatedItemService itemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WorkPermitTypeConfigParam param) {
        WorkPermitTypeConfig config = WorkPermitTypeConfigConvert.toEntity(param);
        this.baseMapper.insert(config);
        saveItems(config.getId(), param);
        return config.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, WorkPermitTypeConfigParam param) {
        WorkPermitTypeConfig config = this.baseMapper.selectById(id);
        Assert.isNotFound(config, "未查询到作业类型配置");
        WorkPermitTypeConfigConvert.updateEntity(config, param);
        this.baseMapper.updateById(config);
        removeItems(id);
        saveItems(id, param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WorkPermitTypeConfig config = this.baseMapper.selectById(id);
        Assert.isNotFound(config, "未查询到作业类型配置");
        removeItems(id);
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public WorkPermitTypeConfigDetailVO getById(Long id) {
        WorkPermitTypeConfig config = this.baseMapper.selectById(id);
        Assert.isNotFound(config, "未查询到作业类型配置");
        WorkPermitTypeConfigDetailVO detail = WorkPermitTypeConfigConvert.toDetailVO(config);
        List<WorkPermitAssociatedItem> items = itemMapper.selectList(Wrappers.<WorkPermitAssociatedItem>lambdaQuery()
                                                                             .eq(WorkPermitAssociatedItem::getWorkPermitTypeId, id)
                                                                             .orderByAsc(WorkPermitAssociatedItem::getSortNo)
                                                                             .orderByAsc(WorkPermitAssociatedItem::getId));
        Map<String, List<WorkPermitAssociatedItem>> itemMap = items.stream()
                .collect(Collectors.groupingBy(WorkPermitAssociatedItem::getItemType));
        detail.setRiskCheckItems(WorkPermitTypeConfigConvert.toItemVOList(itemMap.get(WorkPermitAssociatedItemTypeEnum.RISK_CHECK.getCode())));
        detail.setAcceptanceItems(WorkPermitTypeConfigConvert.toItemVOList(itemMap.get(WorkPermitAssociatedItemTypeEnum.ACCEPTANCE.getCode())));
        detail.setRiskCheckCount(detail.getRiskCheckItems().size());
        detail.setAcceptanceCount(detail.getAcceptanceItems().size());
        return detail;
    }

    @Override
    public Pagination<WorkPermitTypeConfigPageVO> getPage(WorkPermitTypeConfigQueryParam param) {
        Page<WorkPermitTypeConfigPageVO> page = this.baseMapper.selectConfigPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page, page.getRecords());
    }

    @Override
    public List<String> listLevels(WorkPermitTypeConfigQueryParam param) {
        return this.baseMapper.selectLevels(param);
    }

    @Override
    public List<WorkPermitTypeConfigNameVO> listNames(WorkPermitTypeConfigQueryParam param) {
        return this.baseMapper.selectNames(param);
    }

    @Override
    public Pagination<WorkPermitAssociatedItemPageVO> getRiskCheckItemPage(WorkPermitAssociatedItemQueryParam param) {
        return getAssociatedItemPage(param, WorkPermitAssociatedItemTypeEnum.RISK_CHECK);
    }

    @Override
    public List<Long> listRiskCheckItemIds(WorkPermitAssociatedItemQueryParam param) {
        return listAssociatedItemIds(param, WorkPermitAssociatedItemTypeEnum.RISK_CHECK);
    }

    @Override
    public Pagination<WorkPermitAssociatedItemPageVO> getAcceptanceItemPage(WorkPermitAssociatedItemQueryParam param) {
        return getAssociatedItemPage(param, WorkPermitAssociatedItemTypeEnum.ACCEPTANCE);
    }

    @Override
    public List<Long> listAcceptanceItemIds(WorkPermitAssociatedItemQueryParam param) {
        return listAssociatedItemIds(param, WorkPermitAssociatedItemTypeEnum.ACCEPTANCE);
    }

    @Override
    public void export(WorkPermitTypeConfigQueryParam param, HttpServletResponse response) {
        List<WorkPermitTypeConfigExportVO> rows = WorkPermitTypeConfigConvert.toExportVOList(this.baseMapper.selectConfigList(param));
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = URLEncoder.encode("作业类型配置", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), WorkPermitTypeConfigExportVO.class)
                    .sheet("作业类型配置")
                    .doWrite(rows);
        } catch (Exception e) {
            log.error("作业类型配置导出失败", e);
            throw new ServiceException("导出数据失败");
        }
    }

    private Pagination<WorkPermitAssociatedItemPageVO> getAssociatedItemPage(WorkPermitAssociatedItemQueryParam param,
                                                                             WorkPermitAssociatedItemTypeEnum itemType) {
        Page<WorkPermitAssociatedItemPageVO> page = this.baseMapper.selectAssociatedItemPage(PagingUtils.convert(param), param, itemType.getCode());
        return PagingUtils.convert(page, page.getRecords());
    }

    private List<Long> listAssociatedItemIds(WorkPermitAssociatedItemQueryParam param, WorkPermitAssociatedItemTypeEnum itemType) {
        return this.baseMapper.selectAssociatedItemIds(param, itemType.getCode());
    }

    private void saveItems(Long workPermitTypeId, WorkPermitTypeConfigParam param) {
        saveItems(workPermitTypeId, WorkPermitAssociatedItemTypeEnum.RISK_CHECK, param.getRiskCheckItems());
        saveItems(workPermitTypeId, WorkPermitAssociatedItemTypeEnum.ACCEPTANCE, param.getAcceptanceItems());
    }

    private void saveItems(Long workPermitTypeId, WorkPermitAssociatedItemTypeEnum itemType, List<WorkPermitAssociatedItemParam> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        int index = 0;
        for (WorkPermitAssociatedItemParam itemParam : items) {
            WorkPermitAssociatedItem item = WorkPermitTypeConfigConvert.toItemEntity(workPermitTypeId, itemType, itemParam, index);
            itemMapper.insert(item);
            index++;
        }
    }

    private void removeItems(Long workPermitTypeId) {
        List<WorkPermitAssociatedItem> items = itemMapper.selectList(Wrappers.<WorkPermitAssociatedItem>lambdaQuery()
                                                                             .eq(WorkPermitAssociatedItem::getWorkPermitTypeId, workPermitTypeId));
        for (WorkPermitAssociatedItem item : items) {
            DatabaseContextHolder.softDelete(itemService, item.getId());
        }
    }
}

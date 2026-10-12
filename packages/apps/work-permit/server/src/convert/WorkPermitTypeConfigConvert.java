package com.guhecloud.cube.srv.gen.work.permit.server.convert;

import com.guhecloud.cube.srv.gen.work.permit.server.entity.WorkPermitAssociatedItem;
import com.guhecloud.cube.srv.gen.work.permit.server.entity.WorkPermitTypeConfig;
import com.guhecloud.cube.srv.gen.work.permit.server.enumeration.WorkPermitAssociatedItemTypeEnum;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitAssociatedItemParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigParam;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitAssociatedItemVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigDetailVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigExportVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigPageVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业类型配置转换器。
 */
public class WorkPermitTypeConfigConvert {

    private WorkPermitTypeConfigConvert() {}

    public static WorkPermitTypeConfig toEntity(WorkPermitTypeConfigParam param) {
        WorkPermitTypeConfig config = new WorkPermitTypeConfig();
        updateEntity(config, param);
        return config;
    }

    public static void updateEntity(WorkPermitTypeConfig config, WorkPermitTypeConfigParam param) {
        config.setTypeName(param.getTypeName());
        config.setWorkPermitLevel(param.getWorkPermitLevel());
        config.setLevelDefinition(param.getLevelDefinition());
        config.setPermitValidHours(param.getPermitValidHours());
    }

    public static WorkPermitTypeConfigDetailVO toDetailVO(WorkPermitTypeConfig config) {
        WorkPermitTypeConfigDetailVO detail = new WorkPermitTypeConfigDetailVO();
        detail.setId(config.getId());
        detail.setTypeName(config.getTypeName());
        detail.setWorkPermitLevel(config.getWorkPermitLevel());
        detail.setLevelDefinition(config.getLevelDefinition());
        detail.setPermitValidHours(config.getPermitValidHours());
        return detail;
    }

    public static List<WorkPermitTypeConfigExportVO> toExportVOList(List<WorkPermitTypeConfigPageVO> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return new ArrayList<>();
        }
        List<WorkPermitTypeConfigExportVO> result = new ArrayList<>();
        for (WorkPermitTypeConfigPageVO row : rows) {
            WorkPermitTypeConfigExportVO export = new WorkPermitTypeConfigExportVO();
            export.setTypeName(row.getTypeName());
            export.setWorkPermitLevel(row.getWorkPermitLevel());
            export.setLevelDefinition(row.getLevelDefinition());
            export.setRiskCheckCount(row.getRiskCheckCount());
            export.setAcceptanceCount(row.getAcceptanceCount());
            export.setPermitValidHours(row.getPermitValidHours());
            result.add(export);
        }
        return result;
    }

    public static WorkPermitAssociatedItem toItemEntity(Long workPermitTypeId, WorkPermitAssociatedItemTypeEnum itemType,
                                                 WorkPermitAssociatedItemParam param, int defaultSortNo) {
        WorkPermitAssociatedItem item = new WorkPermitAssociatedItem();
        item.setWorkPermitTypeId(workPermitTypeId);
        item.setItemType(itemType.getCode());
        item.setItemName(param.getItemName());
        item.setSortNo(param.getSortNo() == null ? defaultSortNo : param.getSortNo());
        return item;
    }

    public static List<WorkPermitAssociatedItemVO> toItemVOList(List<WorkPermitAssociatedItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<WorkPermitAssociatedItemVO> result = new ArrayList<>();
        for (WorkPermitAssociatedItem item : items) {
            WorkPermitAssociatedItemVO vo = new WorkPermitAssociatedItemVO();
            vo.setId(item.getId());
            vo.setItemName(item.getItemName());
            vo.setSortNo(item.getSortNo());
            result.add(vo);
        }
        return result;
    }
}

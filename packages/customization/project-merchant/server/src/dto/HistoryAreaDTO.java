package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.HistoryArea;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 空间、区域历史面积
 * </p>
 *
 * @author bq
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HistoryAreaDTO extends ConvertionDTO<HistoryAreaDTO, HistoryArea> {

    private static final long serialVersionUID = 1L;

    public HistoryAreaDTO() {
    }

    public HistoryAreaDTO(HistoryArea historyArea) {
        super();
        map(historyArea);
    }

    public HistoryAreaDTO(List<HistoryArea> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 对象类型(区域、空间)
     */
    private String objectType;

    /**
     * 对象ID
     */
    private String objectId;

    /**
     * 建筑面积
     */
    private Float buildingArea;

    /**
     * 占地面积
     */
    private Float coverArea;

    /**
     * 租赁面积
     */
    private Float leaseArea;

    /**
     * 租赁实际面积
     */
    private Float leaseRealArea;

    /**
     * 租赁公摊面积
     */
    private Float leasePublicArea;

    /**
     * 消防面积
     */
    private Float fireArea;

    /**
     * 清洁面积
     */
    private Float cleanArea;

    /**
     * 车位面积
     */
    private Float parkingArea;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private Object object;

}

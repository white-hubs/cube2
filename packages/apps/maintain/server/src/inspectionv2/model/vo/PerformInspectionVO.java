package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.KeyValueDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 执行巡检任务详情
 * @date 2023/4/21 11:46
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class PerformInspectionVO {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 巡检记录id
     */
    private Long recordId;
    /**
     * 巡检对象
     */
    private String targetName;
    /**
     * 巡检对象id
     */
    private Long targetId;
    /**
     * 对象分类
     */
    private String targetCategory;
    /**
     * 对象类型
     */
    private String targetType;
    /**
     *
     */
    private String targetTypeName;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 所属区域
     */
    private String areaName;
    /**
     * 具体地址
     */
    private String address;
    /**
     * 巡检标准
     */
    private List<InspectionStandardVO> standardVOS;
    /**
     * 关联对象集合
     */
    private List<KeyValueDTO> childTargetNameList;
}

package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
import lombok.Data;

import java.util.List;

@Data
public class InspectTargetInfoDTO {

    /**
     * 巡检对象类型：device设备 stall挡位 等
     */
    private String targetCategory;

    /**
     * 巡检对象具体类型
     */
    private String targetType;

    /**
     * 巡检对象具体类型名
     */
    private String targetTypeName;

    /**
     * 巡检对象ID
     */
    private Long targetId;

    /**
     * 巡检对象编码
     */
    private String targetCode;

    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 区域ID
     */
    private String areaId;

    /**
     * 区域名
     */
    private String areaName;

    /**
     * 具体地址
     */
    private String address;
    /**
     * 所属部门
     */
    private String depName;

    /**
     * 标准ID
     */
    private List<Long> standardIds;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地址
     */
    private String location;

    private List<InspectionStandardVO> standardDTOList;

    /**
     * 主对象id(有值表示是被关联的对象)
     */
    private Long targetParentId;

}

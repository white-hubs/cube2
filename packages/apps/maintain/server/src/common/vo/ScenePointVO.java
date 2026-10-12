package com.guhecloud.cube.srv.gen.maintain.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wql
 * @describe 现场点
 * @date 2023/6/5 18:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenePointVO {
    /**
     * 现场点id
     */
    private Long id;
    /**
     * 编码
     */
    private String codeNum;
    /**
     * 名称
     */
    private String name;
    /**
     * 现场点类型名称
     */
    private String typeName;
    /**
     * 现场点id
     */
    private Long typeId;
    /**
     * 管理部门名称
     */
    private String depName;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 所属区域id
     */
    private String areaId;
    /**
     * 所属区域名称
     */
    private String areaName;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 定位
     */
    private String location;
    private String latAndLon;

    /**
     * 关联设备基础信息列表
     */
    private List<ScenePointDeviceBaseVO> deviceList;

}

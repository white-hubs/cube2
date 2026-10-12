package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wql
 * @describe 现场点新增参数
 * @date 2023/6/5 17:11
 */
@Data
public class InspectScenePointParam {
    /**
     * 现场点id
     */
    private Long id;
    /**
     * 现场点编码
     */
    @NotNull(message = "现场点编码不能为空")
    private String codeNum;
    /**
     * 名称
     */
    @NotNull(message = "现场点名称不能为空")
    private String name;
    /**
     * 现场点类型id
     */
    @NotNull(message = "现场点类型不能为空")
    private Long typeId;
    /**
     * 现场点类型名称
     */
    private String typeName;
    /**
     * 管理部门id
     */
    @NotNull(message = "管理部门名称不能为空")
    private Long depId;
    /**
     * 管理部门名称
     */
    private String depName;
    /**
     * 所属区域id
     */
    @NotNull(message = "所属区域不能为空")
    private String areaId;
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
     * 关联设备ID列表（编辑时传 null 表示不修改现有关联）
     */
    private List<Long> deviceIdList;
}

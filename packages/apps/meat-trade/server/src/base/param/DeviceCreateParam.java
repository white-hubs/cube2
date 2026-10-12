package com.guhecloud.cube.biz.meattrade.server.base.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备管理新增
 *
 * @author lzy
 * @date 2021/9/9
 */
@Data
public class DeviceCreateParam {

    /**
     * 设备名称
     */
    @NotEmpty(message = "设备名称不能为空")
    @Size(max = 20, message = "设备名称不能大于20")
    private String name;

    /**
     * 设备编号
     */
    @Size(max = 20, message = "设备编号不能大于100")
    private String code;

    /**
     * 设备类型
     */
    @NotEmpty(message = "设备类型不能为空")
    @Size(max = 20, message = "设备类型不能大于20")
    private String type;

    /**
     * 设备规格
     */
    @Size(max = 20, message = "设备规格不能大于20")
    private String specification;

    /**
     * 生产厂家
     */
    @Size(max = 20, message = "生产厂家不能大于20")
    private String manufacturer;

    /**
     * 型号
     */
    @Size(max = 20, message = "设备型号不能大于20")
    private String model;

    /**
     * token信息
     */
    private String token;

    /**
     * Mac地址
     */
    @Size(max = 20, message = "MAC地址不能大于20")
    private String mac;

    /**
     * mac状态
     */
    private Boolean macStatus;

    /**
     * 区域主键
     */
    @NotNull(message = "所在区域不能为空")
    private List<String> areaIds;

    /**
     * 安装时间
     */
    private LocalDateTime installTime;

    /**
     * 安装地址
     */
    @Size(message = "安装地址不能为大于20")
    private String installAddress;

    /**
     * 部门主键
     */
    @NotNull(message = "部门不能为空")
    private List<String> deptIds;

    /**
     * 联系人
     */
    @Size(message = "部门联系人不能大于20")
    private String deptLiaison;

    /**
     * 联系电话
     */
    @Size(message = "联系电话不能大于20")
    private String liaisonPhone;

    /**
     * 联系地址
     */
    @Size(message = "联系地址不能大于20")
    private String liaisonAddress;

    /**
     * 关联设备
     */
    private List<String> cameraIds;

    /**
     * 是否在线
     */
    private Boolean status;
}

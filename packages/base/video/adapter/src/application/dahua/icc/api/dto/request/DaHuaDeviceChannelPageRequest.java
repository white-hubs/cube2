package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.EVP_API_DEVICE_CHANNEL_PAGE;

/**
 * 分页查询设备信息请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaDeviceChannelPageRequest extends AbstractDaHuaRequest {

    /**
     * 当前页, 默认1
     */
    private Integer pageNum;

    /**
     * 每页记录数, 默认10
     */
    private Integer pageSize;

    /**
     * 排序属性 支持排序属性：id、updateTime、ownerCode、deviceCode、channelCode、
     * unitType、channelType、cameraType、channelSn
     */
    private String sort;

    /**
     * 排序方式 ：ASC-升序， DESC-降序(默认)
     */
    private String sortType;

    /**
     * 设备所属组织编码
     */
    private String ownerCode;

    /**
     * 设备编码列表 最大支持500
     */
    private List<String> deviceCodeList;

    /**
     * 通道编码列表
     */
    private List<String> channelCodeList;

    /**
     * 设备大类
     */
    private String deviceCategory;

    /**
     * 设备小类 此字段生效，则deviceCategory必填
     */
    private String deviceType;

    /**
     * 单元类型,多个
     */
    private List<String> unitTypeList;

    /**
     * 通道类型列表
     */
    private List<String> channelTypeList;

    /**
     * 在线状态
     */
    private Integer isOnline;

    /**
     * 是否已经接入
     */
    private Integer access;

    /**
     * 是否是虚拟通道
     */
    private Integer isVirtual;

    /**
     * 状态
     */
    private Integer stat;

    /**
     * 是否包含子节点,默认true,展示下级通道
     */
    private Integer includeSubOwnerCodeFlag;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(EVP_API_DEVICE_CHANNEL_PAGE).build();
    }
}

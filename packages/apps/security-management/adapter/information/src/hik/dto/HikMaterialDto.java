package com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto;

import lombok.Data;

/**
 * 节目列表
 */
@Data
public class HikMaterialDto {

    /**
     * 素材id
     */
    private Integer materialId;
    /**
     * 素材名称
     */
    private String materialName;
    /**
     * 素材别名
     */
    private String materialAlias;
    /**
     * 素材类型描述
     */
    private String materialTypeName;
    /**
     * 所属租住编码
     */
    private String orgIndexCode;
    /**
     * 所属组织名称
     */
    private String orgName;
    /**
     * 静态素材后缀名
     */
    private String staticMaterialFormat;
    /**
     * 静态素材类型编码：picture-图片，flash-Flash,audio-音频,video-视频，document-文本，pdf-PDF，web-网页，doc-Word文档，excel-Excel文档，ppt-PPT文档
     */
    private String staticMaterialType;
    /**
     * 静态素材类型描述
     */
    private String staticMaterialTypeName;
    /**
     * 静态素材文件存储地址
     */
    private String staticFileUrl;
    /**
     * 动态素材类型编码
     */
    private String dynamicMaterialType;
    /**
     * 动态素材类型值名称
     */
    private String dynamicMaterialTypeName;
    /**
     * 动态网页URL地址
     */
    private String dynamicWebUrl;
    /**
     * 视频直播素材取流类型编码：streamMedia-过流媒体，normalIPC-直连
     */
    private String dynamicStreamDestionType;
    /**
     * 视频直播素材取流类型描述
     */
    private String dynamicStreamDestionTypeName;
    /**
     * 视频直播素材取流地址
     */
    private String dynamicStreamMediaUrl;
    /**
     * IPC信息对象
     */
    private Object ipcInfo;
    /**
     * 数据源素材URL地址
     */
    private String address;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
}

package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request;

import lombok.Data;

/**
 * author: ht
 * create: 2025-05-22 16:35
 */
@Data
public class DoorEventUploadRequest {
    /**
     * 记录唯一标识
     * 示例值：REC_20231125_001
     */
    private String recordId;
    /**
     * 关联人员信息
     * @see Person
     */
    private Person person;
    /**
     * 备注信息（最多255字符）
     * 示例值："访客临时通行"
     */
    private String remark;
    /**
     * 关联设备信息
     * @see Device
     */
    private Device device;
    /**
     * 凭证类型
     * 1-IC卡
     * 2-身份证
     * 3-指纹
     * 4-人脸
     * 5-二维码
     * （与credentialType同步）
     */
    private Integer cardType;
    /**
     * 凭证编号（加密存储）
     * 示例值："862481***3489"
     */
    private String cardNo;
    /**
     * 事件触发时间
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    private String crossTime;
    /**
     * 抓拍照片存储路径
     * 示例："/photos/20231125/001.jpg"
     */
    private String pictureFile;
    /**
     * 记录类型枚举
     */
    private Integer recordType;
    /**
     * 门禁事件类型枚举
     */
    private Integer eventType;

}

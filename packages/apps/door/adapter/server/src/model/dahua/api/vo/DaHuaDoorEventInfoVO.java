package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import lombok.Data;

/**
 * 数据列表
 */
@Data
public class DaHuaDoorEventInfoVO {
    /**
     * 卡号
     */
    private String cardNumber;
    /**
     * 卡状态，-1-空白卡，0-正常卡，1-挂失卡，2-注销卡
     */
    private Integer cardStatus;
    /**
     * 卡类型，0-IC卡, 1-有源RFID, 2-CPU卡
     */
    private Integer cardType;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 设备编码
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 进出门类型，1-进门, 2出门, 3-进/出门
     */
    private Integer enterOrExit;
    /**
     * 记录id
     */
    private Long id;
    /**
     * 人员类型，1-内部人员, 2-访客：内部人员是在人员管理中添加的人员，访客是在访客子系统中录入的访客
     */
    private Integer imageType;
    /**
     * 开门结果，0-失败，1-成功
     */
    private Integer openResult;
    /**
     * 开门类型，详见 开门类型 字典
     */
    private Integer openType;
    /**
     * 证件号码
     */
    private String paperNumber;
    /**
     * 人员编号
     */
    private String personCode;
    /**
     * 人员ID
     */
    private Long personId;
    /**
     * 人员姓名
     */
    private String personName;
    /**
     * 抓图，相对路径，V1.1.4新增，推荐使用，拼接方式可查看：FAQ-常见问题-图片服务-OSS方式
     */
    private String recordImageUrl;
    /**
     * 抓图，绝对路径，兼容历史版本，不推荐使用
     */
    private String recordImage;
    /**
     * 开门失败原因
     */
    private String remark;
    /**
     * 刷卡时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String swingTime;
    /**
     * 入库时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String createTime;
    /**
     * 口罩状态(3-带口罩,2—没带口罩,1-未识别)
     */
    private Integer maskState;
    /**
     * 是否超温
     */
    private Boolean overTemp;
    /**
     * 体温
     */
    private Float curTemp;
}

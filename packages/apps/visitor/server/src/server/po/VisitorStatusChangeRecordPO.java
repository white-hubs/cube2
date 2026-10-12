package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预约状态变更记录
 */
@Data
@TableName("visitor_status_change_record")
@EqualsAndHashCode(callSuper = true)
public class VisitorStatusChangeRecordPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = 3910140153546496455L;
    private Long id;
    /**
     * 预约id(访客id)
     */
    private Long reservationId;
    /**
     * 访客姓名
     */
    private String name;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 访客状态 0:默认,1:预约,2:在访,3：离访,4:取消预约
     */
    private String status;
    /**
     * 被访人ID
     */
    private String isvId;
    /**
     * 电话
     */
    private String phone;
    /**
     * 卡号
     */
    private String cardNumber;
    /**
     * 预约到达时间
     */
    private String appointmentArrayTime;
    /**
     * 预约离开时间
     */
    private String appointmentLeaveTime;
    /**
     * 离开时间
     */
    private String leaveTime;
    /**
     * 到达时间
     */
    private String arrayTime;
    /**
     * 上传的访客照片地址
     */
    private String visitorPic;
    /**
     * 抓拍的访客照片地址
     */
    private String capturePic;
    /**
     * 人脸图片地址，visitorPic和capturePic哪个参数有值，facepath就取值哪个，
     */
    private String facePath;
    /**
     * 认证号码
     */
    private String certificateNumber;
    /**
     * 认证类型
     */
    private String certificateType;
    /**
     * 车牌号码
     */
    private String carNumber;
    /**
     * 工作地点
     */
    private String workPlace;
    /**
     * 来访原因
     */
    private String reason;
    /**
     * 域ID
     */
    private String domainId;
    /**
     * 通道编码
     */
    private String channelCode;
}

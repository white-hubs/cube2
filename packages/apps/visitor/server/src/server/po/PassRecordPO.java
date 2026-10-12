package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通行记录
 */
@Data
@TableName("pass_record")
@EqualsAndHashCode(callSuper = true)
public class PassRecordPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = 5496753428021338156L;
    private Long id;
    /**
     * 访客姓名
     */
    private String visitorName;
    private String personType;
    /**
     * 访客状态：0-来访变更为离访或者其他状态，1-预约变更为来访状态，2-离访状态
     */
    private Integer enterOrExist;
    /**
     * 访客图片
     */
    private String visitorPic;
    /**
     * 预约id
     */
    private Long visitorId;

    private String channelCode;
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 通行时间(yyyy-mm-dd HH:mm:ss)
     */
    private String swingTime;
    /**
     * 开门类型
     */
    private String openType;
}

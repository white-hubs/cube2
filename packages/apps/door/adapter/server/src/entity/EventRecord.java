package com.guhecloud.cube.srv.gen.adapter.access.control.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 门禁事件记录实体类
 * 对应表：event_record
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("event_record")
public class EventRecord extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 事件名称
     * 示例：员工通行、访客登记
     */
    private String eventName;

    /**
     * 事件唯一标识
     * 格式示例：EVT_20231125_001
     */
    private String eventId;

    /**
     * 事件发生时间
     * 精确到秒
     */
    private LocalDateTime eventTime;

    /**
     * 关联人员唯一标识
     */
    private String personId;

    /**
     * 关联人员姓名
     */
    private String personName;

    /**
     * 门禁设备编码
     * 关联设备管理系统
     */
    private String doorIndexCode;

    /**
     * 门禁点名称
     */
    private String doorName;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 进出方向
     */
    private String inAndOutType;

    /**
     * 事件处理结果
     * 示例：SUCCESS-放行, REJECT-拒绝
     */
    private String eventResult;

    /**
     * 验证方式
     */
    private String eventWay;

    /**
     * 刷卡卡号
     * 加密存储
     */
    private String cardNo;

}

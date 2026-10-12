package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.vo;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration.AttributeControlExecuteResultEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 事件联动物联设备执行记录
 *
 * @author lanchun
 * @date 2022/6/29 19:33
 */
@Data
public class EventIotDeviceProRecordVO {

    private Long id;

    /**
     * 事件关联预案主键
     */
    private Long eventLinkageId;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 执行方式
     */
    private ExecuteModeEnum executeMode;

    /**
     * 执行状态
     */
    private AttributeControlExecuteResultEnum executeStatus;

    /**
     * 执行描述
     */
    private String executeStatusDescribe;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 属性值
     */
    private String propValue;

    /**
     * 业务平台翻译后的值
     */
    private String translatedValue;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime operatorTime;
}

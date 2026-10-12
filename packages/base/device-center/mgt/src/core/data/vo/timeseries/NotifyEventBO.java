package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mioopp
 * @create 2020-07-14 16:34
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NotifyEventBO {

    private String instanceId;

    private String eventId;

    private String eventName;

    private String eventType;

    private String eventSource;

    private Long deviceId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date eventTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date delayEventTime;

    private String version;

    //产品分类字段
    private String category;

    private String secondCategory;

}

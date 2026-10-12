package com.guhecloud.cube.base.flow.form.rocketmq.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormDataToBusinessMessage {
    public static final String TOPIC = "FORM_DATA_TO_BUSINESS";

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 关联业务对象json字符串
     */
    private String businessObject;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timespl;

}

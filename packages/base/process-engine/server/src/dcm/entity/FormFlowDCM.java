package com.guhecloud.cube.base.flow.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-26  09:31
 * @Description:
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FormFlowDCM implements Identifiable {
    /**
     * 表单名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 应用
     */
    private String application;

    /**
     * 状态
     */
    private String status;

    /**
     * 启停
     */
    private Boolean enable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 流程定义json
     */
    private String flowJson;

    /**
     * 表单定义Json
     */
    private String formJson;

    /**
     * 是否自动去重
     */
    private Boolean autoRemoveDuplication;

    public static FormFlowDCM from(Map<String, Object> map) {
        String name = Optional.ofNullable(map.get("name")).orElse("").toString();
        String type = Optional.ofNullable(map.get("type")).orElse("").toString();
        String application = Optional.ofNullable(map.get("application")).orElse("").toString();
        String status = Optional.ofNullable(map.get("status")).orElse("").toString();
        boolean enable = (boolean) Optional.ofNullable(map.get("enable")).orElse(false);
        String remark = Optional.ofNullable(map.get("remark")).orElse("").toString();
        String flowJson = Optional.ofNullable(map.get("flowJson")).orElse("").toString();
        String formJson = Optional.ofNullable(map.get("formJson")).orElse("").toString();
        boolean autoRemoveDuplication = (boolean) Optional.ofNullable(map.get("autoRemoveDuplication")).orElse(false);

        return FormFlowDCM.builder().name(name)
                .type(type)
                .application(application)
                .status(status)
                .enable(enable)
                .remark(remark)
                .flowJson(flowJson)
                .formJson(formJson)
                .autoRemoveDuplication(autoRemoveDuplication)
                .build();
    }

    @Override
    @JsonIgnore
    public String getId() {
        return getName();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s:%s:%s:%s:%s", name, type, application, status, enable, remark, flowJson, autoRemoveDuplication);
    }
}

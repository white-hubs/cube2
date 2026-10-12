package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * @author pengjian
 * @date 2022/6/17 16:53
 * @description
 */
@Data
@NoArgsConstructor
public class StartProcessParam {

    @NotNull
    @NotBlank
    private String processDefinitionKey;

    @NotNull
    private BusinessInfo businessInfo;

    private Map<String, Object> variables;

    private Long userId;

    public StartProcessParam(String processDefinitionKey, BusinessInfo businessInfo) {
        this.processDefinitionKey = processDefinitionKey;
        this.businessInfo = businessInfo;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class BusinessInfo {
        @NotBlank
        private String businessKey;

        private String receiptCode;
        @NotNull
        @NotBlank
        private String businessType;

        @NotBlank
        private String applicationCode;
    }

}

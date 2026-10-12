package com.guhecloud.cube.deliver.project.merchant.enums;

/**
 * 批发商
 */

public enum WholesalerEnum {

    BUSINESS_TYPE_PROTOCOL("wholesalerProtocol", "协议签订"),
    BUSINESS_TYPE_QUESTIONNAIRE("wholesalerQuestionnaire", "调查问卷"),

    // 协议签订任务的有效状态
    PROTOCOL_STATUS_NOT_CREATE("wholesalerProValidStatus_not_create", "未生效"),
    PROTOCOL_STATUS_VALID("wholesalerProValidStatus_valid", "有效"),
    PROTOCOL_STATUS_NOT_VALID("wholesalerProValidStatus_not_valid", "失效"),

    // 调查问卷任务的有效状态
    QUES_STATUS_NOT_CREATE("questionnaireValidStatus_not_create", "未生效"),
    QUES_STATUS_VALID("questionnaireValidStatus_valid", "有效"),
    QUES_STATUS_NOT_VALID("questionnaireValidStatus_not_valid", "失效"),

    // 调查问卷题型
    QUESTION_TYPE_SELECT("questionType_select", "单选题"),
    QUESTION_TYPE_MULT_SELECT("questionType_mult_select", "多选题"),
    QUESTION_TYPE_Q_A("questionType_question_answer", "问答题"),

    ;

    private String code;
    private String name;

    WholesalerEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(String code) {
        for (WholesalerEnum wholesalerEnum : WholesalerEnum.values()) {
            if (wholesalerEnum.getCode().equals(code)) return wholesalerEnum.getName();
        }
        return "";
    }
}

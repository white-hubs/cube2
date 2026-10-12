package com.guhecloud.cube.base.dictionary.server.enumeration;

import com.guhecloud.platform.base.standard.exception.ErrorSpec;
import lombok.Getter;

@Getter
public enum DictionaryErrorReasonEnum implements ErrorSpec {
    NO_DICTIONARY_DATA("101001", "未查询到字典"),
    NO_DICTIONARY_ID("101002", "字典id不能为空"),
    ADD_DICTIONARY("101003", "添加字典失败"),
    UPDATE_DICTIONARY("101004", "修改字典失败"),

    ;

    private final String code;
    private String message;

    DictionaryErrorReasonEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String errorCode() {
        return code;
    }

    @Override
    public String errorMessage() {
        return message;
    }

    @Override
    public void setErrorMessage(String message) {
        this.message = message;
    }
}

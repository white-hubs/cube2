package com.guhecloud.cube.srv.gen.official.management.server.enumeration;

import lombok.Getter;

/**
 * 工作事项状态
 */
public enum FocusWorkStatus {

    UNDONE("未完成"),

    STOP("终止"),

    PROCESSING("处理中"),

    PROCESSING_DELAY("延期"),

    FINISH("已完成");

    @Getter
    String label;

    FocusWorkStatus(String label) {
        this.label = label;
    }

}

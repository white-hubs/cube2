package com.guhecloud.cube.srv.gen.official.management.server.enumeration;

import lombok.Getter;

/**
 * 工作事项状态
 */
public enum FocusWorkUpdateStatus {

    STOP("终止"),

    PROCESSING_DELAY("延期");

    @Getter
    String label;

    FocusWorkUpdateStatus(String label) {
        this.label = label;
    }

}

package com.guhecloud.cube.srv.gen.safety.personnel.server.common;

/**
 * 安全人员服务技术常量（非业务状态枚举）。
 */
public final class SafetyPersonnelConstants {

    private SafetyPersonnelConstants() {
    }

    /**
     * 文件场景：安委会附件。
     */
    public static final String SCENE_COMMITTEE_ATTACHMENT = "SAFETY_PERSONNEL::COMMITTEE_ATTACHMENT";

    /**
     * 文件场景：证书附件。
     */
    public static final String SCENE_CERT_ATTACHMENT = "SAFETY_PERSONNEL::CERT_ATTACHMENT";

    /**
     * 消息场景：证书到期提醒。
     */
    public static final String MESSAGE_SCENE_CERT_WARN = "SAFETY_PERSONNEL::CERT_WARN";

    /**
     * 消息应用编码。
     */
    public static final String MESSAGE_APPLICATION_CODE = "safetyPersonnel";

    /**
     * 消息应用名称。
     */
    public static final String MESSAGE_APPLICATION_NAME = "安全生产统一智管平台";
}

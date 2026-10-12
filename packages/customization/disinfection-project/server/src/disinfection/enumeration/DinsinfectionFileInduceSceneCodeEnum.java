package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DinsinfectionFileInduceSceneCodeEnum {
    /**
     * 业务文件
     */
    DATA_TYPE("DISINFECTION::DATA_TYPE"),
    /**
     * 项目文件
     */
    PROJECT("DISINFECTION::PROJECT"),
    /**
     * 关键场所文件
     */
    KEY_PLACE("DISINFECTION::KEY_PLACE"),
    /**
     * 资料
     */
    MATERIAL("DISINFECTION::MATERIAL"),
    /**
     * 任务
     */
    TASK("DISINFECTION::TASK"),
    /**
     * 上传完成记录
     */
    UPLOAD_COMPLETION_RECORD("DISINFECTION::UPLOAD_COMPLETION_RECORD");
    private final String code;
}

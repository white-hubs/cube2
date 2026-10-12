package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

@Data
public class RiskWarningSignVO {
    /**
     * 警示标志 ID。
     */
    private Long id;

    /**
     * 警示标志编号。
     */
    private String code;

    /**
     * 警示标志名称。
     */
    private String name;

    /**
     * 警示标志类型，取值由风险管控业务配置约定。
     */
    private String signType;

    /**
     * 警示标志图标文件 ID；文件上传和关联场景码为 RISK::CONTROL_WARNING_SIGN_ICON。
     */
    private String iconFileId;

    /**
     * 警示标志图标文件对象，由 iconFileId 调用文件服务解析得到，用于前端图片展示。
     */
    private FileVO iconFile;

    /**
     * 启用状态，true 表示启用，false 表示停用。
     */
    private Boolean enabled;
}

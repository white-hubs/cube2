package com.guhecloud.cube.srv.gen.safety.personnel.server.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 安全生产统一智管平台枚举元数据。
 */
@Data
public class SafetyPersonnelEnumsVO {

    /**
     * 人员类型，含查询专用 ALL。
     */
    private List<EnumOptionVO> personTypeCodes;

    /**
     * 台账人员状态。
     */
    private List<EnumOptionVO> personStatuses;

    /**
     * 证书业务状态。
     */
    private List<EnumOptionVO> certStatuses;

    /**
     * 证书到期展示状态。
     */
    private List<EnumOptionVO> certExpireStatuses;

    /**
     * 安全履历记录类型。
     */
    private List<EnumOptionVO> resumeRecordTypes;
}

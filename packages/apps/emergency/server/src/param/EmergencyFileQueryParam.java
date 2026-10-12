package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileExpirationStatusEnum;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileLevelEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 预案文件列表查询参数
 *
 * @author lanchun
 * @date 2022/9/1 17:07
 */
@Data
public class EmergencyFileQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 事件类型编号
     */
    @Size(max = 100, message = "事件类型编号不能大于100")
    private String emergencyEventTypeCode;

    /**
     * 级别
     */
    private EmergencyFileLevelEnum levelCode;

    /**
     * 到期状态
     */
    private EmergencyFileExpirationStatusEnum expirationStatus;
}

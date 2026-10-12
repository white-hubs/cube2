package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileLevelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预案文件保存参数
 *
 * @author lanchun
 * @date 2022/9/1 17:01
 */
@Data
public class EmergencyFileModifyParam {

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 20, message = "名称不能大于20个字符")
    private String name;

    /**
     * 预案事件编号
     */
    @NotBlank(message = "事件分类不能为空")
    @Size(max = 50, message = "事件分类不能大于50")
    private String emergencyEventTypeCode;

    /**
     * 备注
     */
    @Size(max = 200, message = "事件名称不能大于200个字符")
    private String remark;

    /**
     * 文档附件
     */
    @NotEmpty(message = "文档附件不能为空")
    private String documentFileId;

    /**
     * 流程附件
     */
    @NotEmpty(message = "流程附件不能为空")
    private String processFileId;

    /**
     * 级别
     */
    private EmergencyFileLevelEnum levelCode;

    /**
     * 文件类型主键
     */
    private Long emergencyFileTypeId;

    /**
     * 到期时间
     */
    private LocalDateTime expirationDate;
}

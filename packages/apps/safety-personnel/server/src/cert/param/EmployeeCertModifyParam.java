package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 人员证书新增或编辑参数。
 */
@Data
public class EmployeeCertModifyParam {

    /**
     * OUAA用户ID；必填。
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 用户姓名；选人后前端回填，可为空，服务端以 userId 解析结果为准。
     */
    @Size(max = 64, message = "用户姓名不能超过64个字符")
    private String userName;

    /**
     * 证书分类ID；必填。
     */
    @NotNull(message = "证书分类ID不能为空")
    private Long categoryId;

    /**
     * 证书类型ID；必填。
     */
    @NotNull(message = "证书类型ID不能为空")
    private Long typeId;

    /**
     * 证书名称；必填。
     */
    @NotBlank(message = "证书名称不能为空")
    @Size(max = 128, message = "证书名称不能超过128个字符")
    private String certName;

    /**
     * 证书编号；可为空。
     */
    @Size(max = 128, message = "证书编号不能超过128个字符")
    private String certNo;

    /**
     * 发证机构；可为空。
     */
    @Size(max = 128, message = "发证机构不能超过128个字符")
    private String issueOrg;

    /**
     * 发证日期；可为空。
     */
    private LocalDate issueDate;

    /**
     * 有效期开始日期；可为空。
     */
    private LocalDate validStart;

    /**
     * 有效期结束日期；可为空；为空时可按证书类型有效期自动计算。
     */
    private LocalDate validEnd;

    /**
     * 复审状态；由表单下拉手动维护，可为空。
     */
    private String reviewStatus;

    /**
     * 附件文件ID；可为空。
     */
    private Long attachmentFileId;

    /**
     * 证书状态，VALID/LEFT/DISABLED等；可为空，默认VALID。
     */
    private String certStatus;
}

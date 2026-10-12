package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class KeyPersonSaveParam {
    /**
     * 姓名，新增和编辑均必填。
     */
    @Size(max = 50, message = "姓名不能大于50")
    private String name;

    /**
     * 手机号。新增必填；编辑时可不传表示不变。
     */
    @Size(max = 50, message = "手机号不能大于50")
    private String phone;

    /**
     * 身份证号。可为空；编辑时传空字符串表示清空。
     */
    @Size(max = 64, message = "身份证号不能大于64")
    private String idCard;

    /**
     * mdata 区域 sequence，后端据此反查并保存区域信息。
     */
    @Size(max = 500, message = "区域序列号不能大于500")
    private String areaSequence;

    /**
     * 风险等级：HIGH 高风险，MEDIUM 中风险，LOW 低风险。
     */
    private String riskLevel;

    /**
     * 监管人员用户主键，来源于 ouaa 当前租户用户列表。
     */
    private Long supervisorUserId;

    /**
     * 监管原因，最多 500 字。
     */
    @Size(max = 500, message = "监管原因不能大于500")
    private String supervisionReason;

    /**
     * 人员照片 fileId，文件内容由 base/file 管理。
     */
    private Long photoFileId;

    /**
     * 是否清空人员照片。编辑时为 true 表示移除原照片。
     */
    private Boolean clearPhoto;

    /**
     * 人员档案附件 fileId 列表；编辑时不传表示保留，空数组表示清空，非空数组表示覆盖。
     */
    private List<Long> personAttachmentFileIds;
}

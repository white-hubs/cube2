package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCert;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 人员证书响应。
 */
@Data
public class EmployeeCertVO {

    /**
     * 人员证书ID。
     */
    private Long id;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名。
     */
    private String userName;

    /**
     * 手机号快照。
     */
    private String phone;

    /**
     * 部门名称快照。
     */
    private String deptName;

    /**
     * 岗位名称快照。
     */
    private String postName;

    /**
     * 证书分类ID。
     */
    private Long categoryId;

    /**
     * 证书分类名称。
     */
    private String categoryName;

    /**
     * 证书类型ID。
     */
    private Long typeId;

    /**
     * 证书类型名称。
     */
    private String typeName;

    /**
     * 证书名称。
     */
    private String certName;

    /**
     * 证书编号。
     */
    private String certNo;

    /**
     * 发证机构。
     */
    private String issueOrg;

    /**
     * 发证日期。
     */
    private LocalDate issueDate;

    /**
     * 有效期开始日期。
     */
    private LocalDate validStart;

    /**
     * 有效期结束日期。
     */
    private LocalDate validEnd;

    /**
     * 剩余有效天数；无结束日期时为null。
     */
    private Long remainingDays;

    /**
     * 到期展示状态，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertExpireStatusEnum}：normal/expiring/expired。
     */
    private String expireStatus;

    /**
     * 到期提前提醒天数快照。
     */
    private Integer warnDaysSnapshot;

    /**
     * 是否需要复审。
     */
    private Boolean needReview;

    /**
     * 复审状态，由表单手动维护。
     */
    private String reviewStatus;

    /**
     * 证书附件，由文件服务返回。
     */
    private FileVO attachmentFile;

    /**
     * 证书业务状态，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertStatusEnum}：VALID/LEFT/DISABLED。
     */
    private String certStatus;

    /**
     * 是否已发送到期提醒。
     */
    private Boolean warnNotified;

    /**
     * 到期提醒发送时间。
     */
    private LocalDateTime warnNotifyTime;

    public static EmployeeCertVO of(EmployeeCert cert) {
        return of(cert, null);
    }

    public static EmployeeCertVO of(EmployeeCert cert, FileVO attachmentFile) {
        EmployeeCertVO vo = new EmployeeCertVO();
        vo.setId(cert.getId());
        vo.setUserId(cert.getUserId());
        vo.setUserName(cert.getUserName());
        vo.setCategoryId(cert.getCategoryId());
        vo.setTypeId(cert.getTypeId());
        vo.setCertName(cert.getCertName());
        vo.setCertNo(cert.getCertNo());
        vo.setIssueOrg(cert.getIssueOrg());
        vo.setIssueDate(cert.getIssueDate());
        vo.setValidStart(cert.getValidStart());
        vo.setValidEnd(cert.getValidEnd());
        vo.setWarnDaysSnapshot(cert.getWarnDaysSnapshot());
        vo.setNeedReview(cert.getNeedReview());
        vo.setReviewStatus(cert.getReviewStatus());
        vo.setAttachmentFile(attachmentFile);
        vo.setCertStatus(cert.getCertStatus());
        vo.setWarnNotified(cert.getWarnNotified());
        vo.setWarnNotifyTime(cert.getWarnNotifyTime());
        return vo;
    }
}

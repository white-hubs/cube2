package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商户相关功能通知市场方（目前合同、认证）
 * </p>
 *
 * @author bq
 * @since 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerNoticeDTO extends ConvertionDTO<WholesalerNoticeDTO, WholesalerNotice> implements Serializable {

    private static final long serialVersionUID = 1L;

    public WholesalerNoticeDTO() {
    }

    public WholesalerNoticeDTO(WholesalerNotice notice) {
        super();
        map(notice);
    }

    public WholesalerNoticeDTO(List<WholesalerNotice> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 通知对象类型（用户、部门、岗位、部门岗位）
     */
    private String noticeType;

    /**
     * 通知的用户IDS字符串
     */
    private String userIds;

    /**
     * 通知的用户ID数组
     */
    private List<String> userIdList;

    /**
     * 通知的部门IDS字符串
     */
    private String deptIds;

    /**
     * 通知的部门ID数组
     */
    private List<String> deptIdList;

    /**
     * 通知的岗位ID字符串
     */
    private String postIds;

    /**
     * 通知的岗位ID数组
     */
    private List<String> postIdList;

    /**
     * 站内信
     */
    private boolean site;

    /**
     * 短信
     */
    private boolean sms;

    /**
     * 微信
     */
    private boolean wx;

    /**
     * 弹窗
     */
    private boolean pop;

    /**
     * 通知的用户IDS
     */
    private Set<String> noticeUserIds;

    private String createBy;

    private LocalDateTime createTime;

}

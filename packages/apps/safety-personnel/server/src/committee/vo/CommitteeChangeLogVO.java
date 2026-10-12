package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeChangeLog;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.SafetyCommittee;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CommitteeChangeTypeEnum;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 安委会变更记录响应。
 */
@Data
public class CommitteeChangeLogVO {

    /**
     * 变更记录ID。
     */
    private Long id;

    /**
     * 变更类型。
     */
    private String changeType;

    /**
     * 变更类型名称。
     */
    private String changeTypeName;

    /**
     * 变更内容说明。
     */
    private String changeContent;

    /**
     * 操作人姓名。
     */
    private String operatorName;

    /**
     * 变更时间。
     */
    private LocalDateTime changeTime;

    public static CommitteeChangeLogVO of(CommitteeChangeLog log) {
        CommitteeChangeLogVO vo = new CommitteeChangeLogVO();
        vo.setId(log.getId());
        vo.setChangeType(log.getChangeType());
        vo.setChangeTypeName(CommitteeChangeTypeEnum.getDescription(log.getChangeType()));
        vo.setChangeContent(formatChangeContent(log.getChangeType(), log.getChangeContent()));
        vo.setOperatorName(log.getOperatorName());
        vo.setChangeTime(log.getChangeTime());
        return vo;
    }

    private static String formatChangeContent(String changeType, String changeContent) {
        SafetyCommittee committee = parseCommittee(changeContent);
        String changeTypeName = CommitteeChangeTypeEnum.getDescription(changeType);
        if (committee == null) {
            if (StringUtils.isBlank(changeContent)) {
                return changeTypeName;
            }
            return StringUtils.isBlank(changeTypeName) ? changeContent : changeTypeName + "：" + changeContent;
        }

        CommitteeChangeTypeEnum type = CommitteeChangeTypeEnum.of(changeType);
        String actionName = StringUtils.isBlank(changeTypeName) ? "变更" : changeTypeName;
        String committeeName = blankToDash(committee.getCommitteeName());
        String deptName = blankToDash(committee.getDeptName());
        String contactPhone = blankToDash(committee.getContactPhone());
        int memberCount = Objects.requireNonNullElse(committee.getMemberCount(), 0);
        if (CommitteeChangeTypeEnum.DELETE == type) {
            return actionName + "安委会「" + committeeName + "」，删除前负责单位/部门：" + deptName
                    + "，联系电话：" + contactPhone + "，成员数量：" + memberCount;
        }
        if (CommitteeChangeTypeEnum.UPDATE == type) {
            return actionName + "安委会「" + committeeName + "」，变更后负责单位/部门：" + deptName
                    + "，联系电话：" + contactPhone + "，成员数量：" + memberCount;
        }
        return actionName + "安委会「" + committeeName + "」，负责单位/部门：" + deptName
                + "，联系电话：" + contactPhone + "，成员数量：" + memberCount;
    }

    private static SafetyCommittee parseCommittee(String changeContent) {
        if (StringUtils.isBlank(changeContent)) {
            return null;
        }
        try {
            return JSON.parseObject(changeContent, SafetyCommittee.class);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private static String blankToDash(String value) {
        return StringUtils.isBlank(value) ? "-" : value;
    }
}

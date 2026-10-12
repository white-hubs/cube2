package com.guhecloud.cube.srv.gen.conferencemanage.server.util;

import com.guhecloud.cube.base.dictionary.utils.BusinessConfigUtils;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomApprovalEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.OperateEnum;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 14:32
 * @Description: 会议室能力配置
 */
public class ConferenceAbility {

    /**
     * 人事管理能力配置
     */
    public static final String CONFERENCE_ABILITY = "MEETING_ROOM_MANAGEM_ABILITY";

    /**
     * 是否需要审批能力
     *
     * @return
     */
    public static BookRoomEnum getConferenceStatus() {
        if (approvalFlag()) {
            return BookRoomEnum.APPROVAL;
        } else {
            return BookRoomEnum.RESERVED;
        }
    }

    /**
     * 是否需要审批能力
     *
     * @return
     */
    public static OperateEnum getRecordStatus() {
        if (approvalFlag()) {
            return OperateEnum.APPOINTMENT_IN;
        } else {
            return OperateEnum.APPOINTMENT;
        }
    }

    /**
     * 是否有审批能力
     *
     * @return boolean
     */
    public static boolean approvalFlag() {
        return BusinessConfigUtils.abilityFlag(CONFERENCE_ABILITY, BookRoomApprovalEnum.MEETING_ROOM_RESERVATION.name());
    }

}

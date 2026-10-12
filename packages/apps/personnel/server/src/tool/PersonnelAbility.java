package com.guhecloud.cube.srv.gen.personnel.management.server.tool;

import com.guhecloud.cube.base.dictionary.utils.BusinessConfigUtils;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.PersonnelApprovalBizEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/26 14:44
 * @Description: 人事管理能力配置
 */

public class PersonnelAbility {

    /**
     * 人事管理能力配置
     */
    public static final String PERSONNEL_ABILITY = "PERSONNELMANAGEMENT_ABILITY";

    public static String getStaffStatusByFlow() {
        if (regularWorkerFlag()) {
            return StaffStatusEnum.ON_TRIAL.name();
        } else {
            return StaffStatusEnum.FORMAL.name();
        }
    }

    /**
     * 是否有转正考核能力
     *
     * @return boolean
     */
    public static boolean regularWorkerFlag() {
        return BusinessConfigUtils.abilityFlag(PERSONNEL_ABILITY, PersonnelApprovalBizEnum.PERSONNEL_REGULAR_WORKER.name());
    }

}

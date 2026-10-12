package com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade;

import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 人事服务访问门面。
 */
@Slf4j
@Service("safetyPersonnelPersonnelFacade")
@RequiredArgsConstructor
public class SafetyPersonnelPersonnelFacadeService {

    private final StaffClient staffClient;

    /**
     * 按员工 ID 查询人事详情。
     *
     * @param staffId 员工ID
     *
     * @return 员工详情，不存在时返回 null
     */
    public StaffDetailDTO getStaffById(Long staffId) {
        if (staffId == null) {
            return null;
        }
        try {
            return staffClient.getById(staffId);
        } catch (Exception e) {
            log.warn("查询人事员工失败，staffId={}", staffId, e);
            return null;
        }
    }

    /**
     * 从人事员工解析 OUAA 用户 ID。
     *
     * @param staffId 员工ID
     *
     * @return OUAA用户ID
     */
    public Long resolveUserIdByStaffId(Long staffId) {
        StaffDetailDTO staff = getStaffById(staffId);
        return staff == null ? null : staff.getUserId();
    }
}

package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.domain.repository;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-02  11:45
 * @Description:
 * @Version: 1.0
 */
public interface UserManageRepository {
    /**
     * 批量同步照片到度目考勤机
     */
    void syncPhotoDuMu(String[] urlList, List<UserDTO> users, String devicePwd, Integer tenant);

    /**
     * 批量同步照片到人脸服务
     */
    void syncPhotoFace(List<UserDTO> users, String url);
}

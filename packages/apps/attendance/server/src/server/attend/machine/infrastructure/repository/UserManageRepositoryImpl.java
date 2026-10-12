package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.infrastructure.repository;

import cn.hutool.http.HttpUtil;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.domain.repository.UserManageRepository;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-02  11:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserManageRepositoryImpl implements UserManageRepository {
    private final FileClient fileClient;
    private static final String ADD_USER = "userManage/addUser";

    /**
     * 批量同步照片到度目考勤机
     */
    @Override
    public void syncPhotoDuMu(String[] urlList, List<UserDTO> users, String devicePwd, Integer tenant) {
        users.forEach(user -> {
            for (String url : urlList) {
                try {
                    String param = this.getJsonParam(user, devicePwd, tenant);
                    if (ObjectUtils.isNotEmpty(param)) {
                        String str = HttpUtil.post(url + ADD_USER, param);
                        log.info("考勤机人员信息同步结果：{}", str);
                    }
                } catch (Exception e) {
                    log.error("同步地址：{}。百度考勤机同步人员信息失败:", url, e);
                }
            }
        });
    }

    /**
     * 批量同步照片到人脸服务
     */
    @Override
    public void syncPhotoFace(List<UserDTO> users, String url) {
        users.forEach(ex -> {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", ex.getId());
            paramMap.put("userName", ex.getName());
            paramMap.put("photographKey", ex.getPhotographKey());
            String str = HttpUtil.post(url, JsonUtils.serialize(paramMap));
            if (!"success".equals(str)) {
                log.error("更新" + ex.getName() + "考勤照片失败:" + str);
            }
        });
    }

    /**
     * 构建考勤机同步参数
     */
    private String getJsonParam(UserDTO user, String devicePwd, Integer tenant) {
        log.info("考勤机：构建同步参数：{}", JsonUtils.serialize(user));
        // 获取图片
        List<FileVO> file = fileClient.getByBusinessKey(user.getPhotographKey());
        if (file.isEmpty()) {
            log.info("考勤机：人脸照片不存在");
            return null;
        }
        FileVO fileVO = file.getFirst();
        // 图片转base64
        String photoBase = fileClient.getFileBase64(fileVO.getId());
        if (ObjectUtils.isEmpty(photoBase)) {
            log.info("考勤机：照片转base64失败");
            return null;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pass", DigestUtils.md5DigestAsHex(devicePwd.getBytes()));
        paramMap.put("image_content", photoBase);
        paramMap.put("image_type", "image");
        paramMap.put("user_id", user.getId().toString());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getName());
        userInfo.put("user_type", 1);
        userInfo.put("card_number", user.getMobile());
        userInfo.put("id", user.getId());
        userInfo.put("mobile", user.getMobile());
        userInfo.put("tenant", tenant);
        paramMap.put("user_info", userInfo);
        log.info("考勤机请求参数：{}", JsonUtils.serialize(paramMap));
        return JsonUtils.serialize(paramMap);
    }
}

package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu;

import cn.hutool.http.HttpUtil;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.domain.repository.UserManageRepository;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.DelStaffMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-22  13:34
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserManageApplication {
    private final DictionaryAttApplication dictionaryAttApplication;
    private final FileClient fileClient;
    private final UserClient userClient;
    private final UserManageRepository userManageRepository;
    private static final String ADD_USER = "userManage/addUser";
    private static final String DELETE_USER = "userManage/deleteUser";

    /**
     * 批量同步人员照片
     */
    public void addAllUser() {
        List<UserDTO> userList = userClient.list(new UserQuery());
        DataDictionaryDTO dumuDictionary = dictionaryAttApplication.getDataDictionaryByItem("att-face-recognition", "baidu_machine");
        if (ObjectUtils.isNotEmpty(dumuDictionary) && ObjectUtils.isNotEmpty(dumuDictionary.getName())) {
            log.info("att:考勤机配置：{}", JsonUtils.serialize(dumuDictionary));
            String[] urlList = dumuDictionary.getName().split(",");
            // 同步度目考勤机照片
            userManageRepository.syncPhotoDuMu(urlList, userList, dumuDictionary.getVariable(), 1);
        }
        log.info("att:考勤机同步人脸照片结束");
        DataDictionaryDTO faceDictionary = dictionaryAttApplication.getDataDictionaryByItem("att-face-recognition", "att_update_staff_photograph");
        if (ObjectUtils.isNotEmpty(faceDictionary) && ObjectUtils.isNotEmpty(faceDictionary.getName())) {
            log.info("att:人脸服务配置：{}", JsonUtils.serialize(faceDictionary));
            // 同步人脸服务照片
            userManageRepository.syncPhotoFace(userList, faceDictionary.getName());
        }
        log.info("att:人脸服务同步人脸照片结束");
    }

    /**
     * 注册或更新人员人脸信息
     */
    public void addUser(UserUpdateMessage userUpdateMessage) {
        log.info("百度考勤机同步人员信息:{}", JsonUtils.serialize(userUpdateMessage));
        DataDictionaryDTO dictionary = new DataDictionaryDTO();
        dictionary = dictionaryAttApplication.getDataDictionaryByItem("att-face-recognition", "baidu_machine");
        log.info("考勤机配置：{}", JsonUtils.serialize(dictionary));
        if (dictionary == null || ObjectUtils.isEmpty(dictionary.getName())) {
            log.warn("考勤机配置为空！");
            return;
        }
        String[] urlList = dictionary.getName().split(",");
        for (String url : urlList) {
            try {
                String param = this.getJsonParam(ObjectUtils.copying(userUpdateMessage, UserDTO.class), dictionary.getVariable(), userUpdateMessage.getTenant());
                if (ObjectUtils.isNotEmpty(param)) {
                    String str = HttpUtil.post(url + ADD_USER, param);
                    log.info("考勤机人员信息同步结果：{}", str);
                }
            } catch (Exception e) {
                log.error("同步地址：{}。百度考勤机同步人员信息失败:", url, e);
            }
        }
        log.info("百度考勤机同步人员信息结束");
    }

    /**
     * 删除人员
     */
    public void deleteUser(DelStaffMessage message) {
        log.info("百度考勤机删除人员信息:{}", JsonUtils.serialize(message));
        try {
            DataDictionaryDTO dictionary = dictionaryAttApplication.getDataDictionaryByItem("att-face-recognition", "baidu_machine");
            Map<String, Object> map = new HashMap<>();
            map.put("pass", DigestUtils.md5DigestAsHex(dictionary.getVariable().getBytes()));
            map.put("user_id", message.getStaffId().toString());
            String str = HttpUtil.post(dictionary.getName() + DELETE_USER, JsonUtils.serialize(map));
            log.info("考勤机人员信息删除结果：{}", str);
        } catch (Exception e) {
            log.error("百度考勤机删除人员信息失败");
        }
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
        FileVO fileVO = file.get(0);
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

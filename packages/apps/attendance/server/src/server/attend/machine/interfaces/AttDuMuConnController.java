package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces;

import cn.hutool.http.HttpUtil;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度云 度目
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
// 这里我们系统没有用，是有可能三方系统在用(不建议删除)
public class AttDuMuConnController {

    private final UserClient userClient;

    /**
     * 设备(考勤机)ip地址
     */
    //    @Value("${attend-value.ip}")
    private String deviceIp = "192.168.2.88";

    /**
     * 设备(考勤机)密码
     */
    private String devicePwd = "admin123";

    /**
     * 图片存放目录
     */
    //    @Value("${attend-value.address}")
    private String photoAddress = "C:/Users/admin/Desktop/dumu/photo";

    /**
     * 租户
     */
    private Integer tenant = 58197260;

    /**
     * 设置人员照片
     */
    @GetMapping("/att/dumu/initPhoto")
    public String setUserAndPhoto() {

        if (StringUtils.isBlank(photoAddress)) {
            log.error("初始化人员照片时未配置人员照片地址：" + photoAddress);
            throw new ServiceException("未配置人员照片地址：" + photoAddress);
        }
        File file = new File(photoAddress);
        String[] photoFiles = file.list();
        if (photoFiles == null) {
            log.error("初始化人员照片时文件夹为空：" + photoAddress);
            throw new ServiceException("文件夹为空：" + photoAddress);
        }

        Map<String, UserDTO> userDTOMap = new HashMap<>();
        // 获取人员信息
        UserQueryParam queryParam = new UserQueryParam();
        queryParam.setEnabled(true);
        queryParam.setPageNumber(1);
        queryParam.setPageSize(999);
        Pagination<UserDTO> pagination = userClient.listUserByTenant(tenant, queryParam);
        if (pagination.getTotal() > 0) {
            pagination.getRecords().forEach(userDTO -> userDTOMap.put(userDTO.getName(), userDTO));
        }

        String url = "http://" + deviceIp + ":8080/userManage/addUser";
        String pwd = DigestUtils.md5DigestAsHex(devicePwd.getBytes());
        StringBuilder stringBuffer = new StringBuilder();
        for (String photoFile : photoFiles) {
            // 获取图片
            File photo = new File(photoAddress + "//" + photoFile);
            // 图片转base64
            String photoBase = this.imageToBase64(photo);
            // 组装请求参数
            String staffPhotoName = photo.getName().substring(0, photo.getName().lastIndexOf("."));
            String jsonData = this.getJsonParam(userDTOMap.get(staffPhotoName), photoBase, pwd, staffPhotoName);
            // 发送请求
            String resp = HttpUtil.post(url, jsonData);
            // 解读返回值
            if (StringUtils.isBlank(resp) || Integer.parseInt(String.valueOf(JsonUtils.deserialize(resp).get("code"))) <= 0) {
                System.out.println(resp);
            } else {
                System.out.println("OK!===========================================================");
            }
            stringBuffer.append(resp);
        }
        return stringBuffer.toString();
    }

    private String imageToBase64(File file) {
        byte[] date = new byte[0];
        int dataSize = 0;
        try (InputStream in = Files.newInputStream(file.toPath())) {
            date = new byte[in.available()];
            dataSize = in.read(date);
        } catch (IOException e) {
            log.error("初始化人员照片时录入失败：" + file.getAbsolutePath());
        }
        if (dataSize > 0) {
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(/*Objects.requireNonNull(*/date/*)*/);
        } else {
            log.error("初始化人员照片时base64照片为空：" + file.getAbsolutePath());
        }
        return null;
    }

    /**
     * 获取入参json string
     *
     * @param user          用户信息
     * @param base64        图片
     * @param md5Pwd        加密的密码
     * @param photoUserName 图片名称(图片名称以人员名称命名
     *
     * @return string入参
     */
    private String getJsonParam(UserDTO user, String base64, String md5Pwd, String photoUserName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pass", md5Pwd);
        paramMap.put("image_content", base64);
        paramMap.put("image_type", "image");
        paramMap.put("user_id", photoUserName);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", photoUserName);
        userInfo.put("user_type", 1);
        userInfo.put("card_number", photoUserName);
        if (user != null) {
            paramMap.put("user_id", String.valueOf(user.getId()));
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getName());
            userInfo.put("username", user.getUsername());
            userInfo.put("mobile", user.getMobile());
            userInfo.put("entry", user.getEntryDate());
            userInfo.put("userDeptId", user.getOrganization() == null ? null : user.getOrganization().getId());
            userInfo.put("userDeptName", user.getOrganization() == null ? null : user.getOrganization().getName());
        }

        paramMap.put("user_info", userInfo);
        return JsonUtils.serialize(paramMap);
    }
}

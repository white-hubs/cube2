package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import cn.hutool.http.HttpUtil;
import com.aliyun.facebody20191230.Client;
import com.aliyun.facebody20191230.models.CompareFaceAdvanceRequest;
import com.aliyun.facebody20191230.models.CompareFaceResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.IdentityVerifyEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.FaceRecognitionRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.dto.DuMuFaceDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.dto.FaceRecognitionConfigDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-03  09:57
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaceRecognitionRepositoryImpl implements FaceRecognitionRepository {
    private final FileClient fileClient;
    private static Client aLiYueclient;
    /**
     * 度目人脸识别接口
     */
    private static final String IDENTIFY = "/recognitionManage/identify";

    @Override
    public String faceRecognitionDuMu(Long fileId, UserDTO user, DataDictionaryDTO dataDictionary, String devicePass) {
        log.info("考勤：度目人脸识别开始：{},{}", user.getId(), user.getName());
        String faceRecognitionResult = "";
        Map<String, Object> paramMap = new HashMap<>();
        // 图片转base64
        String photoBase = fileClient.getFileBase64(fileId);
        if (ObjectUtils.isEmpty(photoBase)) {
            log.info("考勤机：照片转base64失败");
        }
        paramMap.put("pass", DigestUtils.md5DigestAsHex(devicePass.getBytes()));
        paramMap.put("image_content", photoBase);
        paramMap.put("image_type", "image");
        paramMap.put("quality_control", "NORMAL");
        String result = "";
        try {
            result = HttpUtil.post(dataDictionary.getName() + IDENTIFY, JsonUtils.serialize(paramMap));
            log.info("考勤：度目人脸识别结果：{}", result);
        } catch (Exception e) {
            log.error("度目人脸识别故障，稍后处理");
            return faceRecognitionResult;
        }
        DuMuFaceDTO duMuFaceDTO = JsonUtils.deserialize(result, DuMuFaceDTO.class);
        faceRecognitionResult = IdentityVerifyEnum.FACE_DB_COMPARE_FAILED.getRemarks();
        if (IdentityVerifyEnum.OK.getCode().equals(duMuFaceDTO.getCode())) {
            faceRecognitionResult = "不是本人,请联系管理员核对信息";
            // 判断是否是本人
            if (ObjectUtils.isNotEmpty(duMuFaceDTO.getUser_list()) &&
                user.getId().equals(duMuFaceDTO.getUser_list().get(0).getUser_id())) {
                faceRecognitionResult = IdentityVerifyEnum.OK.getRemarks();
            }
        }
        return faceRecognitionResult;
    }

    @Override
    public String faceRecognitionKaiHe(String photoFileUrl, UserDTO user, DataDictionaryDTO dataDictionary) {
        log.info("考勤：开河人脸识别开始:{},{}", user.getId(), user.getName());
        String faceRecognitionResult = "";
        try {
            String url = dataDictionary.getName();
            Map<String, Object> map = new HashMap<>();
            map.put("image_path", photoFileUrl);
            map.put("hint_user_name", user.getName());
            map.put("hint_user_id", user.getId());
            String result = HttpUtil.post(url, map, 3000);
            log.info("考勤：开河人脸识别结果：{}", result);
            faceRecognitionResult = IdentityVerifyEnum.getRemarksByResult(result);
        } catch (Exception e) {
            log.error("开河人脸识别故障，稍后处理");
        }
        return faceRecognitionResult;

    }

    @Override
    public String faceRecognitionALiYue(Long fileId, UserDTO user, DataDictionaryDTO dataDictionary) {
        Assert.parameterIsNull(user.getPhotographList(), "系统未配置人脸照片，请联系管理员");
        FaceRecognitionConfigDTO faceRecognitionConfig = JsonUtils.deserialize(dataDictionary.getVariable(),
                                                                               FaceRecognitionConfigDTO.class);
        Client client = FaceRecognitionRepositoryImpl.createClient(faceRecognitionConfig);
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 新照片base64
            String newPhotoBase = fileClient.getFileBase64(fileId);
            // 数据库照片base64
            String originalPhotoBase = fileClient.getFileBase64(user.getPhotographList().getFirst().getId());
            Assert.parameterIsNull(newPhotoBase, "打卡照片异常，请重试");
            Assert.parameterIsNull(originalPhotoBase, "系统配置的人脸照片异常，请联系管理员");
            CompareFaceAdvanceRequest compareFaceAdvanceRequest = new CompareFaceAdvanceRequest()
                    .setImageDataA(newPhotoBase)
                    .setImageDataB(originalPhotoBase);
            long startTime = System.currentTimeMillis();
            CompareFaceResponse compareFaceResponse = client.compareFaceAdvance(compareFaceAdvanceRequest, runtime);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.debug("耗时：" + duration + " 毫秒");
            // 获取整体结果
            Assert.isUnknownError(compareFaceResponse.getStatusCode() != 200, "请求阿里云服务异常");
            Assert.parameterIsNull(compareFaceResponse.getBody(), "请求阿里云服务异常body");
            Assert.parameterIsNull(compareFaceResponse.getBody().getData(), "请求阿里云服务异常data");
            if (compareFaceResponse.getBody().getData().getConfidence() > faceRecognitionConfig.getConfidence()) {
                return IdentityVerifyEnum.SUCCESS.getRemarks();
            }
            return IdentityVerifyEnum.FACE_DB_COMPARE_FAILED.getRemarks() + "相识度：" +
                   compareFaceResponse.getBody().getData().getConfidence();
        } catch (TeaException e) {
            log.error("att:阿里云T", e);
            throw new ServiceException("阿里云T" + e.getMessage());
        } catch (Exception e) {
            log.error("att:阿里云E", e);
            throw new ServiceException("阿里云E");
        }
    }

    /**
     * 获取阿里云客户端
     */
    private static Client createClient(FaceRecognitionConfigDTO faceRecognitionConfig) {
        if (ObjectUtils.isNotEmpty(aLiYueclient)) {
            return aLiYueclient;
        }
        Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(faceRecognitionConfig.getAccessKeyId())
                .setAccessKeySecret(faceRecognitionConfig.getAccessKeySecret());
        config.endpoint = faceRecognitionConfig.getEndpoint();
        try {
            aLiYueclient = new Client(config);
            return aLiYueclient;
        } catch (Exception e) {
            log.error("att:创建阿里云人脸识别客户端失败");
            throw new ServiceException("创建阿里云人脸识别客户端失败");
        }
    }
}

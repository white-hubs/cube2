package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInErrorEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.DeviceInfoEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.FaceRecognitionTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.IdentityVerifyEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.FaceRecognitionRepository;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-03  10:27
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaceRecognitionFactory {
    private final FaceRecognitionRepository faceRecognitionRepository;
    private final DataDictionaryClient dataDictionaryClient;

    public String faceRecognition(Long imgId, String imgUrl, UserDTO user) {
        try {
            DataDictionaryaramParam param = new DataDictionaryaramParam();
            param.setItemCode("THIRD_SERVICE");
            param.setCode("att_identity_verify");
            List<DataDictionaryDTO> dictionary = dataDictionaryClient.getDataDictionaryByItem(param);
            if (ObjectUtils.isEmpty(dictionary)) {
                log.info("考勤：未配置人脸识别配置");
                return IdentityVerifyEnum.CONFIG_FAILED.getRemarks();
            }
            DataDictionaryDTO dataDictionary = dictionary.getFirst();
            log.info("考勤：人脸识别配置:{}", JsonUtils.serialize(dataDictionary));
            Map<String, String> jsonObject = JsonUtils.deserialize(dataDictionary.getVariable(), new TypeReference<>() {});
            if (MapUtils.isEmpty(jsonObject)) {
                throw new ServiceException("考勤:人脸识别配置为空");
            }
            if (FaceRecognitionTypeEnum.DUMU.name().equals(jsonObject.get(DeviceInfoEnum.DEVICE_TYPE.getCode()))) {
                String devicePass = jsonObject.get(DeviceInfoEnum.DEVICE_PASSWORD.getCode());
                return faceRecognitionRepository.faceRecognitionDuMu(imgId, user, dataDictionary, devicePass);
            } else if (FaceRecognitionTypeEnum.ALIYUN.name()
                    .equals(jsonObject.get(DeviceInfoEnum.DEVICE_TYPE.getCode()))) {
                return faceRecognitionRepository.faceRecognitionALiYue(imgId, user, dataDictionary);
            } else {
                return faceRecognitionRepository.faceRecognitionKaiHe(imgUrl, user, dataDictionary);
            }
        } catch (Exception e) {
            try {
                int length = e.getMessage().length();
                return length > 200 ? e.getMessage().substring(0, 200) : e.getMessage();
            } catch (Exception ex) {
                return ClockInErrorEnum.ERROR_MESSAGE.getRemarks();
            }
        }
    }
}

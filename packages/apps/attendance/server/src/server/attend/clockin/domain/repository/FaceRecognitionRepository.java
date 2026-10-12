package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository;

import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-03  09:56
 * @Description:
 * @Version: 1.0
 */
public interface FaceRecognitionRepository {
    /**
     * 度目考勤机人脸识别
     */
    String faceRecognitionDuMu(Long fileId, UserDTO user, DataDictionaryDTO dataDictionary, String devicePass);

    /**
     * 开河平台人脸识别
     */
    String faceRecognitionKaiHe(String photoFileUrl, UserDTO user, DataDictionaryDTO dataDictionary);

    /**
     * 阿里云平台人脸对比
     */
    String faceRecognitionALiYue(Long fileId, UserDTO user, DataDictionaryDTO dataDictionary);

}

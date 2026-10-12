package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 海康门禁远程核验事件明细。
 *
 * @param deviceName 设备名称
 * @param majorEventType 主事件类型
 * @param subEventType 子事件类型
 * @param name 核验人员名称
 * @param cardReaderNo 读卡器编号
 * @param doorNo 门编号
 * @param employeeNoString 人员编号
 * @param serialNo 事件流水号
 * @param remoteCheck 是否为远程核验
 * @param currentVerifyMode 当前核验方式
 * @param mask 是否佩戴口罩
 * @param picturesNumber 图片数量
 * @param purePwdVerifyEnable 是否启用纯密码核验
 * @param faceRect 人脸框信息
 */
public record HikRemoteCheckAccessControllerEventRequest(
        String deviceName,
        Integer majorEventType,
        Integer subEventType,
        String name,
        Integer cardReaderNo,
        Integer doorNo,
        String employeeNoString,
        Integer serialNo,
        Boolean remoteCheck,
        String currentVerifyMode,
        String mask,
        Integer picturesNumber,
        Boolean purePwdVerifyEnable,
        @JsonProperty("FaceRect") HikRemoteCheckFaceRectRequest faceRect
) {
}

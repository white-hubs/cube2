package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.valueobject.AttClockWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  11:21
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInParam {
    /**
     * 班次id
     */
    private Long id;
    /**
     * 打卡记录唯一标识
     */
    @NotNull(message = "打卡标识不能为空")
    private String uuid;
    /**
     * 来源（GPS、第三方、wifi）
     */
    @NotNull(message = "打卡来源不能为空")
    private String ways;
    /**
     * 打卡地点、WiFi名称
     */
    @NotNull(message = "打卡信息不能为空")
    private String name;
    /**
     * 是否为外勤
     */
    private Boolean outClock = false;
    /**
     * 打卡照片
     */
    private Long photoFileId;
    /**
     * 打卡照片地址
     */
    private String photoFileUrl;
    /**
     * 人脸识别结果
     */
    private String faceRecognitionResult;
    private LocalDateTime actualClockInTime;
    private Long userId;
    /**
     * 打卡失败原因
     */
    private String failReason;

    public static ClockInParam getClockInParam(ClockInMachineParam param, Long classId, String uuid) {
        ClockInParam clock = new ClockInParam();
        clock.setId(classId);
        clock.setUuid(uuid);
        clock.setWays(AttClockWayEnum.DEVICE.name());
        clock.setName(param.getLocation());
        clock.setActualClockInTime(param.getClockInTime());
        return clock;
    }

    public static AttClockInRecordsPO getClockInParam(ClockInMachineParam param, String userName) {
        AttClockInRecordsPO clock = new AttClockInRecordsPO();
        clock.setUserId(param.getUserId());
        clock.setUserName(userName);
        clock.setWays(AttClockWayEnum.DEVICE.name());
        clock.setActualClockInTime(param.getClockInTime());
        clock.setName(param.getLocation());
        return clock;
    }

}

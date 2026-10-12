package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class MemberDeviceAutomaticPageResult {
    private Long id;
    /**
     * 所属分组id
     */
    private Long groupId;
    /**
     * 指令名
     */
    private String name;
    /**
     * 显示的条件
     */
    private String display;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 指令写值内容
     */
    private List<MemberDevicePropDTO> propWriteValueList;
    // 循环类型 JobLoopTypeEnum
    private JobLoopTypeEnum loopType;
    // 日期 年/月/周 (例 每月：3,18,26； 每周：2,4)
    private String loopDay;
    // 时间 例如：15:21
    private String loopHour;
}

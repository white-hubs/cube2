package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberDeviceAutomatic;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePropDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 自动指令
 * 如果 loopType为空就不是循环执行；而是使用设备属性和值判断执行
 */
@Data
public class DeviceAutomaticCommandParam {
    private Long id;
    /**
     * 所属组
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
     * 指令写入集合
     */
    private List<MemberDevicePropDTO> propWriteValueList;
    /**
     * 循环类型 JobLoopTypeEnum
     */
    private JobLoopTypeEnum loopType;
    /**
     * 日期 年/月/周 (例 每月：3,18,26； 每周：2,4)
     */
    private String loopDay;
    /**
     * 时间 例如：15:21
     */
    private String loopHour;

    public MemberDeviceAutomatic toMemberDeviceAutomatic() {
        MemberDeviceAutomatic memberDeviceAutomatic = new MemberDeviceAutomatic();
        memberDeviceAutomatic.setId(IdWorker.getId());
        memberDeviceAutomatic.setGroupId(groupId);
        memberDeviceAutomatic.setDeviceId(deviceId);
        memberDeviceAutomatic.setName(name);
        memberDeviceAutomatic.setDisplay(display);
        memberDeviceAutomatic.setPropWriteValueList(JsonUtils.serialize(propWriteValueList));
        memberDeviceAutomatic.setLoopType(loopType);
        memberDeviceAutomatic.setLoopDay(loopDay);
        memberDeviceAutomatic.setLoopHour(loopHour);
        return memberDeviceAutomatic;
    }
}

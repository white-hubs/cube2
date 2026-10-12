package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ManualControlParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DeviceMethodControlCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DevicePropControlCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupDeviceCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupDeviceQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ManualControlDurationSettingResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePageResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Set;

public interface DisinfectionMemberGroupDeviceService extends IService<MemberGroupDevice> {

    Boolean devicePropControlBatch(DevicePropControlCommandParam param);

    Boolean bindDeviceOne(MemberGroupDeviceCommandParam param);

    Boolean bindDeviceBatch(MemberGroupDeviceCommandParam param);

    Boolean unbindDeviceBatch(MemberGroupDeviceCommandParam param);

    List<DeviceProductCountDTO> getGroupDeviceProductList(MemberGroupDeviceQueryParam param);

    Pagination<MemberDevicePageResult> memberGroupDevicePage(MemberGroupDeviceQueryParam param);

    Set<Long> getAllBindDeviceIdList();

    List<Integer> getTenantsByDeviceId(long iotDeviceId);

    Boolean deviceMethodControlBatch(DeviceMethodControlCommandParam param);

    /**
     * 手动控制设备：按指令枚举（驱蚊/雾灌/设备关闭/定时任务）及可选时长下发
     */
    Boolean manualControl(ManualControlParam param);

    /**
     * 查询当前用户对设备手动控制指令的最新时长设置
     */
    List<ManualControlDurationSettingResult> getManualControlDurationSettings(Long deviceId);
}

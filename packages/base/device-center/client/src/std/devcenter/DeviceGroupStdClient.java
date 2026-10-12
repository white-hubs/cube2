package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceGroupParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceGroupVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceGroupStdClient {

    /**
     * 标签下的设备分组列表
     *
     * @param tagCode
     * @param param
     *
     * @return
     */
    @PostMapping("/tags/{tagCode}/device-group-all/by-device-ids")
    List<DeviceGroupVO> deviceGroupByTagAndDeviceIds(@PathVariable("tagCode") String tagCode,
                                                     @RequestBody @Valid QueryDeviceGroupParam param
                                                    );

    /**
     * 设备分组详情
     *
     * @param id 分组主键
     *
     * @return DeviceGroupVO
     */
    @GetMapping("/device-groups/{id}")
    DeviceGroupVO getGroup(@PathVariable("id") Long id);
}

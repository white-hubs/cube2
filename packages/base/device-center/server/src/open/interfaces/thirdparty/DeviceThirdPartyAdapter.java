package com.guhecloud.cube.base.device.center.open.interfaces.thirdparty;

import com.guhecloud.cube.base.device.center.open.interfaces.thirdparty.query.HeadquartersDataGatherQry;
import com.guhecloud.cube.base.device.center.open.interfaces.thirdparty.vo.HeadquartersDataGatherDeviceVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceThirdPartyAdapter {

    private final DeviceCenterMapper deviceCenterMapper;

    /**
     * 对外提供设备获取接口
     *
     * @param qry
     *
     * @return
     */
    @PreAuthorize("hasScope('device_manage')")
    @GetMapping("/device-headquarters-Gather")
    public List<HeadquartersDataGatherDeviceVO> headquartersDataGather(HeadquartersDataGatherQry qry) {
        Period period = null;
        if (StringUtils.isNotEmpty(qry.getPeriod())) {
            String[] split = qry.getPeriod().split(",");
            period = new Period(DateUtils.parse(split[0]), DateUtils.parse(split[1]));
        }
        return deviceCenterMapper.selectThirdPartyHeadquartersDataGather(period, SessionContextHolder.getRequester().getTenant());
    }
}

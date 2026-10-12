package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossConnectionPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossConnectionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossConnectionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线损线路控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossConnectionController {

    private final LineLossConnectionService lineLossConnectionService;

    /**
     * 分页查询线损线路
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    @GetMapping("/lineLoss/connection/page")
    public Pagination<LineLossConnectionVO> pageByLineName(LineLossConnectionPageParam param) {
        return lineLossConnectionService.pageLossConnection(param);
    }

    /**
     * 根据线路 ID 查询线路设备信息
     *
     * @param param 查询参数
     *
     * @return 线路设备信息
     */
    @GetMapping("/lineLoss/connection/devices")
    public LineLossDeviceVO getLineDevices(LineLossDeviceParam param) {
        return lineLossConnectionService.getLineDevices(param);
    }
}

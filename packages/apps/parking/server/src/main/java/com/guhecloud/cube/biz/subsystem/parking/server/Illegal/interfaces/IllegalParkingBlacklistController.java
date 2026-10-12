package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.IllegalParkingBlacklistCommandService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.IllegalParkingBlacklistCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.IllegalParkingBlacklistQueryService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param.IllegalParkingBlacklistQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param.VehiclePenaltyRecordParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.IllegalParkingConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingBlacklistVO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.VehiclePenaltyRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 黑名单管理
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 10:37
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/illegal/parking/blacklist")
public class IllegalParkingBlacklistController {
    private final IllegalParkingBlacklistCommandService illegalParkingBlacklistCommandService;
    private final IllegalParkingBlacklistQueryService illegalParkingBlacklistQueryService;
    private final IllegalParkingConfigService illegalParkingConfigService;

    /**
     * 添加黑名单
     */
    @PostMapping("/create")
    public Boolean createBlacklist(@Valid @RequestBody IllegalParkingBlacklistCommand command) {
        String timeCycle = illegalParkingConfigService.getIllegalParkingConfigTimeCycle();
        command.setCycle(timeCycle);
        return illegalParkingBlacklistCommandService.createBlacklist(command);
    }

    /**
     * 移除黑名单
     */
    @DeleteMapping("/remove/{id}")
    public Boolean removeBlacklist(@PathVariable("id") Long id) {
        return illegalParkingBlacklistCommandService.removeBlacklist(id);
    }

    /**
     * 黑名单列表
     */
    @GetMapping("/list")
    public Pagination<IllegalParkingBlacklistVO> getBlacklist(IllegalParkingBlacklistQueryParam queryParam) {
        return illegalParkingBlacklistQueryService.getBlacklist(queryParam);
    }

    /**
     * 黑名单违停明细
     */
    @GetMapping("/detail")
    public Pagination<VehiclePenaltyRecordVO> getBlacklistDetail(VehiclePenaltyRecordParam param) {
        return illegalParkingBlacklistQueryService.getBlacklistDetail(param);
    }

}

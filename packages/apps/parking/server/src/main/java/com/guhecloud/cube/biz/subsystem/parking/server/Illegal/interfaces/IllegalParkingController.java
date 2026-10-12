package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.IllegalParkingCommandService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.IllegalParkingQueryService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param.IllegalParkingQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 违规停车接口
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/illegal/parking")
public class IllegalParkingController {

    private final IllegalParkingQueryService illegalParkingQueryService;
    private final IllegalParkingCommandService illegalParkingCommandService;

    /**
     * 新增车辆违停工单
     */
    @PostMapping("/submit")
    public String submitApproval(@RequestBody SubmitInitFormCommand submitInitFormCommand) {
        return illegalParkingCommandService.submitApproval(submitInitFormCommand);
    }

    /**
     * 车辆违停工单列表
     */
    @GetMapping("/list")
    public Pagination<IllegalParkingRecordVO> recordList(IllegalParkingQueryParam parkingQueryParam) {
        return illegalParkingQueryService.recordList(parkingQueryParam);
    }

}

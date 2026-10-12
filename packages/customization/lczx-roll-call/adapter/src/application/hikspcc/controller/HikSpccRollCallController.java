package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.controller;

import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.enums.CallTypeEnum;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.service.HikSpccRollCallService;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.param.PersonRollCallRecordParam;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.vo.PersonRollCallRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaofeng
 * @describe 海康 智慧监管综合平台 人员清点对接
 * @date 2024/10/31 15:07
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikSpccRollCallController {
    private final HikSpccRollCallService hikParkingService;

    /**
     * 采集点名任务记录
     *
     * @return 点名记录
     */
    @PostMapping("/hikspcc/rollcall-record")
    public List<PersonRollCallRecordVO> getRollCallRecord(@RequestBody @Valid PersonRollCallRecordParam param) {
        return this.hikParkingService.getRollCallRecord(param);
    }

    /**
     * 获取 人员点名类别枚举
     */
    @GetMapping("/hikspcc/call-type")
    public List<Map<String, Object>> getCallTypeEnum() {
        return Arrays.stream(CallTypeEnum.values()).map(callTypeEnum -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", callTypeEnum.getCode());
            map.put("desc", callTypeEnum.getDesc());
            return map;
        }).collect(Collectors.toList());
    }
}

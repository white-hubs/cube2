package com.guhecloud.cube.srv.gen.safety.management.notify.controller;

import com.guhecloud.cube.srv.gen.safety.management.notify.dto.request.AlarmNoticeModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.notify.dto.vo.AlarmNoticeVO;
import com.guhecloud.cube.srv.gen.safety.management.notify.service.AlarmNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmNoticeController {

    private final AlarmNoticeService alarmNoticeService;

    /**
     * 报警消息推送配置
     *
     * @param request
     */
    @PostMapping("/alarm-notice")
    public void save(@Valid @RequestBody AlarmNoticeModifyRequest request) {
        this.alarmNoticeService.save(request);
    }

    /**
     * 报警消息推送配置获取
     *
     * @param businessType
     *
     * @return
     */
    @GetMapping("/alarm-notice")
    public List<AlarmNoticeVO> getAlarmNotices(@RequestParam String businessType) {
        return this.alarmNoticeService.getAlarmNoticeConfigInfo(businessType, null);
    }
}

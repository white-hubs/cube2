package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileNoticeModifyParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileNoticeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileNoticeVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预案文件通知配置
 *
 * @author lanchun
 * @date 2024/5/17 14:53
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileNoticeController {

    private final EmergencyFileNoticeService emergencyFileNoticeService;

    /**
     * 预案文件消息配置保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/emergency-file-notice")
    public Long save(@Valid @RequestBody EmergencyFileNoticeModifyParam param) {
        return this.emergencyFileNoticeService.save(param);
    }

    /**
     * 预案文件消息配置详情
     *
     * @return
     */
    @GetMapping("/emergency-file-notice")
    public EmergencyFileNoticeVO getEmergencyFileNoticeByMsgSceneCode() {
        return this.emergencyFileNoticeService.getEmergencyFileNotice();
    }
}

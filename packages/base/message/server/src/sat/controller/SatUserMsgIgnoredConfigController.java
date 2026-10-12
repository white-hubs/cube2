package com.guhecloud.cube.base.message2.server.sat.controller;

import com.guhecloud.cube.base.message2.server.sat.entity.SatUserMsgIgnoredConfig;
import com.guhecloud.cube.base.message2.server.sat.param.SatUserMsgIgnoredConfigParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatUserMsgIgnoredConfigService;
import com.guhecloud.cube.base.message2.server.sat.vo.SatUserMsgIgnoredConfigVO;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户消息忽略配置接口管理
 *
 * @author lanchun
 * @date 2024/9/2 10:44
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatUserMsgIgnoredConfigController {

    private final SatUserMsgIgnoredConfigService satUserMsgIgnoredConfigService;

    /**
     * 用户消息忽略配置保存
     *
     * @param satUserMsgIgnoredConfigParam
     *
     * @return
     */
    @PostMapping("/sat-user-msg/ignored-config")
    public Long save(@Valid @RequestBody SatUserMsgIgnoredConfigParam satUserMsgIgnoredConfigParam) {
        return this.satUserMsgIgnoredConfigService.save(SecurityContextHolder.getPrincipal().getId(),
                                                        satUserMsgIgnoredConfigParam);
    }

    /**
     * 用户消息忽略配置开关修改
     *
     * @param enabled
     */
    @PutMapping("/sat-user-msg/ignored-config/enabled/{enabled}")
    public void updateEnabled(@PathVariable("enabled") Boolean enabled) {
        this.satUserMsgIgnoredConfigService.updateEnabled(SecurityContextHolder.getPrincipal().getId(), enabled);
    }

    /**
     * 用户消息忽略配置详情
     *
     * @return
     */
    @GetMapping("/sat-user-msg/ignored-config")
    public SatUserMsgIgnoredConfigVO getSatUserMsgIgnoredConfig() {
        SatUserMsgIgnoredConfig satUserMsgIgnoredConfig = this.satUserMsgIgnoredConfigService
                .getSatUserMsgIgnoredConfig(SecurityContextHolder.getPrincipal().getId());
        if (Objects.isNull(satUserMsgIgnoredConfig)) {
            return null;
        }
        return SatUserMsgIgnoredConfigVO.builder()
                .enabled(satUserMsgIgnoredConfig.getEnabled())
                .startTime(satUserMsgIgnoredConfig.getStartTime())
                .endTime(satUserMsgIgnoredConfig.getEndTime())
                .tomorrows(satUserMsgIgnoredConfig.getTomorrows())
                .channels(StringUtils.isNotBlank(satUserMsgIgnoredConfig.getChannels()) ?
                          Arrays.stream(satUserMsgIgnoredConfig.getChannels().split(",")).collect(Collectors.toSet())
                                                                                        : Collections.emptySet())
                .build();
    }
}

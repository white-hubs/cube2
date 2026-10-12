package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossJobConfigParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossJobConfigService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossJobConfigVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线损job配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossJobConfigController {
    private final LineLossJobConfigService lineLossJobConfigService;

    /**
     * 修改线损采集配置
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/lineloss/collectConfig/edit")
    public Boolean saveOrUpdateConfig(@RequestBody @Valid LineLossJobConfigParam param) {
        return lineLossJobConfigService.saveOrUpdateConfig(param);
    }

    /**
     * 线损采集配置详情
     *
     * @return 配置信息
     */
    @GetMapping("/lineloss/collectConfig/detail")
    public LineLossJobConfigVO detail() {
        return lineLossJobConfigService.detail();
    }

}

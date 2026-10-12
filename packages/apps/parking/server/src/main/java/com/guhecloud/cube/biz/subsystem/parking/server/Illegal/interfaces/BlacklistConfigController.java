package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.BlacklistConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.BlacklistConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.BlacklistConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.BlacklistConfigVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 黑名单阈值配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:36
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/blacklist/config")
public class BlacklistConfigController {
    private final BlacklistConfigService blacklistConfigService;

    /**
     * 新增或更新
     *
     * @param param
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody BlacklistConfigCommand param) {
        return blacklistConfigService.saveOrUpdate(param);

    }

    /**
     * 获取黑名单阈值配置
     */
    @PostMapping("/get")
    public BlacklistConfigVO getConfig() {
        return blacklistConfigService.getConfig();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Boolean deleted(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(BlacklistConfigPO.class, id);
    }

}

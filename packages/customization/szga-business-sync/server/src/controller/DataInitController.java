package com.guhecloud.cube.srv.gen.business.info.sync.server.controller;

import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserOneCardService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/13 15:09
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataInitController {
    private final SyncUserOneCardService syncUserOneCardService;

    /**
     * 一卡通数据初始化
     *
     * @param tenant
     * @param file
     */
    @GetMapping("/public/one/card/file")
    public void userOneCardInit(@RequestParam("tenant") Integer tenant, @RequestParam("file") MultipartFile file) {
        String result = "";
        try {
            SessionContextHolder.requestTenant(tenant);
            result = syncUserOneCardService.userOneCardInit(tenant, file);
        } catch (Exception e) {
            log.error("一卡通数据初始化失败:", e);
        }

    }
}

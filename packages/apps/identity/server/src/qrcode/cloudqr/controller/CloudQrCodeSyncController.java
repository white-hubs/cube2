package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.controller;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.CloudQrCodeSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 云二维码同步Controller
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeSyncController {

    private final CloudQrCodeSyncService syncService;

    /**
     * 同步二维码到云端
     */
    @PostMapping("/cloud-qr-code/sync")
    public void syncToCloud() {
        syncService.syncToCloud();
    }
}

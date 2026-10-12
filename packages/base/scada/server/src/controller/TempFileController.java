package com.guhecloud.kaihe.scada.server.controller;

import com.guhecloud.kaihe.scada.server.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TempFileController {

    private final FileService fileService;

    @PostMapping("/Temp")
    public String getTemp(@RequestParam("tempName") String tempName) {
        byte[] bytes = fileService.getFile(tempName, FileService.TEMP_DIAGRAMS_FOLDER);
        return bytes == null ? "" : new String(bytes);
    }

}

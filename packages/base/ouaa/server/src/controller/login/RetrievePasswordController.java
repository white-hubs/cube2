package com.guhecloud.cube.biz.ouaa.server.controller.login;

import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrievePasswordResetDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrievePasswordTokenDTO;
import com.guhecloud.cube.biz.ouaa.server.param.retrieve.RetrievePasswordResetParam;
import com.guhecloud.cube.biz.ouaa.server.param.retrieve.RetrievePasswordTokenParam;
import com.guhecloud.cube.biz.ouaa.server.service.retrieve.RetrievePasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RetrievePasswordController {
    private final RetrievePasswordService retrievePasswordService;

    /**
     * 获取密码修改token
     */
    @GetMapping("/retrieve-password/token")
    public RetrievePasswordTokenDTO getRetrievePasswordToken(@Valid @ModelAttribute RetrievePasswordTokenParam retrievePasswordTokenParam) {
        return retrievePasswordService.getToken(retrievePasswordTokenParam);
    }

    /**
     * 重新设置密码
     */
    @PutMapping("/retrieve-password/reset")
    public RetrievePasswordResetDTO resetPassword(@Valid @RequestBody RetrievePasswordResetParam retrievePasswordResetParam) {
        return retrievePasswordService.resetPassword(retrievePasswordResetParam);
    }
}

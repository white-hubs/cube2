package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.validator;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeRepository;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CloudQrCodeValidator {

    private final CloudQrCodeRepository cloudQrCodeRepository;

    public CloudQrCodeValidator(CloudQrCodeRepository cloudQrCodeRepository) {this.cloudQrCodeRepository = cloudQrCodeRepository;}

    public void validateCreateCloudQrCodeCommand(CreateQrCodeCommand command) {
        var byGrantClientCodeAndName = this.cloudQrCodeRepository.findByGrantClientIdAndName(command.getGrantClientId(), command.getName());
        Assert.isParameterError(Objects.nonNull(byGrantClientCodeAndName), "二维码名称已存在");
        var byGrantClientCodeAndCode = this.cloudQrCodeRepository.findByGrantClientIdAndCode(command.getGrantClientId(), command.getCode());
        Assert.isParameterError(Objects.nonNull(byGrantClientCodeAndCode), "二维码code已存在");
    }

    public void validateUpdateCloudQrCodeCommand(UpdateQrCodeCommand command) {
        if (Objects.nonNull(command.getId())) {
            var byId = this.cloudQrCodeRepository.findById(command.getId());
            Assert.isNotFound(Objects.isNull(byId), "二维码不存在");
        }
        if (Objects.nonNull(command.getCode())) {
            var existQrCode = this.cloudQrCodeRepository.findByGrantClientIdAndCode(SecurityContextHolder.getPrincipal()
                                                                                            .getAccount(), command.getCode());
            Assert.isNotFound(Objects.isNull(existQrCode), "二维码不存在");
        }
    }
}

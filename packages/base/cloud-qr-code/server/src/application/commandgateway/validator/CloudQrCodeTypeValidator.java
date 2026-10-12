package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.validator;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeTypeRepository;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CloudQrCodeTypeValidator {

    private final CloudQrCodeTypeRepository cloudQrCodeTypeRepository;

    public CloudQrCodeTypeValidator(CloudQrCodeTypeRepository cloudQrCodeTypeRepository
                                   ) {this.cloudQrCodeTypeRepository = cloudQrCodeTypeRepository;}

    public void validateCreateCloudQrTypeCommand(CreateQrCodeTypeCommand command) {
        var byGrantClientIdAndName = this.cloudQrCodeTypeRepository.findByGrantClientIdAndName(command.getGrantClientId(), command.getName());
        Assert.isParameterError(Objects.nonNull(byGrantClientIdAndName), "二维码类型名称已存在");
    }

    public void validateUpdateCloudQrTypeCommand(UpdateQrCodeTypeCommand command) {
        if (Objects.nonNull(command.getId())) {
            var byId = this.cloudQrCodeTypeRepository.findById(command.getId());
            Assert.isNotFound(Objects.isNull(byId), "二维码类型不存在");
            var byGrantClientCodeAndName = this.cloudQrCodeTypeRepository.findByGrantClientIdAndName(byId.getGrantClientId(), command.getName());
            Assert.isParameterError(
                    Objects.nonNull(byGrantClientCodeAndName) &&
                    !byGrantClientCodeAndName.getId().equals(command.getId()), "二维码类型名称已存在");
        }
        if (Objects.nonNull(command.getCode())) {
            var byCode = this.cloudQrCodeTypeRepository.findByGrantClientIdAndCode(SecurityContextHolder.getPrincipal()
                                                                                           .getAccount(), command.getCode());
            Assert.isNotFound(Objects.isNull(byCode), "二维码类型不存在");
            var byGrantClientCodeAndName = this.cloudQrCodeTypeRepository.findByGrantClientIdAndName(byCode.getGrantClientId(), command.getName());
            Assert.isParameterError(
                    Objects.nonNull(byGrantClientCodeAndName) &&
                    !byGrantClientCodeAndName.getId().equals(command.getId()), "二维码类型名称已存在");
        }
    }
}

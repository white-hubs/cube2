package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchCreateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchDeleteQrCodeTypeByCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchUpdateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.DeleteQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.validator.CloudQrCodeTypeValidator;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject.QrCodeType;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrCodeTypeConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeTypeRepository;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CloudQrCodeTypeCommandService {

    private final CloudQrCodeTypeRepository cloudQrCodeTypeRepository;
    private final CloudQrCodeTypeValidator cloudQrCodeTypeValidator;

    public CloudQrCodeTypeCommandService(CloudQrCodeTypeRepository cloudQrCodeTypeRepository,
                                         CloudQrCodeTypeValidator cloudQrCodeTypeValidator
                                        ) {
        this.cloudQrCodeTypeRepository = cloudQrCodeTypeRepository;
        this.cloudQrCodeTypeValidator = cloudQrCodeTypeValidator;
    }

    public QrCodeType createQrCodeTypeCommand(CreateQrCodeTypeCommand command) {
        cloudQrCodeTypeValidator.validateCreateCloudQrTypeCommand(command);
        var qrCodeType = QrCodeTypeConverters.commandToEntity(command);
        return cloudQrCodeTypeRepository.save(qrCodeType);
    }

    public QrCodeType updateQrCodeTypeCommand(UpdateQrCodeTypeCommand command) {
        cloudQrCodeTypeValidator.validateUpdateCloudQrTypeCommand(command);
        var qrCodeType = cloudQrCodeTypeRepository.findById(command.getId());
        qrCodeType = QrCodeTypeConverters.commandToEntity(command, qrCodeType);
        return cloudQrCodeTypeRepository.save(qrCodeType);
    }

    public QrCodeType updateQrCodeTypeByCodeCommand(UpdateQrCodeTypeCommand command) {
        cloudQrCodeTypeValidator.validateUpdateCloudQrTypeCommand(command);
        var qrCodeType = cloudQrCodeTypeRepository.findByGrantClientIdAndCode(SecurityContextHolder.getPrincipal()
                                                                                      .getAccount(), command.getCode());
        qrCodeType = QrCodeTypeConverters.commandToEntity(command, qrCodeType);
        return cloudQrCodeTypeRepository.save(qrCodeType);
    }

    public void deleteQrCodeTypeCommand(DeleteQrCodeTypeCommand command) {
        cloudQrCodeTypeRepository.deleteById(command.getId());
    }

    /**
     * 批量创建二维码类型（高性能）
     *
     * @param command 批量创建命令
     *
     * @return 创建的二维码类型列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCodeType> batchCreateQrCodeTypes(BatchCreateQrCodeTypeCommand command) {
        // 批量校验
        for (var item : command.getItems()) {
            cloudQrCodeTypeValidator.validateCreateCloudQrTypeCommand(item);
        }

        // 批量转换为实体
        var qrCodeTypes = command.getItems().stream()
                .map(QrCodeTypeConverters::commandToEntity)
                .toList();

        // 批量保存
        return cloudQrCodeTypeRepository.batchSave(qrCodeTypes);
    }

    /**
     * 批量更新二维码类型（高性能，通过code更新）
     *
     * @param command 批量更新命令
     *
     * @return 更新的二维码类型列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCodeType> batchUpdateQrCodeTypes(BatchUpdateQrCodeTypeCommand command) {
        // 批量校验
        for (var item : command.getItems()) {
            cloudQrCodeTypeValidator.validateUpdateCloudQrTypeCommand(item);
        }

        // 提取所有code
        var codes = command.getItems().stream()
                .map(UpdateQrCodeTypeCommand::getCode)
                .filter(Objects::nonNull)
                .toList();

        // 批量查询现有数据
        var existingQrCodeTypesMap = cloudQrCodeTypeRepository.findByCodes(codes);

        // 批量更新
        var qrCodeTypesToUpdate = new ArrayList<QrCodeType>();
        for (var item : command.getItems()) {
            var existingQrCodeType = existingQrCodeTypesMap.get(item.getCode());
            if (existingQrCodeType != null) {
                var updatedQrCodeType = QrCodeTypeConverters.commandToEntity(item, existingQrCodeType);
                qrCodeTypesToUpdate.add(updatedQrCodeType);
            }
        }

        // 批量保存
        return cloudQrCodeTypeRepository.batchUpdate(qrCodeTypesToUpdate);
    }

    /**
     * 批量通过code删除二维码类型
     *
     * @param command 批量删除命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteQrCodeTypesByCode(BatchDeleteQrCodeTypeByCodeCommand command) {
        cloudQrCodeTypeRepository.deleteByCode(command.getCodes());
    }
}

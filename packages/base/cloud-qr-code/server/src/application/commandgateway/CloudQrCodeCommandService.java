package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchCreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchDeleteQrCodeByCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchUpdateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.DeleteQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeEnabledCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.validator.CloudQrCodeValidator;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrCode;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrCodeConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeRepository;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CloudQrCodeCommandService {

    private final CloudQrCodeRepository cloudQrCodeRepository;
    private final CloudQrCodeValidator cloudQrCodeValidator;

    public CloudQrCodeCommandService(CloudQrCodeRepository cloudQrCodeRepository,
                                     CloudQrCodeValidator cloudQrCodeValidator
                                    ) {
        this.cloudQrCodeRepository = cloudQrCodeRepository;
        this.cloudQrCodeValidator = cloudQrCodeValidator;
    }

    public QrCode createCloudQrCodeCommand(CreateQrCodeCommand command) {
        cloudQrCodeValidator.validateCreateCloudQrCodeCommand(command);
        var qrCode = QrCodeConverters.commandToEntity(command);
        return cloudQrCodeRepository.save(qrCode);
    }

    public QrCode updateCloudQrCodeCommand(UpdateQrCodeCommand command) {
        cloudQrCodeValidator.validateUpdateCloudQrCodeCommand(command);
        var qrCode = cloudQrCodeRepository.findById(command.getId());
        qrCode = QrCodeConverters.commandToEntity(command, qrCode);
        return cloudQrCodeRepository.save(qrCode);
    }

    public QrCode updateCloudQrCodeEnabledCommand(UpdateQrCodeEnabledCommand command) {
        var qrCode = cloudQrCodeRepository.findById(command.getId());
        qrCode.setEnabled(command.getEnabled());
        return cloudQrCodeRepository.save(qrCode);
    }

    /**
     * 通过code更新二维码
     *
     * @param code    二维码code
     * @param command 更新命令
     *
     * @return 更新后的二维码
     */
    public QrCode updateCloudQrCodeByCodeCommand(String code, UpdateQrCodeCommand command) {
        cloudQrCodeValidator.validateUpdateCloudQrCodeCommand(command);
        var qrCode = cloudQrCodeRepository.findByGrantClientIdAndCode(
                SecurityContextHolder.getPrincipalProperty(Principal::getAccount), code
                                                                     );
        qrCode = QrCodeConverters.commandToEntity(command, qrCode);
        return cloudQrCodeRepository.save(qrCode);
    }

    public void deleteCloudQrCodeCommand(DeleteQrCodeCommand command) {
        cloudQrCodeRepository.deleteById(command.getId());
    }

    /**
     * 通过code删除二维码
     *
     * @param code 二维码code
     */
    public void deleteCloudQrCodeByCodeCommand(String code) {
        var qrCode = cloudQrCodeRepository.findByGrantClientIdAndCode(
                SecurityContextHolder.getPrincipalProperty(Principal::getAccount),
                code
                                                                     );
        if (qrCode == null) {
            throw new IllegalArgumentException("二维码不存在: " + code);
        }
        cloudQrCodeRepository.deleteById(qrCode.getId());
    }

    /**
     * 批量创建二维码（高性能）
     *
     * @param command 批量创建命令
     *
     * @return 创建的二维码列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCode> batchCreateQrCodes(BatchCreateQrCodeCommand command) {
        // 批量转换为实体
        var qrCodes = QrCodeConverters.commandToEntities(command);
        // 批量保存
        return cloudQrCodeRepository.batchSave(qrCodes);
    }

    /**
     * 批量更新二维码（高性能，通过code更新）
     *
     * @param command 批量更新命令
     *
     * @return 更新的二维码列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCode> batchUpdateQrCodes(BatchUpdateQrCodeCommand command) {
        // 提取所有code
        var codes = command.getItems().stream()
                .map(UpdateQrCodeCommand::getCode)
                .filter(Objects::nonNull)
                .toList();

        // 批量查询现有数据
        var existingQrCodesMap = cloudQrCodeRepository.findByCodes(codes);
        // 批量更新
        var qrCodesToUpdate = new ArrayList<QrCode>();
        for (var item : command.getItems()) {
            var existingQrCode = existingQrCodesMap.get(item.getCode());
            if (existingQrCode != null) {
                var updatedQrCode = QrCodeConverters.commandToEntity(item, existingQrCode);
                qrCodesToUpdate.add(updatedQrCode);
            }
        }
        // 批量保存
        return cloudQrCodeRepository.batchUpdate(qrCodesToUpdate);
    }

    /**
     * 批量通过code删除二维码
     *
     * @param command 批量删除命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteQrCodesByCode(BatchDeleteQrCodeByCodeCommand command) {
        cloudQrCodeRepository.deleteByCode(command.getCodes());
    }
}

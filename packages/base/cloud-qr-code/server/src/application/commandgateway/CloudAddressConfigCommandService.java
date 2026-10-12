package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.SaveAddressConfigCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.AddressConfig;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.AddressConfigConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudAddressConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 云地址配置CommandService
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudAddressConfigCommandService {

    private final CloudAddressConfigRepository cloudAddressConfigRepository;

    /**
     * 保存云地址配置
     * <p>
     * 由于表中只有一条数据，此方法会更新已存在的记录或创建新记录
     *
     * @param command 保存命令
     *
     * @return 保存后的配置实体
     */
    public AddressConfig saveAddressConfig(SaveAddressConfigCommand command) {
        // 查询现有配置
        AddressConfig existingConfig = cloudAddressConfigRepository.find();

        // 转换命令到实体
        AddressConfig addressConfig = AddressConfigConverters.commandToEntity(command, existingConfig);

        // 保存
        return cloudAddressConfigRepository.save(addressConfig);
    }
}

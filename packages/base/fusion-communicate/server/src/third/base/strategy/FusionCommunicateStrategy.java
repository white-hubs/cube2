package com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.IFusionCommunicateAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.IFusionCommunicateFile;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.IFusionCommunicateRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.IProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.constants.FusionCommunicateConfigConstant;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 厂家-实现类的映射器
 *
 * @author lanchun
 * @date 2024/7/31 14:40
 */
@Slf4j
@Component
public class FusionCommunicateStrategy {

    private final IProviderConfig iProviderConfig;

    private final Map<String, IFusionCommunicateAccount> fusionCommunicateMap;

    private final Map<String, IFusionCommunicateFile> fusionCommunicateFileMap;

    private final Map<String, IFusionCommunicateRoom> fusionCommunicateRoomMap;

    @Autowired
    public FusionCommunicateStrategy(
            IProviderConfig iProviderConfig,
            List<IFusionCommunicateAccount> fusionCommunicates,
            List<IFusionCommunicateFile> fusionCommunicateFiles,
            List<IFusionCommunicateRoom> fusionCommunicateRooms
                                    ) {
        this.iProviderConfig = iProviderConfig;
        this.fusionCommunicateMap = fusionCommunicates
                .stream().collect(Collectors.toMap(
                        IFusionCommunicateAccount::getProviderCode,
                        Function.identity()));
        this.fusionCommunicateFileMap = fusionCommunicateFiles
                .stream().collect(Collectors.toMap(
                        IFusionCommunicateFile::getProviderCode,
                        Function.identity()));
        this.fusionCommunicateRoomMap = fusionCommunicateRooms
                .stream().collect(Collectors.toMap(
                        IFusionCommunicateRoom::getProviderCode,
                        Function.identity()));
    }

    public IFusionCommunicateAccount getFusionCommunicateAccount() {
        ProviderConfig providerConfig = beforeCheck();
        IFusionCommunicateAccount fusionCommunicateMapOrDefault = fusionCommunicateMap
                .getOrDefault(providerConfig.getProviderCode(), null);
        if (Objects.isNull(fusionCommunicateMapOrDefault)) {
            throw new UnsupportedOperationException(
                    "not found " + providerConfig.getProviderCode() + " fusion com.guhecloud.cube.biz.fusion_communicateAccount impl");
        }
        return fusionCommunicateMapOrDefault;
    }

    public IFusionCommunicateFile getFusionCommunicateFile() {
        ProviderConfig providerConfig = beforeCheck();
        IFusionCommunicateFile fusionCommunicateFile = fusionCommunicateFileMap
                .getOrDefault(providerConfig.getProviderCode(), null);
        if (Objects.isNull(fusionCommunicateFile)) {
            throw new UnsupportedOperationException(
                    "not found " + providerConfig.getProviderCode() + " fusion com.guhecloud.cube.biz.fusion_communicateFile impl");
        }
        return fusionCommunicateFile;
    }

    public IFusionCommunicateRoom getFusionCommunicateRoom() {
        ProviderConfig providerConfig = beforeCheck();
        IFusionCommunicateRoom fusionCommunicateRoom = fusionCommunicateRoomMap
                .getOrDefault(providerConfig.getProviderCode(), null);
        if (Objects.isNull(fusionCommunicateRoom)) {
            throw new UnsupportedOperationException(
                    "not found " + providerConfig.getProviderCode() + " fusion com.guhecloud.cube.biz.fusion_communicateRoom impl");
        }
        return fusionCommunicateRoom;
    }

    public ProviderConfig beforeCheck() {
        ProviderConfig providerConfig = this.iProviderConfig.getProviderConfig(FusionCommunicateConfigConstant.CONFIG_KEY);
        if (Objects.isNull(providerConfig)) {
            throw new ResourceNotFoundException("缺失厂家配置");
        }
        return providerConfig;
    }
}

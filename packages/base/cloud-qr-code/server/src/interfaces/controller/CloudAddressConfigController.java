package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.controller;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.CloudAddressConfigCommandService;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.SaveAddressConfigCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudAddressConfigQueryService;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.AddressConfigConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudAddressConfigVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 云地址配置控制器
 * <p>
 * 注意：此配置表只有一条数据，保存操作会自动更新已存在的记录
 *
 * @author system
 */
@Slf4j
@RestController
@RequestMapping("/cloud-address-config")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudAddressConfigController {

    private final CloudAddressConfigCommandService cloudAddressConfigCommandService;
    private final CloudAddressConfigQueryService cloudAddressConfigQueryService;

    /**
     * 查询云地址配置
     * <p>
     * 返回表中唯一的配置记录
     */
    @GetMapping
    public CloudAddressConfigVO queryAddressConfig() {
        var addressConfig = cloudAddressConfigQueryService.queryAddressConfig();
        if (addressConfig == null) {
            return null;
        }
        return AddressConfigConverters.entityToVO(addressConfig);
    }

    /**
     * 保存云地址配置
     * <p>
     * 由于表中只有一条数据，此接口会自动更新已存在的记录或创建新记录
     */
    @PostMapping
    public CloudAddressConfigVO saveAddressConfig(@Valid @RequestBody SaveAddressConfigCommand command) {
        var addressConfig = cloudAddressConfigCommandService.saveAddressConfig(command);
        return AddressConfigConverters.entityToVO(addressConfig);
    }
}

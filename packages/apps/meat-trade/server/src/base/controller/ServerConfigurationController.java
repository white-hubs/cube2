package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.guhecloud.cube.biz.meattrade.server.base.entity.ServerConfiguration;
import com.guhecloud.cube.biz.meattrade.server.base.param.ServerConfigurationCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.ServerConfigurationUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.ServerConfigurationService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.ServerConfigurationVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lzy
 * @date 2021/9/13
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerConfigurationController {

    private final ServerConfigurationService serverConfigurationService;

    /**
     * 服务器配置信息新增
     *
     * @param param 服务器配置新增信息
     */
    @PostMapping("/sever-configuration")
    public void save(@Valid @RequestBody ServerConfigurationCreateParam param) {
        this.serverConfigurationService.save(param);
    }

    /**
     * 服务器配置信息编辑
     *
     * @param param 服务器配置编辑信息
     */
    @PutMapping("/server-configuration")
    public void update(@Valid @RequestBody ServerConfigurationUpdateParam param) {
        this.serverConfigurationService.update(param);
    }

    /**
     * 服务器配置信息ID查询
     *
     * @param id 服务器配置直接
     *
     * @return 服务器配置信息
     */
    @GetMapping("/server-configuration/{id}")
    public ServerConfigurationVO getById(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                                         @PathVariable("id") Long id
                                        ) {
        ServerConfiguration serverConfiguration = this.serverConfigurationService.getById(id);
        return ObjectUtils.copying(serverConfiguration, ServerConfigurationVO.class);
    }

    /**
     * 服务器配置信息查询
     *
     * @return 服务器配置信息
     */
    @GetMapping("/server-configuration/one")
    public ServerConfigurationVO getServeConfiguration() {
        List<ServerConfigurationVO> list = ObjectUtils.copying(this.serverConfigurationService.list(), ServerConfigurationVO.class);
        ServerConfigurationVO serverConfigurationVO = new ServerConfigurationVO();
        serverConfigurationVO.setId(null);
        return CollectionUtils.isEmpty(list) ? serverConfigurationVO : list.get(0);
    }
}

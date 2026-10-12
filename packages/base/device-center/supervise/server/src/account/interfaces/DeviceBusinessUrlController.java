package com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces;

import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceBusinessUrlCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.command.DeviceBusinessUrlModifyParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 待办业务路径配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceBusinessUrlController {

    private final DeviceBusinessUrlCommandService commandService;

    /**
     * 新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/device-business-url")
    public Long save(@Valid @RequestBody DeviceBusinessUrlModifyParam param) {
        return this.commandService.save(param);
    }

    /**
     * 修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/device-business-url/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody DeviceBusinessUrlModifyParam param) {
        this.commandService.update(id, param);
    }
}

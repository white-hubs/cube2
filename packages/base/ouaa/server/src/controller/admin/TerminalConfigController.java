package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.biz.ouaa.server.param.TerminalConfigParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalConfigQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalConfigService;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalConfigVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/13 15:35
 * @Description: 访问端配置
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TerminalConfigController {
    private final TerminalConfigService terminalConfigService;

    /**
     * 新增访问端配置
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/terminal/config")
    public Long save(@Valid @RequestBody TerminalConfigParam param) {
        return terminalConfigService.save(param);
    }

    /**
     * 修改访问端配置
     *
     * @param id
     * @param param
     */
    @PutMapping("/terminal/config/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "应用端主键不能小于1") Long id,
                       @Valid @RequestBody TerminalConfigParam param
                      ) {
        terminalConfigService.update(id, param);
    }

    /**
     * 删除访问端配置
     *
     * @param id
     */
    @DeleteMapping("/terminal/config/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "应用端主键不能小于1") Long id) {

        terminalConfigService.remove(id);
    }

    /**
     * 查询访问段列表
     *
     * @param param
     *
     * @return
     */

    @GetMapping("/terminal-config/list")
    public List<TerminalConfigVO> lists(TerminalConfigQueryParam param) {
        return this.terminalConfigService.lists(param);
    }
}

package com.guhecloud.cube.biz.ouaa.server.controller.context;

import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 访问端
 *
 * @author lanchun
 * @date 2022/6/10 15:49
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class ContextTerminalController {

    private final TerminalService terminalService;

    /**
     * 获取当前用户所登录产品的访问端列表
     *
     * @return
     */
    @GetMapping("/context-terminals")
    public List<TerminalVO> listTerminalByProductCode() {
        // 当前访问端
        return terminalService.listTenantTerminals();
    }

    @GetMapping("/context-terminals/{code}")
    public TerminalVO getTerminalByCode(@PathVariable("code") String code) {
        Terminal terminal = this.terminalService.getByCode(code);
        return ObjectUtils.copying(terminal, TerminalVO.class);
    }
}

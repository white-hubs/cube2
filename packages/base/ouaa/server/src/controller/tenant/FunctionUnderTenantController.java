package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.guhecloud.cube.biz.ouaa.server.entity.Function;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.vo.FunctionVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能管理接口
 *
 * @author Woody
 * @date 2021/7/19
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FunctionUnderTenantController {

    private final MenuFunctionService menuFunctionService;

    private final FunctionService functionService;

    /**
     * 功能列表(菜单主键查询)
     *
     * @param menuId
     *
     * @return
     */
    @GetMapping("/function-under-tenant/{menuId}")
    public List<FunctionVO> listFunctions(@PathVariable("menuId") Long menuId) {
        Set<Long> functionIds = this.menuFunctionService.listMenuFunctions(menuId).stream()
                .map(menuFunction -> menuFunction.getFunctionId()).collect(Collectors.toSet());
        List<Function> functions = this.functionService.listFunctions(functionIds);
        List<FunctionVO> functionVOS = ObjectUtils.copying(functions, FunctionVO.class);
        functionVOS.stream().forEach(functionVO -> {
            functionVO.setMenuId(menuId);
        });
        return functionVOS;
    }
}

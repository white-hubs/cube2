package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.biz.ouaa.server.entity.Function;
import com.guhecloud.cube.biz.ouaa.server.entity.MenuFunction;
import com.guhecloud.cube.biz.ouaa.server.param.FunctionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.vo.FunctionVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能管理接口
 *
 * @author lanchun
 * @date 2021/12/24 10:32
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class FunctionController {

    private final FunctionService functionService;

    private final MenuFunctionService menuFunctionService;

    /**
     * 功能新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/function")
    public Long save(@Valid @RequestBody FunctionModifyParam param) {
        return this.functionService.save(param);
    }

    /**
     * 功能修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/function/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody FunctionModifyParam param) {
        this.functionService.update(id, param);
    }

    /**
     * 功能列表(菜单主键查询)
     *
     * @param menuId
     *
     * @return
     */
    @GetMapping("/functions/menu/{menuId}")
    public List<FunctionVO> listFunctions(@PathVariable("menuId") Long menuId) {
        List<MenuFunction> menuFunctions = this.menuFunctionService.listMenuFunctions(menuId);
        if (CollectionUtils.isEmpty(menuFunctions)) {
            return Collections.emptyList();
        }
        Set<Long> functionIds = menuFunctions.stream().map(menuFunction -> menuFunction.getFunctionId()).collect(Collectors.toSet());
        List<Function> functions = this.functionService.listFunctions(functionIds);
        List<FunctionVO> functionVOS = ObjectUtils.copying(functions, FunctionVO.class);
        functionVOS.stream().forEach(functionVO -> {
            functionVO.setMenuId(menuId);
        });
        return functionVOS;
    }

    /**
     * 功能详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/function/{id}")
    public FunctionVO getFunction(@PathVariable("id") Long id) {
        Function function = this.functionService.getById(id);
        Assert.isNotFound(function, "功能不存在");
        return ObjectUtils.copying(function, FunctionVO.class);
    }

    /**
     * 功能删除
     *
     * @param id
     */
    @DeleteMapping("/function/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.functionService.remove(id);
    }
}

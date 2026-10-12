package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Function;
import com.guhecloud.cube.biz.ouaa.server.param.FunctionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 功能业务操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
public interface FunctionService extends IService<Function> {

    Long save(FunctionModifyParam param);

    void update(Long id, FunctionModifyParam param);

    Function getById(Serializable id);

    List<Function> listFunctions(Collection<Long> ids);

    void remove(Long id);

    List<MenuFunctionVO> getAllMenuFunction();
}

package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 访问端业务操作接口
 *
 * @author Woody
 * @date 2021/7/16
 */
public interface TerminalService extends IService<Terminal> {

    Long save(TerminalModifyParam param);

    void update(Long id, TerminalModifyParam param);

    void remove(Long id);

    Terminal getById(Serializable id);

    Terminal getByCode(String code);

    List<Terminal> listByIds(Collection<? extends Serializable> idList);

    Page<Terminal> listByPage(TerminalQueryParam param);

    List<Terminal> listProductTerminals(Long productId);

    List<Terminal> listProductTerminals(Collection<Long> productIds);

    List<String> getTenantTerminal();

    List<TerminalVO> listTenantTerminals();

    List<TerminalVO> listTerminals();

    Page<Terminal> lisTenantByPage(TerminalQueryParam param);
}

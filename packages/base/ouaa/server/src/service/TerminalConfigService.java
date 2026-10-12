package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalConfig;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalConfigParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalConfigQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalConfigVO;

import java.util.List;

/**
 * 终端配置服务接口
 */
public interface TerminalConfigService extends IService<TerminalConfig> {

    /**
     * 新增
     *
     * @param param
     *
     * @return
     */
    Long save(TerminalConfigParam param);

    /**
     * 更新
     *
     * @param id
     * @param param
     */
    void update(Long id, TerminalConfigParam param);

    /**
     * 修改
     *
     * @param id
     */
    void remove(Long id);

    /**
     * 查询列表
     *
     * @param param
     *
     * @return
     */
    List<TerminalConfigVO> lists(TerminalConfigQueryParam param);
}

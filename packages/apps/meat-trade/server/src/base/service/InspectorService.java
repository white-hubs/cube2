package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.InspectorUpdateParam;

public interface InspectorService extends IService<Inspector> {

    /**
     * 抽检员新增
     *
     * @param param 抽检员新增信息
     */
    void save(InspectorCreateParam param);

    /**
     * 抽检员更新
     *
     * @param param 抽检员更新信息
     */
    void update(InspectorUpdateParam param);

    /**
     * 抽检员分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 抽检员分页信息
     */
    Page<Inspector> page(InspectorQueryParam queryParam);

    /**
     * 抽检员删除
     *
     * @param id 抽检员主键
     */
    void remove(Long id);

    /**
     * 根据抽检员名称获取抽检员信息
     *
     * @param name 抽检员名称
     *
     * @return 抽检员信息
     */
    Inspector getByName(String name);

}

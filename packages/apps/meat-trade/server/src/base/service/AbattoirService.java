package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirBimStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirUpdateParam;

import java.util.Collection;
import java.util.List;

public interface AbattoirService extends IService<Abattoir> {

    /**
     * 屠宰场新增
     *
     * @param createParam 新增参数
     */
    Long save(AbattoirCreateParam createParam);

    /**
     * 屠宰场编辑
     *
     * @param updateParam 编辑参数
     */
    void update(AbattoirUpdateParam updateParam);

    /**
     * 屠宰场列表集合
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场列表集合
     */
    List<Abattoir> list(AbattoirQueryParam queryParam);

    /**
     * 屠宰场分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场信息
     */
    Page<Abattoir> page(AbattoirQueryParam queryParam);

    /**
     * 屠宰场报表统计查询
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场信息
     */
    Page<AbattoirStatDTO> statPage(AbattoirQueryParam queryParam);

    /**
     * 屠宰场报表统计(按商品品类分类统计)
     *
     * @param queryParam
     *
     * @return
     */
    Page<AbattoirStatDTO> statGoodsCategoryPage(AbattoirQueryParam queryParam);

    /**
     * 屠宰场供货数据信息
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场信息
     */
    Page<AbattoirBimStatDTO> bimStatPage(AbattoirQueryParam queryParam);

    List<AbattoirBimStatDTO> bimStatList(AbattoirQueryParam queryParam);

    /**
     * 根据屠宰场主键获取屠宰场集合
     *
     * @param ids 屠宰场主键集合
     *
     * @return 屠宰场信息集合
     */
    List<Abattoir> getByIds(Collection<Long> ids, Integer tenant);

    /**
     * 屠宰场删除
     *
     * @param id 屠宰场主键
     */
    void remove(Long id);

    /**
     * 根据屠宰场名称查询屠宰场
     *
     * @param name 屠宰场名称
     *
     * @return 屠宰场信息
     */
    Abattoir getByName(String name);

    /**
     * 根据编号查询屠宰场
     *
     * @param code 屠宰场编号
     *
     * @return 屠宰场信息
     */
    Abattoir getByCode(String code);

    Abattoir generateCodeAndAttr(String name);
}

package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteNav;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteNavVO;

import java.util.List;

public interface SysSiteNavService extends IService<SysSiteNav> {

    void add(SysSiteNavParam param);
    void update(Long id, SysSiteNavParam param);
    List<SysSiteNavVO> list(SysSiteNavQueryParam param);
    SysSiteNavVO detail(Long id);
    void toggleStatus(Long id, Integer status);
    void delete(Long id);

}

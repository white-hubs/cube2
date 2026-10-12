package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSitePage;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePageVO;

import java.util.List;

public interface SysSitePageService extends IService<SysSitePage> {

    void add(SysSitePageParam param);
    void update(SysSitePageParam param);
    List<SysSitePageVO> list(SysSitePageQueryParam param);
    SysSitePageVO detail(Long id);
    void delete(Long id);

}

package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Dictionary;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryUpdateParam;

import java.util.List;

public interface DictionaryService extends IService<Dictionary> {

    void save(DictionaryCreateParam param);

    void update(DictionaryUpdateParam param);

    void initSpotCheckItem(Integer tenant);

    Page<Dictionary> page(DictionaryQueryParam queryParam);

    List<Dictionary> getByType(String type);

}

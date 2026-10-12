package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.HomeApplicationCard;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationCardBatchModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationCardModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface HomeApplicationCardService extends IService<HomeApplicationCard> {

    void save(HomeApplicationCardModifyParam param);

    void saveBatch(HomeApplicationCardBatchModifyParam param);

    HomeApplicationCard getHomeApplicationCardByCode(String code);

    List<HomeApplicationCard> listHomeApplicationCards();

    List<HomeApplicationCard> listHomeApplicationCards(Collection<? extends Serializable> applicationIds);

    void removeByCardCode(String cardCode);
}

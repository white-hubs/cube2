package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Card;
import com.guhecloud.cube.biz.ouaa.server.param.CardModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.CardQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface CardService extends IService<Card> {

    Long save(CardModifyParam param);

    void update(Long id, CardModifyParam param);

    Card getById(Serializable id);

    List<Card> listCards(CardQueryParam param);

    List<Card> listByTerminalApplicationIds(Long terminalId, String keyword, Collection<? extends Serializable> applicationIds);

    List<Card> listBasicByTerminalApplicationIds(Long terminalId, String keyword);

    List<Card> listByTerminalApplication(Long terminalId, Long applicationId);

    List<Card> listByIds(Collection<? extends Serializable> idList);

    void remove(Long id);

    void remove(Long terminalId, Long applicationId, String code);
}

package com.guhecloud.cube.biz.metric.server.rule.repository;

import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleLibraryPO;

import java.util.List;

public interface RuleLibraryRepository {

    Long insertConfig(RuleLibraryPO scoreRange);

    Long updateConfig(RuleLibraryPO scoreRange);

    void removeConfig(Long id);

    RuleLibraryPO findById(Long id);

    List<RuleLibraryPO> getConfigs();
}

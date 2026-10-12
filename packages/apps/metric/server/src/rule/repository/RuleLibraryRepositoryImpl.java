package com.guhecloud.cube.biz.metric.server.rule.repository;

import com.guhecloud.cube.biz.metric.server.rule.repository.dao.RuleLibraryMapper;
import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleLibraryPO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
public class RuleLibraryRepositoryImpl implements RuleLibraryRepository {

    private final RuleLibraryMapper ruleLibraryMapper;

    public RuleLibraryRepositoryImpl(RuleLibraryMapper ruleLibraryMapper
                                    ) {this.ruleLibraryMapper = ruleLibraryMapper;}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertConfig(RuleLibraryPO scoreRange) {
        this.ruleLibraryMapper.insert(scoreRange);
        return scoreRange.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateConfig(RuleLibraryPO scoreRange) {
        this.ruleLibraryMapper.updateById(scoreRange);
        return scoreRange.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeConfig(Long id) {
        DatabaseContextHolder.softDelete(RuleLibraryPO.class, Collections.singleton(id));
    }

    @Override
    public RuleLibraryPO findById(Long id) {
        return this.ruleLibraryMapper.selectById(id);
    }

    @Override
    public List<RuleLibraryPO> getConfigs() {
        return this.ruleLibraryMapper.selectList(null);
    }
}

package com.guhecloud.cube.biz.metric.server.metric.library.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.metric.server.metric.library.query.MetricLibraryQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.dao.MetricLibraryMapper;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.po.MetricLibraryPO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public class MetricLibraryRepositoryImpl implements MetricLibraryRepository {

    private final MetricLibraryMapper metricLibraryMapper;

    public MetricLibraryRepositoryImpl(MetricLibraryMapper metricLibraryMapper) {this.metricLibraryMapper = metricLibraryMapper;}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(MetricLibraryPO metricLibraryPO) {
        metricLibraryMapper.insert(metricLibraryPO);
        return metricLibraryPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(MetricLibraryPO metricLibraryPO) {
        metricLibraryMapper.updateById(metricLibraryPO);
        return metricLibraryPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        DatabaseContextHolder.softDelete(MetricLibraryPO.class, Collections.singleton(id));
    }

    @Override
    public MetricLibraryPO findById(Long id) {
        return metricLibraryMapper.selectById(id);
    }

    @Override
    public MetricLibraryPO findByName(String name) {
        List<MetricLibraryPO> libraryPOS = metricLibraryMapper.selectList(Wrappers.<MetricLibraryPO>lambdaQuery()
                                                                                  .eq(MetricLibraryPO::getName, name));
        if (CollectionUtils.isEmpty(libraryPOS)) {
            return null;
        }
        return libraryPOS.get(0);
    }

    @Override
    public List<MetricLibraryPO> findByIds(Collection<Long> ids) {
        return metricLibraryMapper.selectList(Wrappers.<MetricLibraryPO>lambdaQuery()
                                                      .in(MetricLibraryPO::getId, ids));
    }

    @Override
    public List<MetricLibraryPO> findAll() {
        return metricLibraryMapper.selectList(null);
    }

    @Override
    public List<MetricLibraryPO> findByCodes(Collection<String> codes) {
        return metricLibraryMapper.selectList(Wrappers.<MetricLibraryPO>lambdaQuery()
                                                      .in(MetricLibraryPO::getCode, codes));
    }

    @Override
    public Page<MetricLibraryPO> findAll(MetricLibraryQueryParam param) {
        return metricLibraryMapper.selectPage(PagingUtils.convert(param), Wrappers.<MetricLibraryPO>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), MetricLibraryPO::getName, param.getKeyword())
                .orderByDesc(MetricLibraryPO::getCreateTime));
    }
}

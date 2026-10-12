package com.guhecloud.cube.biz.metric.server.metric.library.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.metric.server.metric.library.query.MetricLibraryQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.po.MetricLibraryPO;

import java.util.Collection;
import java.util.List;

public interface MetricLibraryRepository {

    Long save(MetricLibraryPO metricLibraryPO);

    Long update(MetricLibraryPO metricLibraryPO);

    void removeById(Long id);

    MetricLibraryPO findById(Long id);

    MetricLibraryPO findByName(String name);

    List<MetricLibraryPO> findByIds(Collection<Long> ids);

    List<MetricLibraryPO> findAll();

    List<MetricLibraryPO> findByCodes(Collection<String> codes);

    Page<MetricLibraryPO> findAll(MetricLibraryQueryParam param);
}

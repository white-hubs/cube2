package com.guhecloud.cube.biz.metric.server.metric.library.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.metric.server.metric.library.query.MetricLibraryQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.MetricLibraryRepository;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.po.MetricLibraryPO;
import com.guhecloud.cube.biz.metric.server.metric.library.vo.MetricLibraryVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class MetricLibraryQueryService {

    private final MetricLibraryRepository metricLibraryRepository;

    private final UserClient userClient;

    public MetricLibraryQueryService(MetricLibraryRepository metricLibraryRepository, UserClient userClient
                                    ) {
        this.metricLibraryRepository = metricLibraryRepository;
        this.userClient = userClient;
    }

    public Pagination<MetricLibraryVO> getMetricLibraryPage(MetricLibraryQueryParam param) {
        Page<MetricLibraryPO> metricLibraryPage = this.metricLibraryRepository.findAll(param);
        if (CollectionUtils.isEmpty(metricLibraryPage.getRecords())) {
            return Pagination.empty();
        }
        List<MetricLibraryVO> metricLibraryVOS = ObjectUtils.copying(metricLibraryPage.getRecords(), MetricLibraryVO.class);
        metricLibraryVOS.forEach(metricLibraryVO -> {
            UserDTO user;
            if (StringUtils.isNotBlank(metricLibraryVO.getCreatorId()) &&
                ActorTypeEnum.USER.name().equals(metricLibraryVO.getCreatorId())) {
                user = userClient.getUser(Long.valueOf(metricLibraryVO.getCreatorId()));
            } else {
                user = userClient.getUserByUserName(metricLibraryVO.getCreateBy());
            }
            if (Objects.nonNull(user)) {
                metricLibraryVO.setCreateByName(user.getName());
            }
        });
        return PagingUtils.convert(metricLibraryPage, metricLibraryVOS);
    }

    public MetricLibraryVO getMetricLibraryDetail(Long id) {
        MetricLibraryPO metricLibraryPO = this.metricLibraryRepository.findById(id);
        return ObjectUtils.copying(metricLibraryPO, MetricLibraryVO.class);
    }

    public List<MetricLibraryVO> getMetricLibraries() {
        List<MetricLibraryPO> metricLibraryPOS = this.metricLibraryRepository.findAll();
        return ObjectUtils.copying(metricLibraryPOS, MetricLibraryVO.class);
    }

    public List<MetricLibraryVO> getMetricLibraryByCodes(Collection<String> codes) {
        List<MetricLibraryPO> metricLibraryPOS = this.metricLibraryRepository.findByCodes(codes);
        return ObjectUtils.copying(metricLibraryPOS, MetricLibraryVO.class);
    }
}

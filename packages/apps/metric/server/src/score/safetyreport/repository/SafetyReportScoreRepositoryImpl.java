package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.param.SafetyReportQueryParam;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.dao.SafetyReportScoreMapper;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.po.SafetyReportScorePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.CompressUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class SafetyReportScoreRepositoryImpl implements SafetyReportScoreRepository {

    private final SafetyReportScoreMapper safetyReportScoreMapper;

    public SafetyReportScoreRepositoryImpl(SafetyReportScoreMapper safetyReportScoreMapper) {
        this.safetyReportScoreMapper = safetyReportScoreMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Score saveScore(Score score) {
        this.removeByRequestId(score.getRequestId());
        SafetyReportScorePO safetyReportScorePO = new SafetyReportScorePO();
        safetyReportScorePO.setName(score.getName());
        safetyReportScorePO.setRequestId(score.getRequestId());
        safetyReportScorePO.setBeginTime(score.getBeginTime());
        safetyReportScorePO.setEndTime(score.getEndTime());
        safetyReportScorePO.setScore(score.getScore());
        safetyReportScorePO.setSuggestion(score.getSuggestion());
        try {
            safetyReportScorePO.setData(CompressUtils.gzip(Objects.requireNonNull(JsonUtils.serialize(score))));
        } catch (IOException e) {
            log.error("规则指标原数据压缩失败:", e);
        }
        this.safetyReportScoreMapper.insert(safetyReportScorePO);
        return score;
    }

    @Override
    public Score getScoreByRequestId(String requestId) {
        List<SafetyReportScorePO> safetyReportScorePOS = this.safetyReportScoreMapper.selectList(Wrappers.<SafetyReportScorePO>lambdaQuery()
                                                                                                         .eq(SafetyReportScorePO::getRequestId, requestId)
                                                                                                );
        if (CollectionUtils.isEmpty(safetyReportScorePOS)) {
            return null;
        }
        return convert(safetyReportScorePOS.get(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRequestId(String requestId) {
        List<SafetyReportScorePO> safetyReportScorePOS = this.safetyReportScoreMapper.selectList(Wrappers.<SafetyReportScorePO>lambdaQuery()
                                                                                                         .eq(SafetyReportScorePO::getRequestId, requestId)
                                                                                                );
        if (!CollectionUtils.isEmpty(safetyReportScorePOS)) {
            Set<Long> ids = safetyReportScorePOS.stream().map(SafetyReportScorePO::getId).collect(Collectors.toSet());
            DatabaseContextHolder.softDelete(SafetyReportScorePO.class, ids);
        }
    }

    @Override
    public Pagination<Score> getPage(SafetyReportQueryParam param) {
        Page<SafetyReportScorePO> safetyReportScorePOPage = this.safetyReportScoreMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                                            .<SafetyReportScorePO>lambdaQuery()
                                                                                                            .like(StringUtils.isNotBlank(param.getKeyword()), SafetyReportScorePO::getName, param.getKeyword())
                                                                                                            .orderByDesc(SafetyReportScorePO::getCreateTime)
                                                                                                   );
        if (CollectionUtils.isEmpty(safetyReportScorePOPage.getRecords())) {
            return Pagination.empty();
        }
        List<Score> scores = safetyReportScorePOPage.getRecords().stream().map(this::convert).collect(Collectors.toList());
        return PagingUtils.convert(safetyReportScorePOPage, scores);
    }

    public Score convert(SafetyReportScorePO safetyReportScorePO) {
        try {
            return JsonUtils.deserialize(CompressUtils.ungzip(safetyReportScorePO.getData()), Score.class);
        } catch (IOException e) {
            log.error("解压数据失败:", e);
        }
        return null;
    }
}

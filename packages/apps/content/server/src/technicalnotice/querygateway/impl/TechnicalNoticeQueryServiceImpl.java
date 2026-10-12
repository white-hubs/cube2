package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.dao.TechnicalNoticeMapper;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.po.TechnicalNotice;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.TechnicalNoticeQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.query.TechnicalNoticeCollectionQuery;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TechnicalNoticeQueryServiceImpl extends ServiceImpl<TechnicalNoticeMapper, TechnicalNotice> implements TechnicalNoticeQueryService {

    @Override
    public Page<TechnicalNotice> pageTechnicalNotice(@NonNull TechnicalNoticeCollectionQuery query) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), TechnicalNotice::getTitle, query.getKeyword())
                .ge(Objects.nonNull(query.getPeriod()) && Objects.nonNull(query.getPeriod()
                                                                                  .getBegin()), TechnicalNotice::getNoticeTime, ObjectUtils.ifNull(query.getPeriod(), Period::getBegin))
                .le(Objects.nonNull(query.getPeriod()) && Objects.nonNull(query.getPeriod()
                                                                                  .getEnd()), TechnicalNotice::getNoticeTime, ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .page(PagingUtils.convert(query));
    }
}

package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.po.TechnicalNotice;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.query.TechnicalNoticeCollectionQuery;

/**
 * @author lzy
 * @date 2022/5/20
 */
public interface TechnicalNoticeQueryService extends IService<TechnicalNotice> {

    /**
     * 技术通报分页信息
     *
     * @param query 查询参数
     *
     * @return 技术通报分页信息
     */
    Page<TechnicalNotice> pageTechnicalNotice(TechnicalNoticeCollectionQuery query);
}

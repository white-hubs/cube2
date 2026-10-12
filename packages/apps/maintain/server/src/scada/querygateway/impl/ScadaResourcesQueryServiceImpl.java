package com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.scada.dao.ScadaResourcesMapper;
import com.guhecloud.cube.srv.gen.maintain.server.scada.interfaces.vo.ScadaResourcesVO;
import com.guhecloud.cube.srv.gen.maintain.server.scada.po.ScadaResourcesPO;
import com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway.ScadaResourcesQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway.query.ScadaResourcesQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScadaResourcesQueryServiceImpl extends ServiceImpl<ScadaResourcesMapper, ScadaResourcesPO> implements ScadaResourcesQueryService {

    @Override
    public ScadaResourcesVO getVOById(Long id) {
        ScadaResourcesPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检计划");
        return ObjectUtils.copying(po, ScadaResourcesVO.class);
    }

    @Override
    public Pagination<ScadaResourcesVO> getPage(ScadaResourcesQueryParam param) {
        Page<ScadaResourcesPO> page = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(ScadaResourcesPO::getName, param.getKeyword())
                    )
                .eq(StringUtils.isNotEmpty(param.getCategoryCode()), ScadaResourcesPO::getCategoryCode, param.getCategoryCode())
                .page(PagingUtils.convert(param));

        return PagingUtils.convert(page, ObjectUtils.copying(page.getRecords(), ScadaResourcesVO.class));
    }

    @Override
    public List<ScadaResourcesVO> getList(ScadaResourcesQueryParam param) {
        List<ScadaResourcesPO> pos = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(ScadaResourcesPO::getName, param.getKeyword())
                    )
                .eq(StringUtils.isNotEmpty(param.getCategoryCode()), ScadaResourcesPO::getCategoryCode, param.getCategoryCode())
                .list();

        return ObjectUtils.copying(pos, ScadaResourcesVO.class);
    }
}

package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTypeQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTypeQueryServiceImpl extends ServiceImpl<InspectTypeMapper, InspectTypePO> implements InspectTypeQueryService {

    @Override
    public Pagination<InspectTypeVO> getPage(InspectTypeQueryParam queryParam) {
        Page<InspectTypePO> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), InspectTypePO::getName, queryParam.getKeyword())
                .orderByDesc(InspectTypePO::getCreateTime)
                .page(PagingUtils.convert(queryParam));

        return PagingUtils.convert(page, InspectTypeVO.class);
    }

    @Override
    public List<InspectTypeVO> getList(InspectTypeQueryParam queryParam) {
        List<InspectTypePO> list = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), InspectTypePO::getName, queryParam.getKeyword())
                .list();

        return ObjectUtils.copying(list, InspectTypeVO.class);
    }

}

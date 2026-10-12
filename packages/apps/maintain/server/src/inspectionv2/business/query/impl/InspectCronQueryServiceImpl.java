package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectCronQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectCronMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectCronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectCronVO;
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
public class InspectCronQueryServiceImpl extends ServiceImpl<InspectCronMapper, InspectCronPO> implements InspectCronQueryService {

    @Override
    public InspectCronVO getVoById(Long id) {
        InspectCronPO po = this.getById(id);
        return ObjectUtils.copying(po, InspectCronVO.class);
    }

    @Override
    public Pagination<InspectCronVO> getPage(InspectCronQueryParam queryParam) {
        Page<InspectCronPO> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), InspectCronPO::getName, queryParam.getKeyword())
                .orderByDesc(InspectCronPO::getCreateTime)
                .page(PagingUtils.convert(queryParam));

        return PagingUtils.convert(page, InspectCronVO.class);
    }

    @Override
    public List<InspectCronVO> getList(InspectCronQueryParam queryParam) {
        List<InspectCronPO> list = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), InspectCronPO::getName, queryParam.getKeyword())
                .orderByDesc(InspectCronPO::getCreateTime)
                .list();

        return ObjectUtils.copying(list, InspectCronVO.class);
    }

}

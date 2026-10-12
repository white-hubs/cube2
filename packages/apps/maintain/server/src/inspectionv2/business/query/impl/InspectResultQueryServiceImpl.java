package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectResultQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectResultMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultSimpleVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectResultQueryServiceImpl extends ServiceImpl<InspectResultMapper, InspectResultPO> implements InspectResultQueryService {

    @Override
    public InspectResultVO getById(Long id) {

        List<InspectResultPO> pos = this.lambdaQuery()
                .eq(InspectResultPO::getId, id)
                .list();
        InspectResultVO vo = null;
        if (CollectionUtils.isNotEmpty(pos)) {
            InspectResultPO po = pos.get(0);
            vo = ObjectUtils.copying(po, InspectResultVO.class);

            List<InspectResultOptionCommandModel> items = ObjectUtils.copying(pos, InspectResultOptionCommandModel.class);
            vo.setOptions(items);
        }
        return vo;
    }

    @Override
    public InspectResultSimpleVO getSimpleVOById(Long id) {

        List<InspectResultPO> pos = this.lambdaQuery()
                .eq(InspectResultPO::getId, id)
                .list();

        InspectResultSimpleVO vo = null;
        if (CollectionUtils.isNotEmpty(pos)) {
            InspectResultPO po = pos.get(0);
            vo = ObjectUtils.copying(po, InspectResultSimpleVO.class);

            List<InspectResultOptionCommandModel> items = ObjectUtils.copying(pos, InspectResultOptionCommandModel.class);
            vo.setOptions(items);
        }
        return vo;
    }

    @Override
    public Pagination<InspectResultVO> getPage(InspectResultQueryParam queryParam) {
        Page<InspectResultPO> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), InspectResultPO::getName, queryParam.getKeyword())
                .orderByDesc(InspectResultPO::getCreateTime)
                .groupBy(InspectResultPO::getCode)
                .page(PagingUtils.convert(queryParam));
        List<InspectResultVO> collect = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList())
                .stream().map(po -> {
                    InspectResultVO vo = ObjectUtils.copying(po, InspectResultVO.class);
                    this.extendItemName(vo);
                    return vo;
                }).collect(Collectors.toList());

        return PagingUtils.convert(page, collect);
    }

    @Override
    public List<InspectResultVO> getList(InspectResultQueryParam queryParam) {
        List<InspectResultPO> pos = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(queryParam.getIdList()), InspectResultPO::getId, queryParam.getIdList())
                .eq(Objects.nonNull(queryParam.getEnabled()), InspectResultPO::getEnable, queryParam.getEnabled())
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), InspectResultPO::getName, queryParam.getKeyword())
                .list();

        List<InspectResultVO> vos = new ArrayList<>();
        Optional.ofNullable(pos).orElse(Collections.emptyList())
                .stream().collect(Collectors.groupingBy(InspectResultPO::getId))
                .forEach((id, ls) -> {
                    InspectResultVO vo = ObjectUtils.copying(ls.get(0), InspectResultVO.class);
                    vo.setOptions(ObjectUtils.copying(ls, InspectResultOptionCommandModel.class));
                    vos.add(vo);
                });
        return vos;
    }

    @Override
    public List<InspectResultOptionCommandModel> getOptions(Long id) {
        List<InspectResultPO> pos = this.lambdaQuery()
                .eq(InspectResultPO::getId, id)
                .list();

        return ObjectUtils.copying(pos, InspectResultOptionCommandModel.class);
    }

    private void extendItemName(InspectResultVO vo) {
        List<InspectResultPO> resultPOS = this.lambdaQuery()
                .eq(InspectResultPO::getId, vo.getId())
                .select(InspectResultPO::getItemName)
                .list();

        String itemNames = Optional.ofNullable(resultPOS).orElse(Collections.emptyList())
                .stream().map(InspectResultPO::getItemName)
                .collect(Collectors.joining(","));

        vo.setItemNames(itemNames);
    }

}

package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectItemTemplateQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectResultQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectItemTemplateMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemTemplateQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectItemTemplateVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultSimpleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectItemTemplateQueryServiceImpl extends ServiceImpl<InspectItemTemplateMapper, InspectItemTemplatePO> implements InspectItemTemplateQueryService {
    private final InspectResultQueryService inspectResultQueryService;

    @Override
    public Pagination<InspectItemTemplateVO> getPage(InspectItemTemplateQueryParam param) {
        Page<InspectItemTemplatePO> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getReferType()), InspectItemTemplatePO::getReferType, param.getReferType())
                .like(StringUtils.isNotEmpty(param.getKeyword()), InspectItemTemplatePO::getAlias, param.getKeyword())
                .page(PagingUtils.convert(param));
        Pagination<InspectItemTemplateVO> pagination = PagingUtils.convert(page, InspectItemTemplateVO.class);
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            pagination.getRecords().forEach(d -> {
                InspectResultSimpleVO inspectResult = inspectResultQueryService.getSimpleVOById(d.getResultId());
                if (inspectResult != null && !CollectionUtils.isEmpty(inspectResult.getOptions())) {
                    Map<String, String> map = inspectResult.getOptions().stream()
                            .collect(Collectors.toMap(InspectResultOptionCommandModel::getItemValue, InspectResultOptionCommandModel::getItemName));
                    d.setImageResultNames(this.handlerItemNames(d.getImageResult(), map));
                    d.setRemarkResultNames(this.handlerItemNames(d.getRemarkResult(), map));
                    d.setResultNames(inspectResult.getOptions().stream()
                                             .map(InspectResultOptionCommandModel::getItemName).collect(Collectors.joining(",")));
                    InspectResultOptionCommandModel option = inspectResult.getOptions().stream()
                            .filter(i -> i.getItemValue().equals(d.getResultItemValue())).findFirst().orElse(null);
                    d.setResultItemName(option != null ? option.getItemName() : "");
                }
                d.setReferTypeNames(Arrays.stream(d.getReferType().split(","))
                                            .map(t -> InspectTargetCategoryEnum.valueOf(t).getDescription())
                                            .collect(Collectors.joining(",")));
            });
        }
        return pagination;
    }

    private String handlerItemNames(String valueString, Map<String, String> map) {
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(valueString)) {
            StringBuilder imageBuffer = new StringBuilder();
            String[] strs = valueString.split(",");
            for (String str : strs) {
                if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(map.get(str))) {
                    imageBuffer.append(map.get(str)).append(",");
                }
            }
            if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(imageBuffer)) {
                return imageBuffer.deleteCharAt(imageBuffer.length() - 1).toString();
            }
        }
        return null;
    }

    @Override
    public InspectItemTemplateVO getInspectItemDetail(Long id) {
        InspectItemTemplatePO po = this.baseMapper.selectById(id);
        Assert.isNotFound(po, "未查询到相关信息");
        InspectItemTemplateVO vo = ObjectUtils.copying(po, InspectItemTemplateVO.class);
        InspectResultSimpleVO inspectResult = inspectResultQueryService.getSimpleVOById(vo.getResultId());
        vo.setResultVO(inspectResult);
        return vo;
    }
}

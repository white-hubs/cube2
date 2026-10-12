package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectResultQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardTypeReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultSimpleVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardQueryServiceImpl extends ServiceImpl<InspectStandardMapper, InspectStandardPO> implements InspectStandardQueryService {

    private final InspectStandardMapper standardMapper;

    private final InspectStandardItemQueryService standardItemQueryService;
    private final InspectStandardTypeReferQueryService standardTypeReferQueryService;
    private final InspectResultQueryService resultQueryService;
    private final InspectTargetInfoQuery targetInfoQuery;
    private final RiskSourceLedgerClient riskSourceLedgerClient;

    @Override
    public InspectStandardVO getVOById(Long id) {
        InspectStandardPO po = this.getById(id);
        Assert.isNotFound(po, "ID不存在");

        InspectStandardVO vo = ObjectUtils.copying(po, InspectStandardVO.class);

        vo.setStandardItems(standardItemQueryService.getListByStandardId(vo.getId()));
        vo.setStandardTypeRefers(standardTypeReferQueryService.getListByStandardId(vo.getId()));
        return vo;
    }

    @Override
    public Pagination<InspectStandardVO> getPage(InspectStandardQueryParam param) {
        QueryWrapper<Object> wrapper = Wrappers.query()
                .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                        .like("s.name", param.getKeyword())
                    )
                .eq(StringUtils.isNotBlank(param.getTargetCategory()), "r.target_category", param.getTargetCategory());
        if (Boolean.TRUE.equals(param.getTargetTypeEq())) {
            wrapper.eq(StringUtils.isNotBlank(param.getTargetType()), "r.target_type", param.getTargetType());
        } else {
            wrapper.like(StringUtils.isNotBlank(param.getTargetType()), "r.target_type", param.getTargetType());
        }
        wrapper.groupBy("s.id");
        wrapper.orderByDesc("s.name");

        Page<InspectStandardVO> page = standardMapper.getPage(PagingUtils.convert(param), wrapper);

        List<InspectStandardVO> vos = page.getRecords().stream().map(po -> {
            InspectStandardVO vo = InspectStandardVO.builder()
                    .id(po.getId())
                    .code(po.getCode())
                    .name(po.getName())
                    .remark(po.getRemark())
                    .enable(po.getEnable())
                    .defaultStandard(po.isDefaultStandard())
                    .build();
            this.extendStandardItemNames(vo);
            if (param.getFlagDetails() != null && param.getFlagDetails()) {
                this.extendResult(vo);
            }
            return vo;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, vos);
    }

    @Override
    public List<InspectStandardVO> getList(InspectStandardQueryParam param) {

        List<InspectStandardVO> vos = standardMapper.getList(Wrappers.query()
                                                                     .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                                                                             .like("s.name", param.getKeyword())
                                                                         )
                                                                     .eq(StringUtils.isNotBlank(param.getTargetCategory()), "r.target_category", param.getTargetCategory())
                                                                     .like(StringUtils.isNotBlank(param.getTargetType()), "r.target_type", param.getTargetType())
                                                            );
        if (vos.size() > 0) {
            vos.stream().forEach(vo -> {
                this.extendStandardItemNames(vo);
                if (param.getFlagDetails() != null && param.getFlagDetails()) {
                    this.extendResult(vo);
                }
            });
        }

        return vos;
    }

    @Override
    public List<InspectStandardVO> getListWithResult(List<Long> standardIds) {
        List<InspectStandardPO> pos = this.listByIds(standardIds);
        List<InspectStandardVO> vos = Optional.ofNullable(pos).orElse(Collections.emptyList())
                .parallelStream().map(po -> {
                    InspectStandardVO vo = ObjectUtils.copying(po, InspectStandardVO.class);
                    this.extendResult(vo);
                    return vo;
                }).collect(Collectors.toList());
        return vos;
    }

    /**
     * 根据 目标对象ID 获取标准
     *
     * @param targetCategory 目标类别
     * @param targetId       目标ID
     *
     * @return 巡检标准
     */
    @Override
    public InspectStandardTargetVO getVoByTarget(String targetCategory, Long targetId) {
        InspectStandardTargetVO vo = null;

        List<InspectTargetInfoDTO> infoDTOS = targetInfoQuery.getInspectObject(targetCategory, Collections.singletonList(targetId), null);
        if (infoDTOS != null && !infoDTOS.isEmpty()) {
            InspectTargetInfoDTO infoDTO = infoDTOS.get(0);
            vo = ObjectUtils.copying(infoDTO, InspectStandardTargetVO.class);
            /* 如果是档位或者现场点需要查询其关联的风险源 */
            if (InspectTargetCategoryEnum.STALL.name().equals(targetCategory) ||
                InspectTargetCategoryEnum.SITE_POINT.name().equals(targetCategory)) {
                RiskSourceLedgerPageParam riskSourceLedgerPageParam = new RiskSourceLedgerPageParam();
                if (InspectTargetCategoryEnum.SITE_POINT.name().equals(targetCategory)) {
                    riskSourceLedgerPageParam.setPrintIdList(Collections.singletonList(targetId));
                } else {
                    riskSourceLedgerPageParam.setStallIdList(Collections.singletonList(targetId));
                }
                Map<Long, List<RiskSourceLedgerVO>> riskSourceMap = riskSourceLedgerClient.postRiskSourceByStallOrPoint(riskSourceLedgerPageParam);
                if (riskSourceMap != null) {
                    List<KeyValueDTO> childTargetList = new ArrayList<>();
                    riskSourceMap.values().forEach(list -> list.forEach(r -> childTargetList.add(KeyValueDTO.builder()
                                                                                                         .key(InspectTargetCategoryEnum.RISK_SOURCE.name())
                                                                                                         .longKey(r.getId())
                                                                                                         .value(r.getName()).build())));
                    vo.setChildTargetNameList(childTargetList);
                }
            }

            // 获取关联的默认标准
            if (StringUtils.isNotBlank(infoDTO.getTargetType())) {
                List<Long> defaultLongList = new ArrayList<>();
                List<InspectStandardTypeReferPO> referPOList = standardTypeReferQueryService.list(Wrappers
                                                                                                          .<InspectStandardTypeReferPO>lambdaQuery()
                                                                                                          .select(InspectStandardTypeReferPO::getStandardId)
                                                                                                          .eq(InspectStandardTypeReferPO::getTargetCategory, targetCategory)
                                                                                                          .eq(InspectStandardTypeReferPO::getTargetType, infoDTO.getTargetType())
                                                                                                          .eq(InspectStandardTypeReferPO::getDefaultStandard, "1")
                                                                                                 );
                Optional.ofNullable(referPOList).orElse(Collections.emptyList())
                        .forEach(referPO -> defaultLongList.add(referPO.getStandardId()));

                if (!defaultLongList.isEmpty()) {
                    vo.setStandardVOS(this.getListWithResult(defaultLongList));
                }
            }
        }

        return vo;
    }

    @Override
    public List<InspectStandardVO> getStandardsByTargetType(String targetCategory, String targetType) {
        List<InspectStandardTypeReferPO> refers = standardTypeReferQueryService.list(Wrappers.<InspectStandardTypeReferPO>lambdaQuery()
                                                                                             .eq(InspectStandardTypeReferPO::getTargetCategory, targetCategory)
                                                                                             .eq(InspectStandardTypeReferPO::getDefaultStandard, "1")
                                                                                             .eq(InspectStandardTypeReferPO::getTargetType, targetType));
        if (CollectionUtils.isNotEmpty(refers)) {
            List<Long> standardIds = refers.stream().map(InspectStandardTypeReferPO::getStandardId).collect(Collectors.toList());
            //查询标准
            List<InspectStandardPO> standards = standardMapper.selectBatchIds(standardIds);
            if (CollectionUtils.isNotEmpty(standards)) {
                return standards.stream().map(s -> {
                    InspectStandardVO vo = ObjectUtils.copying(s, InspectStandardVO.class);
                    //查询巡检标准中的巡检项
                    List<InspectStandardItemVO> items = standardItemQueryService.getListByStandardId(vo.getId());
                    if (CollectionUtils.isNotEmpty(items)) {
                        items.forEach(d -> {
                            InspectResultSimpleVO inspectResult = resultQueryService.getSimpleVOById(d.getResultId());
                            if (inspectResult != null &&
                                !com.guhecloud.platform.base.util.CollectionUtils.isEmpty(inspectResult.getOptions())) {
                                d.setResultNames(inspectResult.getOptions().stream()
                                                         .map(InspectResultOptionCommandModel::getItemName)
                                                         .collect(Collectors.joining(",")));
                                InspectResultOptionCommandModel option = inspectResult.getOptions().stream()
                                        .filter(i -> i.getItemValue().equals(d.getResultItemValue())).findFirst().orElse(null);
                                d.setResultItemName(option != null ? option.getItemName() : "");
                            }
                            if (StringUtils.isNotBlank(d.getReferType())) {
                                d.setReferTypeNames(Arrays.stream(d.getReferType().split(","))
                                                            .map(t -> InspectTargetCategoryEnum.valueOf(t).getDescription())
                                                            .collect(Collectors.joining(",")));
                            }
                        });
                    }
                    vo.setStandardItems(items);
                    return vo;
                }).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private void extendResult(InspectStandardVO vo) {
        //查询巡检项
        vo.setStandardItems(standardItemQueryService.getListByStandardId(vo.getId()));
        //查询巡检结果
        this.buildStandardItem(vo.getStandardItems());
    }

    private void buildStandardItem(List<InspectStandardItemVO> standardItemVOS) {
        if (CollectionUtils.isNotEmpty(standardItemVOS)) {
            standardItemVOS.forEach(item -> {
                item.setResultVO(resultQueryService.getSimpleVOById(item.getResultId()));
            });
        }
    }

    private void extendStandardItemNames(InspectStandardVO vo) {
        List<InspectStandardItemPO> standardItemPOS = standardItemQueryService.lambdaQuery()
                .eq(InspectStandardItemPO::getStandardId, vo.getId())
                .list();
        if (CollectionUtils.isNotEmpty(standardItemPOS)) {
            vo.setStandardItems(ObjectUtils.copying(standardItemPOS, InspectStandardItemVO.class));
            vo.setStandardItemNames(standardItemPOS.stream().map(InspectStandardItemPO::getName).collect(Collectors.joining(",")));
        }
        if (CollectionUtils.isNotEmpty(vo.getStandardItems())) {
            vo.getStandardItems().forEach(d -> {
                InspectResultSimpleVO inspectResult = resultQueryService.getSimpleVOById(d.getResultId());
                if (inspectResult != null && CollectionUtils.isNotEmpty(inspectResult.getOptions())) {
                    Map<String, String> map = inspectResult.getOptions().stream()
                            .collect(Collectors.toMap(InspectResultOptionCommandModel::getItemValue, InspectResultOptionCommandModel::getItemName));
                    d.setResultNames(inspectResult.getOptions().stream().map(InspectResultOptionCommandModel::getItemName)
                                             .collect(Collectors.joining(",")));
                    d.setImageResultNames(this.handlerItemNames(d.getImageResult(), map));
                    d.setRemarkResultNames(this.handlerItemNames(d.getRemarkResult(), map));
                    InspectResultOptionCommandModel option = inspectResult.getOptions().stream()
                            .filter(i -> i.getItemValue().equals(d.getResultItemValue())).findFirst().orElse(null);
                    d.setResultItemName(option != null ? option.getItemName() : "");
                }
            });

        }
    }

    private String handlerItemNames(String valueString, Map<String, String> map) {
        if (StringUtils.isNotBlank(valueString)) {
            StringBuilder imageBuffer = new StringBuilder();
            String[] strs = valueString.split(",");
            for (String str : strs) {
                if (StringUtils.isNotBlank(map.get(str))) {
                    imageBuffer.append(map.get(str)).append(",");
                }
            }
            if (StringUtils.isNotBlank(imageBuffer)) {
                return imageBuffer.deleteCharAt(imageBuffer.length() - 1).toString();
            }
        }
        return null;
    }

}

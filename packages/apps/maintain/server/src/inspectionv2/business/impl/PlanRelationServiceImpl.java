package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanRelationService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectResultQueryServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.PlanRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.PlanRelationDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.ItemResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlanRelationServiceImpl extends ServiceImpl<PlanRelationMapper, PlanRelationPO> implements PlanRelationService {

    private final PlanRelationMapper planRelationMapper;

    private final InspectResultQueryServiceImpl inspectResultQueryService;

    private final InspectTargetInfoQuery targetInfoQuery;

    /**
     * 根据计划id查询计划的对象(分层次)
     *
     * @param param
     *
     * @return
     */
    @Override
    public Pagination<List<InspectTargetInfoDTO>> getTargetInfoByPlanId(PlanDetailParam param) {
        Assert.isParameterError(param.getId() == null, "id不能为空");
        Pagination<List<InspectTargetInfoDTO>> result = Pagination.empty();

        List<Long> targetIdList = new ArrayList<>();
        Page<PlanRelationPO> page = null;
        if (param.getPageNumber() != null && param.getPageSize() != null) {
            // 如果给了分页参数就分页查询
            page = planRelationMapper.selectPage(new Page<>(param.getPageNumber(), param.getPageSize()), Wrappers
                    .<PlanRelationPO>lambdaQuery()
                    .eq(PlanRelationPO::getPlanId, param.getId())
                    .eq(StringUtils.isNotBlank(param.getTargetCategory()), PlanRelationPO::getTargetCategory, param.getTargetCategory())
                    .like(StringUtils.isNotBlank(param.getTargetName()), PlanRelationPO::getTargetName, param.getTargetName())
                    .groupBy(PlanRelationPO::getTargetId));
            page.getRecords().forEach(r -> targetIdList.add(r.getTargetId()));
            if (CollectionUtils.isEmpty(targetIdList)) {
                return result;
            }
        }

        List<PlanRelationPO> planRelationPOList = planRelationMapper.selectList(Wrappers.<PlanRelationPO>lambdaQuery()
                                                                                        .eq(PlanRelationPO::getPlanId, param.getId())
                                                                                        .eq(StringUtils.isNotBlank(param.getTargetCategory()), PlanRelationPO::getTargetCategory, param.getTargetCategory())
                                                                                        .like(StringUtils.isNotBlank(param.getTargetName()), PlanRelationPO::getTargetName, param.getTargetName())
                                                                                        .in(CollectionUtils.isNotEmpty(targetIdList), PlanRelationPO::getTargetId, targetIdList)
                                                                               );
        if (CollectionUtils.isEmpty(planRelationPOList)) {
            return result;
        }
        List<List<InspectTargetInfoDTO>> resultList = new ArrayList<>();
        Map<String, List<PlanRelationPO>> map = planRelationPOList.stream()
                .collect(Collectors.groupingBy(p -> p.getTargetCategory().name()));
        Set<Long> standardIds = new HashSet<>();
        Set<Long> tempItemIds = new HashSet<>();
        Set<Long> itemIds = new HashSet<>();
        Set<Long> resultIds = new HashSet<>();
        for (PlanRelationPO po : planRelationPOList) {
            standardIds.add(po.getStandardId());
            if (ObjectUtils.isEmpty(po.getIsRawItem()) || !Boolean.TRUE.equals(po.getIsRawItem())) {
                tempItemIds.add(po.getItemId());
            } else {
                itemIds.add(po.getItemId());
            }
        }
        Map<Long, InspectionStandardVO> inspectionStandardVOMap;
        if (CollectionUtils.isNotEmpty(standardIds)) {
            inspectionStandardVOMap = Optional.ofNullable(planRelationMapper.getObjStandardList(standardIds))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(InspectionStandardVO::getId, Function.identity()));
        } else {
            inspectionStandardVOMap = new HashMap<>();
        }
        Map<Long, InspectionItemVO> tempItemVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(tempItemIds)) {
            Optional.ofNullable(planRelationMapper.getTemplateItem(tempItemIds))
                    .orElse(new ArrayList<>()).forEach(t -> {
                        tempItemVOMap.putIfAbsent(t.getId(), t);
                        if (Objects.nonNull(t.getResultId())) {
                            resultIds.add(t.getResultId());
                        }
                    });
        }
        Map<Long, InspectionItemVO> itemVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(itemIds)) {
            Optional.ofNullable(planRelationMapper.getStandardItem(itemIds))
                    .orElse(new ArrayList<>()).forEach(t -> {
                        itemVOMap.putIfAbsent(t.getId(), t);
                        if (Objects.nonNull(t.getResultId())) {
                            resultIds.add(t.getResultId());
                        }
                    });
        }
        Map<Long, InspectResultVO> resultVOMap;
        if (CollectionUtils.isNotEmpty(resultIds)) {
            InspectResultQueryParam resultParam = new InspectResultQueryParam();
            resultParam.setIdList(new ArrayList<>(resultIds));
            resultVOMap = inspectResultQueryService.getList(resultParam)
                    .stream().collect(Collectors.toMap(InspectResultVO::getId, Function.identity()));
        } else {
            resultVOMap = new HashMap<>();
        }
        map.forEach((type, typeList) -> {
            Map<Long, Long> targetNatureMap = new HashMap<>();
            Set<Long> targetIdSet = new HashSet<>();
            typeList.forEach(r -> {
                targetIdSet.add(r.getTargetId());
                targetNatureMap.put(r.getTargetId(), r.getTargetParentId());
            });
            List<InspectTargetInfoDTO> targetInfoDTOList = targetInfoQuery.getTargetInfo(new ArrayList<>(targetIdSet), type);
            targetInfoDTOList.forEach(vo -> {
                PlanRelationPO relationPO = typeList.get(0);
                vo.setTargetParentId(targetNatureMap.get(vo.getTargetId()));
                vo.setLocation(relationPO.getLocation());
                List<PlanRelationPO> pos = typeList.stream().filter(t -> t.getTargetId().equals(vo.getTargetId()))
                        .collect(Collectors.toList());
                List<InspectionStandardVO> standardList = getStandardVOList(pos, inspectionStandardVOMap, tempItemVOMap, itemVOMap, resultVOMap);
                vo.setStandardDTOList(standardList);
            });
            resultList.add(targetInfoDTOList);
        });
        if (page != null) {
            return PagingUtils.convert(page, resultList);
        } else {
            return new Pagination<>((long) resultList.size(), 1L, resultList);
        }
    }

    private static List<InspectionStandardVO> getStandardVOList(List<PlanRelationPO> pos,
                                                                Map<Long, InspectionStandardVO> inspectionStandardVOMap,
                                                                Map<Long, InspectionItemVO> tempItemVOMap,
                                                                Map<Long, InspectionItemVO> itemVOMap,
                                                                Map<Long, InspectResultVO> resultVOMap
                                                               ) {
        Map<Long, List<PlanRelationPO>> collect = pos.stream().collect(Collectors.groupingBy(PlanRelationPO::getStandardId));
        List<InspectionStandardVO> standardList = new ArrayList<>();

        collect.forEach((k, v) -> {
            InspectionStandardVO standard = inspectionStandardVOMap.get(k);
            if (standard != null) {
                List<InspectionItemVO> itemList = new ArrayList<>();
                v.forEach(po -> {
                    InspectionItemVO item;
                    if (ObjectUtils.isEmpty(po.getIsRawItem()) || !Boolean.TRUE.equals(po.getIsRawItem())) {
                        item = tempItemVOMap.get(po.getItemId());
                    } else {
                        item = itemVOMap.get(po.getItemId());
                    }
                    if (item != null) {
                        item.setStandardId(item.getStandardId());
                        item.setResultVO(resultVOMap.get(item.getResultId()));
                        itemList.add(item);
                    }
                });
                standard.setStandardItems(itemList);
                standardList.add(standard);
            }
        });
        return standardList;
    }

    @Override
    public Pagination<InspectTargetInfoDTO> selectPlanTargetPage(PlanPageParam param) {
        StringBuilder orderByBuff = new StringBuilder(
                "'" + InspectTargetCategoryEnum.STALL.name() + "','" + InspectTargetCategoryEnum.SITE_POINT.name() + "','" +
                InspectTargetCategoryEnum.RISK_SOURCE.name());
        for (InspectTargetCategoryEnum value : InspectTargetCategoryEnum.values()) {
            if (!InspectTargetCategoryEnum.STALL.name().equals(value.name()) &&
                !InspectTargetCategoryEnum.SITE_POINT.name().equals(value.name()) &&
                !InspectTargetCategoryEnum.RISK_SOURCE.name().equals(value.name())) {
                orderByBuff.append("','").append(value.name());
            }
        }
        Page<PlanRelationPO> page = this.baseMapper.selectRelationTarget(PagingUtils.convert(param), param.getPlanId(), param.getTargetCategory(), orderByBuff
                .append("'").toString());
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<PlanRelationPO> planRelationPOList = page.getRecords();

        List<InspectTargetInfoDTO> recordList = new ArrayList<>();
        Map<String, List<PlanRelationPO>> map = planRelationPOList.stream()
                .collect(Collectors.groupingBy(p -> p.getTargetCategory().name(), LinkedHashMap::new, Collectors.toList()));
        map.forEach((type, typeList) -> {
            Map<Long, Long> targetNatureMap = new HashMap<>();
            Set<Long> targetIdSet = new HashSet<>();
            typeList.forEach(r -> {
                targetIdSet.add(r.getTargetId());
                targetNatureMap.put(r.getTargetId(), r.getTargetParentId());
            });
            List<InspectTargetInfoDTO> targetInfoDTOList = targetInfoQuery.getTargetInfo(new ArrayList<>(targetIdSet), type);
            targetInfoDTOList.forEach(vo -> {
                PlanRelationPO relationPO = typeList.get(0);
                vo.setTargetParentId(targetNatureMap.get(vo.getTargetId()));
                vo.setLatAndLon(relationPO.getLatAndLon());
                vo.setLocation(relationPO.getLocation());
                vo.setStandardDTOList(this.getObjStandardList(relationPO.getPlanId(), vo.getTargetId()));
                recordList.add(vo);
            });
        });
        return PagingUtils.convert(page, recordList);
    }

    /**
     * 根据计划/任务id和对象id查询其巡检计划
     *
     * @param plainId
     * @param targetId
     *
     * @return
     */
    @Override
    public List<StandardResult> getStandardList(Long plainId, Long targetId) {
        Assert.isParameterError(plainId == null || targetId == null, "planId 或 targetId 为空");
        List<StandardResult> resultList = new ArrayList<>();
        // 查询巡检计划原始数据（扁平化数据）
        List<PlanRelationDTO> relationDTOList = planRelationMapper.selectRelationListRaw(plainId, targetId);
        // 查询巡检计划新加数据
        List<PlanRelationDTO> relationDTOAdd = planRelationMapper.selectRelationListAdd(plainId, targetId);

        // 合并两个数据就是全部的数据
        relationDTOList.addAll(relationDTOAdd);
        /**
         * 处理层级
         */
        relationDTOList.stream().collect(Collectors.groupingBy(PlanRelationDTO::getStandardId)).forEach((k, v) -> {
            StandardResult standardResult = new StandardResult();
            standardResult.setStandardId(k);
            standardResult.setStandardName(v.get(0).getStandardName());
            standardResult.setTargetCategory(v.get(0).getTargetCategory());
            /**
             * 处理计划里面的巡检项
             */
            List<ItemResult> itemList = new ArrayList<>();
            v.forEach(r -> {
                ItemResult itemResult = ObjectUtils.copying(r, ItemResult.class);
                itemResult.setItemId(r.getItemId());
                itemResult.setItemName(r.getItemName());
                itemResult.setResultNames(r.getResultNames());
                itemResult.setDefaultResult(r.getDefaultResult());
                InspectTargetInfoDTO targetInfoDTO = targetInfoQuery.getTargetInfoById(r.getTargetId(), r.getTargetCategory());
                if (targetInfoDTO != null) {
                    itemResult.setReferTypeNames(targetInfoDTO.getTargetTypeName());
                }
                itemList.add(itemResult);
            });
            // 设置巡检项列表
            standardResult.setItemList(itemList);
            resultList.add(standardResult);
        });
        // 返回
        return resultList;
    }

    /**
     * @Author wql
     * @Description 查询对象关联的标准信息
     * @Date 14:05 2023/4/21
     **/
    @Override
    public List<InspectionStandardVO> getObjStandardList(Long planId, Long objId) {
        List<InspectionStandardVO> standardList = new ArrayList<>();
        List<PlanRelationPO> pos = planRelationMapper.selectList(Wrappers.<PlanRelationPO>lambdaQuery()
                                                                         .eq(PlanRelationPO::getPlanId, planId)
                                                                         .eq(PlanRelationPO::getTargetId, objId));
        Map<Long, List<PlanRelationPO>> collect = pos.stream().collect(Collectors.groupingBy(PlanRelationPO::getStandardId));
        collect.forEach((k, v) -> {
            List<InspectionStandardVO> objStandardList = planRelationMapper.getObjStandardList(Collections.singleton(k));
            if (CollectionUtils.isNotEmpty(objStandardList)) {
                InspectionStandardVO standard = objStandardList.get(0);
                List<InspectionItemVO> itemList = new ArrayList<>();
                v.forEach(po -> {
                    List<InspectionItemVO> items;
                    if (ObjectUtils.isEmpty(po.getIsRawItem()) || !Boolean.TRUE.equals(po.getIsRawItem())) {
                        items = planRelationMapper.getTemplateItem(Collections.singleton(po.getItemId()));
                    } else {
                        items = planRelationMapper.getStandardItem(Collections.singleton(po.getItemId()));
                    }
                    if (CollectionUtils.isNotEmpty(items)) {
                        InspectionItemVO item = items.get(0);
                        item.setStandardId(item.getStandardId());
                        item.setResultVO(inspectResultQueryService.getById(item.getResultId()));
                        itemList.add(item);
                    }
                });
                standard.setStandardItems(itemList);
                standardList.add(standard);
            }
        });
        return standardList;
    }

    @Override
    public PlanRelationPO getObjInfo(Long planId, Long objId) {
        List<PlanRelationPO> list = planRelationMapper.selectList(Wrappers.<PlanRelationPO>lambdaQuery()
                                                                          .eq(PlanRelationPO::getPlanId, planId)
                                                                          .eq(PlanRelationPO::getTargetId, objId));
        return ObjectUtils.isNotEmpty(list) ? list.get(0) : null;
    }
}

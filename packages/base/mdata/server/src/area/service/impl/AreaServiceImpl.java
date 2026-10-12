package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.dto.AreaIdTypeDTO;
import com.guhecloud.cube.biz.mdata.server.area.converters.AreaConverterForAreaListVO;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaMapper;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaImportDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaSortEditDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.SpaceAreaImportDTO;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaExtend;
import com.guhecloud.cube.biz.mdata.server.area.enumeration.VisualizationAreaTypeEnum;
import com.guhecloud.cube.biz.mdata.server.area.enumeration.WhereSymbolEnum;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaCreateParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaExtendParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaUpdateParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaExtendService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaCacheInfo;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaListVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaStatisticsVo;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.EnergyAreaVo;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridAreaExtend;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridRelationMember;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaExtendService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridRelationMemberService;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.database.util.EntityUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.mdata.server.area.service.AreaBatchOptService.areaByIdMap;
import static com.guhecloud.cube.biz.mdata.server.area.service.AreaBatchOptService.areaByPathNameMap;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;

    private final AreaExtendService areaExtendService;

    private final GridAreaExtendService gridAreaExtendService;

    private final GridRelationMemberService gridRelationMemberService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull AreaCreateParam param) {
        Area areaByName = this.getByName(param.getName(), param.getParentId());
        Assert.isConflict(Objects.nonNull(areaByName), "存在相同区域名称");
        Area area = ObjectUtils.copying(param, Area.class);
        area.setCode(this.generateCode());
        this.save(area);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAreaExtent(@NonNull AreaCreateParam param) {
        Area areaByName = this.getByName(param.getName(), param.getParentId());
        Assert.isConflict(Objects.nonNull(areaByName), "存在相同区域名称");
        Area area = ObjectUtils.copying(param, Area.class);

        List<Area> areaList = this.lambdaQuery().select(Area::getSort)
                .eq(Area::getParentId, param.getParentId())
                .orderByAsc(Area::getSort)
                .list();
        //初始化序号
        if (!CollectionUtils.isEmpty(areaList)) {
            area.setSort(areaList.get(areaList.size() - 1).getSort() + 1);
        } else {
            area.setSort(1);
        }
        area.setCode(this.generateCode());
        this.save(area);
        if (Objects.nonNull(param.getAreaExtend())) {
            this.areaExtendService.save(area.getId(), param.getAreaExtend());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AreaUpdateParam param) {
        Area area = this.getById(param.getId());
        Assert.isNotFound(area, "不存在区域信息");
        Area areaByName = this.getByName(param.getName(), area.getParentId());
        var resultIdNotEqual = Objects.nonNull(areaByName) && !Objects.equals(areaByName.getId(), area.getId());
        Assert.isConflict(resultIdNotEqual, "存在相同区域名称");
        ObjectUtils.copying(param, area);
        this.updateById(area);
    }

    @Override
    public void updateAreaExtent(@NonNull AreaUpdateParam param) {
        this.update(param);
        if (Objects.isNull(param.getAreaExtend())) {
            param.setAreaExtend(new AreaExtendParam());
        }
        this.areaExtendService.save(param.getId(), param.getAreaExtend());
    }

    @Override
    public List<Area> getList(@NonNull AreaQueryParam queryParam) {
        LambdaQueryChainWrapper<Area> queryWrapper = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Area::getName, queryParam.getKeyword())
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), Area::getId, queryParam.getIds())
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Area::getName, queryParam.getKeyword())
                .eq(queryParam.getStatusId() != null, Area::getStatusId, queryParam.getStatusId())
                .like(queryParam.getFeatureId() != null, Area::getFeatureIds, queryParam.getFeatureId())
                .like(queryParam.getTagId() != null, Area::getTagIds, queryParam.getTagId());
        this.extSymbolWhere(queryWrapper, queryParam);
        queryWrapper.orderByAsc(Area::getSequence);
        return queryWrapper.list();
    }

    @Override
    public List<AreaIdTypeDTO> getAreaIdTypes(@NonNull AreaQueryParam queryParam) {
        var queryWrapper = this.lambdaQuery()
                .select(Area::getId, Area::getName)
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Area::getName, queryParam.getKeyword())
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), Area::getId, queryParam.getIds())
                .like(StringUtils.isNotEmpty(queryParam.getDeptId()), Area::getAreaDepartmentId, queryParam.getDeptId());
        var areas = queryWrapper.list();
        return areas.stream().map(po -> new AreaIdTypeDTO(po.getId(), po.getName(), po.getCode(), null)).collect(Collectors.toList());
    }

    @Override
    public List<Area> getListByLevel(@NonNull Integer level) {
        return this.lambdaQuery().eq(Area::getLevel, level).eq(Area::getEnabled, true).list();
    }

    @Override
    public Page<Area> page(@NonNull AreaQueryParam queryParam) {
        LambdaQueryChainWrapper<Area> queryWrapper =
                this.lambdaQuery()
                        .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Area::getName, queryParam.getKeyword())
                        .in(!CollectionUtils.isEmpty(queryParam.getIds()), Area::getId, queryParam.getIds())
                        .eq(queryParam.getStatusId() != null, Area::getStatusId, queryParam.getStatusId())
                        .like(queryParam.getFeatureId() != null, Area::getFeatureIds, queryParam.getFeatureId())
                        .like(queryParam.getTagId() != null, Area::getTagIds, queryParam.getTagId());
        this.extSymbolWhere(queryWrapper, queryParam);
        queryWrapper.orderByAsc(Area::getSequence);
        return queryWrapper.page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<Area> getSequence(@NonNull List<String> sequences) {
        return this.baseMapper.getBySequences(sequences);
    }

    @Override
    public List<Area> getListBySequences(@NonNull String sequence) {
        return this.baseMapper.getListBySequences(sequence);
    }

    @Override
    public List<EnergyAreaVo> innerList() {

        return areaMapper.innerList();
    }

    @Override
    public List<Area> getByIds(@NonNull List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(!CollectionUtils.isEmpty(ids), Area::getId, ids).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        List<Area> areaList = this.getByParentId(id);
        Assert.isParameterError(CollectionUtils.isNotEmpty(areaList), "存在下级区域");
        List<GridRelationMember> gridRelationMembers = this.gridRelationMemberService.lambdaQuery()
                .eq(GridRelationMember::getGridAreaId, id)
                .list();
        DatabaseContextHolder.softDelete(this.gridRelationMemberService, gridRelationMembers);
        List<GridAreaExtend> gridAreaExtends = this.gridAreaExtendService.lambdaQuery()
                .eq(GridAreaExtend::getGridAreaId, id)
                .list();
        DatabaseContextHolder.softDelete(this.gridAreaExtendService, gridAreaExtends);
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Area getByCode(@NonNull String code) {
        return this.lambdaQuery().eq(Area::getCode, code).one();
    }

    @Override
    public Area getByName(@NonNull String name) {
        return this.lambdaQuery().eq(Area::getName, name).one();
    }

    @Override
    public Area getByName(@NonNull String name, Long parentId) {
        if (Objects.isNull(parentId)) {
            return this.lambdaQuery()
                    .isNull(Area::getParentId)
                    .eq(Area::getName, name).one();
        }
        return this.lambdaQuery()
                .eq(Area::getParentId, parentId)
                .eq(Area::getName, name).one();
    }

    @Override
    public List<Area> getByParentId(@NonNull Long parentId) {
        return this.lambdaQuery().eq(BasicEnhancedTreeEntity::getParentId, parentId).list();
    }

    @Override
    public List<Area> getByParentIds(@NonNull List<Long> parentIds) {
        return CollectionUtils.isEmpty(parentIds) ? Collections.emptyList() : this.lambdaQuery().in(Area::getParentId, parentIds).list();
    }

    @Override
    public String getAreaNameById(Long id, Integer tenant) {

        return areaMapper.getAreaNameById(id, tenant);
    }

    @Override
    public Area getAreaNameBySequence(String sequence) {
        return areaMapper.getAreaNameBySequence(sequence);
    }

    @Override
    public String getAreaNamesById(Long id) {
        String areaNames = getAreaNames(id, "");
        return areaNames;
    }

    @Override
    public List<AreaStatisticsVo> getAreaStatistics(Long id) {
        List<AreaStatisticsVo> areaStatisticsVos = new ArrayList<>();
        List<Area> areas = areaMapper.selectList(Wrappers.<Area>lambdaQuery()
                                                         .eq(BasicEnhancedTreeEntity::getParentId, id));
        if (CollectionUtils.isNotEmpty(areas)) {
            areaStatisticsVos = ObjectUtils.copying(areas, AreaStatisticsVo.class);
        } else {
            Area area = areaMapper.selectById(id);
            AreaStatisticsVo copying = ObjectUtils.copying(area, AreaStatisticsVo.class);
            areaStatisticsVos.add(copying);
        }
        for (AreaStatisticsVo areaStatisticsVo : areaStatisticsVos) {
            List<String> collect = Arrays.stream(areaStatisticsVo.getSequence().split("-")).collect(Collectors.toList());
            String areaNames = areaMapper.getAreaNames(collect);
            areaStatisticsVo.setName(areaNames);
        }
        return areaStatisticsVos;
    }

    @Override
    public List<Area> getAreaByName(String areaName) {
        return this.baseMapper.selectList(Wrappers.<Area>lambdaQuery()
                                                  .eq(Area::getName, areaName));
    }

    @Override
    public String getAndAppendAreaName(List<Long> areaIds, Map<Long, List<Area>> areaMap) {
        List<String> areaNames = areaIds.stream().map(areaId -> {
            if (areaMap.containsKey(areaId)) {
                return areaMap.get(areaId).get(0).getName();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return areaNames.stream().collect(Collectors.joining("-"));
    }

    @Override
    public List<AreaVO> getAllAreaFullName() {
        List<Area> areas = super.baseMapper.selectList(new LambdaQueryWrapper<Area>());
        if (ObjectUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        Map<Integer, List<Area>> map = areas.stream().collect(Collectors.groupingBy(Area::getLevel));
        List<AreaVO> result = new ArrayList<>();
        List<Area> areaList = map.get(1);
        if (ObjectUtils.isNotEmpty(areaList)) {
            areaList.forEach(ex -> {
                result.add(ConvertUtils.convert(AreaVO.class, ex));
                this.regionalSubset(ex, map, 2, result);
            });
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> areaDataImport(List<AreaImportDTO> datas) {
        List<String> errorList = new ArrayList<>();
        Assert.isParameterError(CollectionUtils.isEmpty(datas), "无法导入空的excel");
        Pair<Map<String, List<AreaImportDTO>>, Set<Pair<String, String>>> areaMapAndParentPaths
                = this.getAreaMapAndParentPaths(datas, errorList);
        Map<String, List<AreaImportDTO>> areaMap = areaMapAndParentPaths.getLeft();
        Set<Pair<String, String>> parentPaths = areaMapAndParentPaths.getRight();
        List<Area> updateAreaList = new ArrayList<>();
        List<AreaExtend> updateAreaExtendList = new ArrayList<>();
        List<Area> insertAreaList = new ArrayList<>();
        List<AreaExtend> insertAreaExtendList = new ArrayList<>();

        List<Pair<String, String>> sorted = parentPaths
                .stream().sorted(Comparator.comparing(t -> t.getRight().length())).collect(Collectors.toList());
        //数据新增和更新
        for (Pair<String, String> data : sorted) {
            AreaImportDTO preInsDatum = CollectionUtils.isEmpty(areaMap.getOrDefault(data.getRight(), new ArrayList<>()))
                                        ? new AreaImportDTO() : areaMap.get(data.getRight()).get(0);
            this.buildInsertData(data, preInsDatum, updateAreaList, updateAreaExtendList, insertAreaList, insertAreaExtendList);
        }
        //数据新增和更新
        if (!CollectionUtils.isEmpty(updateAreaList)) {
            this.updateBatchById(updateAreaList);
        }
        if (!CollectionUtils.isEmpty(insertAreaList)) {
            // 级联数据不支持批量新增
            for (Area area : insertAreaList) {
                this.save(area);
            }
        }
        if (!CollectionUtils.isEmpty(updateAreaExtendList)) {
            areaExtendService.updateBatchById(updateAreaExtendList);
        }
        if (!CollectionUtils.isEmpty(insertAreaExtendList)) {
            areaExtendService.saveBatch(insertAreaExtendList);
        }

        return errorList;
    }

    private void buildInsertData(Pair<String, String> data, AreaImportDTO preInsDatum, List<Area> updateAreaList,
                                 List<AreaExtend> updateAreaExtendList, List<Area> insertAreaList, List<AreaExtend> insertAreaExtendList
                                ) {
        if (areaByPathNameMap.containsKey(data.getRight())) {
            //当前路径已经存在与系统，当前路径是导入的路径（有可能是导入的父路径，导入的父路径不做更新操作），做更新操作
            if (StringUtils.isNotBlank(preInsDatum.getAreaName())) {
                AreaCacheInfo areaCacheInfo = areaByPathNameMap.get(data.getRight());
                Area area = areaByIdMap.get(areaCacheInfo.getId());
                area.setAliasName(preInsDatum.getAliasName());
                area.setVisualizationAreaType(StringUtils.isNotBlank(preInsDatum.getAreaType())
                                              ? VisualizationAreaTypeEnum.valueOfDesc(preInsDatum.getAreaType())
                                              : null);
                area.setSort(StringUtils.isNotBlank(preInsDatum.getSort()) ? Integer.parseInt(preInsDatum.getSort()) : null);
                updateAreaList.add(area);
                if (StringUtils.isNotBlank(preInsDatum.getIntroduce())) {
                    AreaExtend areaExtend = new AreaExtend();
                    areaExtend.setId(area.getId());
                    areaExtend.setIntroduce(preInsDatum.getIntroduce());
                    updateAreaExtendList.add(areaExtend);
                }
            }
            return;
        }

        List<String> pathAreaList = Arrays.stream(data.getRight().split("/")).collect(Collectors.toList());
        Area area = new Area();
        area.setName(pathAreaList.remove(pathAreaList.size() - 1));
        area.setId(IdWorker.getId());
        area.setCode(generateCode());
        area.setVisualizationAreaType(StringUtils.isNotBlank(preInsDatum.getAreaType())
                                      ? VisualizationAreaTypeEnum.valueOfDesc(preInsDatum.getAreaType())
                                      : null);
        area.setAliasName(preInsDatum.getAliasName());
        area.setSort(StringUtils.isNotBlank(preInsDatum.getSort()) ? Integer.parseInt(preInsDatum.getSort()) : null);
        //不存在父级
        if (CollectionUtils.isEmpty(pathAreaList)) {
            AreaCacheInfo copying = ObjectUtils.copying(area, AreaCacheInfo.class);
            copying.setPathName(area.getName());
            areaByPathNameMap.put(area.getName(), copying);
        } else {
            //设置父级id
            setParentId(pathAreaList, area, insertAreaList);
        }
        insertAreaList.add(area);
        if (StringUtils.isNotBlank(preInsDatum.getIntroduce())) {
            AreaExtend areaExtend = new AreaExtend();
            areaExtend.setId(area.getId());
            areaExtend.setIntroduce(preInsDatum.getIntroduce());
            insertAreaExtendList.add(areaExtend);
        }
    }

    /**
     * 设置父级Id
     */
    private boolean setParentId(List<String> pathAreaList, Area area, List<Area> insertAreaList) {
        //是否存在父级
        if (CollectionUtils.isEmpty(pathAreaList)) {
            return false;
        }
        String parentPath = String.join("/", pathAreaList);
        AreaCacheInfo areaCacheInfo = areaByPathNameMap.get(String.join("/", pathAreaList));
        if (areaCacheInfo == null) {
            Area parentArea = new Area();
            parentArea.setName(pathAreaList.remove(pathAreaList.size() - 1));
            parentArea.setId(IdWorker.getId());
            parentArea.setCode(generateCode());
            boolean setFlag = this.setParentId(pathAreaList, parentArea, insertAreaList);
            insertAreaList.add(parentArea);
            areaCacheInfo = ObjectUtils.copying(parentArea, AreaCacheInfo.class);
            areaCacheInfo.setPathName(setFlag ? String.join("/", pathAreaList) + "/" + parentArea.getName() : parentArea.getName());
            areaByPathNameMap.put(areaCacheInfo.getPathName(), areaCacheInfo);
        }
        area.setParentId(areaCacheInfo.getId());
        AreaCacheInfo copying = ObjectUtils.copying(area, AreaCacheInfo.class);
        copying.setPathName(parentPath + "/" + area.getName());
        areaByPathNameMap.put(copying.getPathName(), copying);
        return true;
    }

    /**
     * 导入数据构造
     *
     * @param datas
     * @param errorList
     *
     * @return
     */
    private Pair<Map<String, List<AreaImportDTO>>, Set<Pair<String, String>>> getAreaMapAndParentPaths(List<AreaImportDTO> datas,
                                                                                                       List<String> errorList
                                                                                                      ) {
        Map<String, List<AreaImportDTO>> areaMap = new HashMap<>();
        // 父级层级
        Set<Pair<String, String>> parentPaths = new HashSet<>();
        for (AreaImportDTO data : datas) {
            if (StringUtils.isBlank(data.getAreaName())) {
                errorList.add("导入数据区域数据为空");
                continue;
            }
            if (areaMap.containsKey(data.getAreaName())) {
                errorList.add("导入数据存在名称相同的区域" + data.getAreaName());
                continue;
            }
            List<AreaImportDTO> dtoArrayList = new ArrayList<>();
            dtoArrayList.add(data);
            areaMap.put(data.getAreaName(), dtoArrayList);
            List<String> parentAreaList = Arrays.stream(data.getAreaName().split("/")).collect(Collectors.toList());

            if (parentAreaList.size() > 1) {
                String areaName = parentAreaList.remove(parentAreaList.size() - 1);
                parentPaths.add(Pair.of(areaName, String.join("/", parentAreaList) + "/" + areaName));
                this.setParentPath(parentAreaList, parentPaths);
            } else {
                parentPaths.add(Pair.of(data.getAreaName(), data.getAreaName()));
            }
        }
        return Pair.of(areaMap, parentPaths);
    }

    @Override
    public List<AreaImportDTO> areaDataExport(AreaQueryParam queryParam) {
        List<Area> areas = this.getList(queryParam);
        if (CollectionUtils.isEmpty(areas)) {
            return new ArrayList<>();
        }
        List<Long> sequenceIdList = areas.stream().map(t -> Arrays.stream(t.getSequence().split("-")).collect(Collectors.toList()))
                .flatMap(Collection::stream).map(Long::valueOf).collect(Collectors.toList());
        Map<Long, String> areaMap = this.lambdaQuery()
                .in(Area::getId, sequenceIdList)
                .list().stream().collect(Collectors.toMap(Area::getId, Area::getName));
        List<AreaImportDTO> importDTOS = new ArrayList<>();
        for (Area area : areas) {
            AreaImportDTO importDTO = ObjectUtils.copying(area, AreaImportDTO.class);
            importDTO.setAreaType(area.getVisualizationAreaType() != null ? area.getVisualizationAreaType().getDesc() : null);
            StringBuilder areaName = new StringBuilder();
            for (String string : area.getSequence().split("-")) {
                if (StringUtils.isNotBlank(areaName.toString())) {
                    areaName.append("/");
                }
                areaName.append(areaMap.get(Long.valueOf(string)));
            }
            importDTO.setAreaName(areaName.toString());
            importDTOS.add(importDTO);
        }
        return importDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void areaSortEdit(List<AreaSortEditDTO> areaSortEditDTOList) {
        Map<Long, Integer> areaMap = new HashMap<>();
        for (int i = 0; i < areaSortEditDTOList.size(); i++) {
            AreaSortEditDTO areaSortEditDTO = areaSortEditDTOList.get(i);
            areaMap.put(areaSortEditDTO.getId(), i);
            this.getAreaSortEditDTO(areaSortEditDTO.getChildren(), areaMap);
            if (areaMap.size() > 10) {
                areaMapper.updateAreaSort(areaMap);
                areaMap.clear();
            }
        }
        if (MapUtils.isNotEmpty(areaMap)) {
            areaMapper.updateAreaSort(areaMap);
        }
    }

    // 获取导出的空间数据
    @Override
    public List<SpaceAreaImportDTO> getSpaceAreaDataExport(AreaQueryParam queryParam) {
        List<Area> dataList = areaMapper.getImportArea(queryParam);
        if (CollectionUtils.isEmpty(dataList)) {
            return new ArrayList<>();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(dataList);
        queryParam.setSequences(sequences);
        List<AreaVO> list = areaMapper.getSpaceExportList(queryParam);
        List<AreaVO> areas = TreeUtils.assembleTrees(list);

        Map<Long, String> areaMap = Optional.ofNullable(list).orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(AreaVO::getId, AreaVO::getName, (o, n) -> n));
        List<SpaceAreaImportDTO> importDTOS = new ArrayList<>();
        for (AreaVO area : areas) {
            this.areaChildSort(area);
        }
        areas.sort(Comparator.comparing(AreaVO::getSort));
        for (AreaVO area : areas) {
            this.handlerName(area, importDTOS, areaMap);
        }

        return importDTOS;
    }

    /**
     * 子集合排序(递归)
     *
     * @param area 区域
     */
    private void areaChildSort(AreaVO area) {
        if (area == null || CollectionUtils.isEmpty(area.getChildren())) {
            return;
        }
        area.getChildren().sort(Comparator.comparing(AreaVO::getSort));
        area.getChildren().forEach(this::areaChildSort);
    }

    /**
     * 处理组装全路径名称(递归)
     */
    private void handlerName(AreaVO area, List<SpaceAreaImportDTO> resultList, Map<Long, String> areaMap) {
        SpaceAreaImportDTO importDTO = SpaceAreaImportDTO.builder().areaName(area.getName())
                .builtUpArea(area.getBuiltUpArea() == null ? "" : String.valueOf(area.getBuiltUpArea())).introduce(area.getIntroduce())
                .statusName(area.getStatusName()).build();
        StringBuilder areaName = new StringBuilder();
        for (String string : area.getSequence().split("-")) {
            if (StringUtils.isNotBlank(areaName.toString())) {
                areaName.append("/");
            }
            areaName.append(areaMap.get(Long.valueOf(string)));
        }
        importDTO.setAreaName(areaName.toString());
        resultList.add(importDTO);
        List<AreaVO> chaildrenList = area.getChildren();
        if (!CollectionUtils.isEmpty(chaildrenList)) {
            chaildrenList.forEach(c -> handlerName(c, resultList, areaMap));
        }
    }

    /**
     * 递归获取区域和排序的关系
     */
    private void getAreaSortEditDTO(List<AreaSortEditDTO> areaSortEditDTOList, Map<Long, Integer> areaMap) {
        if (CollectionUtils.isEmpty(areaSortEditDTOList)) {
            return;
        }
        for (int i = 0; i < areaSortEditDTOList.size(); i++) {
            AreaSortEditDTO areaSortEditDTO = areaSortEditDTOList.get(i);
            areaMap.put(areaSortEditDTO.getId(), i);
            this.getAreaSortEditDTO(areaSortEditDTO.getChildren(), areaMap);
        }
    }

    /**
     * 递归
     *
     * @param parentAreaList
     * @param parentPaths
     */
    private void setParentPath(List<String> parentAreaList, Set<Pair<String, String>> parentPaths) {
        if (parentAreaList.size() > 1) {
            String areaName = parentAreaList.remove(parentAreaList.size() - 1);
            parentPaths.add(Pair.of(areaName, String.join("/", parentAreaList) + "/" + areaName));
            this.setParentPath(parentAreaList, parentPaths);
        }
    }

    /**
     * 获取区域编号信息
     *
     * @return 区域编号信息
     */
    private String generateCode() {
        int i = 1;
        while (true) {
            String code = "QY-" + String.format("%06d", new Random().nextInt(100000));
            Area areaByCode = this.getByCode(code);
            if (Objects.isNull(areaByCode)) {
                return code;
            }
            Assert.isConflict(i == 10, "存在设备类型,请重试!");
            i++;
        }
    }

    private String getAreaNames(Long id, String name) {
        Area area = areaMapper.selectById(id);
        StringBuilder stringBuilder = new StringBuilder();
        if (Objects.nonNull(area) && Objects.isNull(area.getParentId())) {
            return area.getName() + name;
        } else if (Objects.nonNull(area)) {
            String s1 = stringBuilder.append("/" + area.getName()).append(name).toString();
            String areaNames = getAreaNames(area.getParentId(), s1);
            return areaNames;
        } else {
            return null;
        }
    }

    private void regionalSubset(Area area, Map<Integer, List<Area>> map, Integer level, List<AreaVO> result) {
        List<Area> areas = map.get(level);
        if (ObjectUtils.isNotEmpty(areas)) {
            areas.stream().forEach(item -> {
                if (area.getId().equals(item.getParentId())) {
                    item.setName(area.getName() + "-" + item.getName());
                    result.add(ConvertUtils.convert(AreaVO.class, item));
                    regionalSubset(item, map, level + 1, result);
                }
            });
        }
    }

    private void extSymbolWhere(LambdaQueryChainWrapper<Area> queryWrapper, AreaQueryParam queryParam) {
        if (queryParam != null && StringUtils.isNotBlank(queryParam.getAreaSymbol()) && queryParam.getAreaNumber() != null) {
            WhereSymbolEnum symbolEnum = WhereSymbolEnum.valueOfName(queryParam.getAreaSymbol());
            if (symbolEnum == null) {
                return;
            }
            switch (symbolEnum) {
                case LT:
                    queryWrapper.lt(Area::getBuiltUpArea, queryParam.getAreaNumber());
                    break;
                case GT:
                    queryWrapper.gt(Area::getBuiltUpArea, queryParam.getAreaNumber());
                    break;
                case LE:
                    queryWrapper.le(Area::getBuiltUpArea, queryParam.getAreaNumber());
                    break;
                case GE:
                    queryWrapper.ge(Area::getBuiltUpArea, queryParam.getAreaNumber());
                    break;
                case EQ:
                    queryWrapper.eq(Area::getBuiltUpArea, queryParam.getAreaNumber());
                    break;
            }
        }
    }

    @Override
    public List<AreaListVO> getAreaTreesWithChildren(List<Long> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            return Collections.emptyList();
        }

        // 1. 先获取所有区域
        List<Area> allAreas = this.list();
        if (CollectionUtils.isEmpty(allAreas)) {
            return Collections.emptyList();
        }
        List<AreaListVO> areaVOS = AreaConverterForAreaListVO.toAreaListVOS(allAreas);
        //查找父区域为空的设置为0
        areaVOS.forEach(area -> {
            if (area.getParentId() == null) {
                area.setParentId(0L);
            }
        });
        // 2. 获取传入区域ID对应的区域（父区域），并收集它们的sequence
        Map<Long, AreaListVO> areaMap = MapUtils.listToMap(areaVOS, AreaListVO::getId, area -> area);
        List<AreaListVO> result = new ArrayList<>();

        for (Long areaId : areaIds) {
            AreaListVO parentArea = areaMap.get(areaId);
            if (parentArea != null) {
                // 添加父区域
                result.add(parentArea);
            }
        }
        // 3. 查找子区域
        findChildren(result, MapUtils.listGroupToMap(areaVOS, AreaListVO::getParentId));
        return result;
    }

    private void findChildren(List<AreaListVO> result, Map<Long, List<AreaListVO>> childsMap) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        for (AreaListVO area : result) {
            var children = childsMap.get(area.getId());
            area.setChildren(children);
            findChildren(children, childsMap);
        }
    }
}

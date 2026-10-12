package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.server.constant.DictionaryConstant;
import com.guhecloud.cube.base.dictionary.server.constant.SqlConstant;
import com.guhecloud.cube.base.dictionary.server.constant.VersionConstant;
import com.guhecloud.cube.base.dictionary.server.dao.CascadeDictionaryMapper;
import com.guhecloud.cube.base.dictionary.server.dao.DictionaryItemMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryErrorReasonEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.StatusEnum;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryDetailParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryUpdateParam;
import com.guhecloud.cube.base.dictionary.server.service.CascadeDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.CascadeDictionaryVO;
import com.guhecloud.cube.base.dictionary.server.vo.CascadeTreeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CascadeDictionaryServiceImpl extends ServiceImpl<CascadeDictionaryMapper, CascadeDictionary> implements CascadeDictionaryService {
    private final CascadeTreeServiceImpl cascadeTreeService;
    private final DictionaryItemMapper itemMapper;

    @Override
    public CascadeDictionaryVO getCascadeDictionaryById(Long id) {
        CascadeDictionary cascadeDictionary = baseMapper.selectById(id);
        CascadeDictionaryVO dictionary = ConvertUtils.convert(CascadeDictionaryVO.class, cascadeDictionary);
        return Optional.ofNullable(dictionary).orElse(new CascadeDictionaryVO());
    }

    @Override
    public CascadeDictionaryVO getCascadeDictionary(CascadeDictionaryDetailParam param) {
        CascadeDictionary cascadeDictionary = baseMapper.selectOne(new LambdaQueryWrapper<CascadeDictionary>()
                                                                           .eq(ObjectUtils.isNotEmpty(param.getCode()), CascadeDictionary::getCode, param.getCode())
                                                                           .eq(ObjectUtils.isNotEmpty(param.getName()), CascadeDictionary::getName, param.getName())
                                                                           .last(SqlConstant.LIMIT));
        CascadeDictionaryVO cascadeDictionaryVO = ConvertUtils.convert(CascadeDictionaryVO.class, cascadeDictionary);
        if (ObjectUtils.isNotEmpty(cascadeDictionary)) {
            this.dictionaryExtend(cascadeDictionary, cascadeDictionaryVO);
        }
        return cascadeDictionaryVO;
    }

    @Override
    public List<CascadeDictionaryVO> getCascadeDictionaryItem(CascadeDictionaryParam param) {
        if (ObjectUtils.isNotEmpty(param.getSuperiorsId())) {
            return this.getCascadeDictionaryItemById(param);
        }
        if (ObjectUtils.isNotEmpty(param.getId())) {
            CascadeDictionary cascadeDictionary = baseMapper.selectById(param.getId());
            Assert.parameterIsNull(cascadeDictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
            param.setItemCode(cascadeDictionary.getItemCode());
        }
        List<CascadeDictionaryVO> dictionaryTree = new ArrayList<>();
        Integer maxLevel = baseMapper.getMaxLevel(param.getItemCode(), StatusEnum.getStatusNameList(param.getDisable()));
        if (ObjectUtils.isEmpty(maxLevel)) {
            return dictionaryTree;
        }
        Map<Integer, List<CascadeDictionary>> levelMap = new HashMap<>();
        for (int i = 1; i <= maxLevel; i++) {
            List<CascadeDictionary> cascadeDictionaryList = baseMapper.selectList(new LambdaQueryWrapper<CascadeDictionary>()
                                                                                          .eq(CascadeDictionary::getItemCode, param.getItemCode())
                                                                                          .eq(CascadeDictionary::getLevel, i)
                                                                                          .in(CascadeDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable())));
            levelMap.put(i, cascadeDictionaryList);
        }
        dictionaryTree = ConvertUtils.toList(CascadeDictionaryVO.class, levelMap.get(1));
        dictionaryTree.forEach(ex -> {
            if (maxLevel > 1 && ObjectUtils.isNotEmpty(levelMap)) {
                this.recursionTree(maxLevel, 2, levelMap, ex);
            }
        });
        if (ObjectUtils.isNotEmpty(param.getKeyword())) {
            List<CascadeDictionaryVO> dictionaryList = new ArrayList<>();
            for (CascadeDictionaryVO ex : dictionaryTree) {
                this.recursionTree(ex, param.getKeyword());
                if (ObjectUtils.isNotEmpty(ex.getNextList()) || ex.getName().contains(param.getKeyword())) {
                    dictionaryList.add(ex);
                }
            }
            dictionaryTree = dictionaryList;
        }
        return dictionaryTree;
    }

    public List<CascadeDictionaryVO> getCascadeDictionaryItemById(CascadeDictionaryParam param) {
        List<CascadeDictionary> dictionaryList = baseMapper.selectList(new LambdaQueryWrapper<CascadeDictionary>()
                                                                               .eq(CascadeDictionary::getSuperiorsId, param.getSuperiorsId()));
        if (ObjectUtils.isEmpty(dictionaryList)) {
            return new ArrayList<>();
        }
        List<CascadeDictionaryVO> dictionaryTree = ConvertUtils.toList(CascadeDictionaryVO.class, dictionaryList);
        for (CascadeDictionaryVO ex : dictionaryTree) {
            this.recursionTreeById(ex);
        }
        return dictionaryTree;
    }

    public void recursionTreeById(CascadeDictionaryVO dictionary) {
        List<CascadeDictionary> dictionaryList = baseMapper.selectList(new LambdaQueryWrapper<CascadeDictionary>()
                                                                               .eq(CascadeDictionary::getSuperiorsId, dictionary.getId()));
        List<CascadeDictionaryVO> cascadeDictionaryList =
                ObjectUtils.isNotEmpty(dictionaryList) ? ConvertUtils.toList(CascadeDictionaryVO.class, dictionaryList) : new ArrayList<>();
        for (CascadeDictionaryVO ex : cascadeDictionaryList) {
            this.recursionTreeById(ex);
        }
        dictionary.setNextList(cascadeDictionaryList);
    }

    @Override
    public Pagination<CascadeDictionaryVO> getCascadeDictionaryTreeByPage(CascadeDictionaryPageParam param) {
        Page<CascadeDictionary> page = new Page<>(param.getPageNumber(), param.getPageSize());
        baseMapper.selectPage(page, new LambdaQueryWrapper<CascadeDictionary>()
                .eq(CascadeDictionary::getItemCode, param.getItemCode())
                .in(CascadeDictionary::getStatus, StatusEnum.getStatusNameList(true))
                .like(ObjectUtils.isNotEmpty(param.getName()), CascadeDictionary::getName, param.getName())
                .like(ObjectUtils.isNotEmpty(param.getCode()), CascadeDictionary::getCode, param.getCode())
                .eq(ObjectUtils.isNotEmpty(param.getSuperiorsId()), CascadeDictionary::getSuperiorsId, param.getSuperiorsId())
                .eq(ObjectUtils.isEmpty(param.getSuperiorsId()), CascadeDictionary::getLevel, 1));
        return PagingUtils.convert(page, ConvertUtils.toList(CascadeDictionaryVO.class, page.getRecords()));
    }

    @Override
    public List<CascadeDictionaryVO> getAllCascadeDictionaryItem(CascadeDictionaryParam param) {
        List<CascadeDictionary> cascadeDictionaryList = this.baseMapper.selectList(new LambdaQueryWrapper<CascadeDictionary>()
                                                                                           .eq(CascadeDictionary::getItemCode, param.getItemCode())
                                                                                           .in(CascadeDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                                                                                           .in(ObjectUtils.isNotEmpty(param.getCodes()), CascadeDictionary::getCode, param.getCodes()));
        return ConvertUtils.toList(CascadeDictionaryVO.class, cascadeDictionaryList);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public CascadeDictionaryVO addCascadeDictionary(CascadeDictionaryAddParam param) {
        CascadeDictionary dictionary = ConvertUtils.convert(CascadeDictionary.class, param);
        dictionary.setCode(Optional.ofNullable(param.getCode()).orElse(UUID.randomUUID().toString()));
        dictionary.setStatus(Optional.ofNullable(param.getStatus()).orElse(StatusEnum.ENABLE.name()));
        dictionary.setVersion(Optional.ofNullable(param.getVersion()).orElse(VersionConstant.VERSION));
        dictionary.setSuperiorsId(Optional.ofNullable(param.getSuperiorsId()).orElse(DictionaryConstant.SUPERIORS_ID));
        this.cascadeBaseVerification(dictionary, DataBaseOperateTypeEnum.ADD.name());
        boolean bool = baseMapper.insert(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.ADD_DICTIONARY);
        cascadeTreeService.addTree(param.getCascadeTreeParam(dictionary.getId()));
        return ConvertUtils.convert(CascadeDictionaryVO.class, dictionary);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public CascadeDictionaryVO updateCascadeDictionary(CascadeDictionaryUpdateParam param) {
        CascadeDictionary dictionary = baseMapper.selectById(param.getId());
        Assert.parameterIsNull(dictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
        param.fillDataDictionary(dictionary);
        this.cascadeBaseVerification(dictionary, DataBaseOperateTypeEnum.UPDATE.name());
        boolean bool = baseMapper.updateById(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.UPDATE_DICTIONARY);
        cascadeTreeService.updateTree(param.getCascadeTreeParam());
        return ConvertUtils.convert(CascadeDictionaryVO.class, dictionary);
    }

    @Override
    public Boolean deleteCascadeDictionary(Long id) {
        CascadeDictionary dictionary = baseMapper.selectById(id);
        Assert.parameterIsNull(dictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
        dictionary.setStatus(StatusEnum.DELETE.name());
        Boolean bool = baseMapper.updateById(dictionary) > 0;
        this.lambdaUpdate().set(CascadeDictionary::getStatus, StatusEnum.DELETE.name())
                .eq(CascadeDictionary::getSuperiorsId, dictionary.getId())
                .update();
        return bool;
    }

    /**
     * 递归查询树的子集
     */
    private void recursionTree(Integer maxLevel, Integer level, Map<Integer, List<CascadeDictionary>> levelMap,
                               CascadeDictionaryVO thisLevelData
                              ) {
        List<CascadeDictionaryVO> dictionaryList = new ArrayList<>();
        levelMap.get(level).forEach(ex -> {
            if (ex.getSuperiorsId().equals(thisLevelData.getId())) {
                CascadeDictionaryVO dictionary = ConvertUtils.convert(CascadeDictionaryVO.class, ex);
                if (level < maxLevel) {
                    this.recursionTree(maxLevel, level + 1, levelMap, dictionary);
                }
                dictionaryList.add(dictionary);
            }
        });
        thisLevelData.setNextList(dictionaryList);
    }

    /**
     * 递归查询树的子集
     */
    private void recursionTree(CascadeDictionaryVO dictionary, String keyword) {
        List<CascadeDictionaryVO> nextList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(dictionary.getNextList())) {
            for (CascadeDictionaryVO ex : dictionary.getNextList()) {
                this.recursionTree(ex, keyword);
                if (ObjectUtils.isNotEmpty(ex.getNextList()) || ex.getName().contains(keyword)) {
                    nextList.add(ex);
                }
            }
        }
        dictionary.setNextList(nextList);
    }

    public List<CascadeDictionary> getByTenant(Long tenantId) {
        return getBaseMapper().getByTenant(tenantId);
    }

    /**
     * 字典入库校验
     */
    private void cascadeBaseVerification(CascadeDictionary param, String type) {
        Assert.parameterIsNull(param, "字典数据不能为空");
        DictionaryItem dictionaryItem = itemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                     .eq(DictionaryItem::getCode, param.getItemCode()));
        Assert.parameterIsNull(dictionaryItem, "未找到字典项，请先添加字典项");
        List<CascadeDictionary> oldDictionary = baseMapper.selectList(new LambdaQueryWrapper<CascadeDictionary>()
                                                                              .eq(CascadeDictionary::getCode, param.getCode())
                                                                              .eq(CascadeDictionary::getItemCode, param.getItemCode())
                                                                              .in(CascadeDictionary::getStatus, StatusEnum.ENABLE.name(), StatusEnum.DISABLE.name()));
        boolean isUpdate =
                DataBaseOperateTypeEnum.UPDATE.name().equals(type) && ObjectUtils.isNotEmpty(oldDictionary) && oldDictionary.size() == 1;
        boolean bool = ObjectUtils.isNotEmpty(oldDictionary);
        if (isUpdate && oldDictionary.getFirst().getCode().equals(param.getCode())) {
            bool = false;
        }
        Assert.isConflict(bool, "字典编码已存在");
    }

    /**
     * 详情扩展信息
     */
    private void dictionaryExtend(CascadeDictionary cascadeDictionary, CascadeDictionaryVO cascadeDictionaryVO) {
        CascadeTreeVO cascadeTreeVO = cascadeTreeService.getTreeByDictionaryId(cascadeDictionary.getId());
        if (ObjectUtils.isNotEmpty(cascadeTreeVO) && ObjectUtils.isNotEmpty(cascadeTreeVO.getSuperiorsIds())) {
            List<String> superiorsIds = Arrays.asList(cascadeTreeVO.getSuperiorsIds().split(","));
            List<CascadeDictionary> superiorsAll = baseMapper.selectList(new LambdaQueryWrapper<CascadeDictionary>()
                                                                                 .in(CascadeDictionary::getId, superiorsIds));
            StringBuilder codes = new StringBuilder();
            StringBuilder names = new StringBuilder();
            superiorsAll.forEach(ex -> {
                if (ObjectUtils.isNotEmpty(codes)) {
                    codes.append(",");
                }
                if (ObjectUtils.isNotEmpty(names)) {
                    names.append("/");
                }
                codes.append(ex.getCode());
                names.append(ex.getName());
            });
            if (ObjectUtils.isNotEmpty(codes)) {
                codes.append(",");
            }
            if (ObjectUtils.isNotEmpty(names)) {
                names.append("/");
            }
            codes.append(cascadeDictionary.getCode());
            names.append(cascadeDictionary.getName());
            cascadeDictionaryVO.setSuperiorsAllCode(codes.toString());
            cascadeDictionaryVO.setSuperiorsAllName(names.toString());
            cascadeDictionaryVO.setSuperiorsAllId(cascadeTreeVO.getSuperiorsIds());
        }
    }
}

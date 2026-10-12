package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.server.constant.VersionConstant;
import com.guhecloud.cube.base.dictionary.server.dao.DictionaryItemMapper;
import com.guhecloud.cube.base.dictionary.server.dao.SysDictionaryMapper;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.entity.SysDictionary;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryErrorReasonEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.StatusEnum;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.service.ChangeNoticeService;
import com.guhecloud.cube.base.dictionary.server.service.SysDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.SysDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionary> implements SysDictionaryService {
    private final DictionaryItemMapper itemMapper;
    private final ChangeNoticeService changeNoticeService;
    private final static String SYS_ABILITY_CONFIG_DICT_ITEM = "SYS_ABILITY_CONFIG";

    @Override
    public SysDictionaryVO getSysDictionaryById(Long id) {
        Assert.parameterIsNull(id, DictionaryErrorReasonEnum.NO_DICTIONARY_ID);
        SysDictionary sysDictionary = baseMapper.selectById(id);
        SysDictionaryVO dictionary = ConvertUtils.convert(SysDictionaryVO.class, sysDictionary);
        return Optional.ofNullable(dictionary).orElse(new SysDictionaryVO());
    }

    @Override
    public List<SysDictionaryVO> getSysDictionaryByIds(List<Long> ids) {
        Assert.parameterIsNull(ids, DictionaryErrorReasonEnum.NO_DICTIONARY_ID);
        List<SysDictionaryVO> dictionaryList;
        List<SysDictionary> sysDictionariesList = baseMapper.selectList(new LambdaQueryWrapper<SysDictionary>()
                                                                                .in(SysDictionary::getId, ids));
        dictionaryList = Optional.ofNullable(ConvertUtils.toList(SysDictionaryVO.class, sysDictionariesList)).orElse(new ArrayList<>());
        for (SysDictionaryVO ex : dictionaryList) {
            if (ObjectUtils.isNotEmpty(ex)) {
                SysDictionaryVO dictionary = ConvertUtils.convert(SysDictionaryVO.class, ex);
                dictionaryList.add(dictionary);
            }
        }
        return dictionaryList;
    }

    @Override
    public List<SysDictionaryVO> getSysDictionaryByIdsStr(String ids) {
        Assert.parameterIsNull(ids, DictionaryErrorReasonEnum.NO_DICTIONARY_ID);
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return this.getSysDictionaryByIds(idList);
    }

    @Override
    public SysDictionaryVO getSysDictionaryByCode(String itemCode, String code) {
        SysDictionary sysDictionary = baseMapper.selectOne(new LambdaQueryWrapper<SysDictionary>()
                                                                   .eq(SysDictionary::getCode, code)
                                                                   .eq(ObjectUtils.isNotEmpty(itemCode), SysDictionary::getItemCode, itemCode)
                                                                   .in(SysDictionary::getStatus, StatusEnum.getStatusNameList())
                                                                   .last("limit 1"));
        if (Objects.isNull(sysDictionary)) {
            return null;
        }
        SysDictionaryVO convert = ConvertUtils.convert(SysDictionaryVO.class, sysDictionary);
        this.refreshCache(itemCode, Collections.singletonList(convert));
        return convert;
    }

    @Override
    public List<SysDictionaryVO> getSysDictionaryByItem(SysDictionaryaramParam param) {
        List<SysDictionaryVO> dictionaryList = new ArrayList<>();
        List<SysDictionary> list = baseMapper.selectList(new LambdaQueryWrapper<SysDictionary>()
                                                                 .eq(SysDictionary::getItemCode, param.getItemCode())
                                                                 .in(SysDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                                                                 .in(CollectionUtils.isNotEmpty(param.getCodes()), SysDictionary::getCode, param.getCodes())
                                                                 .like(ObjectUtils.isNotEmpty(param.getName()), SysDictionary::getName, param.getName())
                                                                 .like(ObjectUtils.isNotEmpty(param.getCode()), SysDictionary::getCode, param.getCode()));
        if (CollectionUtils.isEmpty(list)) {
            return dictionaryList;
        }
        dictionaryList = ConvertUtils.toList(SysDictionaryVO.class, list);
        this.refreshCache(param.getItemCode(), dictionaryList);
        return dictionaryList;
    }

    @Override
    public Pagination<SysDictionaryVO> getSysDictionaryByItemPage(SysDictionaryPageParam param) {
        Page<SysDictionary> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<SysDictionary>()
                .eq(SysDictionary::getItemCode, param.getItemCode())
                .in(SysDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                .like(ObjectUtils.isNotEmpty(param.getName()), SysDictionary::getName, param.getName())
                .like(ObjectUtils.isNotEmpty(param.getCode()), SysDictionary::getCode, param.getCode()));
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Pagination<SysDictionaryVO> convert = PagingUtils.convert(page, ConvertUtils.toList(SysDictionaryVO.class, page.getRecords()));
        this.refreshCache(param.getItemCode(), convert.getRecords());
        return convert;
    }

    @Override
    public SysDictionaryVO addSysDictionary(SysDictionaryAddParam param) {
        SysDictionary dictionary = ConvertUtils.convert(SysDictionary.class, param);
        dictionary.setCode(Optional.ofNullable(param.getCode()).orElse(UUID.randomUUID().toString()));
        dictionary.setStatus(Optional.ofNullable(param.getStatus()).orElse(StatusEnum.ENABLE.name()));
        dictionary.setVersion(Optional.ofNullable(param.getVersion()).orElse(VersionConstant.VERSION));
        DictionaryItem dictionaryItem = this.sysBaseVerification(dictionary, DataBaseOperateTypeEnum.ADD.name());
        boolean bool = baseMapper.insert(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.ADD_DICTIONARY);
        SysDictionaryVO sysDictionary = ConvertUtils.convert(SysDictionaryVO.class, dictionary);
        changeNoticeService.sendChangeNotice(dictionaryItem, sysDictionary.getCode(), JsonUtils.serialize(sysDictionary), DictionaryTypeEnum.SYS_DICTIONARY, DataBaseOperateTypeEnum.ADD);
        return sysDictionary;
    }

    @Override
    public SysDictionaryVO updateSysDictionary(SysDictionaryParam param) {
        SysDictionary dictionary = baseMapper.selectById(param.getId());
        Assert.parameterIsNull(dictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
        param.fillDataDictionary(dictionary);
        DictionaryItem dictionaryItem = this.sysBaseVerification(dictionary, DataBaseOperateTypeEnum.UPDATE.name());
        boolean bool = baseMapper.updateById(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.UPDATE_DICTIONARY);
        SysDictionaryVO sysDictionary = ConvertUtils.convert(SysDictionaryVO.class, dictionary);
        changeNoticeService.sendChangeNotice(dictionaryItem, sysDictionary.getCode(), JsonUtils.serialize(sysDictionary), DictionaryTypeEnum.SYS_DICTIONARY, DataBaseOperateTypeEnum.UPDATE);
        return sysDictionary;
    }

    @Override
    public Boolean deleteSysDictionary(Long id) {
        SysDictionary dictionary = baseMapper.selectById(id);
        Assert.parameterIsNull(dictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
        dictionary.setStatus(StatusEnum.DELETE.name());
        Boolean bool = baseMapper.updateById(dictionary) > 0;
        changeNoticeService.deleteNotice(dictionary.getItemCode(), dictionary.getCode(), DictionaryTypeEnum.SYS_DICTIONARY);
        return bool;
    }

    public List<SysDictionaryVO> getAll() {
        return list(Wrappers.lambdaQuery(SysDictionary.class).eq(SysDictionary::getDeleted, 0)).stream()
                .map(x -> ConvertUtils.convert(SysDictionaryVO.class, x))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public boolean batchSaveOrUpdate(List<SysDictionary> entities) {
        SysDictionaryService sysDictionaryService = (SysDictionaryService) AopContext.currentProxy();
        return sysDictionaryService.saveOrUpdateBatch(entities, 100);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public int batchRemoveById(List<Long> idsToRemove) {
        return baseMapper.deleteBatchIds(idsToRemove);
    }

    @Override
    public List<SysDictionaryVO> getSysAbilityConfig() {
        SysDictionaryaramParam sysDictionaryaramParam = new SysDictionaryaramParam();
        sysDictionaryaramParam.setItemCode(SYS_ABILITY_CONFIG_DICT_ITEM);
        return getSysDictionaryByItem(sysDictionaryaramParam);
    }

    /**
     * 刷新缓存
     *
     * @param itemCode       字典项编码
     * @param dictionaryList 字典列表
     */
    private void refreshCache(String itemCode, List<SysDictionaryVO> dictionaryList) {
        DictionaryItem dictionaryItem = itemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                     .eq(DictionaryItem::getCode, itemCode));
        dictionaryList.forEach(sysDictionaryVO ->
                                       changeNoticeService.refreshCache(dictionaryItem,
                                                                        sysDictionaryVO.getCode(), JsonUtils.serialize(sysDictionaryVO), DictionaryTypeEnum.SYS_DICTIONARY)
                              );
    }

    /**
     * 字典入库校验
     */
    private DictionaryItem sysBaseVerification(SysDictionary param, String type) {
        Assert.parameterIsNull(param, "字典数据不能为空");
        DictionaryItem dictionaryItem = itemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                     .eq(DictionaryItem::getCode, param.getItemCode()));
        Assert.parameterIsNull(dictionaryItem, "未找到字典项，请先添加字典项");
        List<SysDictionary> oldDictionary = baseMapper.selectList(new LambdaQueryWrapper<SysDictionary>()
                                                                          .eq(SysDictionary::getCode, param.getCode())
                                                                          .eq(SysDictionary::getItemCode, param.getItemCode())
                                                                          .in(SysDictionary::getStatus, StatusEnum.ENABLE.name(), StatusEnum.DISABLE.name()));
        Assert.isConflict(DataBaseOperateTypeEnum.ADD.name().equals(type) && ObjectUtils.isNotEmpty(oldDictionary), "字典编码已存在");
        Assert.isConflict(ObjectUtils.isNotEmpty(oldDictionary) && oldDictionary.size() > 1, "字典编码已存在");
        Assert.isConflict(ObjectUtils.isNotEmpty(oldDictionary) && !oldDictionary.get(0).getCode().equals(param.getCode())
                          && DataBaseOperateTypeEnum.ADD.name().equals(type), "字典编码已存在");
        return dictionaryItem;
    }
}

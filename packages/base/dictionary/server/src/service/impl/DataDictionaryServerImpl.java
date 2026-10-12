package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.server.constant.VersionConstant;
import com.guhecloud.cube.base.dictionary.server.dao.DataDictionaryMapper;
import com.guhecloud.cube.base.dictionary.server.dao.DictionaryItemMapper;
import com.guhecloud.cube.base.dictionary.server.entity.DataDictionary;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryErrorReasonEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.StatusEnum;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.service.ChangeNoticeService;
import com.guhecloud.cube.base.dictionary.server.service.DataDictionaryServer;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataDictionaryServerImpl extends ServiceImpl<DataDictionaryMapper, DataDictionary> implements DataDictionaryServer {
    private final DictionaryItemMapper itemMapper;
    private final ChangeNoticeService changeNoticeService;
    private final DataDictionaryMapper dataDictionaryMapper;

    @Override
    public DataDictionaryVO getDataDictionaryById(Long id) {
        Assert.parameterIsNull(id, DictionaryErrorReasonEnum.NO_DICTIONARY_ID);
        log.info("数据库");
        DataDictionary dictionary = baseMapper.selectById(id);
        DataDictionaryVO dataDictionary = ConvertUtils.convert(DataDictionaryVO.class, dictionary);
        return Optional.ofNullable(dataDictionary).orElse(new DataDictionaryVO());
    }

    @Override
    public List<DataDictionaryVO> getDataDictionaryByIds(List<Long> ids) {
        Assert.parameterIsNull(ids, DictionaryErrorReasonEnum.NO_DICTIONARY_ID);
        List<Long> idsList = new ArrayList<>();
        List<DataDictionaryVO> dictionaryList = new ArrayList<>();
        List<DataDictionary> dataDictionaryList = baseMapper.selectList(new LambdaQueryWrapper<DataDictionary>()
                                                                                .in(DataDictionary::getId, idsList));
        for (DataDictionary ex : dataDictionaryList) {
            if (ObjectUtils.isNotEmpty(ex)) {
                DataDictionaryVO dictionary = ConvertUtils.convert(DataDictionaryVO.class, ex);
                dictionaryList.add(dictionary);
            }
        }
        return dictionaryList;
    }

    @Override
    public List<DataDictionaryVO> getDataDictionaryByIdsStr(String ids) {
        Assert.parameterIsNull(ids, DictionaryErrorReasonEnum.NO_DICTIONARY_ID);
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return this.getDataDictionaryByIds(idList);
    }

    @Override
    public List<DataDictionaryVO> getDataDictionaryByItem(DataDictionaryaramParam param) {
        List<DataDictionaryVO> dictionaryList;
        List<DataDictionary> list = baseMapper.selectList(new LambdaQueryWrapper<DataDictionary>()
                                                                  .eq(DataDictionary::getItemCode, param.getItemCode())
                                                                  .in(DataDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                                                                  .in(CollectionUtils.isNotEmpty(param.getCodes()), DataDictionary::getCode, param.getCodes())
                                                                  .like(ObjectUtils.isNotEmpty(param.getName()), DataDictionary::getName, param.getName())
                                                                  .like(ObjectUtils.isNotEmpty(param.getCode()), DataDictionary::getCode, param.getCode()));
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        dictionaryList = ConvertUtils.toList(DataDictionaryVO.class, list);
        refreshCache(param.getItemCode(), dictionaryList);
        return dictionaryList;
    }

    @Override
    public List<DataDictionaryVO> adminGetDataDictionaryByItem(DataDictionaryaramParam param) {

        List<DataDictionaryVO> dictionaryList = dataDictionaryMapper.selectListNotLimitTenant(param, StatusEnum.getStatusNameList(param.getDisable()));
        if (CollectionUtils.isEmpty(dictionaryList)) {
            return new ArrayList<>();
        }
        refreshCache(param.getItemCode(), dictionaryList);
        return dictionaryList;
    }

    /**
     * 刷新缓存
     *
     * @param itemCode       字典项
     * @param dictionaryList 字典数据
     */
    private void refreshCache(String itemCode, List<DataDictionaryVO> dictionaryList) {
        DictionaryItem dictionaryItem = itemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                     .eq(DictionaryItem::getCode, itemCode));
        dictionaryList.forEach(dataDictionaryVO ->
                                       changeNoticeService.refreshCache(dictionaryItem,
                                                                        dataDictionaryVO.getCode(), JsonUtils.serialize(dataDictionaryVO), DictionaryTypeEnum.DATA_DICTIONARY)
                              );
    }

    @Override
    public Pagination<DataDictionaryVO> getDataDictionaryByItemPage(DataDictionaryPageParam param) {
        Page<DataDictionary> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<DataDictionary>()
                .eq(DataDictionary::getItemCode, param.getItemCode())
                .in(DataDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                .like(ObjectUtils.isNotEmpty(param.getName()), DataDictionary::getName, param.getName())
                .like(ObjectUtils.isNotEmpty(param.getCode()), DataDictionary::getCode, param.getCode()));
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Pagination<DataDictionaryVO> convert = PagingUtils.convert(page, ConvertUtils.toList(DataDictionaryVO.class, page.getRecords()));
        refreshCache(param.getItemCode(), convert.getRecords());
        return convert;
    }

    @Override
    public DataDictionaryVO getDataDictionaryByItemAndCode(DataDictionaryaramByItemAndCodeParam param) {
        DataDictionary dictionary = super.baseMapper.selectOne(new LambdaQueryWrapper<DataDictionary>()
                                                                       .eq(DataDictionary::getItemCode, param.getItemCode())
                                                                       .eq(DataDictionary::getCode, param.getCode())
                                                                       .in(DataDictionary::getStatus, StatusEnum.getStatusNameList(true))
                                                                       .last("limit 1"));
        if (Objects.isNull(dictionary)) {
            return null;
        }
        DataDictionaryVO convert = ConvertUtils.convert(DataDictionaryVO.class, dictionary);
        this.refreshCache(param.getItemCode(), Collections.singletonList(convert));
        return convert;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public DataDictionaryVO addDataDictionary(DataDictionaryAddParam param) {
        DataDictionary dictionary = ConvertUtils.convert(DataDictionary.class, param);
        dictionary.setCode(Optional.ofNullable(param.getCode()).orElse(UUID.randomUUID().toString()));
        DictionaryItem dictionaryItem = this.dataBaseVerification(dictionary);
        dictionary.setStatus(Optional.ofNullable(param.getStatus()).orElse(StatusEnum.ENABLE.name()));
        dictionary.setVersion(Optional.ofNullable(param.getVersion()).orElse(VersionConstant.VERSION));
        boolean bool = baseMapper.insert(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.ADD_DICTIONARY);
        DataDictionaryVO dataDictionary = ConvertUtils.convert(DataDictionaryVO.class, dictionary);
        changeNoticeService.sendChangeNotice(dictionaryItem, dataDictionary.getCode(), JsonUtils.serialize(dataDictionary), DictionaryTypeEnum.DATA_DICTIONARY);
        return dataDictionary;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public DataDictionaryVO updateDataDictionary(DataDictionaryParam param) {
        DataDictionary dictionary = baseMapper.selectById(param.getId());
        DictionaryItem dictionaryItem = this.dataBaseVerification(dictionary, DataBaseOperateTypeEnum.UPDATE.name());
        param.fillDataDictionary(dictionary);
        boolean bool = baseMapper.updateById(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.UPDATE_DICTIONARY);
        DataDictionaryVO dataDictionary = ConvertUtils.convert(DataDictionaryVO.class, dictionary);
        changeNoticeService.sendChangeNotice(dictionaryItem, dataDictionary.getCode(), JsonUtils.serialize(dataDictionary), DictionaryTypeEnum.DATA_DICTIONARY);
        return dataDictionary;
    }

    @Override
    public Boolean deleteDataDictionary(Long id) {
        DataDictionary dictionary = baseMapper.selectById(id);
        Assert.parameterIsNull(dictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
        dictionary.setStatus(StatusEnum.DELETE.name());
        Boolean bool = baseMapper.updateById(dictionary) > 0;
        changeNoticeService.deleteNotice(dictionary.getItemCode(), dictionary.getCode(), DictionaryTypeEnum.DATA_DICTIONARY);
        return bool;
    }

    public List<DataDictionaryVO> getByTenant(Long tenantId) {
        return getBaseMapper().getByTenant(tenantId).stream()
                .map(x -> ConvertUtils.convert(DataDictionaryVO.class, x))
                .collect(Collectors.toList());
    }

    @Override
    public int batchRemoveByIds(List<Long> ids) {
        return baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public void batchAddOrUpdate(List<DataDictionary> dataDictionaryParams) {
        DataDictionaryServer o = (DataDictionaryServer) AopContext.currentProxy();
        o.saveOrUpdateBatch(dataDictionaryParams, 100);
        DictionaryItem dictionaryItem = itemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                     .eq(DictionaryItem::getCode, dataDictionaryParams.get(0)
                                                                             .getItemCode()));
        dataDictionaryParams.forEach(dataDictionary -> {
                                         DataDictionaryVO copying = ObjectUtils.copying(dataDictionary, DataDictionaryVO.class);
                                         changeNoticeService.sendChangeNotice(dictionaryItem, copying.getCode(), JsonUtils.serialize(copying), DictionaryTypeEnum.DATA_DICTIONARY);
                                     }
                                    );
    }

    /**
     * 列表字典入库校验
     */
    private DictionaryItem dataBaseVerification(DataDictionary param) {
        return this.dataBaseVerification(param, null);
    }

    private DictionaryItem dataBaseVerification(DataDictionary param, String type) {
        Assert.parameterIsNull(param, "字典数据不能为空");
        DictionaryItem dictionaryItem = itemMapper.selectOne(new LambdaQueryWrapper<DictionaryItem>()
                                                                     .eq(DictionaryItem::getCode, param.getItemCode()));
        Assert.parameterIsNull(dictionaryItem, "未找到字典项，请先添加字典项");
        List<DataDictionary> oldDictionary = baseMapper.selectList(new LambdaQueryWrapper<DataDictionary>()
                                                                           .eq(DataDictionary::getCode, param.getCode())
                                                                           .eq(DataDictionary::getItemCode, param.getItemCode())
                                                                           .in(DataDictionary::getStatus, StatusEnum.ENABLE.name(), StatusEnum.DISABLE.name()));
        boolean bool = ObjectUtils.isNotEmpty(oldDictionary);
        boolean isUpdate =
                DataBaseOperateTypeEnum.UPDATE.name().equals(type) && ObjectUtils.isNotEmpty(oldDictionary) && oldDictionary.size() == 1;
        if (isUpdate && oldDictionary.get(0).getCode().equals(param.getCode())) {
            bool = false;
        }
        Assert.isConflict(bool, "字典编码已存在");
        return dictionaryItem;
    }
}

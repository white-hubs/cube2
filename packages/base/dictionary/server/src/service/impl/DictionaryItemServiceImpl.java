package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.server.dao.DictionaryItemMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import com.guhecloud.cube.base.dictionary.server.entity.DataDictionary;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.entity.SysDictionary;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryTypeEnum;
import com.guhecloud.cube.base.dictionary.server.param.DictionaryItemParam;
import com.guhecloud.cube.base.dictionary.server.service.CascadeDictionaryService;
import com.guhecloud.cube.base.dictionary.server.service.ChangeNoticeService;
import com.guhecloud.cube.base.dictionary.server.service.DataDictionaryServer;
import com.guhecloud.cube.base.dictionary.server.service.DictionaryItemService;
import com.guhecloud.cube.base.dictionary.server.service.SysDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.DictionaryItemVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryItemServiceImpl extends ServiceImpl<DictionaryItemMapper, DictionaryItem> implements DictionaryItemService {
    private static final String ITEM_CODE_RULE = "^[A-Z][A-Z_]*[A-Z]$";
    private final CascadeDictionaryService cascadeDictionaryService;
    private final DataDictionaryServer dataDictionaryService;
    private final SysDictionaryService sysDictionaryService;
    private final ChangeNoticeService changeNoticeService;

    @Override
    public DictionaryItemVO getDictionaryItemByCode(String code) {
        DictionaryItem item = baseMapper.selectOne(new LambdaQueryWrapper<>(DictionaryItem.class)
                                                           .eq(DictionaryItem::getCode, code));
        return ConvertUtils.convert(DictionaryItemVO.class, item);
    }

    @Override
    public List<DictionaryItemVO> getDictionaryItemAll() {
        List<DictionaryItem> item = baseMapper.selectList(new LambdaQueryWrapper<>(DictionaryItem.class));
        return ConvertUtils.toList(DictionaryItemVO.class, item);
    }

    @Override
    public DictionaryItemVO addDictionaryItem(DictionaryItemParam param) {
        DictionaryItem item = ConvertUtils.convert(DictionaryItem.class, param);
        Pattern pattern = Pattern.compile(ITEM_CODE_RULE);
        Matcher matcher = pattern.matcher(param.getCode());
        Assert.isParameterError(!matcher.find(), "字典项编码只能是大字母和_组成");
        DictionaryItemVO dictionaryItemByCode = getDictionaryItemByCode(param.getCode());
        Assert.isConflict(Objects.nonNull(dictionaryItemByCode), "字典项编号已存在");
        baseMapper.insert(item);
        return ConvertUtils.convert(DictionaryItemVO.class, item);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public boolean batchAddOrUpdate(List<DictionaryItemParam> dictionaryItemParams) {
        List<DictionaryItem> existedItems = baseMapper.selectList(new LambdaQueryWrapper<>(DictionaryItem.class));
        Map<String, DictionaryItem> existedItemsByCode = existedItems.stream()
                .collect(Collectors.toMap(DictionaryItem::getCode, Function.identity()));
        List<DictionaryItem> toBeUpdatedItems = new ArrayList<>(dictionaryItemParams.size());
        for (DictionaryItemParam dictionaryItemParam : dictionaryItemParams) {
            DictionaryItem item = existedItemsByCode.getOrDefault(dictionaryItemParam.getCode(), new DictionaryItem());
            if (item.getId() == null) {
                item.setCode(dictionaryItemParam.getCode());
                item.setRemarks(dictionaryItemParam.getRemarks());
            }
            item.setRemarks(dictionaryItemParam.getRemarks());
            toBeUpdatedItems.add(item);
        }
        DictionaryItemService dictionaryItemService = (DictionaryItemService) AopContext.currentProxy();
        return dictionaryItemService.saveOrUpdateBatch(toBeUpdatedItems, 100);
    }

    @Override
    public int batchRemoveByCode(List<String> codes) {
        List<DictionaryItem> existedItems = baseMapper.selectList(new LambdaQueryWrapper<>(DictionaryItem.class));
        Map<String, DictionaryItem> existedItemsByCode = existedItems.stream()
                .collect(Collectors.toMap(DictionaryItem::getCode, Function.identity()));
        return baseMapper.deleteBatchIds(codes.stream()
                                                 .map(existedItemsByCode::get)
                                                 .filter(Objects::nonNull)
                                                 .map(DictionaryItem::getId)
                                                 .collect(Collectors.toList()));
    }

    @Override
    public void refreshDictionaryItemCache(String code) {
        DictionaryItem dictionaryItem = this.lambdaQuery().eq(DictionaryItem::getCode, code).one();
        Assert.isNotFound(dictionaryItem == null, "字典项编码不存在");
        //级联字典
        List<CascadeDictionary> cascadeDictionaryList = cascadeDictionaryService.lambdaQuery().eq(CascadeDictionary::getItemCode, code)
                .list();
        for (CascadeDictionary cascadeDictionary : cascadeDictionaryList) {
            changeNoticeService.sendChangeNotice(dictionaryItem, cascadeDictionary.getCode(),
                                                 JsonUtils.serialize(cascadeDictionary), DictionaryTypeEnum.TREE_DICTIONARY);
        }
        //通用字典
        List<DataDictionary> dataDictionaryList = dataDictionaryService.lambdaQuery().eq(DataDictionary::getItemCode, code).list();
        for (DataDictionary dataDictionary : dataDictionaryList) {
            changeNoticeService.sendChangeNotice(dictionaryItem, dataDictionary.getCode(),
                                                 JsonUtils.serialize(dataDictionary), DictionaryTypeEnum.DATA_DICTIONARY);
        }
        //系统字典
        List<SysDictionary> sysDictionaryList = sysDictionaryService.lambdaQuery().eq(SysDictionary::getItemCode, code).list();
        for (SysDictionary sysDictionary : sysDictionaryList) {
            changeNoticeService.sendChangeNotice(dictionaryItem, sysDictionary.getCode(), JsonUtils.serialize(sysDictionary),
                                                 DictionaryTypeEnum.SYS_DICTIONARY, DataBaseOperateTypeEnum.ADD);
        }
    }
}

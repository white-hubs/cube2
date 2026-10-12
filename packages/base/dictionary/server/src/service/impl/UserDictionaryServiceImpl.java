package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.server.constant.VersionConstant;
import com.guhecloud.cube.base.dictionary.server.dao.UserDictionaryMapper;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.entity.UserDictionary;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryErrorReasonEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.QueryTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.StatusEnum;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryModifyParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.service.ChangeNoticeService;
import com.guhecloud.cube.base.dictionary.server.service.DictionaryItemService;
import com.guhecloud.cube.base.dictionary.server.service.UserDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.UserDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author 15561
 * @description 针对表【user_dictionary(用户字典)】的数据库操作Service实现
 * @createDate 2024-10-28 13:51:15
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDictionaryServiceImpl extends ServiceImpl<UserDictionaryMapper, UserDictionary>
        implements UserDictionaryService {
    private final ChangeNoticeService changeNoticeService;
    private final DictionaryItemService dictionaryItemService;
    private static final String LIMIT = "limit 1";
    private static final String SERVICE_CONFIGURATION = "SERVICE_CONFIGURATION";

    @Override
    public UserDictionaryVO getUserDictionaryByCode(UserDictionaryParam userDictionaryParam) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Assert.isParameterError(StringUtils.isBlank(userDictionaryParam.getCode()), "字典编码不能为空");
        UserDictionary userDictionary = this.lambdaQuery()
                .eq(UserDictionary::getCode, userDictionaryParam.getCode())
                .eq(UserDictionary::getUserId, userId)
                .eq(UserDictionary::getItemCode, userDictionaryParam.getItemCode())
                .in(UserDictionary::getStatus, StatusEnum.getStatusNameList(userDictionaryParam.getDisable()))
                .last(LIMIT)
                .one();
        if (Objects.isNull(userDictionary)) {
            return null;
        }
        UserDictionaryVO userDictionaryVO = ObjectUtils.copying(userDictionary, UserDictionaryVO.class);
        //记录缓存
        this.refreshCache(userDictionaryParam.getItemCode(), Collections.singletonList(userDictionaryVO));
        return userDictionaryVO;
    }

    @Override
    public List<UserDictionaryVO> getUserDictionaryList(UserDictionaryParam param) {
        boolean userConditions = false;
        if (ObjectUtils.isEmpty(param.getQueryType()) || QueryTypeEnum.PERSONAL.name().equals(param.getQueryType())) {
            userConditions = true;
            if (ObjectUtils.isEmpty(param.getUserIds())) {
                Set<Long> userIds = new HashSet<>();
                userIds.add(SecurityContextHolder.getPrincipal().getId());
                param.setUserIds(userIds);
            }
        }
        List<UserDictionary> userDictionaryList = this.lambdaQuery()
                .eq(UserDictionary::getItemCode, param.getItemCode())
                .in(userConditions, UserDictionary::getUserId, param.getUserIds())
                .in(UserDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                .in(CollectionUtils.isNotEmpty(param.getCodes()), UserDictionary::getCode, param.getCodes())
                .like(ObjectUtils.isNotEmpty(param.getName()), UserDictionary::getName, param.getName())
                .like(ObjectUtils.isNotEmpty(param.getCode()), UserDictionary::getCode, param.getCode())
                .list();
        if (CollectionUtils.isEmpty(userDictionaryList)) {
            return Collections.emptyList();
        }
        List<UserDictionaryVO> dictionaryVOList = ObjectUtils.copying(userDictionaryList, UserDictionaryVO.class);
        this.refreshCache(param.getItemCode(), dictionaryVOList);
        return dictionaryVOList;
    }

    @Override
    public Pagination<UserDictionaryVO> getUserDictionaryByItemPage(UserDictionaryPageParam param) {
        Page<UserDictionary> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<UserDictionary>()
                .eq(UserDictionary::getItemCode, param.getItemCode())
                .eq(UserDictionary::getUserId, SecurityContextHolder.getPrincipal().getId())
                .in(UserDictionary::getStatus, StatusEnum.getStatusNameList(param.getDisable()))
                .like(ObjectUtils.isNotEmpty(param.getName()), UserDictionary::getName, param.getName())
                .like(ObjectUtils.isNotEmpty(param.getCode()), UserDictionary::getCode, param.getCode()));
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Pagination<UserDictionaryVO> convert = PagingUtils.convert(page, ObjectUtils.copying(page.getRecords(), UserDictionaryVO.class));
        this.refreshCache(param.getItemCode(), convert.getRecords());
        return convert;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDictionaryVO addUserDictionary(UserDictionaryAddParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDictionary dictionary = ObjectUtils.copying(param, UserDictionary.class);
        dictionary.setUserId(userId);
        String uuid = UUID.randomUUID().toString();
        dictionary.setCode(Optional.ofNullable(param.getCode()).orElse(uuid));
        DictionaryItem dictionaryItem = this.dataBaseVerification(dictionary, null);
        dictionary.setStatus(Optional.ofNullable(param.getStatus()).orElse(StatusEnum.ENABLE.name()));
        dictionary.setVersion(Optional.ofNullable(param.getVersion()).orElse(VersionConstant.VERSION));
        boolean bool = baseMapper.insert(dictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.ADD_DICTIONARY);
        UserDictionaryVO userDictionaryVO = ObjectUtils.copying(dictionary, UserDictionaryVO.class);
        changeNoticeService.sendChangeNotice(dictionaryItem, userDictionaryVO.getCode(), JsonUtils.serialize(userDictionaryVO), DictionaryTypeEnum.USER_DICTIONARY);
        return userDictionaryVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addOrUpdateBatchUserDictionary(List<UserDictionaryModifyParam> param) {
        List<UserDictionary> dictionary = ObjectUtils.copying(param, UserDictionary.class);
        Map<Long, DictionaryItem> dictionaryItemMap = new HashMap<>();
        this.remove(new LambdaQueryWrapper<UserDictionary>()
                            .eq(UserDictionary::getItemCode, SERVICE_CONFIGURATION));
        dictionary.forEach(ex -> {
            Assert.parameterIsNull(ex.getUserId(), "用户id不能为空");
            String uuid = UUID.randomUUID().toString();
            ex.setCode(Optional.ofNullable(ex.getCode()).orElse(uuid));
            ex.setStatus(Optional.ofNullable(ex.getStatus()).orElse(StatusEnum.ENABLE.name()));
            ex.setVersion(Optional.ofNullable(ex.getVersion()).orElse(VersionConstant.VERSION));
            ex.setDeleted(false);
            EntityAuditor.populateCreateAudit(ex);
            DictionaryItem dictionaryItem = this.dataBaseVerification(ex, ObjectUtils.isNotEmpty(ex.getId())
                                                                          ? DataBaseOperateTypeEnum.UPDATE.name()
                                                                          : DataBaseOperateTypeEnum.ADD.name());
            dictionaryItemMap.put(ex.getId(), dictionaryItem);
        });
        this.saveOrUpdateBatch(dictionary);
        dictionary.forEach(ex -> {
            UserDictionaryVO userDictionaryVO = ObjectUtils.copying(ex, UserDictionaryVO.class);
            changeNoticeService.sendChangeNotice(dictionaryItemMap.get(ex.getUserId()), ex.getCode(), JsonUtils.serialize(userDictionaryVO), DictionaryTypeEnum.USER_DICTIONARY);
        });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDictionaryVO updateUserDictionary(UserDictionaryModifyParam param) {
        UserDictionary userDictionary = this.baseMapper.selectById(param.getId());
        DictionaryItem dictionaryItem = this.dataBaseVerification(userDictionary, DataBaseOperateTypeEnum.UPDATE.name());
        param.fillDataDictionary(userDictionary);
        boolean bool = baseMapper.updateById(userDictionary) > 0;
        Assert.parameterIsNull(!bool, DictionaryErrorReasonEnum.UPDATE_DICTIONARY);
        UserDictionaryVO userDictionaryVO = ObjectUtils.copying(userDictionary, UserDictionaryVO.class);
        changeNoticeService.sendChangeNotice(dictionaryItem, userDictionaryVO.getCode(), JsonUtils.serialize(userDictionaryVO), DictionaryTypeEnum.USER_DICTIONARY);
        return userDictionaryVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserDictionary(Long id) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDictionary dictionary = baseMapper.selectById(id);
        Assert.parameterIsNull(dictionary, DictionaryErrorReasonEnum.NO_DICTIONARY_DATA);
        Assert.isPermissionDenied(!Objects.equals(userId, dictionary.getUserId()), "只能删除自己的字典数据");
        DatabaseContextHolder.softDelete(UserDictionary.class, id);
        changeNoticeService.deleteNotice(dictionary.getItemCode(), dictionary.getCode(), DictionaryTypeEnum.USER_DICTIONARY);

    }

    /**
     * 刷新缓存
     *
     * @param itemCode       字典项编码
     * @param dictionaryList 字典数据
     */
    private void refreshCache(String itemCode, List<UserDictionaryVO> dictionaryList) {
        DictionaryItem dictionaryItem = dictionaryItemService.lambdaQuery()
                .eq(DictionaryItem::getCode, itemCode)
                .last(LIMIT)
                .one();
        dictionaryList.forEach(userDictionaryVO ->
                                       changeNoticeService.refreshCache(dictionaryItem,
                                                                        userDictionaryVO.getCode(), JsonUtils.serialize(userDictionaryVO), DictionaryTypeEnum.USER_DICTIONARY)
                              );
    }

    /**
     * 用户字典入库校验
     */
    private DictionaryItem dataBaseVerification(UserDictionary param, String type) {
        Assert.parameterIsNull(param, "字典数据不能为空");
        DictionaryItem dictionaryItem = dictionaryItemService.lambdaQuery()
                .eq(DictionaryItem::getCode, param.getItemCode())
                .last(LIMIT)
                .one();
        Assert.parameterIsNull(dictionaryItem, "未找到字典项，请先添加字典项");
        List<UserDictionary> existUserDictionary = this.lambdaQuery()
                .eq(UserDictionary::getItemCode, param.getItemCode())
                .eq(UserDictionary::getCode, param.getCode())
                .eq(UserDictionary::getUserId, param.getUserId())
                .in(UserDictionary::getStatus, StatusEnum.ENABLE.name(), StatusEnum.DISABLE.name())
                .list();
        boolean bool = ObjectUtils.isNotEmpty(existUserDictionary);
        if (DataBaseOperateTypeEnum.UPDATE.name().equals(type) && ObjectUtils.isNotEmpty(existUserDictionary) &&
            existUserDictionary.size() == 1) {
            if (existUserDictionary.get(0).getCode().equals(param.getCode())) {
                bool = false;
            }
        }
        Assert.isConflict(bool, "字典编码已存在");
        return dictionaryItem;
    }
}





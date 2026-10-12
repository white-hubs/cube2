package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import com.guhecloud.cube.iot.mgt.base.mapper.IotSysUnitMapper;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictItemMapper;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictItemTempTableMapper;
import com.guhecloud.cube.iot.mgt.base.model.IotSysUnitEntity;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItemRef;
import com.guhecloud.cube.iot.mgt.base.model.SysDictTempTable;
import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemModifyParam;
import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemPageParam;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemRefService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemTreeVo;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.base.vo.IotSysUnitVo;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用字典项服务
 */
@Service
@Slf4j
public class CommonDictItemService {
    @Resource
    private SysDictItemServiceImpl sysDictItemService;
    @Resource
    private SysDictItemRefService sysDictItemRefService;
    @Resource
    private SysDictItemTempTableMapper sysDictItemTempTableMapper;
    @Resource
    private PlatformEvnCom platformEvnCom;
    @Resource
    private SysDictItemMapper sysDictItemMapper;
    @Resource
    private IotSysUnitMapper iotSysUnitMapper;

    public Long saveDictItem(CommonDictItemModifyParam param, Long dictId) {
        SysDictItem parent = Objects.isNull(param.getParentId()) ? null : sysDictItemService.getById(param.getParentId());

        String itemLevel = generateItemLevel(parent, dictId);

        SysDictItem sysDictItem = new SysDictItem();
        sysDictItem.setId(platformEvnCom.nextSnowflakeId());
        sysDictItem.setDictId(dictId);
        sysDictItem.setItemParentId(param.getParentId());
        sysDictItem.setItemSort(param.getSort());
        sysDictItem.setItemLevel(itemLevel);
        sysDictItem.setItemLabel(param.getItemLabel());
        sysDictItem.setItemValue(param.getItemValue());
        sysDictItem.setStatus(0);
        sysDictItem.setRemark(param.getRemark());

        sysDictItemService.save(sysDictItem);

        //更新一下临时表，用来将树拍平为一级
        if (InnerDicEnum.prod_category.id == dictId) {
            updateSysDictItemTempTable(sysDictItem);
        }
        return sysDictItem.getId();
    }

    public boolean updateSysDictItemTempTable(SysDictItem sysDictItem) {
        //找到这个ITEM项所有层级关联
        List<SysDictItem> sysDictItems = sysDictItemMapper.queryIdsByLikeLevelAndSort(sysDictItem.getDictId(),
                                                                                      platformEvnCom.hierarchicalNumbering.findTopLevel(sysDictItem.getItemLevel()));//按照层级排序

        Map<String, SysDictItem> levelMap = sysDictItems.stream().collect(Collectors.toMap(SysDictItem::getItemLevel, Function.identity()));
        for (SysDictItem dictItem : sysDictItems) {

            //c001002001 ==> c001 002 001
            String[] levels = platformEvnCom.hierarchicalNumbering.recoveryStrId(dictItem.getItemLevel(), true);

            //根据level推导出所有的层级级联
            StringBuilder itemLevelConcat = new StringBuilder();
            StringBuilder itemLabelConcat = new StringBuilder();
            StringBuilder itemIdConcat = new StringBuilder();

            //==> c001 c001002 c001003001
            StringBuilder levelCode = new StringBuilder(levels[0]);
            for (int i = 0; i < levels.length; i++) {
                String level = levelCode.toString();

                SysDictItem levelItem = levelMap.get(level);

                itemLevelConcat.append(levelItem.getItemLevel()).append("-");
                itemLabelConcat.append(levelItem.getItemLabel()).append("-");
                itemIdConcat.append(levelItem.getId()).append("-");

                //第一个code忽略，在levelCode创建时已经申明
                if ((i + 1) < levels.length) {
                    levelCode.append(levels[i + 1]);
                }
            }

            boolean isCreate = false;
            SysDictTempTable sysDictTempTable = sysDictItemTempTableMapper.selectById(dictItem.getId());
            if (Objects.isNull(sysDictTempTable)) {
                sysDictTempTable = new SysDictTempTable();
                isCreate = true;
            }

            sysDictTempTable.setId(dictItem.getId());
            sysDictTempTable.setItemLabel(dictItem.getItemLabel());
            sysDictTempTable.setItemValue(dictItem.getItemValue());
            sysDictTempTable.setItemLevel(dictItem.getItemLevel());
            sysDictTempTable.setItemLevelConcat(itemLevelConcat.deleteCharAt(itemLevelConcat.length() - 1).toString());
            sysDictTempTable.setItemLabelConcat(itemLabelConcat.deleteCharAt(itemLabelConcat.length() - 1).toString());
            sysDictTempTable.setItemIdConcat(itemIdConcat.deleteCharAt(itemIdConcat.length() - 1).toString());

            if (isCreate) {
                sysDictItemTempTableMapper.insert(sysDictTempTable);
            } else {
                sysDictItemTempTableMapper.updateById(sysDictTempTable);
            }
        }

        return true;
    }

    /**
     * 新增一个层级，根据父节点(和类别)的层级生成
     *
     * @param parent 父节点
     * @param dictId 类别
     *
     * @return 新的层级
     */
    private synchronized String generateItemLevel(SysDictItem parent, Long dictId) {
        //fixme: 分布式锁
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        if (parent == null) {
            List<SysDictItem> brotherSysDictList = sysDictItemService.queryItemListByParentIdAndDictId(0, dictId, tenant);

            if (Objects.isNull(brotherSysDictList) || brotherSysDictList.isEmpty()) {
                return platformEvnCom.hierarchicalNumbering.get1LevelId(1);
            }

            String preLevel = brotherSysDictList.get(0).getItemLevel(); //找到最大的Item
            return generateNexLevelIdWithCurtItem(preLevel);
        } else {
            List<SysDictItem> brotherSysDictList = sysDictItemService.queryItemListByParentIdAndDictId(parent.getId(), dictId, tenant);

            //生成下一级level
            if (Objects.isNull(brotherSysDictList) || brotherSysDictList.isEmpty()) {
                List<Integer> parentLevelList = platformEvnCom.hierarchicalNumbering.recoveryId(parent.getItemLevel());
                return platformEvnCom.hierarchicalNumbering.nextLevelId(parentLevelList);
            }

            String maxBrotherLevel = brotherSysDictList.get(0).getItemLevel(); //找到最大的一个兄弟节点项
            return generateNexLevelIdWithCurtItem(maxBrotherLevel);
        }
    }

    /**
     * 根据当前层级生递增+1<br/>
     * eg: A001 -> A002
     *
     * @param curtItemLevel 当前层级
     *
     * @return 新的层级
     */
    private String generateNexLevelIdWithCurtItem(String curtItemLevel) {
        List<Integer> preLevelList = platformEvnCom.hierarchicalNumbering.recoveryId(curtItemLevel);//恢复层级
        int nextLevel = preLevelList.get(preLevelList.size() - 1) + 1;//层级+1
        preLevelList.set(preLevelList.size() - 1, nextLevel);//设置层级值

        return platformEvnCom.hierarchicalNumbering.generateId(preLevelList);//生成新的层级
    }

    public boolean updateDictItem(CommonDictItemModifyParam param, Long dictId) {

        Assert.isTrue(Objects.nonNull(param.getId()), "id不能为空");
        SysDictItem sysDictItem = sysDictItemService.getById(param.getId());

        sysDictItem.setDictId(dictId);
        sysDictItem.setItemSort(param.getSort());
        sysDictItem.setItemLabel(param.getItemLabel());
        sysDictItem.setItemValue(StringUtils.isBlank(param.getItemValue()) ? param.getItemLabel() : param.getItemValue());
        sysDictItem.setRemark(param.getRemark());

        return sysDictItemService.updateById(sysDictItem) && updateSysDictItemTempTable(sysDictItem);
    }

    public boolean deleteDictItem(Long id) {
        Assert.isTrue(Objects.nonNull(id), "id不能为空");
        return DatabaseContextHolder.softDelete(sysDictItemService, id);
    }

    /**
     * 查询字典项<br/>
     * 如果字典项关联了其他字典项，则返回关联的字典项
     *
     * @param itemId 字典项id
     *
     * @return 字典项
     */
    public DictItemVo queryDictItemWithAssociated(Long itemId) {
        Assert.isTrue(Objects.nonNull(itemId), "id不能为空");
        SysDictItem sysDictItem = sysDictItemService.getById(itemId);
        Assert.isTrue(Objects.nonNull(sysDictItem), "字典项不存在");

        DictItemVo dictItemVo = convert(sysDictItem);

        return dictItemVo;
    }

    /**
     * 根节点分页查询
     *
     * @param paging 分页参数
     *
     * @return 分页结果
     */
    public Pagination<DictItemVo> listRootByPage(CommonDictItemPageParam paging) {
        //只查询顶层节点
        Page<SysDictItem> page = sysDictItemService.page(PagingUtils.convert(paging), Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getItemParentId, 0)
                .eq(SysDictItem::getDeleted, 0)//not be deleted
                .eq(Objects.nonNull(paging.getDictId()), SysDictItem::getDictId, paging.getDictId())
                .like(StringUtils.isNotBlank(paging.getItemLabel()), SysDictItem::getItemLabel, paging.getItemLabel())
                .orderByDesc(SysDictItem::getUpdateTime));

        if (page.getRecords().isEmpty()) {
            return Pagination.empty();
        }

        List<DictItemVo> recordList = page.getRecords()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        Pagination<DictItemVo> resultPage = PagingUtils.convert(page, recordList);

        //查询子节点如果需要
        if (paging.isWithChildren()) {
            for (DictItemVo dictItemVo : resultPage.getRecords()) {
                dictItemVo.setChildren(loadTopItemTree(dictItemVo.getId()).getChildren());
            }
        }
        return resultPage;
    }

    /**
     * 加载关联的字典项
     *
     * @param dictId source字典id
     * @param itemId source字典项id
     *
     * @return 关联的字典项
     */
    private DictItemVo loadAssociatedItem(Long dictId, Long itemId) {
        SysDictItemRef sysDictItemRef = sysDictItemRefService.lambdaQuery()
                .eq(SysDictItemRef::getSourceDictId, dictId)
                .eq(SysDictItemRef::getSourceItemId, itemId)
                .one();

        if (Objects.isNull(sysDictItemRef)) {
            return null;
        }

        SysDictItem sysDictItem = sysDictItemService.getById(sysDictItemRef.getTargetItemId());
        Assert.isTrue(Objects.nonNull(sysDictItem), "关联字典项不存在");

        return convert(sysDictItem);
    }

    /**
     * 加载字典项树<br/>
     * 从顶层节点开始加载
     *
     * @param dictItemId 字典项id
     *
     * @return 字典项树
     */
    public DictItemVo loadTopItemTree(Long dictItemId) {
        SysDictItem sysDictItem = sysDictItemService.getById(dictItemId);
        Assert.isTrue(Objects.nonNull(sysDictItem), "字典项不存在");

        DictItemVo rootItem = convert(sysDictItem);

        List<SysDictItem> children = sysDictItemService.lambdaQuery()
                .ne(SysDictItem::getItemParentId, 0)
                .eq(SysDictItem::getDeleted, 0)//not be deleted
                .likeLeft(true, SysDictItem::getItemLevel, sysDictItem.getItemLevel())
                .list();

        if (children.isEmpty()) {
            return rootItem;
        }

        for (SysDictItem child : children) {
            putItemToParent(convert(child), rootItem);
        }

        return rootItem;
    }

    /**
     * 复原树节点
     *
     * @param childItem 子节点
     * @param rootItem  根节点
     */
    public void putItemToParent(DictItemVo childItem, DictItemVo rootItem) {
        //直接是根节点的child
        if (childItem.getItemParentId().equals(rootItem.getId())) {
            if (Objects.isNull(rootItem.getChildren())) {
                rootItem.setChildren(new LinkedList<>());
            }

            rootItem.getChildren().add(childItem);
        }

        if (Objects.isNull(rootItem.getChildren())) {
            return;
        }

        //递归查找父节点
        for (DictItemVo item : rootItem.getChildren()) {
            //找到父节点
            if (item.getId().equals(childItem.getItemParentId())) {
                if (Objects.isNull(item.getChildren())) {
                    item.setChildren(new LinkedList<>());
                }

                item.getChildren().add(childItem);
                break;
            }

            //如果有子节点,深度优先匹配
            if (Objects.nonNull(item.getChildren())) {
                putItemToParent(childItem, item);
            }
        }
    }

    /**
     * 加载字典项树<br/>
     * 从底层节点开始加载
     *
     * @param dictItemId 字典项id
     *
     * @return 字典项树
     */
    public DictItemVo loadBottomItemTree(long dictItemId) {
        SysDictItem sysDictItem = sysDictItemService.getById(dictItemId);
        Assert.isTrue(Objects.nonNull(sysDictItem), "字典项不存在");
        DictItemVo bottom = convert(sysDictItem);

        while (!Objects.isNull(bottom.getItemParentId()) && bottom.getItemParentId() != 0) {
            long parentId = sysDictItem.getItemParentId();
            sysDictItem = sysDictItemService.getById(parentId);
            if (Objects.nonNull(sysDictItem)) {
                DictItemVo parent = convert(sysDictItem);

                if (Objects.isNull(parent.getChildren())) {
                    parent.setChildren(new LinkedList<>());
                }

                parent.getChildren().add(bottom);
                bottom = parent;
            } else {
                throw new ServiceException("字典项不存在[" + parentId + "]");
            }
        }

        return bottom;
    }

    /**
     * 加载字典项树<br/>
     * 从底层节点开始加载
     *
     * @param dictItemId 字典项id
     *
     * @return 字典项LabelName级联
     */
    public String loadBottomItemTreeLabelName(long dictItemId, String split) {
        LinkedList<String> labelNames = new LinkedList<>();

        SysDictItem sysDictItem = sysDictItemService.getById(dictItemId);
        Assert.isTrue(Objects.nonNull(sysDictItem), "字典项不存在");
        labelNames.add(sysDictItem.getItemLabel());

        while (!Objects.isNull(sysDictItem.getItemParentId()) && sysDictItem.getItemParentId() != 0) {
            long parentId = sysDictItem.getItemParentId();
            sysDictItem = sysDictItemService.getById(parentId);
            if (Objects.nonNull(sysDictItem)) {
                labelNames.addFirst(sysDictItem.getItemLabel());
            } else {
                throw new ServiceException("字典项不存在[" + parentId + "]");
            }
        }

        return String.join(split, labelNames);
    }

    /**
     * 转换
     *
     * @param sysDictItem 字典项
     *
     * @return 字典项vo
     */
    public DictItemVo convert(SysDictItem sysDictItem) {
        DictItemVo dictItemVo = new DictItemVo();
        dictItemVo.setId(sysDictItem.getId());
        dictItemVo.setDictId(sysDictItem.getDictId());
        dictItemVo.setItemParentId(sysDictItem.getItemParentId());
        dictItemVo.setItemSort(sysDictItem.getItemSort());
        dictItemVo.setItemLevel(sysDictItem.getItemLevel());
        dictItemVo.setItemLabel(sysDictItem.getItemLabel());
        dictItemVo.setItemValue(sysDictItem.getItemValue());
        dictItemVo.setStatus(sysDictItem.getStatus());
        dictItemVo.setRemark(sysDictItem.getRemark());
        dictItemVo.setCreateTimeFormat(DateUtils.format(sysDictItem.getCreateTime()));
        dictItemVo.setUpdateTimeFormat(DateUtils.format(sysDictItem.getUpdateTime()));

        return dictItemVo;
    }

    public List<DictItemVo> queryListByDictId(long id) {
        List<SysDictItem> sysDictItems = sysDictItemService.lambdaQuery()
                .eq(SysDictItem::getDictId, id)
                .orderByAsc(SysDictItem::getItemSort)
                .list();

        if (CollectionUtils.isEmpty(sysDictItems)) {
            return new LinkedList<>();
        }

        return sysDictItems.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    /**
     * 根据字典项id查询字典项
     *
     * @param dictItemId 字典项id
     *
     * @return 字典项
     */
    public DictItemVo getSingleById(Long dictItemId) {
        SysDictItem sysDictItem = sysDictItemService.getById(dictItemId);

        if (Objects.isNull(sysDictItem)) {
            return null;
        }

        return convert(sysDictItem);
    }

    public String dictItemLabelName(Long itemId) {
        return this.getSingleById(itemId).getItemLabel();
    }

    /**
     * 级联保存字典项<br/>
     * <p>
     * 注意: <br/>
     * - 级别不会自动生层<br/>
     * - 不会自动关联<br/>
     * - 不会去设置父子级联
     * </p>
     *
     * @param prodCategoryItem 字典项
     */
    public void saveDictItemWithCascade(DictItemVo prodCategoryItem) {
        //保存当前节点
        SysDictItem sysDictItem = convert2Model(prodCategoryItem);
        sysDictItemService.save(sysDictItem);

        if (prodCategoryItem.getChildren() == null || prodCategoryItem.getChildren().isEmpty()) {
            return;
        }

        for (DictItemVo child : prodCategoryItem.getChildren()) {
            saveDictItemWithCascade(child);
        }

    }

    public void ensureItemBatch(List<DictItemVo> batchWriteItem) {
        if (Objects.isNull(batchWriteItem) || batchWriteItem.isEmpty()) {
            return;
        }

        List<SysDictItem> sysDictItems = batchWriteItem.stream()
                .filter(dictItemVo -> 0 == sysDictItemService.count(new LambdaQueryWrapper<SysDictItem>()
                                                                            .eq(SysDictItem::getId, dictItemVo.getId())))
                .map(this::convert2Model)
                .collect(Collectors.toList());

        sysDictItemService.saveBatch(sysDictItems);
    }

    public SysDictItem convert2Model(DictItemVo dictItemVo) {
        SysDictItem sysDictItem = new SysDictItem();
        sysDictItem.setId(dictItemVo.getId());
        sysDictItem.setDictId(dictItemVo.getDictId());
        sysDictItem.setItemParentId(dictItemVo.getItemParentId());
        sysDictItem.setItemSort(dictItemVo.getItemSort());
        sysDictItem.setItemLevel(dictItemVo.getItemLevel());
        sysDictItem.setItemLabel(dictItemVo.getItemLabel());
        sysDictItem.setItemValue(dictItemVo.getItemValue());
        sysDictItem.setStatus(dictItemVo.getStatus());
        sysDictItem.setRemark(dictItemVo.getRemark());
        sysDictItem.setCreateTime(DateUtils.parse(dictItemVo.getCreateTimeFormat()));
        sysDictItem.setUpdateTime(DateUtils.parse(dictItemVo.getUpdateTimeFormat()));
        return sysDictItem;
    }

    public List<DictItemVo> queryAssociatedFromSourceId(Long sourceItemId, Long targetDictId) {
        List<SysDictItem> itmeList = sysDictItemMapper.queryAssociatedFromSourceId(sourceItemId, targetDictId);
        if (Objects.isNull(itmeList)) {
            return new LinkedList<>();
        }

        return itmeList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public List<DictItemVo> queryAssociatedFromTargetId(Long targetItemId, Long targetDictId) {
        List<SysDictItem> itmeList = sysDictItemMapper.queryAssociatedFromTargetId(targetItemId, targetDictId);
        if (Objects.isNull(itmeList)) {
            return new LinkedList<>();
        }

        return itmeList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public List<Long> queryIdsByLikeLevel(String itemLevel, Long dictId) {
        return sysDictItemMapper.queryIdsByLikeLevel(itemLevel, dictId);
    }

    public List<DictItemTreeVo> listDictItemTree(long dictId, Long dictItemId) {
        //加载并排序,生成一下顺序
        //A001
        //A001002
        //A002
        //A002002
        //A002002001
        //A003
        //A003001
        //A003002
        List<SysDictItem> sysDictItems = sysDictItemService.lambdaQuery()
                .eq(SysDictItem::getDictId, dictId)
                .eq(Objects.nonNull(dictItemId), SysDictItem::getId, dictItemId)
                .orderByAsc(SysDictItem::getItemLevel)//按照层级排序
                .list();

        if (Objects.isNull(sysDictItems) || sysDictItems.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, DictItemTreeVo> tempCache = new HashMap<>();

        //转换
        List<DictItemTreeVo> dictItemTreeVos = new ArrayList<>();
        int levelCharSize = 3;

        //处理第一个元素
        SysDictItem preSysDictItem = sysDictItems.get(0);
        DictItemTreeVo treeVo = toItemTree(preSysDictItem);
        tempCache.put(treeVo.getItemLevel(), treeVo);
        dictItemTreeVos.add(treeVo);

        for (int i = 1; i < sysDictItems.size(); i++) {
            SysDictItem sysDictItem = sysDictItems.get(i);
            treeVo = toItemTree(sysDictItem);
            tempCache.put(treeVo.getItemLevel(), treeVo);

            String curtItemLevel = sysDictItem.getItemLevel();

            //顶级节点
            if (curtItemLevel.length() == (levelCharSize + 1)) {
                dictItemTreeVos.add(treeVo);

                continue;
            }

            //两个等级长度一致: A001, A002; 或则子节点: A001001, A001002
            DictItemTreeVo parent = tempCache.get(curtItemLevel.substring(0, curtItemLevel.length() - levelCharSize));

            //兼容老旧代码结构
            if (Objects.isNull(parent)) {
                log.error("parent is null, itemLevel:{}", curtItemLevel);
                continue;
            }

            parent.getChildren().add(treeVo);
        }

        return dictItemTreeVos;
    }

    private DictItemTreeVo toItemTree(SysDictItem sysDictItem) {
        DictItemTreeVo dictItemTreeVo = new DictItemTreeVo();
        dictItemTreeVo.setId(sysDictItem.getId());
        dictItemTreeVo.setDictId(sysDictItem.getDictId());
        dictItemTreeVo.setItemParentId(sysDictItem.getItemParentId());
        dictItemTreeVo.setItemSort(sysDictItem.getItemSort());
        dictItemTreeVo.setItemLevel(sysDictItem.getItemLevel());
        dictItemTreeVo.setItemLabel(sysDictItem.getItemLabel());
        dictItemTreeVo.setItemValue(sysDictItem.getItemValue());
        dictItemTreeVo.setStatus(sysDictItem.getStatus());
        dictItemTreeVo.setRemark(sysDictItem.getRemark());

        dictItemTreeVo.setChildren(new ArrayList<>());

        return dictItemTreeVo;
    }

    /**
     * 根据字典id批量删除
     *
     * @param ids 字典项id
     */
    public void batchDeleteDictItem(List<Long> ids) {
        Assert.isTrue(ObjectUtils.isNotEmpty(ids), "id不能为空");
        DatabaseContextHolder.softDelete(SysDictItem.class, Sets.newHashSet(ids));
    }

    public List<IotSysUnitVo> queryUnitList() {
        return iotSysUnitMapper.queryAll();
    }

    public Integer deleteUnit(Long id) {
        Integer total = iotSysUnitMapper.checkIsUsed(id);
        Assert.isTrue(0 == total, "已被使用的单位无法删除");
        return iotSysUnitMapper.deleteById(id);
    }

    public void saveUnit(IotSysUnitVo iotSysUnitVo) {
        if (ObjectUtils.isEmpty(iotSysUnitVo.getId())) {
            IotSysUnitEntity entity = convertVo2Entity(new IotSysUnitEntity(), iotSysUnitVo);
            entity.setId(IdWorker.getId());
            iotSysUnitMapper.insert(entity);
        } else {
            IotSysUnitEntity entityOld = iotSysUnitMapper.selectById(iotSysUnitVo.getId());
            IotSysUnitEntity entity = convertVo2Entity(entityOld, iotSysUnitVo);
            iotSysUnitMapper.updateById(entity);
        }
    }

    private IotSysUnitEntity convertVo2Entity(IotSysUnitEntity entity, IotSysUnitVo iotSysUnitVo) {
        entity.setId(iotSysUnitVo.getId());
        entity.setUnitCode(iotSysUnitVo.getUnitCode());
        entity.setUnitName(iotSysUnitVo.getUnitName());
        entity.setUnitStatus(iotSysUnitVo.getUnitStatus());
        entity.setUnitType(iotSysUnitVo.getUnitType());
        entity.setRemark(iotSysUnitVo.getRemark());
        return entity;
    }
}
package com.guhecloud.cube.biz.mdata.server.stall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.stall.dao.StallMapper;
import com.guhecloud.cube.biz.mdata.server.stall.dto.DbcStallDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallIdTypeDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallStatDTO;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.param.ExcelTemplate;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCreateBathParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCreateParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallUpdateParam;
import com.guhecloud.cube.biz.mdata.server.stall.rocketmq.message.StallSyncMessage;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallExpandService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.stat.param.AreaStatQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierStall;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierStallService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.mdata.server.constants.FileSceneConstants.STALL_INFO;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallServiceImpl extends ServiceImpl<StallMapper, Stall> implements StallService {

    private final SupplierStallService supplierStallService;
    private final StallExpandService stallExpandService;
    private final StallMapper stallMapper;
    private final FileRefClient fileRefClient;
    private final FileClient fileClient;

    @Override
    public void save(@NonNull StallCreateParam param) {
        Assert.isConflict(Objects.nonNull(this.getByCode(param.getCode())), "存在档位编号");
        Assert.isConflict(Objects.nonNull(this.getByName(param.getName())), "存在档位名称");
        if (StringUtils.isNotEmpty(param.getVisualizationCode())) {
            Assert.isConflict(Objects.nonNull(this.getByVisualizationCode(param.getVisualizationCode())), "存在相同模型编码");
        }
        Stall stall = ObjectUtils.copying(param, Stall.class);
        stall.setDeptId(String.join(",", param.getDeptIds()));
        stall.setArea(String.join(",", param.getAreaIds()));
        if (Objects.equals(Boolean.FALSE, param.getEnabled()) &&
            (Objects.equals(Boolean.TRUE, param.getLeased()) || Objects.equals(Boolean.TRUE, param.getLeaseType()))) {
            throw new ResourceConflictException("禁用状态下出租属性与出租状态不能开启");
        }
        if (StringUtils.isBlank(param.getVisualizationCode())) {
            stall.setVisualizationCode(null);
        }
        stall.setId(IdWorker.getId());
        stall.setUploadFileState(Boolean.FALSE);
        if (!CollectionUtils.isEmpty(param.getFiles())) {
            uploadStallFiles(stall, param.getFiles());
        }
        this.save(stall);
        this.stallExpandService.save(stall.getId(), param.getStallExpand());
        //推送消息
        this.sendStallMessage(Collections.singletonList(stall));
    }

    /**
     * 导表档位
     *
     * @param param 档位新增参数
     */
    @Override
    public List<String> excelSave(List<ExcelTemplate> param, Map<String, ExcelColumnSelectionProvider> providerMap) {
        List<String> errMsg = new LinkedList<>();
        List<Stall> stalls = new ArrayList<>(param.size());
        for (ExcelTemplate batchImportExcelTemplate : param) {
            try {
                if (this.stallDataValidity(batchImportExcelTemplate)) {
                    errMsg.add("导入错误:建筑面积与实用面积数据格式不合法,应为数字类型");
                    continue;
                }
                if (this.requiredValidity(errMsg, batchImportExcelTemplate)) {
                    continue;
                }
                Stall stall = convertToModel(batchImportExcelTemplate, providerMap);
                //插入前进行判断
                Stall stallByName = this.getByName(stall.getName());
                Assert.isConflict(Objects.nonNull(stallByName) && !stall.getId().equals(stallByName.getId()), "存在相同档位名称");
                if (StringUtils.isNotEmpty(stall.getVisualizationCode())) {
                    Stall stallByVisualizationCode = this.getByVisualizationCode(stall.getVisualizationCode());
                    Assert.isConflict(
                            Objects.nonNull(stallByVisualizationCode) &&
                            !stall.getId().equals(stallByVisualizationCode.getId()), "存在模型编码!");
                }
                this.saveOrUpdate(stall);
            } catch (DuplicateKeyException e) {
                errMsg.add("导入错误:档位已经存在");
            }
        }
        //推送消息
        this.sendStallMessage(stalls);
        return errMsg;
    }

    public Stall convertToModel(ExcelTemplate excelTemplate, Map<String, ExcelColumnSelectionProvider> providerMap) {
        Stall stall = this.getByCode(excelTemplate.getCode());
        if (Objects.isNull(stall)) {
            stall = new Stall();
            stall.setId(IdWorker.getId());
        }
        stall.setCode(excelTemplate.getCode());
        stall.setName(excelTemplate.getName());
        stall.setVisualizationCode(StringUtils.isBlank(excelTemplate.getVisualizationCode()) ? null : excelTemplate.getVisualizationCode());
        stall.setMasterName(excelTemplate.getMasterName());
        stall.setMasterPhone(excelTemplate.getMasterPhone());
        stall.setLocation(excelTemplate.getLocation());
        stall.setBuiltUpArea(StringUtils.isNotBlank(excelTemplate.getBuiltUpArea()) ?
                             excelTemplate.getBuiltUpArea().trim() : null);
        stall.setPracticalArea(StringUtils.isNotBlank(excelTemplate.getPracticalArea()) ?
                               excelTemplate.getPracticalArea().trim() : null);
        stall.setEnabled(true);
        stall.setLeaseType(
                StringUtils.isNotBlank(excelTemplate.getLeaseType()) ? Boolean.parseBoolean(excelTemplate.getLeaseType()) : Boolean.TRUE);
        stall.setLeased(
                StringUtils.isNotBlank(excelTemplate.getLeased()) ? Boolean.parseBoolean(excelTemplate.getLeased()) : Boolean.FALSE);
        //提供了下拉字典才进行设值
        if (Objects.nonNull(providerMap) && !providerMap.isEmpty()) {
            //部门转换
            ExcelColumnSelectionItem item = findMatchedItem(excelTemplate.getDept(), ExcelTemplate.DEPARTMENT_TYPE_KEY, providerMap);
            stall.setDeptId(item.getValue());
            stall.setDeptName(item.getName());

            //区域转换
            item = findMatchedItem(excelTemplate.getArea(), ExcelTemplate.AREA_TYPE_KEY, providerMap);
            stall.setArea(item.getValue());

            //档位类型
            item = findMatchedItem(excelTemplate.getType(), ExcelTemplate.STALL_TYPE_KEY, providerMap);
            stall.setType(item.getValue());//Excel只能选择一个
        }
        return stall;
    }

    public boolean stallDataValidity(ExcelTemplate excelTemplate) {
        String builtUpArea = excelTemplate.getBuiltUpArea();
        String practicalArea = excelTemplate.getPracticalArea();
        return (StringUtils.isNotBlank(builtUpArea) && !isNumber(builtUpArea.trim())) ||
               (StringUtils.isNotBlank(practicalArea) && !isNumber(practicalArea.trim()));
    }

    public boolean requiredValidity(List<String> errMsg, ExcelTemplate excelTemplate) {
        boolean validFail = false;
        if (StringUtils.isBlank(excelTemplate.getCode())) {
            errMsg.add("导入错误:档位编码不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getName())) {
            errMsg.add("导入错误:档位名称不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getType())) {
            errMsg.add("导入错误:档位类型不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getArea())) {
            errMsg.add("导入错误:档位区域不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getLocation())) {
            errMsg.add("导入错误:具体位置不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getBuiltUpArea())) {
            errMsg.add("导入错误:建筑面积不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getPracticalArea())) {
            errMsg.add("导入错误:实用面积不能为空");
            validFail = true;
        }
        if (StringUtils.isBlank(excelTemplate.getDept())) {
            errMsg.add("导入错误:所属部门不能为空");
            validFail = true;
        }
        return validFail;
    }

    /**
     * 根据提供的val在providerMap中查找item信息
     *
     * @param value       对比值
     * @param providerMap map
     *
     * @return item
     */
    private ExcelColumnSelectionItem findMatchedItem(String value, String type, Map<String, ExcelColumnSelectionProvider> providerMap) {
        //提取下拉信息并进行匹配
        ExcelColumnSelectionProvider excelColumnSelectionProvider = providerMap.get(type);
        for (ExcelColumnSelectionItem selectionItem : excelColumnSelectionProvider.selectionItems()) {
            if (selectionItem.getValue().equals(value)) {
                return selectionItem;
            }
        }

        throw new IllegalArgumentException("未找到匹配的信息");
    }

    @Override
    public void update(@NonNull StallUpdateParam param) {
        Stall stall = this.getById(param.getId());
        Assert.isNotFound(stall, "不存在档位!");
        Stall stallByName = this.getByName(param.getName());
        Assert.isConflict(
                Objects.nonNull(stallByName) && !stall.getId().equals(stallByName.getId()),
                "存在档位名称!");
        Stall stallByCode = this.getByCode(param.getCode());
        Assert.isConflict(
                Objects.nonNull(stallByCode) && !stall.getId().equals(stallByCode.getId()),
                "存在档位编号!");
        if (StringUtils.isNotEmpty(param.getVisualizationCode())) {
            Stall stallByVisualizationCode = this.getByVisualizationCode(param.getVisualizationCode());
            Assert.isConflict(Objects.nonNull(stallByVisualizationCode) &&
                              !stall.getId().equals(stallByVisualizationCode.getId()), "存在模型编码!");
        }
        ObjectUtils.copying(param, stall);
        if (Objects.equals(Boolean.FALSE, param.getEnabled()) &&
            (Objects.equals(Boolean.TRUE, param.getLeased()) || Objects.equals(Boolean.TRUE, param.getLeaseType()))) {
            throw new ResourceConflictException("禁用状态下出租属性与出租状态不能开启");
        }
        stall.setUploadFileState(Boolean.FALSE);
        stall.setArea(String.join(",", param.getAreaIds()));
        stall.setDeptId(String.join(",", param.getDeptIds()));
        if (StringUtils.isBlank(param.getVisualizationCode())) {
            stall.setVisualizationCode(null);
        }
        this.fileRefClient.removeByBusinessKeyAndScene(stall.getId(), STALL_INFO);
        if (!CollectionUtils.isEmpty(param.getFiles())) {
            uploadStallFiles(stall, param.getFiles());
        }
        this.updateById(stall);
        this.stallExpandService.save(stall.getId(), param.getStallExpand());
        //推送消息
        this.sendStallMessage(Collections.singletonList(stall));
    }

    public void uploadStallFiles(Stall stall, Set<Long> files) {
        FileRefCreateParam refCreateParam = new FileRefCreateParam();
        refCreateParam.setSceneCode(STALL_INFO);
        refCreateParam.setBusinessKey(stall.getId());
        refCreateParam.setFileIdList(new ArrayList<>(files));
        this.fileRefClient.refInduce(refCreateParam);
    }

    /**
     * 发送档位消息
     *
     * @param dataList
     */
    private void sendStallMessage(List<Stall> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                StallSyncMessage message = StallSyncMessage.builder().stallList(dataList).build();
                RocketMQContextHolder.send(StallSyncMessage.TOPIC, message);
            } catch (Exception e) {
                log.error("档位消息发送失败：", e);
            }
        }
    }

    private void sendStallMessage(StallSyncMessage message) {
        if (message != null) {
            try {
                RocketMQContextHolder.send(StallSyncMessage.TOPIC, message);
            } catch (Exception e) {
                log.error("档位消息发送失败：", e);
            }
        }
    }

    @Override
    public Page<Stall> page(StallQueryParam queryParam) {
        return this.lambdaQuery()
                .in(ListUtils.isNotEmpty(queryParam.getIds()), Stall::getId, queryParam.getIds())
                .eq(Objects.nonNull(queryParam.getBimBind()), Stall::getBimBind, queryParam.getBimBind())
                .eq(StringUtils.isNotEmpty(queryParam.getType()), Stall::getType, queryParam.getType())
                .eq(Objects.nonNull(queryParam.getEnabled()), Stall::getEnabled, queryParam.getEnabled())
                .eq(Objects.nonNull(queryParam.getLeaseType()), Stall::getLeaseType, queryParam.getLeaseType())
                .like(StringUtils.isNotEmpty(queryParam.getDepartmentId()), Stall::getDeptId, queryParam.getDepartmentId())
                .eq(Objects.nonNull(queryParam.getUploadFileState()), Stall::getUploadFileState, queryParam.getUploadFileState())
                .eq(Objects.nonNull(queryParam.getLeased()), Stall::getLeased, queryParam.getLeased())
                .like(Objects.nonNull(queryParam.getAreaId()), Stall::getArea, queryParam.getAreaId())
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Stall::getCode, queryParam.getKeyword())
                        .or()
                        .like(Stall::getName, queryParam.getKeyword()
                             )
                    )
                .orderByDesc(Stall::getCreateTime)
                .orderByDesc(Stall::getCode)
                .orderByDesc(Stall::getId)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        this.sendStallMessage(StallSyncMessage.buildDelStallMessage(Arrays.asList(id)));
        this.stallExpandService.removeByStallId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSupplierStall(@NonNull Collection<Long> stallIds, @NonNull Long supplierId) {
        this.lambdaUpdate().set(Stall::getLeaseType, false)
                .set(Stall::getLeased, true)
                .in(Stall::getId, stallIds).update();
        List<SupplierStall> supplierStalls = new ArrayList<>();
        stallIds.forEach(stallId -> {
            SupplierStall supplierStall = new SupplierStall();
            supplierStall.setStallId(stallId);
            supplierStall.setSupplierId(supplierId);
            supplierStalls.add(supplierStall);
        });
        this.supplierStallService.saveBatch(supplierStalls);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSupplierStall(@NonNull Long supplierId) {
        List<SupplierStall> list = this.supplierStallService.lambdaQuery()
                .eq(SupplierStall::getSupplierId, supplierId).list();
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> stallIds = list.stream().map(SupplierStall::getStallId).collect(Collectors.toList());
            List<Stall> stalls = this.listByIds(stallIds);
            stalls.stream().forEach(stall -> {
                if (Boolean.TRUE.equals(stall.getEnabled())) {
                    stall.setLeaseType(true);
                } else {
                    stall.setLeaseType(false);
                }
                stall.setLeased(false);
                this.updateById(stall);
            });
            this.supplierStallService.removeByIds(list.stream().map(SupplierStall::getId).collect(Collectors.toList()));
        }
    }

    @Override
    public List<Stall> getByCategoryId(@NonNull Long id) {
        return this.lambdaQuery().eq(Stall::getType, id).list();
    }

    @Override
    public List<Stall> getByIds(@NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(Stall::getId, ids).list();
    }

    @Override
    public Stall getByName(@NonNull String name) {
        return this.lambdaQuery().eq(Stall::getName, name).one();
    }

    @Override
    public Stall getByCode(@NotNull String code) {
        return this.lambdaQuery().eq(Stall::getCode, code).one();
    }

    @Override
    public Stall getByVisualizationCode(@NonNull String code) {
        return this.lambdaQuery().eq(Stall::getVisualizationCode, code).one();
    }

    @Override
    public List<Stall> listStalls(@NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(Stall::getId, ids).list();
    }

    @Override
    public Page<Stall> listAreaStalls(@NonNull Long areaId, Paging paging) {
        return this.lambdaQuery().like(Stall::getArea, areaId).page(PagingUtils.convert(paging));
    }

    @Override
    public List<Stall> list(StallQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(queryParam.getType()), Stall::getType, queryParam.getType())
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Stall::getName, queryParam.getKeyword())
                .list();
    }

    @Override
    public List<StallIdTypeDTO> getStallIdTypes(StallQueryParam queryParam) {
        var stalls = this.lambdaQuery()
                .select(Stall::getId, Stall::getName, Stall::getType)
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Stall::getName, queryParam.getKeyword())
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), Stall::getId, queryParam.getIds())
                .like(StringUtils.isNotEmpty(queryParam.getDepartmentId()), Stall::getDeptId, queryParam.getDepartmentId())
                .list();
        return stalls.stream().map(stall -> new StallIdTypeDTO(stall.getId(), stall.getName(), stall.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Stall> listByAreas(@NonNull Collection<String> areas) {
        return CollectionUtils.isEmpty(areas) ? Collections.emptyList() :
               this.baseMapper.listByAreas(areas);
    }

    @Override
    public Page<Stall> pageByAreas(@NonNull AreaStatQueryParam queryParam) {
        return this.baseMapper.pageByAreas(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public List<StallStatDTO> getStallStatByArea() {
        return stallMapper.getStallNumByArea();
    }

    @Override
    public void updateBindBim(@NonNull Long id, @NonNull Boolean bimBind) {
        Stall stall = this.getById(id);
        if (Objects.nonNull(stall)) {
            stall.setBimBind(bimBind);
            this.baseMapper.updateById(stall);
        }
    }

    @Override
    public List<Stall> getMineStalls(Supplier supplier) {
        List<SupplierStall> supplierStalls = this.supplierStallService.listBySupplier(supplier.getId());
        if (CollectionUtils.isEmpty(supplierStalls)) {
            return Collections.emptyList();
        }
        Set<Long> stallIds = supplierStalls.stream().map(SupplierStall::getStallId).collect(Collectors.toSet());
        return this.listByIds(stallIds);
    }

    /**
     * 挡位批量新增(大白菜同步挡位数据使用)
     *
     * @param param 挡位
     */
    @Override
    public void saveOrUpdateBatch(StallCreateBathParam param) {
        List<DbcStallDTO> paramList = param.getStallList();
        if (paramList == null || paramList.isEmpty()) {
            return;
        }
        List<Stall> stallList = new ArrayList<>();
        for (DbcStallDTO dbcStallDTO : paramList) {
            Stall stall = ObjectUtils.copying(dbcStallDTO, Stall.class);
            // 这里框架序列化有问题，唯一键插入不进去，特殊处理
            stall.setVisualizationCode(null);
            if (StringUtils.isBlank(stall.getArea())) {
                stall.setArea(null);
            }
            if (StringUtils.isBlank(stall.getDeptId())) {
                stall.setDeptId(null);
            }
            stallList.add(stall);
        }
        this.saveOrUpdateBatch(stallList, 1000);
    }

    /**
     * 获取挡位数据(完整PO信息)
     *
     * @return 挡位集合
     */
    @Override
    public List<DbcStallDTO> getAllStallEntity() {
        return ConvertUtils.toList(DbcStallDTO.class, this.list());
    }

    @Override
    public Set<Long> getStallsBySceneConfig(StallSceneConfigModifyParam param) {
        Set<Long> stallIds = new HashSet<>();
        switch (param.getStallBindWay()) {
            case APPOINT_STALL:
                stallIds = param.getBusinessIds();
                break;
            case ALL_STALL:
                stallIds = getConditionFilterStallsBySceneConfig(param);
                break;
            default:
                log.debug("not found stall selected way");
        }
        return stallIds;
    }

    @Override
    public void stallUploadFileStateSync() {
        SessionContextHolder.clearSessionContext();
        Long stallCount = this.stallMapper.selectCount(null);
        if (stallCount <= 0) {
            return;
        }
        long pages = stallCount / 1000;
        if (stallCount % 1000 != 0) {
            pages = pages + 1;
        }
        Page<Stall> page = new Page<>();
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        for (int startPage = 1; startPage <= pages; startPage++) {
            page.setCurrent(startPage);
            page.setSize(1000);
            List<Stall> stalls = this.lambdaQuery().page(page).getRecords();
            businessKeysParam.setBusinessKeys(stalls.stream().map(Stall::getId).collect(Collectors.toList()));
            Map<Long, List<FileVO>> stallFileMap = this.fileClient.getByBusinessKeys(businessKeysParam);
            stalls.stream().forEach(stall -> {
                SessionContextHolder.requestTenant(stall.getTenant());
                stall.setUploadFileState(Boolean.FALSE);
                if (stallFileMap.containsKey(stall.getId())) {
                    stall.setUploadFileState(Boolean.TRUE);
                }
                this.updateById(stall);
            });
            SessionContextHolder.clearSessionContext();
        }
    }

    @Override
    public Long getStallMasterCount() {
        return this.baseMapper.selectCount(Wrappers.<Stall>lambdaQuery()
                                                   .isNotNull(Stall::getMasterName)
                                                   .ne(Stall::getMasterName, "")
                                          );
    }

    private Set<Long> getConditionFilterStallsBySceneConfig(StallSceneConfigModifyParam param) {
        return this.baseMapper.selectConditionFilterStallsBySceneConfig(param);
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Map<Long, Long> countByAreaIds(List<Long> areaIds) {
        Map<Long, Long> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(areaIds)) {
            return resultMap;
        }

        // 初始化结果Map
        for (Long areaId : areaIds) {
            resultMap.put(areaId, 0L);
        }

        // 批量查询所有档位（只查询area字段，提高效率）
        List<Stall> allStalls = this.list(
                Wrappers.<Stall>lambdaQuery()
                        .select(Stall::getArea)
                        .isNotNull(Stall::getArea)
                        .ne(Stall::getArea, "")
                                         );

        // 统计每个区域的档位数
        // Stall的area字段可能包含区域ID，使用like匹配
        for (Stall stall : allStalls) {
            if (StringUtils.isBlank(stall.getArea())) {
                continue;
            }

            // 检查area字段是否包含区域ID
            for (Long areaId : areaIds) {
                if (stall.getArea().contains(String.valueOf(areaId))) {
                    resultMap.put(areaId, resultMap.getOrDefault(areaId, 0L) + 1);
                }
            }
        }

        return resultMap;
    }
}

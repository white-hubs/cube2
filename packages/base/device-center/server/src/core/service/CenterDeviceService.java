package com.guhecloud.cube.base.device.center.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.dto.CreateOrUpdateDeviceDTO;
import com.guhecloud.cube.base.device.center.core.dto.DeviceListDto;
import com.guhecloud.cube.base.device.center.core.dto.DevicePageDto;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionItem;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceExpandMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceRefMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.model.BizDeviceExpand;
import com.guhecloud.cube.base.device.center.core.model.BizDeviceRef;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagRef;
import com.guhecloud.cube.base.device.center.core.param.DeviceCustomerPageParam;
import com.guhecloud.cube.base.device.center.core.param.DeviceListQueryParam;
import com.guhecloud.cube.base.device.center.core.param.DevicePageParam;
import com.guhecloud.cube.base.device.center.core.rule.DeviceCustomerFieldWrapper;
import com.guhecloud.cube.base.device.center.core.vo.BatchImportExcelTemplate;
import com.guhecloud.cube.base.device.center.core.vo.DeviceIdAndCode;
import com.guhecloud.cube.base.device.center.core.vo.RemoteThirdPartyIdBind;
import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.ChangeDeviceLntLatCmd;
import com.guhecloud.cube.base.device.center.metadata.param.ChangeDeviceSpatialCoordinatesCmd;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceExpandVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.command.ChangeDeviceInfoCmd;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceBaseArchivesInfoVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceDetailVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.SlaveDeviceVo;
import com.guhecloud.cube.base.device.center.open.service.DeviceInstanceService;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.iot.mgt.core.data.sys.DeviceCodeNameChange;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductChangeEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.DEVICE_CHANGE_TOPIC;
import static com.guhecloud.cube.base.device.center.metadata.lang.Const.PRODUCT_CHANGE_TOPIC;

@Slf4j
@Service
public class CenterDeviceService {
    @Resource
    private CenterBizDeviceMapper centerBizDeviceMapper;
    @Resource
    private CenterBizDeviceExpandMapper deviceExpandMapper;
    @Resource
    private UserClient userClient;
    @Resource
    private AreaClient areaClient;
    @Resource
    private FileClient fileClient;
    @Resource
    private FileRefClient fileRefClient;
    @Resource
    private OrganizationClient organizationClient;
    @Resource
    private CenterBizDeviceRefMapper centerBizDeviceRefMapper;
    @Resource
    private CenterBizTagRefMapper centerBizTagRefMapper;
    @Resource
    private CenterBizTagMapper centerBizTagMapper;
    @Autowired
    private DeviceInstanceService deviceInstanceService;
    @Resource
    private DevDeviceService devDeviceService;

    public Pagination<DevicePageDto> page(DevicePageParam pageParam) {
        Page<DevicePageDto> list = centerBizDeviceMapper.page(PagingUtils.convert(pageParam), pageParam);
        Pagination<DevicePageDto> pagination = PagingUtils.convert(list);
        if (Objects.nonNull(pagination.getRecords())) {
            pagination.getRecords().forEach(this::fillAreaPair);
        }
        return pagination;
    }

    /**
     * 批量删除设备
     *
     * @param ids ids
     *
     * @return true/false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDel(List<Long> ids) {
        LocalDateTime delDate = LocalDateTime.now();
        final Long userId = SecurityContextHolder.getPrincipal().getId();
        final String userType = SecurityContextHolder.getPrincipal().getType().name();
        //物联中心设备逻辑删除
        centerBizDeviceMapper.logicDeletedBatchIds(String.valueOf(userId), userType, ids, delDate);
        //设备属性扩展信息逻辑删除
        deviceExpandMapper.logicDeleteBatchIds(String.valueOf(userId), userType, ids, delDate);
        //设备关联标签逻辑删除
        centerBizTagRefMapper.logicDeleteByDeviceIds(String.valueOf(userId), userType, ids, delDate);
        //设备关联引用逻辑删除
        centerBizDeviceRefMapper.logicDeleteByDeviceIds(String.valueOf(userId), userType, ids, delDate);
        log.info("delete device success,id:{}", ids);

        //通知设备变更
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setTs(System.currentTimeMillis());
        deviceChange.setDeviceChangePairs(new ArrayList<>(ids.size()));

        for (Long deviceId : ids) {
            Long product = centerBizDeviceMapper.queryProductIdByDeviceId(deviceId);

            //通知设备变更
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceId(deviceId);
            deviceInfo.setDeviceProductId(product);
            deviceChange.getDeviceChangePairs()
                    .add(Pair.of(deviceInfo, DeviceChangeEnum.DELETE));
        }
        RocketMQContextHolder.sendAfterCommit(DEVICE_CHANGE_TOPIC, deviceChange);
        return true;
    }

    /**
     * 判断一个设备是否存在<br/>
     * 被标记删除的设备不进行判断
     *
     * @param iotId 物联网的设备ID
     *
     * @return true/false
     */
    public boolean existDeviceId(Long iotId) {
        return centerBizDeviceMapper.existDeviceId(iotId);
    }

    /**
     * 批量导入设备
     *
     * @param batchImportExcelTemplates templates
     * @param providerMap               传入下拉providerMap避免多次拉取
     *
     * @return import error
     */
    public List<String> batchImportDevice(List<BatchImportExcelTemplate> batchImportExcelTemplates,
                                          Map<String, ExcelColumnSelectionProvider> providerMap, String tagCode
                                         ) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<String> errMsg = new LinkedList<>();
        for (BatchImportExcelTemplate batchImportExcelTemplate : batchImportExcelTemplates) {
            try {
                BizDevice bizDevice = convertToModel(batchImportExcelTemplate, providerMap);
                bizDevice.setId(Long.valueOf(batchImportExcelTemplate.getIotDeviceId()));//iot设备ID
                bizDevice.setDeviceIotProdId(Long.valueOf(batchImportExcelTemplate.getProductId()));//iot产品ID
                bizDevice.setDeviceState(0);

                //插入前进行判断,这里需要判断设备已经存在，导入的核心是补齐扩展字段，设备由IOT创建并同步到当前应用
                if (deviceExit(bizDevice.getDeviceName(), bizDevice.getDeviceCode(),
                               Long.valueOf(batchImportExcelTemplate.getIotDeviceId()),
                               bizDevice.getDeviceIotProdId(), tenant)) {
                    errMsg.add(batchImportExcelTemplate.toSimpleString() + "导入错误:已存在相同的(设备名称、设备编码)");
                    continue;
                }
                if (deviceExpandExit(batchImportExcelTemplate)) {
                    errMsg.add(batchImportExcelTemplate.toSimpleString() + "导入错误:使用寿命与采购单价数据格式不合法,应为数字类型");
                    continue;
                }
                BizDeviceExpand bizDeviceExpand = convertToExpandModel(batchImportExcelTemplate);

                saveUpdateDevice(bizDevice, bizDeviceExpand, tagCode);

                //处理设备导入标签
                buildDeviceImportTags(batchImportExcelTemplate);

                //IOT批量导入并没有扩展信息，所以不需要导入
            } catch (DuplicateKeyException e) {
                errMsg.add(batchImportExcelTemplate.toSimpleString() + "导入错误:设备已经存在");
            } catch (Exception e) {
                errMsg.add(batchImportExcelTemplate.toSimpleString() + "导入错误：" + e.getMessage());
                log.error("save device form excel error", e);
            }
        }

        return errMsg;
    }

    private void buildDeviceImportTags(BatchImportExcelTemplate importExcelTemplate) {
        String customerTag = importExcelTemplate.getCustomerTag();
        if (StringUtils.isNotBlank(customerTag)) {
            List<Long> tagIds = getOrCreateTags(customerTag);
            bindDeviceImportTags(tagIds, importExcelTemplate.getIotDeviceId());
        }
    }

    private void bindDeviceImportTags(List<Long> tagIds, String iotDeviceId) {
        List<BizTagRef> tagRefs = new ArrayList<>();
        Long deviceId = Long.valueOf(iotDeviceId);
        for (Long tagId : tagIds) {
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            BizTagRef tagRef = centerBizTagRefMapper.queryByDeviceAndTag(deviceId, tagId, tenant);
            if (ObjectUtils.isNull(tagRef)) {
                tagRef = new BizTagRef();
                tagRef.setId(IdWorker.getId());
                tagRef.setTagId(tagId);
                tagRef.setBusinessKey(deviceId);
                EntityAuditor.populateAudit(tagRef);
                tagRefs.add(tagRef);
            } else if (tagRef.getDeleted()) {
                centerBizTagRefMapper.recover(deviceId, tagId, tenant);
            }
        }

        if (!tagRefs.isEmpty()) {
            centerBizTagRefMapper.insertBatch(tagRefs);
        }
    }

    private List<Long> getOrCreateTags(String customerTag) {
        String[] split = customerTag.replace("，", ",").split(",");
        List<Long> tagIds = new ArrayList<>();
        for (String tagName : split) {
            BizTag bizTag = centerBizTagMapper.getTagByNameAndCodePrefix(tagName, Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE);
            if (ObjectUtils.isNull(bizTag)) {
                bizTag = new BizTag();
                bizTag.setTagCode(Const.DEVICE_CENTER_CUSTOMER_TAG_TYPE.concat(".").concat(Long.toString(IdUtils.next())));
                bizTag.setTagName(tagName);
                centerBizTagMapper.insert(bizTag);
            } else if (bizTag.getDeleted()) {
                centerBizTagMapper.recoverTag(bizTag.getId());
            }
            tagIds.add(bizTag.getId());
        }
        return tagIds;
    }

    /**
     * 新增或恢复设备信息
     *
     * @param bizDevice 设备信息
     */
    public void saveUpdateDevice(BizDevice bizDevice) {
        this.saveUpdateDevice(bizDevice, new BizDeviceExpand(), "");
    }

    /**
     * 新增或恢复设备信息
     *
     * @param bizDevice
     * @param bizDeviceExpand
     */
    public void saveUpdateDevice(BizDevice bizDevice, BizDeviceExpand bizDeviceExpand, String tagCode) {
        //通知设备变更
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setTs(System.currentTimeMillis());
        deviceChange.setDeviceChangePairs(new ArrayList<>(1));

        //通知设备变更
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(bizDevice.getId());
        deviceInfo.setDeviceProductId(bizDevice.getDeviceIotProdId());

        BizDevice oldDeviceInfo = centerBizDeviceMapper.getDeviceInfoNoTenant(bizDevice.getId());
        if (ObjectUtils.isEmpty(oldDeviceInfo)) {
            centerBizDeviceMapper.insert(bizDevice);
            //创建一条默认的扩展信息
            bizDeviceExpand.setId(bizDevice.getId());
            deviceExpandMapper.insert(bizDeviceExpand);

            //创建
            deviceChange.getDeviceChangePairs()
                    .add(Pair.of(deviceInfo, DeviceChangeEnum.CREATE));
        } else {
            bizDevice.setDeleted(false);
            bizDevice.setUpdateTime(LocalDateTime.now());
            ActorTypeEnum userType = ActorTypeEnum.safeValueOf(SecurityContextHolder.getPrincipal().getType().name());

            Principal principal = SecurityContextHolder.getPrincipal(true);
            String userId = Objects.isNull(principal) ? "system" : String.valueOf(principal.getId());

            bizDevice.setUpdateBy(userId);
            bizDevice.setUpdaterId(userId);
            bizDevice.setUpdaterType(userType);
            centerBizDeviceMapper.updateBySelective(bizDevice);
            //检查并恢复实例属性扩展
            checkRecoverDeviceExpand(userId, userType, bizDevice.getId(), bizDeviceExpand);
            //检查并恢复设备关联引用
            checkRecoverDeviceRef(userId, userType, bizDevice.getId());
            //检查并恢复设备标签关联
            checkRecoverDeviceTag(userId, userType, bizDevice.getId(), tagCode);
            if (!bizDevice.getDeviceIotProdId().equals(oldDeviceInfo.getDeviceIotProdId())) {
                //设备已存在时进行覆盖，产品ID不能进行变更
                throw new ResourcePreconditionFailedException("设备ID:" + bizDevice.getId(), "设备已存在，产品ID不能进行变更");
            }

            //更新
            deviceChange.getDeviceChangePairs()
                    .add(Pair.of(deviceInfo, DeviceChangeEnum.UPDATE));
        }
        RocketMQContextHolder.sendAfterCommit(DEVICE_CHANGE_TOPIC, deviceChange);
    }

    /**
     * 检查并恢复设备标签关联
     *
     * @param userId 用户id
     * @param id     设备id
     */
    private void checkRecoverDeviceTag(String userId, ActorTypeEnum userType, Long id, String tagCode) {
        BizTagRef tagRef = new BizTagRef();
        tagRef.setBusinessKey(id);
        tagRef.setDeleted(false);
        tagRef.setUpdateBy(userId);
        tagRef.setUpdaterId(userId);
        tagRef.setUpdaterType(userType);
        tagRef.setUpdateTime(LocalDateTime.now());
        BizTag bizTag = new BizTag();
        bizTag.setTagCode(tagCode);
        centerBizTagRefMapper.updateBySelective(tagRef, bizTag);
    }

    /**
     * 检查并恢复设备关联引用
     *
     * @param userId 用户id
     * @param id     设备id
     */
    private void checkRecoverDeviceRef(String userId, ActorTypeEnum userType, Long id) {
        BizDeviceRef deviceRef = new BizDeviceRef();
        deviceRef.setDeviceId(id);
        deviceRef.setDeleted(false);
        deviceRef.setUpdateBy(userId);
        deviceRef.setUpdaterId(userId);
        deviceRef.setUpdaterType(userType);
        deviceRef.setUpdateTime(LocalDateTime.now());
        centerBizDeviceRefMapper.updateBySelective(deviceRef);
    }

    /**
     * 检查并恢复设备实例属性
     *
     * @param userId 用户id
     * @param id     设备id
     */
    private void checkRecoverDeviceExpand(String userId, ActorTypeEnum userType, Long id) {
        this.checkRecoverDeviceExpand(userId, userType, id, new BizDeviceExpand());
    }

    /**
     * 检查并恢复设备实例属性
     *
     * @param userId
     * @param id
     * @param expand
     */
    private void checkRecoverDeviceExpand(String userId, ActorTypeEnum userType, Long id, BizDeviceExpand expand) {
        if (Objects.isNull(expand)) {
            expand = new BizDeviceExpand();
        }
        expand.setId(id);
        expand.setDeleted(false);
        expand.setUpdateBy(userId);
        expand.setUpdaterId(userId);
        expand.setUpdaterType(userType);
        expand.setUpdateTime(LocalDateTime.now());
        deviceExpandMapper.updateBySelective(expand);
    }

    /**
     * 判断设备名称,设备编码是否重复
     *
     * @param deviceName  设备名称
     * @param deviceCode  设备编码
     * @param iotDeviceId 物联网设备ID
     * @param productId    物联产品ID
     * @param tenant
     *
     * @return true/false
     */
    private boolean deviceExit(String deviceName, String deviceCode, Long iotDeviceId, Long productId, Integer tenant) {
        //设备ID相同，不进行判断
        return centerBizDeviceMapper.checkDeviceExist(deviceName, deviceCode, iotDeviceId, productId, tenant) > 0;
    }

    /**
     * 判断设备扩展信息是否合法
     *
     * @param batchImportExcelTemplate
     *
     * @return
     */
    private boolean deviceExpandExit(BatchImportExcelTemplate batchImportExcelTemplate) {
        String unitPrice = batchImportExcelTemplate.getUnitPrice();
        String usefulLife = batchImportExcelTemplate.getUsefulLife();
        return (StringUtils.isNotBlank(unitPrice) && StringUtils.isNotNumericSpace(unitPrice.trim())) ||
               (StringUtils.isNotBlank(usefulLife) && StringUtils.isNotNumericSpace(usefulLife.trim()));
    }

    /**
     * 将Excel模板转换为设备
     *
     * @param batchImportExcelTemplate model
     * @param providerMap              下拉映射
     *
     * @return bizDevice
     */
    public BizDevice convertToModel(BatchImportExcelTemplate batchImportExcelTemplate,
                                    Map<String, ExcelColumnSelectionProvider> providerMap
                                   ) {
        BizDevice bizDevice = new BizDevice();
        bizDevice.setDeviceName(batchImportExcelTemplate.getDeviceName());
        bizDevice.setDeviceCode(batchImportExcelTemplate.getIotDeviceCode());
        bizDevice.setDeviceIotProdId(Long.valueOf(batchImportExcelTemplate.getProductId()));

        //提供了下拉字典才进行设值
        if (Objects.nonNull(providerMap) && !providerMap.isEmpty()) {
            //部门转换
            ExcelColumnSelectionItem item = findMatchedItem(
                    batchImportExcelTemplate.getDepartmentId(),
                    "维护部门",
                    BatchImportExcelTemplate.DEPARTMENT_TYPE_KEY,
                    providerMap
            );
            bizDevice.setDepartmentId(Long.valueOf(item.getValue()));
            bizDevice.setDepartmentName(item.getName());

            //区域转换
            item = findMatchedItem(
                    batchImportExcelTemplate.getAreaId(),
                    "区域",
                    BatchImportExcelTemplate.AREA_TYPE_KEY,
                    providerMap
            );
            bizDevice.setAreaId(item.getValue());
            bizDevice.setAreaName(item.getName());
            bizDevice.setAreaLastId(Long.parseLong(findLastStr(item.getValue())));

            bizDevice.setDeviceState(0);//默认离线

            //管理人
            item = findMatchedItem(
                    batchImportExcelTemplate.getManagerIds(),
                    "维护人员",
                    BatchImportExcelTemplate.MANAGER_TYPE_KEY,
                    providerMap
            );
            bizDevice.setManagerIds(item.getValue());//Excel只能选择一个
            bizDevice.setManagerNames(item.getName());
        }

        bizDevice.setDeviceState(0);//离线
        return bizDevice;
    }

    /**
     * 将Excel模板转换为设备扩展信息
     *
     * @param batchImportExcelTemplate model
     *
     * @return bizDevice
     */
    public BizDeviceExpand convertToExpandModel(BatchImportExcelTemplate batchImportExcelTemplate) {
        BizDeviceExpand bizDeviceExpand = new BizDeviceExpand();
        bizDeviceExpand.setLntAndLat(batchImportExcelTemplate.getLntAndLat());
        bizDeviceExpand.setPurchaseDate(batchImportExcelTemplate.getPurchaseDate());
        bizDeviceExpand.setProductionDate(batchImportExcelTemplate.getProductionDate());
        bizDeviceExpand.setUnitPrice(batchImportExcelTemplate.getUnitPrice().replace(" ", ""));
        bizDeviceExpand.setUsefulLife(StringUtils.isNotBlank(batchImportExcelTemplate.getUsefulLife()) ?
                                      Integer.valueOf(batchImportExcelTemplate.getUsefulLife().replace(" ", "")) : null);
        return bizDeviceExpand;
    }

    /**
     * 根据提供的val在providerMap中查找item信息
     *
     * @param value       对比值
     * @param providerMap map
     *
     * @return item
     */
    private ExcelColumnSelectionItem findMatchedItem(String value, String fieldName, String type,
                                                     Map<String, ExcelColumnSelectionProvider> providerMap) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }

        //提取下拉信息并进行匹配
        ExcelColumnSelectionProvider excelColumnSelectionProvider = providerMap.get(type);
        if (Objects.isNull(excelColumnSelectionProvider)) {
            throw new IllegalArgumentException(fieldName + "下拉选项未加载成功，暂时无法校验导入值，请稍后重试");
        }

        List<ExcelColumnSelectionItem> selectionItems = excelColumnSelectionProvider.selectionItems();
        if (CollectionUtils.isEmpty(selectionItems)) {
            throw new IllegalArgumentException(fieldName + "下拉选项为空，暂时无法校验导入值，请稍后重试");
        }

        for (ExcelColumnSelectionItem selectionItem : selectionItems) {
            if (selectionItem.getValue().equals(value)) {
                return selectionItem;
            }
        }

        throw new IllegalArgumentException(String.format(
                "%s填写值[%s]无效，未匹配到模板下拉选项，请从模板下拉中选择有效值。可选项示例：%s",
                fieldName,
                value,
                buildSelectionPreview(selectionItems)
        ));
    }

    private String buildSelectionPreview(List<ExcelColumnSelectionItem> selectionItems) {
        return selectionItems.stream()
                .map(item -> String.format("%s(%s)", item.getName(), item.getValue()))
                .limit(5)
                .collect(Collectors.joining("、"));
    }

    /**
     * 根据提供的split找最后一个字符<br/>
     * eg: split(-) 323-2323-23 ----> 23 <br/>
     * eg: split(-) 2323 ----> throw IllegalArgumentException() <br/>
     *
     * @param source source
     *
     * @return str
     */
    private String findLastStr(String source) {
        if (StringUtils.isBlank(source)) {
            throw new IllegalArgumentException("非法的区域信息");
        }

        String[] result = source.split("-");
        if (result.length == 1) {
            return result[0];
        }

        if (result.length > 1) {
            return result[result.length - 1];
        } else {
            throw new IllegalArgumentException("非法的区域信息,未能找到最后一个区域id,info:" + source);
        }
    }

    /**
     * 更新设备密钥
     *
     * @param deviceId  设备ID
     * @param secretKey 密钥
     *
     * @return true/false
     */
    public boolean updateSecretKey(String deviceId, String secretKey) {
        BizDeviceExpand bizDeviceExpand = deviceExpandMapper.selectByIdNoTenant(Long.parseLong(deviceId));
        if (Objects.nonNull(bizDeviceExpand)) {
            bizDeviceExpand.setSecurityPwd(secretKey);
            bizDeviceExpand.setDeleted(false);
            return deviceExpandMapper.updateById(bizDeviceExpand) == 1;
        } else {
            bizDeviceExpand = new BizDeviceExpand();
            bizDeviceExpand.setSecurityPwd(secretKey);
            return deviceExpandMapper.insert(bizDeviceExpand) > 0;
        }
    }

    public boolean pwdCheck(Long deviceId, String pwd) {
        BizDeviceExpand bizDeviceExpand = deviceExpandMapper.selectOne(Wrappers.<BizDeviceExpand>lambdaQuery()
                                                                               .eq(BizDeviceExpand::getId, deviceId));

        //无密码直接通过
        if (Objects.isNull(bizDeviceExpand) || StringUtils.isBlank(bizDeviceExpand.getSecurityPwd())) {
            return true;
        }

        //有密码进行校验
        return bizDeviceExpand.getSecurityPwd().equals(pwd);
    }

    public CreateOrUpdateDeviceDTO detailOfMgt(Long deviceId) {
        BizDevice bizDevice = centerBizDeviceMapper.selectById(deviceId);
        BizDeviceExpand bizDeviceExpand = deviceExpandMapper.selectById(deviceId);

        if (Objects.isNull(bizDevice)) {
            throw new ResourcePreconditionFailedException("设备ID:" + deviceId, "设备不存在");
        }

        CreateOrUpdateDeviceDTO deviceDetailDTO = new CreateOrUpdateDeviceDTO();
        deviceDetailDTO.setId(bizDevice.getId().toString());
        deviceDetailDTO.setDeviceName(bizDevice.getDeviceName());
        deviceDetailDTO.setDeviceCode(bizDevice.getDeviceCode());
        deviceDetailDTO.setProductId(String.valueOf(bizDevice.getDeviceIotProdId()));
        if (Objects.nonNull(bizDevice.getDepartmentId())) {
            deviceDetailDTO.setDepartmentId(bizDevice.getDepartmentId().toString());
            deviceDetailDTO.setDepartmentName(bizDevice.getDepartmentName());
        }

        //恶心的代码，不得已而为之
        if (StringUtils.isNotBlank(bizDevice.getAreaId())) {
            deviceDetailDTO.setAreaIds(bizDevice.getAreaId());

            Collection<Long> ids = Arrays.stream(bizDevice.getAreaId().split("-")).map(Long::parseLong).collect(Collectors.toList());
            Map<Long, AreaDTO> areaIdNameMapping = areaClient.getByIds(ids).stream()
                    .collect(Collectors.toMap(AreaDTO::getId, Function.identity()));
            List<String> areaIdList = new ArrayList<>();
            List<String> areaNameList = new ArrayList<>();
            for (Long areaId : ids) {
                areaIdList.add(String.valueOf(areaId));
                areaNameList.add(areaIdNameMapping.get(areaId).getName());
            }
            deviceDetailDTO.setAreaIdList(areaIdList);
            deviceDetailDTO.setAreaNameList(areaNameList);
        }

        //恶心的代码，不得已而为之
        if (StringUtils.isNotBlank(bizDevice.getManagerIds())) {
            deviceDetailDTO.setManagerIds(bizDevice.getManagerIds());
            List<Long> managerIds = Arrays.stream(bizDevice.getManagerIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<String> managerIdList = new ArrayList<>();
            List<String> managerNameList = new ArrayList<>();
            for (UserDTO userDto : userClient.listEnableUserByIds(managerIds)) {
                managerIdList.add(userDto.getId().toString());
                managerNameList.add(userDto.getName());
            }
            deviceDetailDTO.setManagerIdList(managerIdList);
            deviceDetailDTO.setManagerNameList(managerNameList);
        }

        if (StringUtils.isNotBlank(bizDevice.getThirdPartyId())) {
            deviceDetailDTO.setThirdPartyId(bizDevice.getThirdPartyId());
        }

        if (StringUtils.isNotBlank(bizDevice.getThirdpartyCfg())) {
            deviceDetailDTO.setThirdpartyCfg(bizDevice.getThirdpartyCfg());
        }

        if (Objects.nonNull(bizDeviceExpand)) {
            deviceDetailDTO.setUsefulLife(bizDeviceExpand.getUsefulLife());
            deviceDetailDTO.setDailyWorkTime(bizDeviceExpand.getDailyWorkTime());
            deviceDetailDTO.setLntAndLat(bizDeviceExpand.getLntAndLat());
            deviceDetailDTO.setUnitPrice(bizDeviceExpand.getUnitPrice());
            deviceDetailDTO.setPurchaseDate(bizDeviceExpand.getPurchaseDate());
            deviceDetailDTO.setPicture(bizDeviceExpand.getPicture());

            if (Objects.nonNull(bizDeviceExpand.getPicture())) {
                deviceDetailDTO.setPictureList(fileClient.getByBusinessKey(bizDeviceExpand.getPicture()));
            }
        }

        return deviceDetailDTO;
    }

    public void fillAreaPair(DevicePageDto devicePageDto) {
        //处理区域对信息
        if (StringUtils.isBlank(devicePageDto.getAreaId())) {
            return;
        }

        List<Pair<String, String>> areaListPairs = new ArrayList<>(5);
        List<AreaDTO> areaList = areaClient.getByIds(Arrays.stream(devicePageDto.getAreaId().split("-")).filter(StringUtils::isNotBlank)
                                                             .map(Long::parseLong).collect(Collectors.toList()));

        //区域可能被删除
        if (Objects.isNull(areaList) || areaList.isEmpty()) {
            devicePageDto.setAreaId("");
            devicePageDto.setAreaName("");
            return;
        }

        Collection<Long> ids = Arrays.stream(devicePageDto.getAreaId().split("-")).filter(StringUtils::isNotBlank).map(Long::parseLong)
                .collect(Collectors.toList());
        Map<Long, AreaDTO> areaIdNameMapping = areaList.stream().collect(Collectors.toMap(AreaDTO::getId, Function.identity()));
        for (Long areaId : ids) {
            Pair<String, String> areaPair = new Pair<>();
            areaPair.setKey(String.valueOf(areaId));
            if (areaIdNameMapping.containsKey(areaId)) {
                areaPair.setVal(areaIdNameMapping.get(areaId).getName());
            }
            areaListPairs.add(areaPair);
        }
        devicePageDto.setAreaName(areaListPairs.stream().map(Pair::getVal).collect(Collectors.joining("-")));
        devicePageDto.setAreaPairs(areaListPairs);
    }

    public DeviceDetailVO changeDeviceInfo(ChangeDeviceInfoCmd changeDeviceInfoCmd) {
        // 校验三维模型编码唯一性
        checkDeviceCodeUnique(changeDeviceInfoCmd.getDeviceCode(), changeDeviceInfoCmd.getDeviceId());

        BizDevice bizDevice = buildBizDeviceByChangeCmd(changeDeviceInfoCmd);
        centerBizDeviceMapper.updateById(bizDevice);
        BizDeviceExpand bizDeviceExpand = buildBizDeviceExpandByChangeCmd(changeDeviceInfoCmd);
        deviceExpandMapper.updateById(bizDeviceExpand);

        //通知设备变更
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setTs(System.currentTimeMillis());
        deviceChange.setDeviceChangePairs(new ArrayList<>(1));
        deviceChange.getDeviceChangePairs()
                .add(Pair.of(DeviceInfo.of(bizDevice.getId(), bizDevice.getDeviceIotProdId()), DeviceChangeEnum.UPDATE));

        RocketMQContextHolder.sendAfterCommit(DEVICE_CHANGE_TOPIC, deviceChange);
        return getDeviceDetail(changeDeviceInfoCmd.getDeviceId());
    }

    public DeviceDetailVO changeDeviceSpatialCoordinates(ChangeDeviceSpatialCoordinatesCmd changeDeviceSpatialCoordinatesCmd) {
        DeviceBaseArchivesInfoDTO device = deviceInstanceService.getBaseInfoByDeviceCode(changeDeviceSpatialCoordinatesCmd.getCode());
        BizDeviceExpand deviceExpend = deviceExpandMapper.selectById(device.getId());
        deviceExpend.setSpatialCoordinates(changeDeviceSpatialCoordinatesCmd.getSpatialCoordinates());
        deviceExpandMapper.updateById(deviceExpend);
        return getDeviceDetail(device.getId());
    }

    /**
     * 更新设备经纬度与/或位置详细描述（扩展表 {@code lnt_and_lat}、{@code location_des}）
     */
    public Boolean changeDeviceLntLat(ChangeDeviceLntLatCmd cmd) {
        BizDevice bizDevice = centerBizDeviceMapper.selectById(cmd.getDeviceId());
        if (Objects.isNull(bizDevice)) {
            throw new ResourcePreconditionFailedException("deviceId", "设备不存在");
        }
        if (cmd.getLntAndLat() == null && cmd.getLocationDes() == null) {
            throw new ResourcePreconditionFailedException("lntAndLat", "经纬度与位置描述不能同时为空");
        }
        BizDeviceExpand bizDeviceExpand = deviceExpandMapper.selectById(cmd.getDeviceId());
        if (Objects.isNull(bizDeviceExpand)) {
            String userId = String.valueOf(SecurityContextHolder.getPrincipal().getId());
            int tenant = SessionContextHolder.getRequester().getTenant();
            bizDeviceExpand = new BizDeviceExpand();
            bizDeviceExpand.setId(cmd.getDeviceId());
            bizDeviceExpand.setDeleted(false);
            bizDeviceExpand.setTenant(tenant);
            EntityAuditor.populateAudit(bizDeviceExpand, userId, userId, ActorTypeEnum.safeValueOf(SecurityContextHolder.getPrincipal()
                                                                                                           .getType()
                                                                                                           .name()), LocalDateTime.now());
            if (cmd.getLntAndLat() != null) {
                bizDeviceExpand.setLntAndLat(cmd.getLntAndLat());
            }
            if (cmd.getLocationDes() != null) {
                bizDeviceExpand.setLocationDes(cmd.getLocationDes());
            }
            deviceExpandMapper.insert(bizDeviceExpand);
        } else {
            if (cmd.getLntAndLat() != null) {
                bizDeviceExpand.setLntAndLat(cmd.getLntAndLat());
            }
            if (cmd.getLocationDes() != null) {
                bizDeviceExpand.setLocationDes(cmd.getLocationDes());
            }
            deviceExpandMapper.updateById(bizDeviceExpand);
        }

        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setTs(System.currentTimeMillis());
        deviceChange.setDeviceChangePairs(new ArrayList<>(1));
        deviceChange.getDeviceChangePairs()
                .add(Pair.of(DeviceInfo.of(bizDevice.getId(), bizDevice.getDeviceIotProdId()), DeviceChangeEnum.UPDATE));
        RocketMQContextHolder.sendAfterCommit(DEVICE_CHANGE_TOPIC, deviceChange);
        return true;
    }

    private BizDeviceExpand buildBizDeviceExpandByChangeCmd(ChangeDeviceInfoCmd changeInfo) {
        //这里做一个前置动作，DeviceExpand之前可能不存在，插入一个先
        BizDeviceExpand bizDeviceExpand = deviceExpandMapper.selectById(changeInfo.getDeviceId());
        if (Objects.isNull(bizDeviceExpand)) {
            String userId = String.valueOf(SecurityContextHolder.getPrincipal().getId());
            int tenant = SessionContextHolder.getRequester().getTenant();

            bizDeviceExpand = new BizDeviceExpand();
            bizDeviceExpand.setId(changeInfo.getDeviceId());
            bizDeviceExpand.setDeleted(false);
            bizDeviceExpand.setTenant(tenant);
            EntityAuditor.populateAudit(bizDeviceExpand, userId, userId, ActorTypeEnum.safeValueOf(SecurityContextHolder.getPrincipal()
                                                                                                           .getType()
                                                                                                           .name()), LocalDateTime.now());
            deviceExpandMapper.insert(bizDeviceExpand);
        }

        //是后续逻辑不再变更
        bizDeviceExpand.setId(changeInfo.getDeviceId());
        bizDeviceExpand.setPicture(changeInfo.getPicture());
        bizDeviceExpand.setUnitPrice(ObjectUtils.isNotNull(changeInfo.getUnitPrice()) ? String.valueOf(changeInfo.getUnitPrice()) : null);
        bizDeviceExpand.setPurchaseDate(changeInfo.getPurchaseDate());
        bizDeviceExpand.setProductionDate(changeInfo.getProductionDate());
        bizDeviceExpand.setLntAndLat(changeInfo.getLntAndLat());
        bizDeviceExpand.setLocationDes(changeInfo.getLocationDes());
        bizDeviceExpand.setSpatialCoordinates(changeInfo.getSpatialCoordinates());
        //单位月
        if (ObjectUtils.isNotNull(changeInfo.getUsefulLife())) {
            bizDeviceExpand.setUsefulLife(changeInfo.getUsefulLife());
        }

        //安装图片关联
        if (Objects.nonNull(changeInfo.getPicture())) {
            fileRefClient.ref(changeInfo.getDeviceId(), Collections.singleton(changeInfo.getPicture()));
            bizDeviceExpand.setPicture(changeInfo.getPicture());
        }
        return bizDeviceExpand;
    }

    private BizDevice buildBizDeviceByChangeCmd(ChangeDeviceInfoCmd changeInfo) {
        BizDevice bizDevice = centerBizDeviceMapper.selectById(changeInfo.getDeviceId());

        if (Objects.isNull(bizDevice)) {
            throw new ResourcePreconditionFailedException("设备ID:" + changeInfo.getDeviceId(), "设备不存在");
        }

        // set id??
        bizDevice.setId(changeInfo.getDeviceId());

        //三维模型编码
        if (StringUtils.isNotBlank(changeInfo.getDeviceCode())){
            bizDevice.setDeviceCode(changeInfo.getDeviceCode());
        }

        if (ObjectUtils.isNotNull(changeInfo.getAreaIds())) {
            //去除空
            List<Long> areaIds = changeInfo.getAreaIds().stream().filter(Objects::nonNull).collect(Collectors.toList());

            List<String> areaNames = getAreaInfo(areaIds);

            bizDevice.setAreaName(String.join("-", areaNames));
            bizDevice.setAreaId(areaIds.stream().map(String::valueOf).collect(Collectors.joining("-")));
            bizDevice.setAreaLastId(areaIds.isEmpty() ? null : areaIds.get(areaIds.size() - 1));
        } else {
            bizDevice.setAreaId(null);
            bizDevice.setAreaName(null);
            bizDevice.setAreaLastId(null);
        }

        if (ObjectUtils.isNotNull(changeInfo.getDepartmentId())) {
            OrganizationDTO organization = organizationClient.getOrganizationsById(changeInfo.getDepartmentId());
            if (ObjectUtils.isNotEmpty(organization)) {
                bizDevice.setDepartmentId(organization.getId());
                bizDevice.setDepartmentName(organization.getName());
            }
        } else {
            bizDevice.setDepartmentId(null);
            bizDevice.setDepartmentName(null);
        }

        if (ObjectUtils.isNotEmpty(changeInfo.getManagerIds())) {
            List<UserDTO> userDTOS = userClient.listUserByIds(changeInfo.getManagerIds());
            if (ObjectUtils.isNotEmpty(userDTOS)) {
                bizDevice.setManagerIds(userDTOS.stream().map(userDTO -> String.valueOf(userDTO.getId())).collect(Collectors.joining("-")));
                bizDevice.setManagerNames(userDTOS.stream().map(userDTO -> String.valueOf(userDTO.getName()))
                                                  .collect(Collectors.joining("-")));
            }
        } else {
            bizDevice.setManagerIds(null);
            bizDevice.setManagerNames(null);
        }
        return bizDevice;
    }

    /**
     * 校验三维模型编码唯一性
     *
     * @param deviceCode 三维模型编码
     * @param deviceId   当前设备ID（排除自身）
     */
    public void checkDeviceCodeUnique(String deviceCode, Long deviceId) {
        if (StringUtils.isBlank(deviceCode)) {
            return;
        }
        if (centerBizDeviceMapper.existDeviceCode(deviceCode, deviceId) > 0) {
            throw new ResourcePreconditionFailedException("三维模型编码已存在:" + deviceCode);
        }
    }

    private List<String> getAreaInfo(List<Long> areaIds) {
        if (Objects.isNull(areaIds) || areaIds.isEmpty()) {
            return Collections.singletonList("");
        }

        Map<Long, String> areaIdNameMapping = areaClient.getByIds(areaIds).stream()
                .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
        return areaIds.stream().map(areaId -> {
            if (areaIdNameMapping.containsKey(areaId)) {
                return areaIdNameMapping.get(areaId);
            }
            return "";
        }).collect(Collectors.toList());
    }

    public DeviceDetailVO getDeviceDetail(Long deviceId) {
        DeviceBaseArchivesInfoDTO baseArchivesInfoDTO = deviceInstanceService.getBaseInfoByDeviceId(deviceId);
        DeviceExpandInfoDTO expandInfoDTO = deviceInstanceService.getExpandInfoByDeviceId(deviceId);
        DeviceProductInfoDTO productInfoDTO = deviceInstanceService.getProductInfoByDeviceId(deviceId);
        Assert.isNotFound(baseArchivesInfoDTO, "设备不存在或已被删除");
        DeviceDetailVO deviceDetailVO = getDeviceDetailVO(deviceId, baseArchivesInfoDTO, expandInfoDTO, productInfoDTO);
        //查询关联子设备
        DeviceVo deviceVo = devDeviceService.deviceDetail(deviceId);
        if (Objects.nonNull(deviceVo) && StringUtils.isNotBlank(deviceVo.getSlaveDeviceIds())) {
            List<Long> slaveIds = StringUtils.split(deviceVo.getSlaveDeviceIds(), ",")
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .toList();
            List<SlaveDeviceVo> slaveDeviceList = new ArrayList<>();
            for (Long slaveId : slaveIds) {
                DeviceBaseArchivesInfoDTO infoByDeviceId = deviceInstanceService.getBaseInfoByDeviceId(slaveId);
                if (ObjectUtils.isNotEmpty(infoByDeviceId)) {
                    SlaveDeviceVo slaveDeviceVo = new SlaveDeviceVo();
                    slaveDeviceVo.setId(infoByDeviceId.getId());
                    slaveDeviceVo.setName(infoByDeviceId.getName());
                    slaveDeviceList.add(slaveDeviceVo);
                }
            }
            deviceDetailVO.setSlaveDeviceList(slaveDeviceList);

        }
        return deviceDetailVO;
    }

    public DeviceDetailVO getDeviceDetailVO(Long deviceId, DeviceBaseArchivesInfoDTO baseArchivesInfoDTO, DeviceExpandInfoDTO expandInfoDTO,
                                            DeviceProductInfoDTO productInfoDTO
                                           ) {
        // 设备管理员处理
        Set<IdToName> manager = new HashSet<>();
        if (CollectionUtils.isNotEmpty(baseArchivesInfoDTO.getManagerIds())) {
            Set<Long> managerIds = baseArchivesInfoDTO.getManagerIds();
            UserQuery userQuery = new UserQuery();
            userQuery.setUserIds(managerIds);
            List<UserDTO> userDTOList = userClient.list(userQuery);
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                userDTOList.forEach(userDTO -> {
                    manager.add(new IdToName(userDTO.getId(), userDTO.getName()));
                });
            }
        }

        DeviceDetailVO deviceDetail = new DeviceDetailVO();
        deviceDetail.setExpandInfo(expandInfoDTO);
        deviceDetail.setProductInfo(productInfoDTO);

        DeviceBaseArchivesInfoVO deviceBaseArchivesInfoVO = ObjectUtils.copying(baseArchivesInfoDTO, DeviceBaseArchivesInfoVO.class);
        deviceBaseArchivesInfoVO.setManager(manager);

        // 部门为空处理
        if (Objects.nonNull(baseArchivesInfoDTO.getDepartment())) {
            deviceBaseArchivesInfoVO.setDepartment(new IdToName(baseArchivesInfoDTO.getDepartment().getKey(), baseArchivesInfoDTO
                    .getDepartment().getVal()));
        }

        if (Objects.nonNull(baseArchivesInfoDTO.getArea()) && Objects.nonNull(baseArchivesInfoDTO.getArea().getKey())) {
            BizDevice bizDevice = centerBizDeviceMapper.selectById(deviceId);
            String areaNames = areaClient.getAreaNamesById(baseArchivesInfoDTO.getArea().getKey());
            if (StringUtils.isNotBlank(areaNames)) {
                areaNames = areaNames.replaceAll("/", "-");
            }
            deviceBaseArchivesInfoVO.setArea(new AreaBaseInfoDTO(
                    baseArchivesInfoDTO.getArea().getKey(),
                    areaNames,
                    bizDevice.getAreaId(),
                    areaNames
            ));
        }
        deviceDetail.setBaseArchivesInfo(deviceBaseArchivesInfoVO);
        return deviceDetail;
    }

    public DeviceDetailVO getDeviceDetail(String deviceCode) {
        DeviceBaseArchivesInfoDTO baseArchivesInfoDTO = deviceInstanceService.getBaseInfoByDeviceCode(deviceCode);
        Assert.isNotFound(baseArchivesInfoDTO, "设备不存在或已被删除");
        DeviceExpandInfoDTO expandInfoDTO = deviceInstanceService.getExpandInfoByDeviceId(baseArchivesInfoDTO.getId());
        DeviceProductInfoDTO productInfoDTO = deviceInstanceService.getProductInfoByDeviceId(baseArchivesInfoDTO.getId());
        return getDeviceDetailVO(baseArchivesInfoDTO.getId(), baseArchivesInfoDTO, expandInfoDTO, productInfoDTO);
    }

    public void updateThirdPartyId(RemoteThirdPartyIdBind remoteThirdPartyIdBind) {
        String dbThirdPartyId = centerBizDeviceMapper.queryThirdPartyIdByDeviceId(remoteThirdPartyIdBind.getDeviceId());

        if (StringUtils.isBlank(dbThirdPartyId) || (!dbThirdPartyId.equals(remoteThirdPartyIdBind.getThirdPartyId()))) {
            centerBizDeviceMapper.updateThirdPartyId(remoteThirdPartyIdBind.getDeviceId(), remoteThirdPartyIdBind.getThirdPartyId());
            Long productId = centerBizDeviceMapper.queryProductIdByDeviceId(remoteThirdPartyIdBind.getDeviceId());

            //通知设备变更
            DeviceChange deviceChange = new DeviceChange();
            deviceChange.setTs(System.currentTimeMillis());
            deviceChange.setDeviceChangePairs(new ArrayList<>(1));
            deviceChange.getDeviceChangePairs()
                    .add(Pair.of(DeviceInfo.of(remoteThirdPartyIdBind.getDeviceId(), productId), DeviceChangeEnum.UPDATE));

            RocketMQContextHolder.sendAfterCommit(DEVICE_CHANGE_TOPIC, deviceChange);
        }
    }

    public void iotProductChange(ProductChangeEvent productChangeEvent) {
        RocketMQContextHolder.send(PRODUCT_CHANGE_TOPIC, productChangeEvent);
    }

    public List<DeviceExpandVO> selectDeviceExpand(List<Long> deviceIds) {
        return deviceExpandMapper.selectDeviceExpand(deviceIds);
    }

    public List<DeviceListDto> queryDeviceDetailsNewList(Set<Long> deviceIds) {
        return centerBizDeviceMapper.queryDeviceDetailsNewList(DeviceListQueryParam.builder().filterDeviceIds(deviceIds).build());
    }

    public void changeDeviceName(DeviceCodeNameChange codeNameChange) {
        BizDevice bizDevice = centerBizDeviceMapper.getDeviceInfoNoTenant(codeNameChange.getDeviceId());
        if (ObjectUtils.isNull(bizDevice)) {
            log.error("物联中心不能更新设备,设备不存在，设备id:{}", codeNameChange.getDeviceId());
            return;
        }
        SessionContextHolder.requestTenant(bizDevice.getTenant());
        // 剥离SN与三维模型编码
        bizDevice.setDeviceName(codeNameChange.getName());
        centerBizDeviceMapper.updateById(bizDevice);

        Long productId = centerBizDeviceMapper.queryProductIdByDeviceId(bizDevice.getId());
        //通知设备变更
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setTs(System.currentTimeMillis());
        deviceChange.setDeviceChangePairs(new ArrayList<>(1));
        deviceChange.getDeviceChangePairs()
                .add(Pair.of(DeviceInfo.of(bizDevice.getId(), productId), DeviceChangeEnum.UPDATE));
        RocketMQContextHolder.sendAfterCommit(DEVICE_CHANGE_TOPIC, deviceChange);
    }

    public Set<Long> deviceIdsByTagCode(String tagCode) {
        return this.centerBizDeviceMapper.getDeviceIdsByTagCode(tagCode);
    }

    public Set<Long> deviceIdsByRuleCondition(DeviceCustomerPageParam param) {
        QueryWrapper<BizDevice> bizDeviceQueryWrapper = new DeviceCustomerFieldWrapper().buildCustomerFieldWrapper(null, param.getRuleGroup());
        Page<BizDevice> bizDevicePage = this.centerBizDeviceMapper.selectPage(PagingUtils.convert(param), bizDeviceQueryWrapper);
        return bizDevicePage.getRecords().stream().map(BizDevice::getId).collect(Collectors.toSet());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveAndAddTagRef(String tagCode,
                                        Set<Long> removeDeviceIds,
                                        Set<Long> newDeviceIds
                                       ) {
        BizTag tag = centerBizTagMapper.selectOne(Wrappers.<BizTag>lambdaQuery()
                                                          .eq(BizTag::getTagCode, tagCode)
                                                 );
        if (tag == null) {
            throw new IllegalArgumentException("tagCode not exist");
        }
        if (!CollectionUtils.isEmpty(removeDeviceIds)) {
            for (List<Long> subRemoveIds : ListUtils.partition(removeDeviceIds.stream().toList(), 500)) {
                centerBizTagRefMapper.physicalDeleteByTagDeviceIds(tag.getId(), new HashSet<>(subRemoveIds));
            }
        }
        if (!CollectionUtils.isEmpty(newDeviceIds)) {
            List<BizTagRef> bizTagRefs = newDeviceIds.stream().map(deviceId -> {
                BizTagRef bizTagRef = new BizTagRef();
                bizTagRef.setId(IdWorker.getId());
                bizTagRef.setTagId(tag.getId());
                bizTagRef.setBusinessKey(deviceId);
                EntityAuditor.populateInnerAudit(bizTagRef);
                return bizTagRef;
            }).collect(Collectors.toList());
            for (List<BizTagRef> subBizTagRef : ListUtils.partition(bizTagRefs, 500)) {
                this.centerBizTagRefMapper.insertBatch(subBizTagRef);
            }
        }
    }

    /**
     * 根据设备ID换取设备三维模型编码
     */
    public List<DeviceIdAndCode> queryDeviceCodeByIds(Set<Long> deviceIds) {
        return centerBizDeviceMapper.queryDeviceCodeByIds(deviceIds);
    }
}

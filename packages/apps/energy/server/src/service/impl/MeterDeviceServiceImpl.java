package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.EnergyDevicePropParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.dto.InstanceListByGenreDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterAlarmService;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterReadeRecordMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterReadeRecord;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.AttributeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.QueryTimeUnitEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.job.EnergyJob;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceUpdateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceStatisticsParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterDeviceStatisticsQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.DeviceAttribute;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataTimeFlagVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.KeyValueWithTsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceDetailVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceExcelVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceVOV1;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeInstanceVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeRecordVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TimeDataVo;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.DeviceExportAppParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATETIME_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

/**
 * 描述：仪表管理服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterDeviceServiceImpl extends ServiceImpl<MeterDeviceMapper, MeterDevice> implements MeterDeviceService {

    private static final String ENERGY_APPLICATION_CODE = "ENERGY_CONSUMPTION";

    private static final int COMMON_SECURITY_DEVICE_BATCH_SIZE = 500;

    private final EnergyProjectItemMapper energyProjectItemMapper;

    private final InfluxDBClient influxDBClient;

    private final AdapterDeviceService adapterService;

    private final MeterDeviceMapper meterDeviceMapper;

    private final MeterReadeRecordMapper meterReadeRecordMapper;

    private final AdapterAlarmService adapterAlarmService;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final ApplicationClient applicationClient;

    private final DeviceCenterTagStdClient deviceCenterTagClient;

    private final FileClient fileClient;

    private final SecurityDeviceClient securityDeviceClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MeterDeviceParam param) {
        MeterDevice meterDevice = ObjectUtils.copying(param, MeterDevice.class);
        this.baseMapper.insert(meterDevice);
        syncAddSecurityDevices(Collections.singletonList(meterDevice.getDeviceId()));
        return meterDevice.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, MeterDeviceParam param) {
        MeterDevice meterDevice = this.baseMapper.selectById(id);
        Assert.isNotFound(meterDevice, "未查询到相关信息");
        ObjectUtils.copying(param, meterDevice);
        this.baseMapper.updateById(meterDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long deviceId) {
        this.baseMapper.deleteByDeviceId(String.valueOf(deviceId));
        //删除关联关系
        energyProjectItemMapper.delete(new LambdaQueryWrapper<EnergyProjectItem>().eq(EnergyProjectItem::getDeviceId, deviceId));
        Set deviceIds = new HashSet<>();
        deviceIds.add(deviceId);
        batchDeleteEnergyItem(deviceIds);
        syncDeleteSecurityDevices(Collections.singletonList(deviceId));
    }

    @Override
    public void deleteAndCheck(Long deviceId) {
        //检查设备是否已经被分项关联
        List<EnergyProjectItem> projectReferDevice = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                                .eq(EnergyProjectItem::getDeviceId, deviceId));
        Assert.isNotAcceptable(ListUtils.isNotEmpty(projectReferDevice), "该设备已被关联，请在分项或分区中删除该设备");
        //删除设备
        this.baseMapper.deleteByDeviceId(String.valueOf(deviceId));
        syncDeleteSecurityDevices(Collections.singletonList(deviceId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> deviceIds) {
        deviceIds.forEach(e -> {
            //检查设备是否已经被分项关联
            List<EnergyProjectItem> projectReferDevice = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                                    .eq(EnergyProjectItem::getDeviceId, e));
            boolean check = ListUtils.isEmpty(projectReferDevice);
            if (!check) {
                MeterDevice meterDevice = baseMapper.selectOne(Wrappers.<MeterDevice>lambdaQuery().eq(MeterDevice::getDeviceId, e)
                                                                       .last(" limit 1 "));
                String deviceName = meterDevice == null ? "" : meterDevice.getDeviceName();
                throw new ServiceException("《" + deviceName + "》已被关联，请在分项或分区中删除该设备");
            }

            this.baseMapper.deleteByDeviceId(e);
            //删除关联关系
            energyProjectItemMapper.delete(new LambdaQueryWrapper<EnergyProjectItem>().eq(EnergyProjectItem::getDeviceId, e));
        });
        Set<Long> collect = deviceIds.stream().map(Long::valueOf).collect(Collectors.toSet());
        batchDeleteEnergyItem(collect);
        syncDeleteSecurityDevices(new ArrayList<>(collect));
    }

    private void batchDeleteEnergyItem(Set<Long> deviceIds) {
        //tag处理
        ApplicationDTO application = applicationClient.getApplicationByCode("ENERGY_CONSUMPTION");
        String tag = "builtin.app::" + application.getCode();
        TagBindDeviceCmd tagBindDeviceParam = new TagBindDeviceCmd();
        tagBindDeviceParam.setBusinessKeys(deviceIds);
        tagBindDeviceParam.setTagCode(tag);
        deviceCenterTagClient.unbindTag(tagBindDeviceParam);
    }

    @Override
    public MeterDeviceVo getById(String id) {
        MeterDevice one = this.getOne(Wrappers.<MeterDevice>lambdaQuery()
                                              .eq(MeterDevice::getId, id));
        MeterDeviceVo meterDeviceVO = ObjectUtils.copying(one, MeterDeviceVo.class);
        meterDeviceVO.setInstrumentName(InstrumentTypeEnum.getFireinspEnum(meterDeviceVO.getInstrumentType()));
        if (meterDeviceVO.getInstrumentType().equals(InstrumentTypeEnum.ENERGY_METER.getCode())) {
            List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(one.getDeviceId(), AttributeTypeEnum.getByDesc(one.getInstrumentType()));
            List<KeyValueWithTsVo> keyValueWithTsVos = ObjectUtils.copying(keyValueWithTsDTOS, KeyValueWithTsVo.class);
            Map<String, String> keyName = adapterService.devicePropName(one.getDeviceId());
            for (KeyValueWithTsVo keyValueWithTsVo : keyValueWithTsVos) {
                keyValueWithTsVo.setKeyName(keyName.get(keyValueWithTsVo.getKey()));
            }
            meterDeviceVO.setIotKeyValues(keyValueWithTsVos);
        } else {
            String energyProCode = EnergyJob.getProperties(meterDeviceVO.getInstrumentType());
            BigDecimal readeValue = new BigDecimal("0.00");
            List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(one.getDeviceId(), AttributeTypeEnum.getByDesc(one.getInstrumentType()));
            if (!ListUtils.isEmpty(keyValueWithTsDTOS)) {
                KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                        .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                if (Objects.isNull(keyValueWithTsDTO)) {
                    keyValueWithTsDTO.setValue("0.00");
                }
                readeValue = readeValue.add(new BigDecimal(
                        StringUtils.isEmpty(keyValueWithTsDTO.getValue().toString()) ? "0.00" : keyValueWithTsDTO.getValue().toString()));
            }
            meterDeviceVO.setReadeValue(readeValue);
        }

        return meterDeviceVO;
    }

    @Override
    public MeterDeviceDetailVO getDetailById(String id) {
        //通过ID 查询设备信息
        MeterDevice meterDevice = this.baseMapper.selectById(id);
        Assert.isNotFound(meterDevice, "未找到对应的设备");
        DeviceDetailDTO deviceDetailDTO = deviceCenterStdClient.getDeviceById(Long.valueOf(meterDevice.getDeviceId()));
        MeterDeviceDetailVO deviceDetailVO = MeterDeviceDetailVO.builder()
                .id(meterDevice.getId())
                .deviceId(Long.valueOf(meterDevice.getDeviceId()))
                .deviceName(meterDevice.getDeviceName())
                .deviceCode(meterDevice.getCode())
                .deviceAreaId(meterDevice.getDeviceAreaId())
                .deviceArea(meterDevice.getDeviceArea())
                .initValue(meterDevice.getInitValue())
                .readeValue(meterDevice.getReadeValue())
                .build();
        //设置扩展属性
        if (deviceDetailDTO != null && deviceDetailDTO.getProductInfo() != null) {
            deviceDetailVO.setBrandName(deviceDetailDTO.getProductInfo().getBrandName());
            deviceDetailVO.setModelName(deviceDetailDTO.getProductInfo().getModelName());
            deviceDetailVO.setDeviceStatus(deviceDetailDTO.getBaseArchivesInfo().getStatus().isOnlineState());
            Long pictureId = deviceDetailDTO.getProductInfo().getPicture();
            if (pictureId != null) {
                FileVO fileVO = fileClient.getById(pictureId);
                deviceDetailVO.setFileVOList(Arrays.asList(fileVO));
            }
        }

        return deviceDetailVO;
    }

    @Override
    public MeterDeviceVo getAppById(Long id, Period period) {

        return null;
    }

    @Override
    public Pagination<MeterDeviceVo> getPage(MeterDeviceQueryParam param) {
        //请自行添加查询条件
        Page<MeterDevice> page = this.getLambdaQuery(param).page(PagingUtils.convert(param));
        Pagination<MeterDeviceVo> deviceVOPagination = PagingUtils.convert(page, MeterDeviceVo.class);
        List<DeviceBaseArchivesInfoDTO> listByDeviceId = new ArrayList<>();
        if (ListUtils.nonEmpty(deviceVOPagination.getRecords())) {
            Set<Long> collect = deviceVOPagination.getRecords().stream().map(s -> Long.valueOf(s.getDeviceId()))
                    .collect(Collectors.toSet());
            var deviceParam = new DevicePostByIdSetParam();
            deviceParam.setDeviceIdSet(collect);
            listByDeviceId = deviceCenterStdClient.postListByDeviceId(deviceParam);
        }
        Map<Long, DeviceBaseArchivesInfoDTO> collect = listByDeviceId.stream()
                .collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, s -> s));
        //设置设备的在线状态
        deviceVOPagination.getRecords().forEach(meterDeviceVo -> {
            meterDeviceVo.setDeviceAreaName(meterDeviceVo.getDeviceArea());
            meterDeviceVo.setInstrumentName(InstrumentTypeEnum.getFireinspEnum(meterDeviceVo.getInstrumentType()));
            DeviceBaseArchivesInfoDTO deviceBaseArchivesInfoDTO = collect.get(Long.valueOf(meterDeviceVo.getDeviceId()));
            if (Objects.nonNull(deviceBaseArchivesInfoDTO)) {
                meterDeviceVo.setDeviceStatus(deviceBaseArchivesInfoDTO.getStatus().isOnlineState());
            }
        });
        return deviceVOPagination;
    }

    @Override
    public List<KeyValueDTO> getDropdownList(MeterDeviceQueryParam param) {
        List<MeterDevice> list = this.getLambdaQuery(param).list();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(po -> KeyValueDTO.builder().key(Long.parseLong(po.getDeviceId())).value(po.getDeviceName()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MeterDeviceExcelVO> getExcelList(MeterDeviceQueryParam param) {
        List<MeterDevice> list = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), MeterDevice::getCode, param.getKeyword())
                .or()
                .like(StringUtils.isNotEmpty(param.getKeyword()), MeterDevice::getDeviceName, param.getKeyword())
                .eq(StringUtils.isNotEmpty(param.getInstrumentType()), MeterDevice::getInstrumentType, param.getInstrumentType())
                .eq(StringUtils.isNotEmpty(param.getDeviceArea()), MeterDevice::getDeviceArea, param.getDeviceArea())
                .list();

        List<MeterDeviceVo> meterDeviceVos = ObjectUtils.copying(list, MeterDeviceVo.class);
        meterDeviceVos.forEach(meterDeviceExcelVO -> {
            meterDeviceExcelVO.setInstrumentName(InstrumentTypeEnum.getFireinspEnum(meterDeviceExcelVO.getInstrumentType()));
        });
        List<MeterDeviceExcelVO> meterDeviceExcelVOList = ObjectUtils.copying(meterDeviceVos, MeterDeviceExcelVO.class);
        return meterDeviceExcelVOList;
    }

    @Override
    public List<MeterDeviceVo> getEnergyMeters() {
        List<MeterDevice> meterDevices = this.list(Wrappers.<MeterDevice>lambdaQuery()
                                                           .eq(MeterDevice::getDeviceTypeId, "a74924cb6ec1496e9c476d01891dd5a1"));

        return ObjectUtils.copying(meterDevices, MeterDeviceVo.class);
    }

    @Override
    public void addBatch(List<MeterDeviceParam> param) {
        List<MeterDevice> list = this.list();
        List<String> collect = list.stream().map(MeterDevice::getDeviceId).collect(Collectors.toList());
        List<MeterDeviceParam> objects = new ArrayList<>();
        //去重
        for (MeterDeviceParam meterDeviceParam : param) {
            if (!collect.contains(meterDeviceParam.getDeviceId())) {
                objects.add(meterDeviceParam);
            }
        }

        if (!ListUtils.isEmpty(objects)) {
            //根据租户id查询租户下所有的关联设备信息
            List<String> deviceIdByTenantId = this.meterDeviceMapper.selectList(null)
                    .stream().map(MeterDevice::getDeviceId).collect(Collectors.toList());
            //删除租户下所有的仪表信息
            if (ListUtils.isEmpty(deviceIdByTenantId)) {
                //新增所有的仪表以及删除废弃仪表的关联关系
                deviceIdByTenantId = objects.stream().map(MeterDeviceParam::getDeviceId).collect(Collectors.toList());
                this.addAllMaterAndDeleteDiscard(objects, deviceIdByTenantId);
                return;
            }

            // 忽略重复导入的设备id
            deviceIdByTenantId = ignoreRepeated(deviceIdByTenantId, objects.stream().map(MeterDeviceParam::getDeviceId)
                    .collect(Collectors.toList()));

            if (!ListUtils.isEmpty(deviceIdByTenantId)) {

                //新增所有的仪表以及删除废弃仪表的关联关系
                this.addAllMaterAndDeleteDiscard(objects, deviceIdByTenantId);
            }

        }
    }

    /**
     * 到处设备 支持重复导入
     *
     * @param deviceList
     */
    @Override
    @Transactional
    @Synchronized
    public void addBatch2(List<MeterDeviceParam> deviceList) {
        //查询当前的设备
        List<MeterDevice> meterDevices = this.baseMapper.selectList(null);
        //映射当前的设备 主要是方便判断设备是否存在
        Map<String, MeterDevice> deviceExistMap = meterDevices.stream().collect(Collectors.toMap(MeterDevice::getDeviceId, n -> n));
        List<MeterDevice> saveOrUpdateList = new ArrayList<>();
        if (!ListUtils.isEmpty(deviceList)) {
            LocalDateTime newDate = LocalDateTime.now();
            deviceList.forEach(d -> {
                MeterDevice materDevice;
                if (deviceExistMap.get(d.getDeviceId()) != null) {
                    //存在 更新基础信息
                    materDevice = deviceExistMap.get(d.getDeviceId());
                    materDevice.setDeviceAreaId(d.getDeviceAreaId());
                    materDevice.setDeviceArea(d.getDeviceArea());
                    materDevice.setProductBrandName(d.getProductBrandName());
                    materDevice.setProductId(d.getProductId());
                    materDevice.setProductName(d.getProductName());
                    materDevice.setProductCategoryPathCode(d.getProductCategoryPathCode());
                    materDevice.setProductCategoryName(d.getProductCategoryName());
                    materDevice.setUpdateTime(newDate);
                } else {
                    //不存在 就插入
                    materDevice = ObjectUtils.copying(d, MeterDevice.class);
                    materDevice.setId(IdWorker.getId());
                    materDevice.setCreateTime(newDate);
                }
                // 最后一级区域id
                if (StringUtils.isNotEmpty(materDevice.getDeviceAreaId())) {
                    String[] areaIds = materDevice.getDeviceAreaId().split("-");
                    int length = areaIds.length;
                    if (length > 0) {
                        String areaId = areaIds[length - 1];
                        if (StringUtils.isNotEmpty(areaId)) {
                            materDevice.setLastAreaId(Long.valueOf(areaId));
                        }
                    }
                }
                // 最后一级区域名称
                if (StringUtils.isNotEmpty(materDevice.getDeviceArea())) {
                    String[] areaNames = materDevice.getDeviceArea().split("-");
                    int length = areaNames.length;
                    if (length > 0) {
                        materDevice.setLastAreaName(areaNames[length - 1]);
                    }
                }
                saveOrUpdateList.add(materDevice);
            });
        }
        if (!saveOrUpdateList.isEmpty()) {
            this.saveOrUpdateBatch(saveOrUpdateList, 800);
            syncAddSecurityDevices(saveOrUpdateList.stream().map(MeterDevice::getDeviceId).collect(Collectors.toList()));
        }
    }

    private void syncAddSecurityDevices(List<String> deviceIds) {
        List<Long> longDeviceIds = convertDeviceIds(deviceIds);
        if (longDeviceIds.isEmpty()) {
            return;
        }
        try {
            DeviceExportAppParam param = new DeviceExportAppParam();
            param.setApplicationCode(ENERGY_APPLICATION_CODE);
            param.setMatchedDeviceIds(longDeviceIds);
            securityDeviceClient.addSecurityDevice(param);
        } catch (Exception e) {
            log.warn("同步通用模块新增能耗仪表失败, deviceIds={}", longDeviceIds, e);
        }
    }

    private void syncDeleteSecurityDevices(List<Long> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) {
            return;
        }
        try {
            List<Long> securityDeviceIds = getSecurityDeviceIds(deviceIds);
            if (securityDeviceIds.isEmpty()) {
                return;
            }
            securityDeviceClient.deleteSecurityDeviceBatch(securityDeviceIds);
        } catch (Exception e) {
            log.warn("同步通用模块删除能耗仪表失败, deviceIds={}", deviceIds, e);
        }
    }

    private List<Long> getSecurityDeviceIds(List<Long> deviceIds) {
        List<Long> distinctDeviceIds = deviceIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> securityDeviceIds = new ArrayList<>();
        for (int i = 0; i < distinctDeviceIds.size(); i += COMMON_SECURITY_DEVICE_BATCH_SIZE) {
            List<Long> batchDeviceIds = distinctDeviceIds.subList(i, Math.min(
                    i + COMMON_SECURITY_DEVICE_BATCH_SIZE, distinctDeviceIds.size()));
            Pagination<SecurityDeviceVo> page = securityDeviceClient
                    .getSecurityDevicePage(ENERGY_APPLICATION_CODE, 1, batchDeviceIds.size(), null, batchDeviceIds);
            if (page != null && page.getRecords() != null) {
                page.getRecords().stream().map(SecurityDeviceVo::getId).filter(Objects::nonNull).forEach(securityDeviceIds::add);
            }
        }
        return securityDeviceIds;
    }

    private List<Long> convertDeviceIds(List<String> deviceIds) {
        List<Long> longDeviceIds = new ArrayList<>();
        if (deviceIds == null) {
            return longDeviceIds;
        }
        deviceIds.forEach(deviceId -> {
            try {
                if (StringUtils.isNotEmpty(deviceId)) {
                    longDeviceIds.add(Long.valueOf(deviceId));
                }
            } catch (Exception e) {
                log.warn("同步通用模块能耗仪表时设备ID转换失败, deviceId={}", deviceId, e);
            }
        });
        return longDeviceIds.stream().distinct().collect(Collectors.toList());
    }

    public List<String> ignoreRepeated(List<String> deviceIds, List<String> importDeviceIds) {

        importDeviceIds.removeAll(deviceIds);

        return importDeviceIds;
    }

    @Override
    public List<MeterReadeInstanceVO> getByCategory() {
        List<MeterDevice> meterDevices = this.lambdaQuery().list();
        List<MeterDeviceVo> meterDeviceVos = ObjectUtils.copying(meterDevices, MeterDeviceVo.class);

        meterDeviceVos.forEach(meterDeviceVo -> {
            meterDeviceVo.setInstrumentName(InstrumentTypeEnum.getFireinspEnum(meterDeviceVo.getInstrumentType()));
        });

        Map<String, List<MeterDeviceVo>> listMap = meterDeviceVos.stream()
                .filter(meterDeviceVo -> StringUtils.isNotEmpty(meterDeviceVo.getInstrumentName()))
                .collect(Collectors.groupingBy(MeterDeviceVo::getInstrumentName));
        List<MeterReadeInstanceVO> meterReadeInstanceVOS = new ArrayList<>();
        listMap.entrySet().forEach(s -> {
            meterReadeInstanceVOS.add(new MeterReadeInstanceVO(s.getKey(), s.getValue()));
        });

        return meterReadeInstanceVOS;
    }

    /**
     * 根据仪表类型查询仪表
     *
     * @param id
     * @param instrumentType
     *
     * @return
     */
    @Override
    public List<MeterDeviceVo> getByIdAndCategory(String id, String instrumentType) {
        List<MeterDevice> meterDeviceList = this.lambdaQuery()
                .notIn(MeterDevice::getId, id)
                .eq(MeterDevice::getInstrumentType, instrumentType)
                .list();

        return ObjectUtils.copying(meterDeviceList, MeterDeviceVo.class);
    }

    /**
     * 配置上级仪表
     *
     * @param id
     * @param parentId
     */
    @Override
    public void configSuperiorInstrument(String id, String parentId) {
        String parentName = null;
        String pid = null;
        if (StringUtils.isNotEmpty(parentId) && !"undefined".equals(parentId) && !"null".equals(parentId)) {
            MeterDevice meterDevice = this.getOne(Wrappers.<MeterDevice>lambdaQuery().eq(MeterDevice::getDeviceId, parentId)
                                                          .last(" limit 1 "));
            Assert.isNotFound(meterDevice, "未查询到父级设备相关信息");
            parentName = meterDevice.getDeviceName();
            pid = meterDevice.getDeviceId();
        }

        this.lambdaUpdate()
                .set(MeterDevice::getParentId, pid)
                .set(MeterDevice::getParentName, parentName)
                .eq(MeterDevice::getId, Long.valueOf(id))
                .update();
    }

    /**
     * 获取树形结构
     *
     * @return
     */
    @Override
    public List<MeterDeviceVOV1> getTree(String instrumentType) {
        List<MeterDevice> meterDeviceList = this.list(Wrappers.<MeterDevice>lambdaQuery()
                                                              .eq(StringUtils.isNotEmpty(instrumentType), MeterDevice::getInstrumentType, instrumentType)
                                                     );
        List<MeterDeviceVOV1> copying = ObjectUtils.copying(meterDeviceList, MeterDeviceVOV1.class);
        return this.assembleTrees(copying);
    }

    @Override
    public Map<String, Object> readeRecord(String tenant) {
        Map<String, Object> remap = new HashMap<>();
        SessionContextHolder.requestTenant(Integer.valueOf(tenant));
        LocalDateTime end = LocalDateTime.now();
        List<MeterDevice> list = this.list(Wrappers.<MeterDevice>lambdaQuery()
                                                   .eq(BasicEnhancedTenantEntity::getTenant, Integer.valueOf(tenant)));
        System.out.println("抄表执行：=========================================================");
        Integer success = 0;
        Integer fail = 0;
        if (ListUtils.nonEmpty(list)) {
            for (MeterDevice device : list) {
                try {
                    SessionContextHolder.requestTenant(device.getTenant());
                    String deviceProperties = "";
                    if (device.getInstrumentType().equals(InstrumentTypeEnum.ELECTRICITY_METER.getCode())) {
                        deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.ELECTRICITY_METER.name());
                    } else if (device.getInstrumentType().equals(InstrumentTypeEnum.WATER_METER.getCode())) {
                        deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.WATER_METER.name());
                    } else if (device.getInstrumentType().equals(InstrumentTypeEnum.GAS_METER.getCode())) {
                        deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.GAS_METER.name());
                    } else if (device.getInstrumentType().equals(InstrumentTypeEnum.VAPOUR.getCode())) {
                        deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.VAPOUR.name());
                    }
                    String energyProCode = deviceProperties;
                    List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(device.getDeviceId(), AttributeTypeEnum.getByDesc(device.getInstrumentType()));
                    KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                            .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                    if (Objects.isNull(keyValueWithTsDTO)) {
                        keyValueWithTsDTO.setValue("0.00");
                    }
                    //本期读数
                    BigDecimal readeValue = new BigDecimal(StringUtils.isEmpty(keyValueWithTsDTO.getValue().toString()) ? "0.00"
                                                                                                                        : keyValueWithTsDTO
                                                                   .getValue().toString());
                    //上次读数
                    BigDecimal lastDeviceValue = device.getReadeValue();
                    //本次用量=本次设备读数-上次设备读数
                    BigDecimal subtract = readeValue.subtract(device.getReadeValue());
                    //插入抄表记录表
                    MeterReadeRecord build = MeterReadeRecord.builder()
                            .deviceId(device.getDeviceId())
                            .lastReadeTime(device.getNowReadeTime())
                            .nowReadeTime(end)
                            .lastReadeValue(device.getNowReadeValue())
                            .readeLastValue(device.getReadeValue())
                            .nowReadeValue(subtract)
                            .readeValue(readeValue)
                            .build();
                    meterReadeRecordMapper.insert(build);

                    //构建仪表实体
                    device.setLastReadeTime(device.getNowReadeTime());
                    //本期抄表时间
                    device.setNowReadeTime(end);
                    //上次用量
                    device.setLastReadeValue(device.getNowReadeValue());
                    //本次用量
                    device.setNowReadeValue(subtract);
                    //本期读数
                    device.setReadeValue(readeValue);
                    this.updateById(device);
                    //报警推送
                    adapterAlarmService.pushAlarm(device, lastDeviceValue, readeValue);
                    success += 1;
                } catch (Exception e) {
                    fail += 1;
                    e.printStackTrace();
                }

            }

        }

        remap.put("success", success);
        remap.put("fail", fail);
        return remap;
    }

    @Override
    public Page<MeterReadeRecordVo> getReadeList(MeterDeviceQueryParam param) throws ParseException {
        List<String> deviceIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(param.getDeviceId())) {
            String[] split = param.getDeviceId().split("-");
            deviceIds = Arrays.asList(split);
        }
        LocalDateTime begin = null;
        LocalDateTime end = null;
        if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            begin = DateUtils.parse(split[0]);
            end = DateUtils.parse(split[1]);

        }
        param.setBegin(begin);
        param.setEnd(end);
        Page<MeterReadeRecordVo> meterReadeRecordPage = meterReadeRecordMapper.getMeterReadeRecordPage(PagingUtils.convert(param), param, deviceIds);
        return meterReadeRecordPage;
    }

    @Override
    public Page<DataVo> getDeviceHistory(MeterDeviceQueryParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String[] split = param.getPeriod().split(",");
        //获取
        LocalDateTime dateTime = DateUtils.parse(split[0]);
        String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
        LocalDateTime dateTime1 = DateUtils.parse(split[1]);
        String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T23:59:00Z";

        QueryApi queryApi = influxDBClient.getQueryApi();
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceHour\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.deviceId==\"" + param.getDeviceId() + "\")\n" +
                      "  |> sort(columns: [\"_time\"],desc: true)\n" +
                      "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")";
        List<DataVo> query = queryApi.query(flux, DataVo.class);
        String flux1 = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceHour\")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.deviceId==\"" + param.getDeviceId() + "\")\n" +
                       "  |> sort(columns: [\"_time\"],desc: true)";
        List<DataVo> query1 = queryApi.query(flux1, DataVo.class);
        Page<DataVo> page = new Page<>();
        page.setTotal(query1.size());
        page.setRecords(query);
        return page;
    }

    @Override
    public void updateReade(MeterDeviceUpdateParam param) {
        MeterReadeRecord meterReadeRecord = meterReadeRecordMapper.selectById(param.getId());
        Assert.isParameterError(param.getValue().compareTo(meterReadeRecord.getReadeLastValue()) < 0, "修改数小于上次读数");
        BigDecimal subtract = param.getValue().subtract(meterReadeRecord.getReadeLastValue());
        meterReadeRecord.setReadeValue(param.getValue());
        meterReadeRecord.setNowReadeValue(subtract);
        meterReadeRecordMapper.updateById(meterReadeRecord);
    }

    /**
     * 根据仪表分类获取区域信息
     *
     * @param instrumentType
     *
     * @return
     */
    @Override
    public List<MeterDeviceVo> getDeviceAreaArr(String instrumentType) {
        List<MeterDevice> meterDeviceList = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(instrumentType), MeterDevice::getInstrumentType, instrumentType)
                .groupBy(MeterDevice::getDeviceArea).list();
        return ObjectUtils.copying(meterDeviceList, MeterDeviceVo.class);
    }

    /**
     * 获取所有区域信息
     *
     * @return
     */
    @Override
    public List<MeterDeviceVo> getAllDeviceAreaArr() {
        List<MeterDevice> meterDeviceList = this.lambdaQuery()
                .groupBy(MeterDevice::getDeviceArea).list();
        return ObjectUtils.copying(meterDeviceList, MeterDeviceVo.class);
    }

    @Override
    public List<InstanceListByGenreDTO> getDevices() {
        //        List<Long> deviceIds = meterDeviceMapper.getDeviceIds();
        //        List<InstanceListByGenreDTO> instanceListByGenreDTOS = adapterService.getDevices(deviceIds);
        return null;
    }

    @Override
    public List<Long> sieveOut() {
        List<Long> deviceIds = meterDeviceMapper.getDeviceIds();
        return deviceIds;
    }

    @Override
    public Pagination<KeyValueWithTsDTO> getMeterDeviceStatistics(MeterDeviceStatisticsParam param) {
        MeterDeviceStatisticsQueryParam meterDeviceStatisticsQueryParam = statisticsQueryParam(param);
        DevicePropHistoryPageParam devicePropHistoryPageParam = new DevicePropHistoryPageParam();
        devicePropHistoryPageParam.setDeviceId(meterDeviceStatisticsQueryParam.getDeviceId());
        devicePropHistoryPageParam.setStartTs(meterDeviceStatisticsQueryParam.getStartTs());
        devicePropHistoryPageParam.setEndTs(meterDeviceStatisticsQueryParam.getEndTs());
        List<String> propCodes = new ArrayList<>();
        propCodes.add(AttributeTypeEnum.getByDesc(param.getInstrumentType()));
        devicePropHistoryPageParam.setPropCodes(propCodes);
        devicePropHistoryPageParam.setPage(param.getPageNumber());
        devicePropHistoryPageParam.setPageSize(param.getPageSize());
        Pagination<KeyValueWithTsDTO> keyValueWithTsDTOPagination = adapterService.historyPropPage(devicePropHistoryPageParam);
        return keyValueWithTsDTOPagination;
    }

    @Override
    public Page<DataVo> getQuantityMeterDeviceStatistics(MeterDeviceStatisticsParam param) {
        TimeDataVo timeFlagPeriod = EnergyServiceImpl.getTimeFlagPeriod(param.getTimeFlag().getCode(),
                                                                        DateUtils.formatDate(param.getPeriod()
                                                                                                     .getBegin(), DEFAULT_DATETIME_FORMAT) +
                                                                        "," +
                                                                        DateUtils.formatDate(param.getPeriod()
                                                                                                     .getEnd(), DEFAULT_DATETIME_FORMAT));
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timeFlagPeriod.getBegin() + ", stop: " + timeFlagPeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"device" + timeFlagPeriod.getTableFlag() + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + SessionContextHolder.getRequester().getTenant() + "\" and r.deviceId==\"" +
                      param.getDeviceId() + "\")\n" +
                      "  |> sort(columns: [\"_time\"],desc: true)\n" +
                      "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
        List<DataVo> query = influxDBClient.getQueryApi().query(flux, DataVo.class);
        Page<DataVo> page = new Page<>();
        page.setTotal(query.size());
        page.setRecords(query);
        return page;
    }

    @Override
    public List<DataTimeFlagVo> totalUsage(MeterDeviceStatisticsParam param) {
        TimeDataVo timeFlagPeriod = EnergyServiceImpl.getTimeFlagPeriod(param.getTimeFlag().getCode(),
                                                                        DateUtils.formatDate(param.getPeriod()
                                                                                                     .getBegin(), DEFAULT_DATETIME_FORMAT) +
                                                                        "," +
                                                                        DateUtils.formatDate(param.getPeriod()
                                                                                                     .getEnd(), DEFAULT_DATETIME_FORMAT));
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timeFlagPeriod.getBegin() + ", stop: " + timeFlagPeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"device" + timeFlagPeriod.getTableFlag() + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + SessionContextHolder.getRequester().getTenant() + "\" and r.deviceId==\"" +
                      param.getDeviceId() + "\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> group(columns: [\"_time\"])\n" +
                      "  |> sum(column: \"value\")";
        List<DataTimeFlagVo> query = influxDBClient.getQueryApi().query(flux, DataTimeFlagVo.class);
        Map<String, DataTimeFlagVo> collect = query.stream().collect(Collectors.toMap(s -> {
            String timeFlag = "";
            LocalDateTime date = DateUtils.parse(s.getTime().toEpochMilli());
            if (param.getTimeFlag().getCode().equals("TODAY")) {
                timeFlag = DateUtils.format(date, "yyyyMMdd");
            } else if (param.getTimeFlag().getCode().equals("THIS_MONTH")) {
                timeFlag = DateUtils.format(date, "yyyyMM");
            } else if (param.getTimeFlag().getCode().equals("THIS_YEAR")) {
                timeFlag = DateUtils.format(date, "yyyy");
            }
            return timeFlag;
        }, s -> s));
        List<DataTimeFlagVo> objects = new ArrayList<>();
        LocalDateTime st = timeFlagPeriod.getSt();
        for (int i = 0; i < timeFlagPeriod.getTimeDifference(); i++) {
            DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
            BigDecimal value = BigDecimal.ZERO;
            LocalDateTime startTime = null;
            String format = "";
            if (param.getTimeFlag().getCode().equals("TODAY")) {
                format = "yyyyMMdd";
                startTime = DateUtils.offsetDay(st, i);
            } else if (param.getTimeFlag().getCode().equals("THIS_MONTH")) {
                format = "yyyyMM";
                startTime = DateUtils.offsetMonth(st, i);
            } else if (param.getTimeFlag().getCode().equals("THIS_YEAR")) {
                format = "yyyy";
                startTime = DateUtils.offsetMonth(st, i * 12);
            }
            String formatFlag = DateUtils.format(startTime, format);
            DataTimeFlagVo dataTimeFlagVoCollect = collect.get(formatFlag);
            if (Objects.nonNull(dataTimeFlagVoCollect)) {
                value = value.add(dataTimeFlagVoCollect.getValue());
            }
            dataTimeFlagVo.setValue(value);
            dataTimeFlagVo.setTimeFlag(formatFlag);
            objects.add(dataTimeFlagVo);
        }
        return objects;
    }

    @Override
    public DeviceTSDTO energyUsage(MeterDeviceStatisticsParam param) {
        MeterDeviceStatisticsQueryParam meterDeviceStatisticsQueryParam = statisticsQueryParam(param);
        HistorySampleParam historySampleParam = new HistorySampleParam();
        historySampleParam.setDeviceId(meterDeviceStatisticsQueryParam.getDeviceId().toString());
        historySampleParam.setStep(meterDeviceStatisticsQueryParam.getStep());
        historySampleParam.setStartTs(meterDeviceStatisticsQueryParam.getStartTs());
        historySampleParam.setEndTs(meterDeviceStatisticsQueryParam.getEndTs());
        List<String> propCodes = new ArrayList<>();
        propCodes.add(param.getPropCode());
        historySampleParam.setProperties(propCodes);
        historySampleParam.setAggType("avg");
        DeviceTSDTO deviceTSDTO = adapterService.historySampleProp(historySampleParam);
        return deviceTSDTO;
    }

    private MeterDeviceStatisticsQueryParam statisticsQueryParam(MeterDeviceStatisticsParam param) {
        MeterDeviceStatisticsQueryParam meterDeviceStatisticsQueryParam = new MeterDeviceStatisticsQueryParam();
        Long startTs = DateUtils.toTimestamp(param.getPeriod().getBegin());
        Long endTs = DateUtils.toTimestamp(param.getPeriod().getEnd());
        Integer step = 60;

        if (param.getTimeFlag().getCode().equals(QueryTimeUnitEnum.TODAY.getCode())) {
            step = 24 * 60;
        } else if (param.getTimeFlag().getCode().equals(QueryTimeUnitEnum.THIS_MONTH.getCode())) {
            step = 24 * 60 * 30;
        } else if (param.getTimeFlag().getCode().equals(QueryTimeUnitEnum.THIS_YEAR.getCode())) {
            step = 24 * 60 * 30 * 365;
        }
        meterDeviceStatisticsQueryParam.setDeviceId(Long.valueOf(param.getDeviceId()));
        meterDeviceStatisticsQueryParam.setStep(step);
        meterDeviceStatisticsQueryParam.setStartTs(startTs);
        meterDeviceStatisticsQueryParam.setEndTs(endTs);
        return meterDeviceStatisticsQueryParam;
    }

    /**
     * 新增所有的仪表以及删除废弃仪表的关联关系
     *
     * @param params
     * @param deviceIdByTenantId
     */
    public void addAllMaterAndDeleteDiscard(List<MeterDeviceParam> params, List<String> deviceIdByTenantId) {
        LocalDateTime end = LocalDateTime.now();
        List<String> deviceIds = new ArrayList<>();
        List<MeterDevice> energyProjectItemList = new ArrayList<>();
        deviceIds.addAll(deviceIdByTenantId);
        params = params.stream().filter(param -> deviceIdByTenantId.contains(param.getDeviceId())).collect(Collectors.toList());
        params.forEach(e -> {
            e.setNowReadeTime(end);
            String deviceProperties = "";
            if (e.getInstrumentType().equals(InstrumentTypeEnum.ELECTRICITY_METER.getCode())) {
                deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.ELECTRICITY_METER.name());
            } else if (e.getInstrumentType().equals(InstrumentTypeEnum.WATER_METER.getCode())) {
                deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.WATER_METER.name());
            } else if (e.getInstrumentType().equals(InstrumentTypeEnum.GAS_METER.getCode())) {
                deviceProperties = AttributeTypeEnum.getByName(InstrumentTypeEnum.GAS_METER.name());
            }
            String energyProCode = deviceProperties;
            try {
                List<KeyValueWithTsDTO> keyValueWithTsDTOS = DeviceAttribute.deviceProp(e.getDeviceId(), AttributeTypeEnum.getByDesc(e.getInstrumentType()));
                KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                        .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                if (Objects.isNull(keyValueWithTsDTO)) {
                    KeyValueWithTsDTO keyValueWithTsDTO1 = new KeyValueWithTsDTO();
                    keyValueWithTsDTO1.setValue("0.00");
                    keyValueWithTsDTO = keyValueWithTsDTO1;
                }
                e.setNowReadeTime(end);
                e.setReadeValue(new BigDecimal(
                        Objects.isNull(keyValueWithTsDTO.getValue()) ? "0.00" : keyValueWithTsDTO.getValue().toString()));
                e.setNowReadeValue(new BigDecimal("0.00"));
                e.setLastReadeValue(new BigDecimal("0.00"));

                //插入抄表记录表
                MeterReadeRecord build = MeterReadeRecord.builder()
                        .deviceId(e.getDeviceId())
                        .nowReadeTime(end)
                        .lastReadeValue(new BigDecimal("0.00"))
                        .readeLastValue(new BigDecimal("0.00"))
                        .nowReadeValue(new BigDecimal("0.00"))
                        .readeValue(new BigDecimal(
                                Objects.isNull(keyValueWithTsDTO.getValue()) ? "0.00" : keyValueWithTsDTO.getValue().toString()))
                        .build();
                meterReadeRecordMapper.insert(build);

                energyProjectItemList.add(ObjectUtils.copying(e, MeterDevice.class));

            } catch (Exception exception) {
                log.info("表操作===={}", exception);
            }
        });

        this.saveBatch(energyProjectItemList);
        //tag处理
        ApplicationDTO application = applicationClient.getApplicationByCode("ENERGY_CONSUMPTION");
        String tag = "builtin.app::" + application.getCode();
        Integer integer = deviceCenterTagClient.checkTag(tag);
        if (integer.equals(0)) {
            CreateTagCmd createTagCmd = new CreateTagCmd();
            createTagCmd.setTagCode(tag);
            createTagCmd.setTagName(application.getName());
            deviceCenterTagClient.createTag(createTagCmd);
        }
        TagBindDeviceCmd tagBindDeviceParam = new TagBindDeviceCmd();
        Set<Long> collect = energyProjectItemList.stream().map(s -> Long.valueOf(s.getDeviceId())).collect(Collectors.toSet());
        tagBindDeviceParam.setBusinessKeys(collect);
        tagBindDeviceParam.setTagCode(tag);
        deviceCenterTagClient.bindTag(tagBindDeviceParam);
        syncAddSecurityDevices(energyProjectItemList.stream().map(MeterDevice::getDeviceId).collect(Collectors.toList()));
    }

    /**
     * 组装树结构
     *
     * @param trees 源数据
     *
     * @return 树形结构
     */
    public List<MeterDeviceVOV1> assembleTrees(List<MeterDeviceVOV1> trees) {
        if (ListUtils.isEmpty(trees)) {
            return Collections.emptyList();
        }

        return trees.stream().filter(m -> com.guhecloud.platform.base.util.StringUtils.isBlank(m.getParentId())).peek(
                        m -> m.setChildren(getChildrenLoop(m, trees))
                                                                                                                     )
                .collect(Collectors.toList());
    }

    @Override
    public MeterDeviceVo getLatestDeviceDataFromIot(Long id) {
        MeterDevice device = this.getById(id);
        if (device != null) {
            //获取设备要取的属性
            String propCode = AttributeTypeEnum.getByDesc(device.getInstrumentType());
            //获取设备当前值
            KeyValueWithTsDTO devicePropInfo = this.getDeviceRealValueFromIOT(device.getDeviceId(), propCode);
            //更新设备信息
            if (devicePropInfo != null) {
                LocalDateTime latestTime = DateUtils.parse(devicePropInfo.getTs());
                device.setNowReadeTime(latestTime);
                device.setNowReadeValue(new BigDecimal(devicePropInfo.getValue().toString()));
                device.setReadeValue(new BigDecimal(devicePropInfo.getValue().toString()));
                this.updateById(device);
            }
        }
        return ObjectUtils.copying(device, MeterDeviceVo.class);
    }

    /**
     * 获取非子级的仪表列表(用于添加下级)
     *
     * @param deviceId 仪表id
     *
     * @return 仪表列表
     */
    @Override
    public List<MeterDevice> getNotChildrenDevice(Long deviceId, String instrumentType) {
        if (deviceId == null) {
            return Collections.emptyList();
        }
        List<Long> childrenIdList = new ArrayList<>();
        childrenIdList.add(deviceId);
        List<Long> queryChildrenList = meterDeviceMapper.getChildrenDevice(deviceId);
        if (queryChildrenList != null) {
            childrenIdList.addAll(queryChildrenList);
        }
        return meterDeviceMapper.selectList(Wrappers.<MeterDevice>lambdaQuery()
                                                    .select(MeterDevice::getId, MeterDevice::getDeviceId, MeterDevice::getDeviceName)
                                                    .eq(StringUtils.isNotEmpty(instrumentType), MeterDevice::getInstrumentType, instrumentType)
                                                    .notIn(MeterDevice::getId, childrenIdList)
                                           );
    }

    private KeyValueWithTsDTO getDeviceRealValueFromIOT(String deviceId, String propCode) {
        //通过北向接口 获取物联网数据
        EnergyDevicePropParam energyDevicePropParam = new EnergyDevicePropParam(10);
        energyDevicePropParam.setPropCodes(Arrays.asList(propCode));
        energyDevicePropParam.setDeviceId(Long.valueOf(deviceId));
        List<KeyValueWithTsDTO> keyValueWithTsDTOS = adapterService.deviceProp(energyDevicePropParam);
        if (!ListUtils.isEmpty(keyValueWithTsDTOS)) {
            keyValueWithTsDTOS.sort(Comparator.comparing(KeyValueWithTsDTO::getTs).reversed());
            return keyValueWithTsDTOS.stream().filter(attributeRunningDTO -> propCode.equals(attributeRunningDTO.getKey())).findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     *
     * @return 根节点信息
     */
    private List<MeterDeviceVOV1> getChildrenLoop(MeterDeviceVOV1 root, List<MeterDeviceVOV1> all) {
        return all.stream().filter(m -> Objects.equals(m.getParentId(), String.valueOf(root.getDeviceId()))).peek(
                        m -> m.setChildren(getChildrenLoop(m, all))
                                                                                                                 )
                .collect(Collectors.toList());
    }

    private LambdaQueryChainWrapper<MeterDevice> getLambdaQuery(MeterDeviceQueryParam param) {
        return this.lambdaQuery().and(StringUtils.isNotEmpty(param.getKeyword()), q ->
                        q.like(MeterDevice::getCode, param.getKeyword())
                                .or()
                                .like(MeterDevice::getDeviceName, param.getKeyword()))
                .like(StringUtils.isNotBlank(param.getDeviceName()), MeterDevice::getDeviceName, param.getDeviceName())
                .eq(StringUtils.isNotEmpty(param.getInstrumentType()), MeterDevice::getInstrumentType, param.getInstrumentType())
                .like(StringUtils.isNotEmpty(param.getDeviceAreaId()), MeterDevice::getDeviceAreaId, param.getDeviceAreaId())
                .eq(StringUtils.isNotEmpty(param.getDeviceArea()), MeterDevice::getDeviceArea, param.getDeviceArea());
    }
}

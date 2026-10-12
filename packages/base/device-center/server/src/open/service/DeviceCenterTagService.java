package com.guhecloud.cube.base.device.center.open.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagTypeMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagType;
import com.guhecloud.cube.base.device.center.core.vo.TagCountInfo;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseAndIotPropertyInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.ProductCategoryInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.AttributePermissionEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.TagPercentageDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceAreaSeqVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceSelfInfoVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.ProductCategoryOffLineCountVO;
import com.guhecloud.cube.base.device.center.open.mapper.ProductInfoMapper;
import com.guhecloud.cube.base.device.center.open.vo.DeviceBaseInfoAndNumberVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceBaseInfoSafetyReportVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceFireSafetyReportVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceTags;
import com.guhecloud.cube.base.device.center.open.vo.ProductDeviceCountVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.util.DevicePropUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterTagService {
    private final CenterBizTagTypeMapper tagTypeMapper;
    private final CenterBizTagMapper tagMapper;
    private final CenterBizTagRefMapper tagRefMapper;
    private final CenterBizDeviceMapper deviceMapper;
    private final ProductInfoMapper productInfoMapper;
    private final AreaClient areaClient;
    private final IotDeviceClient iotDeviceClient;

    public Pagination<DeviceSelfInfoVO> queryDeviceSelfInfoPage(String tagCode, QueryDeviceSelfInfoParamPage queryDeviceSelfInfoParamPage) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return Pagination.empty();
        }
        Page<DeviceSelfInfoVO> page = tagMapper.queryDeivceSelfInfoPage(PagingUtils.convert(queryDeviceSelfInfoParamPage), queryDeviceSelfInfoParamPage, tagIdOptional.get());
        return PagingUtils.convert(page);
    }

    /**
     * 根据tagCode查询ID
     *
     * @param tagCode tagCode
     */
    public Long findTagId(String tagCode) {
        BizTag tag = tagMapper.selectOne(Wrappers.<BizTag>lambdaQuery()
                                                 .eq(BizTag::getTagCode, tagCode)
                                        );
        if (tag == null) {
            throw new IllegalArgumentException("tagCode not exist");
        }

        return tag.getId();
    }

    public Optional<Long> findTagIdOptional(String tagCode) {
        BizTag tag = tagMapper.selectOne(Wrappers.<BizTag>lambdaQuery()
                                                 .eq(BizTag::getTagCode, tagCode)
                                        );
        if (tag == null) {
            return Optional.empty();
        }
        return Optional.of(tag.getId());
    }

    public List<Long> findTagIds(List<String> tagCodes) {
        List<BizTag> bizTags = tagMapper.selectList(Wrappers.<BizTag>lambdaQuery()
                                                            .in(BizTag::getTagCode, tagCodes)
                                                   );
        if (CollectionUtils.isEmpty(bizTags)) {
            return Collections.emptyList();
        }
        return bizTags.stream().map(BizTag::getId).collect(Collectors.toList());
    }

    public Optional<Long> findTagTypeIdOpt(String tagTypeCode) {
        BizTagType tagType = tagTypeMapper.selectOne(Wrappers.<BizTagType>lambdaQuery()
                                                             .eq(BizTagType::getTagTypeCode, tagTypeCode));
        if (tagType == null) {
            return Optional.empty();
        }
        return Optional.of(tagType.getId());
    }

    public List<Pair<String, String>> deviceCode(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return Collections.emptyList();
        }
        return tagRefMapper.queryDeviceCode(tagIdOptional.get());
    }

    /**
     * 统计在线设备数量
     *
     * @param tagCode 标签编码
     *
     * @return 在线设备数量
     */
    public Integer queryNormalDevice(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return 0;
        }
        return deviceMapper.queryNormalDevice(tagIdOptional.get());
    }

    public List<DevicesVO> listDevicesVo(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        return tagIdOptional.map(aLong -> deviceMapper.listDevicesVo(aLong)
                .stream()
                .peek(deviceVo -> deviceVo.setDeviceStatus(
                        (!ObjectUtils.isEmpty(deviceVo.getDeviceStatus()) && 1 == deviceVo.getDeviceStatusCode()) ?
                        DeviceInstanceStatusEnum.ONLINE.name() : DeviceInstanceStatusEnum.OFFLINE.name()))
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    public List<DeviceOfflineCountVO> queryDeviceOffline(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        return tagIdOptional.map(aLong -> deviceMapper.queryDeviceOffline(aLong).stream()
                .peek(vo -> vo.setProportion(new BigDecimal(vo.getOffLineNum()
                                                                    .toString())
                                                     .divide(new BigDecimal(vo.getNum()
                                                                                    .toString()), 2, RoundingMode.HALF_UP)))
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    public List<AreaPointVO> queryAreaPoint(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return Collections.emptyList();
        }
        return deviceMapper.queryAreaPoint(tagIdOptional.get());
    }

    public List<AreaDeviceNumberDTO> queryDeviceAreaNum(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        if (tagIdOptional.isEmpty()) {
            return Collections.emptyList();
        }
        List<AreaDeviceNumberDTO> vos = deviceMapper.queryDeviceAreaNum(tagIdOptional.get());

        // 处理区域名称 获取最后一个
        vos.forEach(vo -> {
            if (StringUtils.isNotBlank(vo.getAreaName())) {
                String[] split = vo.getAreaName().split("--");
                vo.setAreaName(split[split.length - 1]);
            }
        });

        return vos;
    }

    public List<AreaDeviceNumberDTO> queryDeviceStartWithAreaNumGroupByAreaLevel(List<String> tagCodes, Integer areaLevel) {
        List<Long> tagIds = findTagIds(tagCodes);
        List<AreaDTO> areaDTOS = areaClient.getAreaByLevelOne(areaLevel);
        if (CollectionUtils.isEmpty(tagIds)) {
            return areaDTOS.stream().map(areaDTO -> {
                AreaDeviceNumberDTO deviceAreaProportionVO = new AreaDeviceNumberDTO();
                deviceAreaProportionVO.setAreaId(areaDTO.getId());
                deviceAreaProportionVO.setAreaName(areaDTO.getName());
                deviceAreaProportionVO.setDeviceNumber(0);
                return deviceAreaProportionVO;
            }).collect(Collectors.toList());
        }
        List<DeviceAreaSeqVO> vos = deviceMapper.queryDeviceAreaSeqNumGroupByAreaLevel(tagIds);
        return areaDTOS.stream().map(areaDTO -> {
            //区域本期数量(包含子级区域)
            Integer twoAreaAlarmTotalCount = vos.stream()
                    .filter(deviceAreaVO -> StringUtils.isNotEmpty(deviceAreaVO.getAreaId()) &&
                                            (deviceAreaVO.getAreaId() + "-").startsWith(areaDTO.getSequence()))
                    .mapToInt(DeviceAreaSeqVO::getNum)
                    .sum();
            AreaDeviceNumberDTO deviceAreaProportionVO = new AreaDeviceNumberDTO();
            deviceAreaProportionVO.setAreaId(areaDTO.getId());
            deviceAreaProportionVO.setAreaName(areaDTO.getName());
            deviceAreaProportionVO.setDeviceNumber(twoAreaAlarmTotalCount);
            return deviceAreaProportionVO;
        }).collect(Collectors.toList());
    }

    public Integer queryDeviceNumByArea(List<String> tagCodes, Set<Long> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            return 0;
        }
        if (CollectionUtils.isEmpty(tagCodes)) {
            return 0;
        }
        List<Long> tagIds = findTagIds(tagCodes);
        List<DeviceAreaSeqVO> vos = deviceMapper.queryDeviceAreaSeqNumGroupByAreaLevel(tagIds);
        int deviceNum = 0;
        for (Long areaId : areaIds) {
            //区域本期数量(包含子级区域)
            int areaDeviceCount = vos.stream()
                    .filter(deviceAreaVO -> StringUtils.isNotEmpty(deviceAreaVO.getAreaId()) &&
                                            deviceAreaVO.getAreaId().contains(areaId.toString()))
                    .mapToInt(DeviceAreaSeqVO::getNum)
                    .sum();
            deviceNum += areaDeviceCount;
        }
        return deviceNum;
    }

    public List<GenreVO> queryGenreDevicesNum(String tagCode) {
        Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return Collections.emptyList();
        }
        return deviceMapper.queryGenreDevicesNum(tagIdOptional.get());
    }

    public List<TagPercentageDTO> tagPercentageByInnerTag(List<String> tagCodes) {
        if (tagCodes == null || tagCodes.isEmpty()) {
            return Collections.emptyList();
        }

        //总设备量
        Integer totalDevice = deviceMapper.queryTotalDevice();
        List<TagPercentageDTO> tagPercentageVos = new ArrayList<>(tagCodes.size() + 1);
        Integer totalTagCount = 0;
        for (String tagCode : tagCodes) {
            //统计标签不多，循环查询
            Optional<Long> tagIdOptional = findTagIdOptional(tagCode);
            if (tagIdOptional.isPresent()) {
                TagCountInfo tagCountInfo = tagRefMapper.tagCountInfo(tagIdOptional.get());
                if (tagCountInfo == null) {
                    continue;
                }
                totalTagCount += tagCountInfo.getTagCount();
                //计算tagCount 在totalDevice中的占比
                TagPercentageDTO tagPercentageVo = new TagPercentageDTO();
                tagPercentageVo.setTagCode(tagCode);
                tagPercentageVo.setTagDeviceNumber(tagCountInfo.getTagCount());
                tagPercentageVo.setTagName(tagCountInfo.getTagName());
                //计算占比，保留2位有效数字
                tagPercentageVo.setPercentage(tagCountInfo.getTagCount() * 100.0 / totalDevice);
                tagPercentageVos.add(tagPercentageVo);
            }
        }

        //计算other tag
        TagPercentageDTO tagPercentageVo = new TagPercentageDTO();
        tagPercentageVo.setTagCode("other");
        tagPercentageVo.setTagName("其他");
        if (totalTagCount == 0 || totalTagCount >= totalDevice) {
            tagPercentageVo.setTagDeviceNumber(0);
            tagPercentageVo.setPercentage(0.0);
        } else {
            tagPercentageVo.setPercentage((totalDevice - totalTagCount) * 100.0 / totalDevice);
            tagPercentageVo.setTagDeviceNumber(totalDevice - totalTagCount);
        }
        tagPercentageVos.add(tagPercentageVo);

        return tagPercentageVos;
    }

    public DeviceStateCountDTO getNumberOfGroupByStatusByTags(List<String> tagCodes) {
        return deviceMapper.selectNumberOfGroupByStatusByTags(tagCodes);
    }

    public Map<String, DeviceTags> getDeviceTagCode(List<String> deviceIds) {
        if (Objects.isNull(deviceIds) || deviceIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, DeviceTags> deviceTagsMap = new HashMap<>(deviceIds.size());

        //偷懒，一个设备一个设备去查询
        for (String deviceId : deviceIds) {
            DeviceTags deviceTags = new DeviceTags();
            deviceTags.setDeviceId(deviceId);
            deviceTags.setTags(tagRefMapper.getDeviceTagCode(Long.parseLong(deviceId)));
            deviceTagsMap.put(deviceId, deviceTags);
        }
        return deviceTagsMap;
    }

    public List<ProductCategoryOffLineCountVO> getTopProductCategoryStatisticsByTagCode(String tagCode) {
        Optional<Long> tagIdOptional = this.findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return Collections.emptyList();
        }
        List<ProductCategoryOffLineCountVO> topProductCategoryStatisticsByTagCode = this.deviceMapper.getTopProductCategoryStatisticsByTagCode(tagIdOptional.get());
        if (CollectionUtils.isEmpty(topProductCategoryStatisticsByTagCode)) {
            return Collections.emptyList();
        }
        //从查询结果集中获取一级品类编号
        Set<String> procCategoryCodes = topProductCategoryStatisticsByTagCode.stream().map(productCategoryOffLineCountVO ->
                                                                                                   productCategoryOffLineCountVO
                                                                                                           .getProdCategoryConcat()
                                                                                                           .split("-")[0]
                                                                                          ).collect(Collectors.toSet());
        List<ProductCategoryInfoDTO> productCategoryInfoDTOS = this.productInfoMapper
                .selectProdCategoryListByCode(procCategoryCodes, SessionContextHolder.getRequester().getTenant()).stream()
                .map(entity -> {
                    ProductCategoryInfoDTO dto = new ProductCategoryInfoDTO();
                    dto.setCode(entity.getCategoryCode());
                    dto.setName(entity.getCategoryName());
                    dto.setPathCode(entity.getCategoryCodeConcat());
                    return dto;
                }).collect(Collectors.toList());
        return productCategoryInfoDTOS.stream().map(productCategoryInfoDTO -> {
            ProductCategoryOffLineCountVO productCategoryOffLineCountVO = new ProductCategoryOffLineCountVO();
            productCategoryOffLineCountVO.setProdCategoryCode(productCategoryInfoDTO.getCode());
            productCategoryOffLineCountVO.setProdCategoryConcat(productCategoryInfoDTO.getPathCode());
            productCategoryOffLineCountVO.setProdCategoryName(productCategoryInfoDTO.getName());
            productCategoryOffLineCountVO.setOfflineNumber(topProductCategoryStatisticsByTagCode.stream()
                                                                   .filter(statistics -> statistics.getProdCategoryConcat()
                                                                           .startsWith(productCategoryInfoDTO.getCode()))
                                                                   .mapToInt(ProductCategoryOffLineCountVO::getOfflineNumber)
                                                                   .sum());
            productCategoryOffLineCountVO.setNormalNumber(topProductCategoryStatisticsByTagCode.stream()
                                                                  .filter(statistics -> statistics.getProdCategoryConcat()
                                                                          .startsWith(productCategoryInfoDTO.getCode()))
                                                                  .mapToInt(ProductCategoryOffLineCountVO::getNormalNumber)
                                                                  .sum());
            return productCategoryOffLineCountVO;
        }).collect(Collectors.toList());
    }

    public List<DeviceBaseAndIotPropertyInfoDTO> getDeviceBaseAndIotPropertyInfoByTagCode(String tagCode,
                                                                                          Set<String> iotPropertyCodes
                                                                                         ) {
        Optional<Long> tagIdOptional = this.findTagIdOptional(tagCode);
        if (!tagIdOptional.isPresent()) {
            return Collections.emptyList();
        }
        List<DeviceBaseAndIotPropertyInfoDTO> deviceBaseInfos = deviceMapper.getDeviceBaseInfoByProductTag(tagIdOptional.get());
        DeviceDetailParam param = new DeviceDetailParam();
        param.setWithSnapShot(true);
        param.setWithPropertySpec(true);
        deviceBaseInfos.stream().forEach(deviceBaseAndIotPropertyInfoDTO -> {
            try {
                param.setDeviceId(deviceBaseAndIotPropertyInfoDTO.getId().toString());
                DeviceDetailDTO detail = this.iotDeviceClient.detail(param);
                if (Objects.nonNull(detail)) {
                    deviceBaseAndIotPropertyInfoDTO.setIotProperties(this.convertAndFilterIotProperties(detail, iotPropertyCodes));
                }
            } catch (Exception e) {
                log.error("获取物联设备信息失败,{}", e);
            }
        });
        return deviceBaseInfos;
    }

    public Set<DeviceBaseAndIotPropertyInfoDTO.IotPropertyValue> convertAndFilterIotProperties(DeviceDetailDTO detail,
                                                                                               Set<String> iotPropertyCodes
                                                                                              ) {
        return detail.getProductPropVoList().stream()
                .filter(productPropDTO -> iotPropertyCodes.contains(productPropDTO.getPropCode()))
                .map(productPropDTO -> {
                    Optional<KeyValueWithTsDTO> propertyValueOptional = detail.getSnapshots().stream()
                            .filter(keyValueWithTsDTO -> productPropDTO.getPropCode().equals(keyValueWithTsDTO.getKey()))
                            .findFirst();
                    DeviceBaseAndIotPropertyInfoDTO.IotPropertyValue deviceAttributeRunningResponse = DeviceBaseAndIotPropertyInfoDTO.IotPropertyValue
                            .builder()
                            .code(productPropDTO.getPropCode())
                            .name(productPropDTO.getPropName())
                            .permission(AttributePermissionEnum.valueOf(DevicePropUtils.convertReadWriteEnum(productPropDTO.getDataWrite())
                                                                                .name()))
                            .dataType(productPropDTO.getDataType())
                            .attributeUnitName(productPropDTO.getUnitName())
                            .build();
                    propertyValueOptional.ifPresent(propertyValue -> {
                        deviceAttributeRunningResponse.setOriginalValue(propertyValue.getValue());
                        deviceAttributeRunningResponse.setTranslatedValue(DevicePropUtils.iotPropVal2Chinese(productPropDTO.getDataType(),
                                                                                                             propertyValue.getValue(),
                                                                                                             productPropDTO.getDataConstraint()));
                    });
                    return deviceAttributeRunningResponse;
                }).collect(Collectors.toSet());
    }

    public DeviceBaseInfoSafetyReportVO getDeviceNumberGroupOnStatusByTagCode(List<String> tagCodes, Period period) {
        List<DeviceBaseInfoAndNumberVO> deviceBaseInfoAndNumberVOS = deviceMapper.selectDeviceNumberGroupOnStatusByTagCode(tagCodes, period)
                .stream()
                .peek(deviceBaseInfoAndNumberVO -> {
                    if (!StringUtils.isEmpty(deviceBaseInfoAndNumberVO.getTagCode())) {
                        int lastIndex = deviceBaseInfoAndNumberVO.getTagCode().lastIndexOf("/");
                        if (lastIndex != -1) {
                            deviceBaseInfoAndNumberVO.setTagCode(deviceBaseInfoAndNumberVO.getTagCode().substring(lastIndex + 1));
                        }
                    }
                }).collect(Collectors.toList());
        Long totalNum = 0L;
        if (!CollectionUtils.isEmpty(deviceBaseInfoAndNumberVOS)) {
            totalNum = deviceBaseInfoAndNumberVOS.stream().map(DeviceBaseInfoAndNumberVO::getTotalNumber).reduce(Long::sum).get();
        } else {
            deviceBaseInfoAndNumberVOS = new ArrayList<>();
        }
        //环比
        var endTime = DateUtils.offsetMonth(period.getEnd(), -1);
        Period previousPeriod = new Period(DateUtils.offsetMonth(period.getBegin(), -1), DateUtils.endOfDay(endTime));
        Long ringNum = deviceMapper.selectDeviceNumberByTagCode(tagCodes, previousPeriod);
        //同比
        Period samePeriod = new Period(DateUtils.offset(period.getBegin(), ChronoUnit.YEARS, -1),
                                       DateUtils.offset(period.getEnd(), ChronoUnit.YEARS, -1));
        Long sameNum = deviceMapper.selectDeviceNumberByTagCode(tagCodes, samePeriod);
        List<DeviceFireSafetyReportVO> deviceFireSafetyReportVOS = deviceMapper.selectFireDeviceNumberByTagCode("builtin.app::FIRECONTROL", period);
        //消防设备
        return DeviceBaseInfoSafetyReportVO.builder()
                .totalNuM(totalNum)
                .ringNum(ringNum)
                .sameNum(sameNum)
                .deviceBaseInfoAndNumberVOS(deviceBaseInfoAndNumberVOS)
                .deviceFireSafetyReportVOS(deviceFireSafetyReportVOS)
                .build();
    }

    public List<ProductDeviceCountVO> getProductStatisticsByTagCode(List<String> tagCodes) {
        if (CollectionUtils.isEmpty(tagCodes)) {
            return null;
        }
        List<Long> tagIds = findTagIds(tagCodes);
        return deviceMapper.getProductDeviceCountByTagCode(tagIds);
    }

    public DeviceUsefulLifeExpireStatisticsVO getDeviceExpireStatistics(List<String> tagCodes) {
        if (CollectionUtils.isEmpty(tagCodes)) {
            return null;
        }
        List<Long> tagIds = findTagIds(tagCodes);
        DeviceUsefulLifeExpireStatisticsVO deviceExpireStatistics = deviceMapper.getDeviceExpireStatistics(tagIds);
        //设备总数
        DeviceStateCountDTO deviceStateCountDTO = deviceMapper.selectNumberOfGroupByStatusByTags(tagCodes);
        if (Objects.isNull(deviceStateCountDTO) || 0 == deviceStateCountDTO.getTotalNumber()) {
            deviceExpireStatistics.setTotalDeviceNumber(0);
            deviceExpireStatistics.setExpireNum(0);
            deviceExpireStatistics.setExpireRate(BigDecimal.ZERO);
        } else {
            deviceExpireStatistics.setTotalDeviceNumber(deviceStateCountDTO.getTotalNumber());
            deviceExpireStatistics.setExpireRate(BigDecimal.valueOf(deviceExpireStatistics.getExpireNum())
                                                         .divide(BigDecimal.valueOf(deviceStateCountDTO.getTotalNumber()), 2, RoundingMode.HALF_UP));
        }
        return deviceExpireStatistics;
    }

    public List<ProductCategoryOffLineCountVO> getProductCategoryStatisticsByTagCodes(QueryDeviceByTagParam param) {
        return deviceMapper.getProductCategoryStatisticsByTagCodes(param);
    }
}

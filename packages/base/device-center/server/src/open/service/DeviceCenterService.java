package com.guhecloud.cube.base.device.center.open.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.guhecloud.cube.base.device.center.core.dto.DevicePageDto;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceExpandMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.param.DeviceCustomerPageParam;
import com.guhecloud.cube.base.device.center.core.rule.DeviceCustomerFieldWrapper;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceService;
import com.guhecloud.cube.base.device.center.core.service.DeviceInfoTool;
import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoWithFullAreaDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceGenreDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceFilterableByGenreQuery;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceCustomInfoParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceProductCountQueryParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceExpandVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceInfoVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceTagFullVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceInstanceDetailVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceListByGenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DeviceGroupDetailQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DevicePageQry;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.TagDeviceGroupDeviceQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceGroupMapper;
import com.guhecloud.cube.base.device.center.open.mapper.ProductInfoMapper;
import com.guhecloud.cube.base.device.center.open.vo.AreaEmptyDeviceTotalVO;
import com.guhecloud.cube.base.device.center.open.vo.CenterBizDeviceVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceManagerVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceTag;
import com.guhecloud.cube.base.device.center.open.vo.ExpiredDeviceVO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.VIDEO_DEVICE_TAG_CODE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterService {

    private final DeviceCenterMapper mapper;
    private final CenterBizDeviceExpandMapper centerBizDeviceExpandMapper;
    private final CenterBizTagMapper centerBizTagMapper;
    private final DeviceCenterTagService deviceCenterTagService;
    private final FileClient fileClient;
    private final AreaClient areaClient;
    private final OrganizationClient organizationClient;
    private final CenterDeviceService centerDeviceService;
    private final ProductInfoMapper productInfoMapper;
    private final CenterBizDeviceMapper centerBizDeviceMapper;
    private final DeviceGroupMapper deviceGroupMapper;
    private final CenterBizTagRefMapper centerBizTagRefMapper;

    public List<DeviceInfoVO> queryCustomDeviceInfos(QueryDeviceCustomInfoParam param) {
        List<DeviceInfoVO> vos = mapper.queryCustomDeviceInfo(param);
        // 扩展信息,  暂时使用代码方式查询。
        this.extendDeviceInfoVO(vos, param.isWithCustomExt(), param.isWithDeviceType());
        return vos;
    }

    public DeviceInstanceDetailVO queryDetailVO(Long deviceId) {
        DeviceInstanceDetailVO detailVO = mapper.selectDetail(deviceId);

        //处理区域 1-2-3-4-5
        Assert.isNotFound(detailVO, "设备不存在");

        //app端需要
        if (StringUtils.isNotBlank(detailVO.getAreaId())) {
            detailVO.setAreaIdList(Arrays.stream(detailVO.getAreaId().split("-")).map(Long::parseLong)
                                           .collect(Collectors.toList()));

            List<Pair<String, String>> areaListPairs = new ArrayList<>(5);
            Collection<Long> ids = Arrays.stream(detailVO.getAreaId().split("-")).map(Long::parseLong)
                    .collect(Collectors.toList());
            Map<Long, AreaDTO> areaIdNameMapping = areaClient.getByIds(ids).stream()
                    .collect(Collectors.toMap(AreaDTO::getId, Function.identity()));
            for (Long areaId : ids) {
                Pair<String, String> areaPair = new Pair<>();
                areaPair.setKey(String.valueOf(areaId));
                areaPair.setVal(areaIdNameMapping.get(areaId).getName());

                areaListPairs.add(areaPair);
            }

            detailVO.setAreaListPairs(areaListPairs);
        }

        if (StringUtils.isNotBlank(detailVO.getDepartmentId())) {
            detailVO.setDepartmentIdList(Arrays.stream(detailVO.getDepartmentId().split("-")).map(Long::parseLong)
                                                 .collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(detailVO.getManagerJson())) {
            detailVO.setManager(Arrays.stream(detailVO.getManagerJson().split(",")).map(Long::parseLong)
                                        .collect(Collectors.toSet()));
        }

        //图片信息
        if (Objects.nonNull(detailVO.getImages())) {
            FileVO fileVO = fileClient.getById(detailVO.getImages());
            detailVO.setFileVO(fileVO);
        }

        // 查询 关联的设备
        List<Long> relatedDeviceIds = mapper.selectRelatedDeviceIds(deviceId);
        detailVO.setAssociatedDeviceId(relatedDeviceIds);

        // 处理设备状态
        detailVO.setDeviceStatus(
                detailVO.getDeviceState() == 0 ? DeviceInstanceStatusEnum.OFFLINE : DeviceInstanceStatusEnum.ONLINE);
        detailVO.setCommunicationStatus(detailVO.getDeviceStatus().getName());

        return detailVO;
    }

    private List<Long> findTagCodeId(List<String> tagCodes) {
        if (Objects.isNull(tagCodes) || tagCodes.isEmpty()) {
            return null;
        }
        List<BizTag> bizTags = centerBizTagMapper
                .selectList(new LambdaQueryWrapper<BizTag>().in(BizTag::getTagName, tagCodes).select(BizTag::getId));
        return bizTags.stream().map(BizTag::getId).collect(Collectors.toList());
    }

    public Pagination<DevicePageInfoVO> deviceInfoPage(DeviceFilterPageParam param) {
        // 分页获取设备
        List<Long> tagCodeIds = findTagCodeId(param.getTagCodes());
        String order = null;
        if (Objects.nonNull(param.getDeviceSort())) {
            order = "`dev`." + param.getDeviceSort().getSortField().getTableField() + " " +
                    param.getDeviceSort().getSort().name();
        }
        Page<DevicePageInfoVO> page = this.mapper.deviceInfoPage(PagingUtils.convert(param), param, tagCodeIds, order);
        extendArea(page);
        return PagingUtils.convert(page);
    }

    public List<DeviceIdTypeDTO> deviceInfoIdTypes(DeviceFilterPageParam param) {
        // 获取设备ID列表
        var tagCodeIds = findTagCodeId(param.getTagCodes());
        var deviceFilterParam = new DeviceFilterParam();
        deviceFilterParam.setKeywordLike(param.getKeyword());
        deviceFilterParam.setAnyMatchedDepartmentId(param.getAnyMatchedDepartmentId());
        deviceFilterParam.setMatchedDeviceIds(param.getMatchedDeviceIds());
        return this.mapper.selectDeviceIdTypes(deviceFilterParam, tagCodeIds);
    }

    public Pagination<DevicePageInfoVO> deviceRuleConditionInfoPage(DeviceCustomerPageParam param) {
        QueryWrapper<BizDevice> bizDeviceQueryWrapper = new DeviceCustomerFieldWrapper().buildCustomerFieldWrapper(null, param.getRuleGroup());
        Page<BizDevice> page = this.centerBizDeviceMapper.selectPage(PagingUtils.convert(param), bizDeviceQueryWrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        Set<Long> prodIds = page.getRecords()
                .stream()
                .map(BizDevice::getDeviceIotProdId).collect(Collectors.toSet());
        Map<Long, DeviceProductInfoDTO> productInfoDTOMap = this.productInfoMapper.selectList(prodIds)
                .stream().collect(Collectors.toMap(DeviceProductInfoDTO::getId, s -> s));
        List<AreaListDTO> areas = this.areaClient.listInnerArea(new AreaQueryParam());
        List<DevicePageInfoVO> devicePageInfoVOS = page.getRecords().stream().map(device -> {
            DeviceBaseArchivesInfoDTO.Status status = new DeviceBaseArchivesInfoDTO.Status();
            status.setOnlineState(1 == device.getDeviceState());
            status.setCommunicationTime(device.getCommunicationTime());
            DevicePageInfoVO devicePageInfoVO = new DevicePageInfoVO();
            devicePageInfoVO.setId(device.getId());
            devicePageInfoVO.setCode(device.getDeviceCode());
            devicePageInfoVO.setName(device.getDeviceName());
            devicePageInfoVO.setStatus(status);
            devicePageInfoVO.setDepartment(new IdToName(device.getDepartmentId(), device.getDepartmentName()));
            devicePageInfoVO.setThirdPartyId(device.getThirdPartyId());
            devicePageInfoVO.setArea(new AreaBaseInfoDTO(device.getAreaLastId(), device.getAreaName(), device.getAreaId()));
            devicePageInfoVO.setProductInfo(productInfoDTOMap.get(device.getDeviceIotProdId()));
            extendArea(areas, devicePageInfoVO.getArea());
            return devicePageInfoVO;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, devicePageInfoVOS);
    }

    public Pagination<DevicePageInfoVO> getGroupDevicePage(DeviceGroupDetailQueryParam param) {
        //获取组绑定的标签
        List<DeviceTag> groupRefTag = centerBizTagRefMapper.getTagCodeByBusinessKey(param.getId());
        if (CollectionUtils.isEmpty(groupRefTag)) {
            return Pagination.empty();
        }
        List<BizTag> bizTags = centerBizTagMapper
                .selectList(new LambdaQueryWrapper<BizTag>().in(BizTag::getTagCode, groupRefTag.getFirst().getTagCode())
                                    .select(BizTag::getId));
        if (CollectionUtils.isEmpty(bizTags)) {
            return Pagination.empty();
        }
        Page<DevicePageInfoVO> page = deviceGroupMapper.deviceInfoPageByTag(PagingUtils.convert(param), param, bizTags.getFirst().getId());
        extendArea(page);
        return PagingUtils.convert(page);
    }

    public Pagination<DevicePageInfoVO> getGroupDevicesPageByTagCode(@Valid @RequestBody TagDeviceGroupDeviceQueryParam param) {
        var page = deviceGroupMapper.groupDevicesPageByTagCode(PagingUtils.convert(param), param);
        extendArea(page);
        return PagingUtils.convert(page);
    }

    private void extendArea(Page<DevicePageInfoVO> page) {
        List<AreaListDTO> areas = this.areaClient.listInnerArea(new AreaQueryParam());
        page.getRecords().forEach(devicePageInfoVO -> {
            extendArea(areas, devicePageInfoVO.getArea());
            DeviceBaseArchivesInfoDTO deviceBaseArchivesInfoDTO = new DeviceBaseArchivesInfoDTO();
            deviceBaseArchivesInfoDTO.setPicture(devicePageInfoVO.getPicture());
            devicePageInfoVO.setPicture(deviceBaseArchivesInfoDTO.getPicture());
        });
    }

    public void extendArea(List<AreaListDTO> areas, AreaBaseInfoDTO area) {
        if (Objects.nonNull(area) && Objects.nonNull(area.getId())) {
            Optional<AreaListDTO> optionalArea = areas.stream()
                    .filter(areaListDTO -> areaListDTO.getId().equals(area.getId()))
                    .findFirst();
            optionalArea.ifPresent(deviceArea -> {
                List<Long> areaIds = Arrays.asList(deviceArea.getSequence().split("-")).stream().map(Long::parseLong)
                        .collect(Collectors.toList());
                Map<Long, AreaListDTO> areaMap = areas.stream().filter(areaListDTO -> areaIds.contains(areaListDTO.getId()))
                        .collect(Collectors.toMap(AreaListDTO::getId, a -> a));
                List<String> areaNames = new ArrayList<>();
                areaIds.stream().forEach(areaId -> {
                    if (areaMap.containsKey(areaId)) {
                        AreaListDTO areaListDTO = areaMap.get(areaId);
                        areaNames.add(areaListDTO.getName());
                    }
                });
                area.setName(deviceArea.getName());
                area.setPathId(deviceArea.getSequence());
                area.setPathName(areaNames.stream().collect(Collectors.joining("-")));
            });
        }
    }

    public List<DeviceInfoVO> deviceInfoList(DeviceFilterParam param) {
        List<Long> tagCodeIds = findTagCodeId(param.getTagCodes());

        List<DeviceInfoVO> deviceInfoVOS = this.mapper.deviceInfoList(param, tagCodeIds);
        this.extendDeviceInfoVO(deviceInfoVOS, param.isWithDeviceExpandInfo(), param.isWithDeviceTagInfo());
        return deviceInfoVOS;
    }

    public AreaEmptyDeviceTotalVO getAlarmEmptyDeviceTotal(DeviceFilterParam param) {
        return this.mapper.getAlarmEmptyDeviceTotal(param);
    }

    public DeviceInfoVO deviceInfoSingle(Long deviceId) {
        DeviceInfoVO deviceInfoVO = this.mapper.deviceInfoSingle(deviceId);
        if (Objects.nonNull(deviceInfoVO)) {
            this.extendDeviceInfoVO(Collections.singletonList(deviceInfoVO), true, true);
        }
        return deviceInfoVO;
    }

    public boolean deviceExits(Long deviceInstanceId) {
        if (Objects.isNull(deviceInstanceId) || deviceInstanceId <= 0) {
            return false;
        }
        Long devices = mapper.deviceExits(deviceInstanceId);
        return Objects.nonNull(devices) && devices > 0;
    }

    public List<Pair<String, String>> getCodeByDeviceId(Set<String> deviceIds) {
        if (ObjectUtils.isNotEmpty(deviceIds)) {
            Set<Long> deviceIdSet = new HashSet<>();
            deviceIds.stream().mapToLong(Long::parseLong).forEach(deviceIdSet::add);
            return mapper.getCodeByDeviceId(deviceIdSet);
        }
        return new ArrayList<>();
    }

    public Integer queryCountNormalDevice(Set<Long> deviceIds) {
        return mapper.selectCountNormalDevice(deviceIds);
    }

    public DeviceStateCountDTO queryNumberOfGroupByStatus(List<Long> deviceIds) {
        DeviceStateCountDTO statusVO = mapper.selectNumberOfGroupByStatus(deviceIds);
        if (ObjectUtils.isNotEmpty(statusVO)) {
            statusVO.setOfflineNumber(statusVO.getOfflineNumber() == null ? 0 : statusVO.getOfflineNumber());
            statusVO.setTotalNumber(statusVO.getTotalNumber() == null ? 0 : statusVO.getTotalNumber());
        }
        return statusVO;
    }

    public List<DevicesVO> queryDevicesVo(List<Long> deviceIds) {
        return mapper.selectDevicesVo(deviceIds, SessionContextHolder.getRequester().getTenant()).stream().peek(item -> {
            item.setDeviceStatus(0 == item.getDeviceStatusCode() ? DeviceInstanceStatusEnum.OFFLINE.getName() :
                                 DeviceInstanceStatusEnum.ONLINE.getName());
        }).collect(Collectors.toList());
    }

    public List<GenreVO> queryGenreDevicesNum(List<Long> deviceIds) {
        return mapper.selectGenreDevicesNum(deviceIds, SessionContextHolder.getRequester().getTenant());
    }

    public List<AreaDeviceNumberDTO> queryDeviceAreaNum(List<Long> deviceIds) {
        List<AreaDeviceNumberDTO> vos = mapper.selectDeviceAreaNum(deviceIds);

        // 处理区域名称 获取最后一个
        vos.forEach(vo -> {
            if (StringUtils.isBlank(vo.getAreaName())) {
                return;
            }
            String[] split = vo.getAreaName().split("--");
            vo.setAreaName(split[split.length - 1]);
        });

        return vos;
    }

    public List<Long> queryDeviceIdsByAll() {
        return mapper.selectDeviceIdsByAll();
    }

    public List<AreaPointVO> queryAreaPoint(List<Long> deviceIds) {
        return mapper.selectApplicationAreaPoint(deviceIds);
    }

    public List<DeviceOfflineCountVO> queryDeviceOffline(Set<Long> deviceIds) {
        return mapper.selectDeviceOffline(deviceIds).stream()
                .peek(vo -> vo.setProportion(new BigDecimal(vo.getOffLineNum().toString())
                                                     .divide(new BigDecimal(vo.getNum().toString()), 2, RoundingMode.HALF_UP)))
                .collect(Collectors.toList());
    }

    private void extendDeviceInfoVO(List<DeviceInfoVO> vos, boolean withDeviceExpandInfo, boolean withDeviceTagInfo) {
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }

        var deviceIds = vos.stream().map(DeviceInfoVO::getId).collect(Collectors.toList());
        if (withDeviceExpandInfo) {
            // 查询自定义扩展信息 DeviceExpandVO
            var deviceExpandVOS = centerBizDeviceExpandMapper.selectDeviceExpand(deviceIds);
            if (CollectionUtils.isNotEmpty(deviceExpandVOS)) {
                var collect = deviceExpandVOS.stream().collect(Collectors
                                                                       .toMap(DeviceExpandVO::getId, Function
                                                                               .identity()));
                vos.forEach(vo -> vo.setDeviceExpandVO(collect.getOrDefault(vo.getId(), null)));
            }
        }
        if (withDeviceTagInfo) {
            // 查询设备标签扩展信息 DeviceTagFullVO
            var deviceTagFullVOS = centerBizTagMapper.selectDeviceTags(deviceIds);
            if (CollectionUtils.isNotEmpty(deviceTagFullVOS)) {
                var collect = deviceTagFullVOS.stream().collect(Collectors
                                                                        .groupingBy(DeviceTagFullVO::getDeviceId));
                vos.forEach(vo -> vo.setDeviceTagFullVOs(collect.getOrDefault(vo.getId(), null)));
            }
        }

        //获取所有的图片id集合
        var imageIds = vos.stream().map(DeviceInfoVO::getIotProductIcon).filter(Objects::nonNull).collect(Collectors.toList());
        imageIds.addAll(vos.stream().map(DeviceInfoVO::getIotProductPicture).filter(Objects::nonNull).collect(Collectors.toSet()));
        Map<Long, FileVO> files = new HashMap<>();
        if (!CollectionUtils.isNotEmpty(imageIds)) {
            var fileParam = new FileParam();
            fileParam.setIds(imageIds);
            files = fileClient.getFileList(fileParam).stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));
        }
        var areas = this.areaClient.listInnerArea(new AreaQueryParam());
        var organizationMap = this.organizationClient.postOrganizationsMapByIds(new OrganizationQueryParam() {{
            setIds(vos.stream().map(DeviceInfoVO::getDepartmentId).collect(Collectors.toSet()));
        }});
        //查询图片&图标信息 && 区域名称 && 部门名称
        for (DeviceInfoVO deviceInfoVO : vos) {
            if (Objects.nonNull(deviceInfoVO.getIotProductIcon())) {
                deviceInfoVO.setIotProductIconFileVO(files.get(deviceInfoVO.getIotProductIcon()));
            }

            if (Objects.nonNull(deviceInfoVO.getIotProductPicture())) {
                deviceInfoVO.setIotProductPictureFileVO(files.get(deviceInfoVO.getIotProductPicture()));
            }

            if (StringUtils.isNotBlank(deviceInfoVO.getAreaId())) {
                var split = deviceInfoVO.getAreaId().split("-");
                deviceInfoVO.setAreaLastId(Long.parseLong(split[split.length - 1]));
                var fullAreaName = Arrays.stream(split)
                        .map(Long::parseLong)
                        .map(areaId -> areas.stream()
                                .filter(area -> areaId.equals(area.getId()))
                                .map(AreaListDTO::getName)
                                .findFirst()
                                .orElse(""))
                        .filter(StringUtils::isNotBlank)
                        .reduce((a, b) -> a + "--" + b)
                        .orElse(null);
                deviceInfoVO.setAreaName(fullAreaName);
            }
            if (Objects.nonNull(deviceInfoVO.getDepartmentId())) {
                var organizationDTO = organizationMap.get(deviceInfoVO.getDepartmentId());
                if (Objects.nonNull(organizationDTO)) {
                    deviceInfoVO.setDepartmentName(organizationDTO.getName());
                }
            }
        }
    }

    public List<DeviceListByGenreVO> queryDeviceListFilterableByGenre(DeviceFilterableByGenreQuery query) {
        List<DeviceGenreDTO> dtos = mapper.selectDeviceListFilterableByGenre(query.getName(), query.getFilteredDeviceIds());
        return this.buildDeviceListByGenre(dtos);
    }

    public List<DeviceListByGenreVO> deviceListByGenre(@RequestBody DeviceFilterableByGenreQuery filterableByGenreQuery) {
        List<DeviceGenreDTO> dtos = mapper
                .selectDeviceListByGenre(filterableByGenreQuery.getDeviceType(), filterableByGenreQuery.getName());
        return ObjectUtils.copying(dtos, DeviceListByGenreVO.class);
    }

    private List<DeviceListByGenreVO> buildDeviceListByGenre(List<DeviceGenreDTO> dtos) {

        List<DeviceListByGenreVO> vos = new ArrayList<>();

        dtos.stream()
                .filter(dto -> StringUtils.isNotEmpty(dto.getGenreName()))
                .collect(Collectors.groupingBy(DeviceGenreDTO::getGenreName))
                .forEach((genreName, instanceGenreDTOS) -> {
                    DeviceListByGenreVO vo = new DeviceListByGenreVO(
                            instanceGenreDTOS.get(0).getGenreId(),
                            genreName,
                            instanceGenreDTOS.size()
                    );

                    List<DeviceListByGenreVO.Instance> instanceList = ObjectUtils
                            .copying(instanceGenreDTOS, DeviceListByGenreVO.Instance.class);

                    instanceList.forEach(instance -> {
                        if (StringUtils.isNotBlank(instance.getAreaName())) {
                            String[] split = instance.getAreaName().split("--");
                            instance.setAreaName(split[split.length - 1]);
                        }
                        instance.setParentId(vo.getId());
                        instance.setDeviceGenreName(vo.getGenreName());
                    });
                    vo.setInstanceList(instanceList);
                    vos.add(vo);
                });

        return vos;
    }

    /**
     * 获取设备管理人员
     *
     * @param deviceInstanceIds 设备id
     *
     * @return 设备对应的管理人员集合
     */
    public Map<Long, Set<Long>> getManagerByDevice(Set<Long> deviceInstanceIds) {
        List<DeviceManagerVO> deviceManagerVOS = mapper.getManagerByDevice(deviceInstanceIds);
        Map<Long, Set<Long>> map = new HashMap<>();
        deviceManagerVOS.forEach(vo -> {
            final String ids = vo.getManagerIds();
            Set<Long> userIds = new HashSet<>();
            Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).forEach(userIds::add);
            if (map.containsKey(vo.getDeviceId())) {
                map.get(vo.getDeviceId()).addAll(userIds);
            } else {
                map.put(vo.getDeviceId(), userIds);
            }
        });
        return map;
    }

    /**
     * 根据设备管理员获取设备实例
     *
     * @param managerId 管理人员id
     *
     * @return 管理的所有设备id
     */
    public List<Long> listByManager(Long managerId) {
        return mapper.listByManager(managerId);
    }

    /**
     * 统计设备属性扩展表中已过期的设备id
     *
     * @return 已过期的设备id
     */
    public List<Long> expiredDevices() {
        List<ExpiredDeviceVO> expiredDeviceVOS = mapper.expiredDevices();
        List<Long> ids = Lists.newCopyOnWriteArrayList();
        expiredDeviceVOS.parallelStream().forEach(vo -> {
            if (ObjectUtils.isNotNull(vo.getInstallDate()) && ObjectUtils.isNotNull(vo.getUsefulLife()) &&
                ObjectUtils.isNotNull(vo.getDailyWorkTime())) {
                //向上取整
                int t = vo.getUsefulLife() / vo.getDailyWorkTime() + (vo.getUsefulLife() % vo.getDailyWorkTime() == 0 ? 0 : 1);
                var dateTime = DateUtils.offsetDay(vo.getInstallDate(), t);
                if (DateUtils.toTimestamp(dateTime) <= DateUtils.toTimestamp(LocalDateTime.now())) {
                    ids.add(vo.getId());
                }
            }
        });
        return ids;
    }

    /**
     * 根据设备id集合查询设备基础信息列表
     *
     * @param deviceIds 设备id
     *
     * @return
     */
    public List<DeviceBaseArchivesInfoDTO> getListByDeviceId(Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        return mapper.getDeviceBaseArchivesInfoListByDeviceId(deviceIds);
    }

    public List<DeviceBaseArchivesInfoDTO> getListByTag(Set<String> tagCodes) {
        if (CollectionUtils.isEmpty(tagCodes)) {
            return Collections.emptyList();
        }
        return mapper.getListByTagCodes(tagCodes);
    }

    /**
     * 根据设备管理员获取设备基础信息
     *
     * @param managerId
     *
     * @return
     */
    public List<DeviceBaseArchivesInfoDTO> getListByManager(Long managerId) {
        return mapper.getListByManager(managerId);
    }

    public Set<DeviceBaseArchivesInfoDTO> getAssociatedVideos(Long deviceId) {
        Long tagId = deviceCenterTagService.findTagId(VIDEO_DEVICE_TAG_CODE);
        return mapper.getAssociatedVideos(deviceId, tagId);
    }

    public Pagination<DevicePageDto> page(DevicePageQry devicePageQuery) {
        Page<DevicePageDto> list = mapper.page(PagingUtils.convert(devicePageQuery), devicePageQuery);
        Pagination<DevicePageDto> pagination = PagingUtils.convert(list);
        if (Objects.nonNull(pagination.getRecords())) {
            pagination.getRecords().forEach(dto -> centerDeviceService.fillAreaPair(dto));
        }
        return pagination;
    }

    public List<DeviceExpandInfoDTO> getExpandListByDeviceId(Set<Long> deviceIds) {
        if (ObjectUtils.isEmpty(deviceIds)) {
            return null;
        }
        return centerBizDeviceExpandMapper.getExpandListByDeviceId(deviceIds);
    }

    public List<DeviceProductInfoWithDeviceDTO> getProductInfoByDeviceId(@RequestParam Set<Long> deviceIds) {
        return productInfoMapper.selectListByDeviceId(deviceIds);
    }

    public Pagination<DeviceBaseArchivesInfoDTO> pageOfBaseInfo(DeviceBaseInfoPageQry baseInfoPageQry) {
        Page<DeviceBaseArchivesInfoDTO> page = mapper.pageOfBaseInfo(PagingUtils.convert(baseInfoPageQry), baseInfoPageQry);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            Map<Long, DeviceProductInfoWithDeviceDTO> map = productInfoMapper
                    .selectListByDeviceId(page.getRecords().stream().map(DeviceBaseArchivesInfoDTO::getId)
                                                  .collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, o -> o));
            page.getRecords().forEach(info -> {
                if (Objects.nonNull(map.get(info.getId()))) {
                    DeviceInfoTool.setPicture(info, map.get(info.getId()).getProductInfo());
                }
            });

        }

        return PagingUtils.convert(page);
    }

    public Pagination<DeviceBaseArchivesInfoWithFullAreaDTO> pageOfBaseInfoWithFullArea(DeviceBaseInfoPageQry baseInfoPageQry) {
        Page<DeviceBaseArchivesInfoWithFullAreaDTO> page = mapper
                .pageOfBaseInfoWithFullArea(PagingUtils.convert(baseInfoPageQry), baseInfoPageQry);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            Map<Long, DeviceProductInfoWithDeviceDTO> map = productInfoMapper
                    .selectListByDeviceId(page.getRecords().stream().map(DeviceBaseArchivesInfoWithFullAreaDTO::getId)
                                                  .collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, o -> o));
            page.getRecords().forEach(info -> {
                if (Objects.nonNull(map.get(info.getId()))) {
                    DeviceInfoTool.setPicture(info, map.get(info.getId()).getProductInfo());
                }
            });

        }

        return PagingUtils.convert(page);
    }

    public List<DeviceBaseDetailsDTO> getDetailsListByDeviceId(Set<Long> deviceIds) {
        return centerBizDeviceMapper.selectDeviceDetailsByDeviceIds(deviceIds);
    }

    /**
     * 按三方 id（如智慧仓库粮仓 id 与物模型设备 thirdparty_id 一致）查设备中心业务设备详情。
     */
    public DeviceBaseDetailsDTO getDetailsByThirdPartyId(String thirdPartyId) {
        if (StringUtils.isBlank(thirdPartyId)) {
            return null;
        }
        return centerBizDeviceMapper.selectDeviceDetailsByThirdPartyId(thirdPartyId.trim());
    }

    /**
     * 根据部门ids获取设备信息(center_biz_device表的信息)
     *
     * @param organizationList 部门id集合
     *
     * @return 设备列表
     */
    public List<CenterBizDeviceVO> getCenterBizDeviceByOrganizations(List<Long> organizationList) {
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(organizationList)) {
            return Collections.emptyList();
        }
        List<BizDevice> bizDeviceList = centerBizDeviceMapper.selectList(Wrappers.<BizDevice>lambdaQuery()
                                                                                 .in(!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(organizationList), BizDevice::getDepartmentId, organizationList)
                                                                        );

        return ConvertUtils.toList(CenterBizDeviceVO.class, bizDeviceList);
    }

    public Set<String> deviceCodesByDeviceIds(Set<Long> deviceIds) {
        if (ListUtils.isEmpty(deviceIds)) {
            return Collections.emptySet();
        }
        return centerBizDeviceMapper.deviceCodesByDeviceIds(deviceIds);
    }

    /**
     * 获取产品设备数量
     *
     * @param param 条件
     *
     * @return 产品设备数量集合
     */
    public List<DeviceProductCountDTO> productDeviceCount(DeviceProductCountQueryParam param) {
        return productInfoMapper.deviceProductCount(param.getProductIds(), param.getDeviceIds());
    }

    public Set<Long> queryCameraDeviceIdsByAreasOrGroups(QueryDeviceByAreasOrGroupsAndTagParam param) {
        return mapper.deviceIdsByAreasOrGroupsAndTag(param);
    }

    /**
     * 根据区域id列表批量获取设备数统计
     *
     * @param areaIds 区域id列表
     *
     * @return 区域id与设备数的映射，key为区域id，value为设备数
     */
    public Map<Long, Long> countByAreaIds(List<Long> areaIds) {
        if (ListUtils.isEmpty(areaIds)) {
            return Collections.emptyMap();
        }

        var resultMap = areaIds.stream()
                .collect(Collectors.toMap(Function.identity(), areaId -> 0L, (left, right) -> left, HashMap::new));
        var distinctAreaIds = areaIds.stream().filter(Objects::nonNull).distinct().toList();
        if (!distinctAreaIds.isEmpty()) {
            mapper.countByAreaIds(distinctAreaIds)
                    .forEach(areaCount -> resultMap.put(areaCount.getKey(), areaCount.getVal()));
        }
        return resultMap;
    }
}

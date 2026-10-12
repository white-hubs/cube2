package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.convert.SecurityDeviceConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceDeletedParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceExportAppParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationCodeStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationStatusStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceOutVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.devicecenter.SecurityDeviceTagFacadeService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityCommonServiceImpl extends ServiceImpl<SecurityCommonMapper, SecurityDevice> implements SecurityCommonService {

    private final SecurityCommonMapper securityCommonMapper;

    private final DeviceCenterTagStdClient deviceCenterTagClient;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final ApplicationClient applicationClient;

    private final SecurityDeviceTagFacadeService securityDeviceTagFacadeService;

    @Override
    public List<Long> getTenantDevices(String applicationCode) {
        List<SecurityDevice> list = this.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                      .eq(SecurityDevice::getApplicationCode, applicationCode));
        List<Long> collect = list.stream().map(SecurityDevice::getDeviceId).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Pagination<SecurityDeviceVo> getSecurityDevicePage(String applicationCode, DeviceParam param) {
        if (!CollectionUtils.isEmpty(param.getTagCodes())) {
            List<Long> deviceIds = securityDeviceTagFacadeService.filterTagDevices(applicationCode, param.getTagCodes());
            if (CollectionUtils.isEmpty(deviceIds)) {
                return Pagination.empty();
            }
            if (!CollectionUtils.isEmpty(param.getIds())) {
                //用标签设备中过滤掉条件传入的设备
                param.setIds(param.getIds().stream().filter(collectDeviceId -> deviceIds.contains(collectDeviceId))
                                     .collect(Collectors.toList()));
            } else {
                param.setIds(deviceIds);
            }
            if (!CollectionUtils.isEmpty(param.getFilterDeviceIds())) {
                //用标签设备中过滤掉条件传入的设备
                param.setFilterDeviceIds(param.getFilterDeviceIds().stream().filter(collectDeviceId -> deviceIds.contains(collectDeviceId))
                                                 .collect(Collectors.toList()));
            }
        }
        Page<SecurityDevice> page = this.page(PagingUtils.convert(param), Wrappers.<SecurityDevice>lambdaQuery()
                .eq(SecurityDevice::getApplicationCode, applicationCode)
                .eq(ObjectUtils.isNotEmpty(param.getProductId()), SecurityDevice::getDeviceProdId, param.getProductId())
                .like(StringUtils.isNotBlank(param.getDeviceProdCategoryCode()), SecurityDevice::getDeviceProdCategoryCode, param.getDeviceProdCategoryCode())
                .in(!CollectionUtils.isEmpty(param.getIds()), SecurityDevice::getDeviceId, param.getIds())
                .notIn(!CollectionUtils.isEmpty(param.getFilterDeviceIds()), SecurityDevice::getDeviceId, param.getFilterDeviceIds())
                .like(StringUtils.isNotEmpty(param.getAreaId()), SecurityDevice::getAreaId, param.getAreaId())
                .and(StringUtils.isNotEmpty(param.getKeyword()), wrapper -> wrapper
                        .like(SecurityDevice::getDeviceName, param.getKeyword()))
                .orderByDesc(BasicEntity::getCreateTime));
        Pagination<SecurityDeviceVo> convert = PagingUtils.convert(page, SecurityDeviceVo.class);
        return convert;
    }

    @Override
    public Pagination<SecurityDeviceVo> doPostSecurityDevicePage(DeviceParam param) {
        Page<SecurityDevice> page = this.page(PagingUtils.convert(param), Wrappers.<SecurityDevice>lambdaQuery()
                .eq(SecurityDevice::getApplicationCode, param.getApplicationCode())
                .like(StringUtils.isNotEmpty(param.getAreaId()), SecurityDevice::getAreaId, param.getAreaId())
                .notIn(CollectionUtils.isNotEmpty(param.getIds()), SecurityDevice::getDeviceId, param.getIds())
                .and(StringUtils.isNotEmpty(param.getKeyword()), wrapper -> wrapper
                        .like(SecurityDevice::getDeviceName, param.getKeyword())
                        .or().like(SecurityDevice::getDeviceProdName, param.getKeyword()))
                .orderByDesc(BasicEntity::getCreateTime));
        Pagination<SecurityDeviceVo> convert = PagingUtils.convert(page, SecurityDeviceVo.class);
        return convert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSecurityDevice(Long id) {
        deleteSecurityDeviceTagBind(id);
    }

    @Override
    @Transactional
    public void deleteSecurityDeviceBatch(List<Long> ids) {
        ids.forEach(s -> {
            deleteSecurityDeviceTagBind(s);
        });
    }

    private void deleteSecurityDeviceTagBind(Long id) {
        SecurityDevice byId = this.getById(id);
        Assert.isNotFound(Objects.isNull(byId), "此设备不存在");
        DatabaseContextHolder.softDelete(this, id);
        Set<Long> businessKeys = new HashSet<>();
        businessKeys.add(byId.getDeviceId());
        //数据同步
        TagBindDeviceCmd tagBindDeviceParam = new TagBindDeviceCmd();
        tagBindDeviceParam.setTagCode(SecurityManagementCommonUtil.applicationConstants + byId.getApplicationCode());
        tagBindDeviceParam.setBusinessKeys(businessKeys);
        deviceCenterTagClient.unbindTag(tagBindDeviceParam);
        securityCommonMapper
                .delCollectionSecurityDeviceInstance(byId.getDeviceId(), byId.getApplicationCode(), DateUtils.currentTimestamp());

    }

    /**
     * 此方法再后续将被替换,后续通过物联中心标签规则同步通过mq发送导入
     * 请参照com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.consumer.DeviceTagRuleChangeMessageConsumer类
     *
     * @param param
     */
    @Deprecated
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSecurityDevice(DeviceExportAppParam param) {
        Integer maxDeviceNumber = 5000;
        Integer insertBatchSize = 500;
        //设备打标签
        ApplicationDTO application = applicationClient.getApplicationByCode(param.getApplicationCode());
        String tag = SecurityManagementCommonUtil.applicationConstants + application.getCode();
        List<DeviceBaseArchivesInfoDTO> listByTag = this.deviceCenterStdClient.getListByTag(Collections.singleton(tag));
        List<Long> binDeviceIds = listByTag.stream().map(DeviceBaseArchivesInfoDTO::getId).collect(Collectors.toList());
        //根据条件获取设备,若存在分页的情况需多次获取打标签
        DeviceFilterPageParam deviceFilterPageParam = ObjectUtils.copying(param, DeviceFilterPageParam.class);
        deviceFilterPageParam.setPageNumber(1);
        deviceFilterPageParam.setPageSize(maxDeviceNumber);
        Pagination<DevicePageInfoVO> devicePageInfoVOPagination = this.deviceCenterStdClient
                .deviceInfoPage(deviceFilterPageParam);
        if (CollectionUtils.isEmpty(devicePageInfoVOPagination.getRecords())) {
            return;
        }
        Integer integer = deviceCenterTagClient.checkTag(tag);
        if (integer.equals(0)) {
            CreateTagCmd createTagCmd = CreateTagCmd.createTopTag(null, tag, application.getName());
            deviceCenterTagClient.createTag(createTagCmd);
        }
        List<DevicePageInfoVO> devicePageInfoVOS = devicePageInfoVOPagination.getRecords();
        for (int statPage = 1; statPage <= devicePageInfoVOPagination.getPages(); statPage++) {
            if (statPage > 1) {
                //再次查询除第一页后的数据
                deviceFilterPageParam.setPageNumber(statPage);
                deviceFilterPageParam.setPageSize(maxDeviceNumber);
                log.info("时间开始{}", System.currentTimeMillis());
                devicePageInfoVOS = this.deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam).getRecords();
                log.info("时间结束{}", System.currentTimeMillis());
            }
            List<SecurityDevice> copying = SecurityDeviceConvert.convert(binDeviceIds, param.getApplicationCode(), devicePageInfoVOS);
            TagBindDeviceCmd tagBindDeviceParam = new TagBindDeviceCmd();
            Set<Long> collect = copying.stream().map(SecurityDevice::getDeviceId).collect(Collectors.toSet());
            tagBindDeviceParam.setBusinessKeys(collect);
            tagBindDeviceParam.setTagCode(tag);
            deviceCenterTagClient.bindTag(tagBindDeviceParam);

            // 分批插入
            for (int i = 0; i < copying.size(); i += insertBatchSize) {
                int end = Math.min(i + insertBatchSize, copying.size());
                List<SecurityDevice> batch = copying.subList(i, end);
                this.securityCommonMapper.insertBatch(batch);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void conditionDeletedSecurityDevice(DeviceDeletedParam param) {
        String tag = SecurityManagementCommonUtil.applicationConstants + param.getApplicationCode();
        List<Long> securityDeviceIds = this.securityCommonMapper.selectConditionDeleteSecurityDeviceIds(param);
        if (CollectionUtils.isEmpty(securityDeviceIds)) {
            return;
        }
        long pages = securityDeviceIds.size() / 1000;
        if (securityDeviceIds.size() % 1000 != 0) {
            pages++;
        }
        TagBindDeviceCmd tagBindDeviceCmd = new TagBindDeviceCmd();
        tagBindDeviceCmd.setTagCode(tag);
        for (int startPage = 0; startPage < pages; startPage++) {
            long skip;
            if (startPage == 0) {
                skip = 0;
            } else {
                skip = startPage * 1000;
            }
            Set<Long> unBindDeviceIds = securityDeviceIds.stream().skip(skip).limit(1000).collect(Collectors.toSet());
            securityCommonMapper
                    .delAppSecurityDeviceInstance(unBindDeviceIds.stream().collect(Collectors.toList()), param.getApplicationCode(), System
                            .currentTimeMillis());
            tagBindDeviceCmd.setBusinessKeys(unBindDeviceIds);
            deviceCenterTagClient.unbindTag(tagBindDeviceCmd);
        }
    }

    @Override
    public List<DeviceProdCategoryVo> getGenreVo(String applicationCode) {
        List<DeviceProdCategoryVo> genreVo = securityCommonMapper.getGenreVo(applicationCode);
        return genreVo;
    }

    @Override
    public List<ModelVo> getModelVo(String applicationCode, String genre) {

        return securityCommonMapper.getModelVo(applicationCode, genre);
    }

    @Override
    public Pagination<SecurityDeviceVo> getDeviceByModel(ModelUnderDeviceParam param) {
        if (CollectionUtils.isNotEmpty(param.getAreaIds())) {
            String join = String.join("-", param.getAreaIds());
            param.setAreaId(join);
        }
        Page<SecurityDevice> page = this.page(PagingUtils.convert(param), Wrappers.<SecurityDevice>lambdaQuery()
                .eq(SecurityDevice::getApplicationCode, param.getApplicationCode())
                .in(CollectionUtils.isNotEmpty(param.getDeviceProdIds()), SecurityDevice::getDeviceProdId, param
                        .getDeviceProdIds())
                .like(StringUtils.isNotEmpty(param.getKeyword()), SecurityDevice::getDeviceName, param.getKeyword())
                .like(StringUtils.isNotEmpty(param.getAreaId()), SecurityDevice::getAreaId, param.getAreaId()));
        Pagination<SecurityDeviceVo> convert = PagingUtils.convert(page, SecurityDeviceVo.class);
        return convert;
    }

    @Override
    public List<SecurityDeviceVo> getDeviceListByModel(ModelUnderDeviceParam param) {
        if (CollectionUtils.isNotEmpty(param.getAreaIds())) {
            String join = String.join("-", param.getAreaIds());
            param.setAreaId(join);
        }
        List<SecurityDevice> list = this.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                      .eq(SecurityDevice::getApplicationCode, param.getApplicationCode())
                                                      .in(CollectionUtils
                                                                  .isNotEmpty(param.getDeviceProdIds()), SecurityDevice::getDeviceProdId, param
                                                                  .getDeviceProdIds())
                                                      .in(CollectionUtils.isNotEmpty(param.getModelIds()), SecurityDevice::getDeviceId, param.getModelIds())
                                                      .like(StringUtils
                                                                    .isNotEmpty(param.getKeyword()), SecurityDevice::getDeviceName, param
                                                                    .getKeyword())
                                                      .like(StringUtils
                                                                    .isNotEmpty(param.getAreaId()), SecurityDevice::getAreaId, param
                                                                    .getAreaId()));
        List<SecurityDeviceVo> copying = ObjectUtils.copying(list, SecurityDeviceVo.class);

        return copying;
    }

    @Override
    public List<SecurityDeviceOutVo> getSecurityDeviceList(String applicationCode, DeviceParam param) {
        List<SecurityDevice> list = this.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                      .eq(SecurityDevice::getApplicationCode, applicationCode)
                                                      .eq(StringUtils
                                                                  .isNotEmpty(param.getKeyword()), SecurityDevice::getDeviceName, param
                                                                  .getKeyword()));
        List<SecurityDeviceOutVo> copying = ObjectUtils.copying(list, SecurityDeviceOutVo.class);

        return copying;
    }

    @Override
    public List<Long> getSecurityDeviceIds(String applicationCode) {
        return this.baseMapper.selectDeviceIds(applicationCode);
    }

    @Override
    public List<SecurityDeviceApplicationCodeStatisticsVO> getApplicationDeviceStatistics() {
        List<SecurityDeviceApplicationCodeStatisticsVO> statistics =
                securityCommonMapper.selectSecurityDeviceApplicationStatistics();
        if (CollectionUtils.isEmpty(statistics)) {
            return Collections.emptyList();
        }
        Map<String, String> applicationNames = getApplicationNames(statistics.stream()
                .map(SecurityDeviceApplicationCodeStatisticsVO::getApplicationCode)
                .collect(Collectors.toList()));
        statistics.forEach(item ->
                item.setApplicationName(applicationNames.get(normalizeApplicationCode(item.getApplicationCode()))));
        statistics.sort(Comparator.comparing(SecurityDeviceApplicationCodeStatisticsVO::getNum).reversed());
        return statistics;
    }

    @Override
    public List<SecurityDeviceApplicationStatusStatisticsVO> getApplicationDeviceStatusStatistics() {
        List<SecurityDeviceApplicationStatusStatisticsVO> statistics =
                securityCommonMapper.selectSecurityDeviceApplicationStatusStatistics();
        if (CollectionUtils.isEmpty(statistics)) {
            return Collections.emptyList();
        }
        Map<String, String> applicationNames = getApplicationNames(statistics.stream()
                .map(SecurityDeviceApplicationStatusStatisticsVO::getApplicationCode)
                .collect(Collectors.toList()));
        statistics.forEach(item ->
                item.setApplicationName(applicationNames.get(normalizeApplicationCode(item.getApplicationCode()))));
        statistics.sort(Comparator.comparingInt((SecurityDeviceApplicationStatusStatisticsVO item) ->
                item.getOnlineNum() + item.getOfflineNum()).reversed());
        return statistics;
    }

    private Map<String, String> getApplicationNames(List<String> applicationCodes) {
        List<String> normalizedCodes = applicationCodes.stream().filter(Objects::nonNull)
                .map(this::normalizeApplicationCode).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(normalizedCodes)) {
            return Collections.emptyMap();
        }
        List<ApplicationDTO> applications = applicationClient.getApplicationsByCodes(normalizedCodes);
        if (CollectionUtils.isEmpty(applications)) {
            return Collections.emptyMap();
        }
        Map<String, String> applicationNames = new HashMap<>();
        applications.forEach(application -> applicationNames.put(application.getCode(), application.getName()));
        return applicationNames;
    }

    private String normalizeApplicationCode(String applicationCode) {
        if (applicationCode.startsWith(SecurityManagementCommonUtil.applicationConstants)) {
            return applicationCode.substring(SecurityManagementCommonUtil.applicationConstants.length());
        }
        return applicationCode;
    }

}

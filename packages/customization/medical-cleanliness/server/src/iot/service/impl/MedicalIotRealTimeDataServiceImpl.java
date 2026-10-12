package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao.MedicalIotRealTimeDataDetailsMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao.MedicalIotRealTimeDataMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeData;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeDataDetails;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.MonitorRecordStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalIotRealTimeDataParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalIotRealTimeDataService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalIotRealTimeDataVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.RealTimeDataAbnormalPageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.StatusStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-21  17:37
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIotRealTimeDataServiceImpl extends ServiceImpl<MedicalIotRealTimeDataMapper, MedicalIotRealTimeData> implements MedicalIotRealTimeDataService {
    private final MedicalIotRealTimeDataDetailsMapper medicalIotRealTimeDataDetailsMapper;
    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final SecurityDeviceClient securityDeviceClient;
    private static final String APP_CODE = "OPERATING_ROOM";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(MedicalIotRealTimeData param) {
        MedicalIotRealTimeData medicalIotRealTimeData = super.getOne(new LambdaQueryWrapper<MedicalIotRealTimeData>()
                                                                             .eq(MedicalIotRealTimeData::getDeviceId, param.getDeviceId()));
        if (ObjectUtils.isEmpty(medicalIotRealTimeData)) {
            return super.save(param);
        }
        param.setDeleted(false);
        param.setId(medicalIotRealTimeData.getId());
        param.setCreateTime(medicalIotRealTimeData.getCreateTime());
        return this.updateById(param);
    }

    @Override
    public Pagination<MedicalIotRealTimeDataVO> getRealTimeDataPage(MedicalIotRealTimeDataParam param) {
        Set<Long> organizationIds = null;
        if (ObjectUtils.isEmpty(param.getDepId())) {
            Long userId = SecurityContextHolder.getPrincipal().getId();
            UserDTO user = userClient.getUser(userId);
            if (!user.getAdmin()) {
                if (ObjectUtils.isEmpty(user.getOrganization())) {
                    return Pagination.empty();
                }
                Set<Long> ids = new HashSet<>();
                ids.add(user.getOrganization().getId());
                OrganizationQueryParam organizationQueryParam = new OrganizationQueryParam();
                organizationQueryParam.setIds(ids);
                List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(organizationQueryParam);
                organizationIds = organizationDTOList.stream().map(OrganizationDTO::getId).collect(Collectors.toSet());
            }
        }
        Page<MedicalIotRealTimeDataVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        List<Long> deviceIds = securityDeviceClient.getSecurityDeviceIds(APP_CODE);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Pagination.empty();
        }
        param.setDeviceIds(new HashSet<>(deviceIds));
        page = super.baseMapper.getRealTimeDataPage(page, param, organizationIds);
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(MedicalIotRealTimeDataVO::getId).collect(java.util.stream.Collectors.toList());
            List<MedicalIotRealTimeDataVO> records = super.baseMapper.getRealTimeDataNorm(param, ids, Collections.emptyList());
            page.setRecords(records);
        }
        return PagingUtils.convert(page);
    }

    @Override
    public StatusStatisticsVO statusStatistics() {
        StatusStatisticsVO statusStatisticsVO = StatusStatisticsVO.builder()
                .total(0L)
                .abnormalTotal(0L)
                .normalTotal(0L)
                .invalidTotal(0L)
                .build();
        Set<Long> organizationIds = null;
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = userClient.getUser(userId);
        if (!user.getAdmin()) {
            if (ObjectUtils.isEmpty(user.getOrganization())) {
                return statusStatisticsVO;
            }
            Set<Long> ids = new HashSet<>();
            ids.add(user.getOrganization().getId());
            OrganizationQueryParam organizationQueryParam = new OrganizationQueryParam();
            organizationQueryParam.setIds(ids);
            List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(organizationQueryParam);
            organizationIds = organizationDTOList.stream().map(OrganizationDTO::getId).collect(Collectors.toSet());
        }
        List<Long> deviceIds = securityDeviceClient.getSecurityDeviceIds(APP_CODE);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return statusStatisticsVO;
        }
        List<Map<String, Long>> data = super.baseMapper.statusStatistics(organizationIds, deviceIds);
        if (ObjectUtils.isNotEmpty(data)) {
            data.forEach(ex -> {
                if (MonitorRecordStatusEnum.NORMAL.name().equals(ex.get("status"))) {
                    statusStatisticsVO.setNormalTotal(ex.get("value"));
                } else if (MonitorRecordStatusEnum.ABNORMAL.name().equals(ex.get("status"))) {
                    statusStatisticsVO.setAbnormalTotal(ex.get("value"));
                } else if (MonitorRecordStatusEnum.INVALID.name().equals(ex.get("status"))) {
                    statusStatisticsVO.setInvalidTotal(ex.get("value"));
                }
            });
            statusStatisticsVO.setTotal(
                    statusStatisticsVO.getAbnormalTotal() + statusStatisticsVO.getNormalTotal() + statusStatisticsVO.getInvalidTotal());
        }
        return statusStatisticsVO;
    }

    @Override
    public Pagination<RealTimeDataAbnormalPageVO> getRealTimeDataAbnormalPage(Paging paging) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        MedicalIotRealTimeDataParam param = ConvertUtils.convert(MedicalIotRealTimeDataParam.class, paging);
        param.setTenant(tenant);
        param.setStatus(MonitorRecordStatusEnum.ABNORMAL.name());
        Page<MedicalIotRealTimeDataVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Set<Long> organizationIds = null;
        if (ObjectUtils.isEmpty(param.getDepId())) {
            Long userId = SecurityContextHolder.getPrincipal().getId();
            UserDTO user = userClient.getUser(userId);
            if (!user.getAdmin()) {
                if (ObjectUtils.isEmpty(user.getOrganization())) {
                    return Pagination.empty();
                }
                Set<Long> ids = new HashSet<>();
                ids.add(user.getOrganization().getId());
                OrganizationQueryParam organizationQueryParam = new OrganizationQueryParam();
                organizationQueryParam.setIds(ids);
                List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(organizationQueryParam);
                organizationIds = organizationDTOList.stream().map(OrganizationDTO::getId).collect(Collectors.toSet());
            }
        }
        List<Long> deviceIds = securityDeviceClient.getSecurityDeviceIds(APP_CODE);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Pagination.empty();
        }
        param.setDeviceIds(new HashSet<>(deviceIds));
        page = super.baseMapper.getRealTimeDataPage(page, param, organizationIds);
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(MedicalIotRealTimeDataVO::getId).collect(java.util.stream.Collectors.toList());
            List<MedicalIotRealTimeDataVO> records = super.baseMapper.getRealTimeDataNorm(param, ids, deviceIds);
            page.setRecords(records);
        }
        List<RealTimeDataAbnormalPageVO> list = new ArrayList<>();
        page.getRecords().forEach(ex -> {
            RealTimeDataAbnormalPageVO realTimeDataAbnormalPageVO = new RealTimeDataAbnormalPageVO();
            realTimeDataAbnormalPageVO.setId(ex.getId());
            realTimeDataAbnormalPageVO.setDeviceId(ex.getDeviceId());
            realTimeDataAbnormalPageVO.setDeviceName(ex.getDeviceName());
            realTimeDataAbnormalPageVO.setValue(ex.getData().size());
            list.add(realTimeDataAbnormalPageVO);
        });
        Pagination<RealTimeDataAbnormalPageVO> pagination = new Pagination<>(page.getTotal(), Long.parseLong(paging.getPageNumber()
                                                                                                                     .toString()), list);
        return pagination;
    }

    @Override
    public MedicalIotRealTimeDataVO getRealTimeDataDetails(Long id) {
        MedicalIotRealTimeData realTimeData = super.baseMapper.selectById(id);
        Assert.parameterIsNull(realTimeData, "数据不存在");
        MedicalIotRealTimeDataVO realTimeDataVO = ConvertUtils.convert(MedicalIotRealTimeDataVO.class, realTimeData);
        realTimeDataVO.setDeviceStatus(realTimeData.getStatus());
        List<MedicalIotRealTimeDataDetails> timeDataDetailsList = medicalIotRealTimeDataDetailsMapper.selectList(new LambdaQueryWrapper<MedicalIotRealTimeDataDetails>()
                                                                                                                         .eq(MedicalIotRealTimeDataDetails::getDeviceId, realTimeData.getDeviceId()));
        realTimeDataVO.setData(timeDataDetailsList);
        return realTimeDataVO;
    }
}

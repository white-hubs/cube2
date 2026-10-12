package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.impl;

import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceSamplingTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceSamplingPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.config.ClickHouseConfigProperties;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao.MedicalDeviceHistoryStatisticsMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao.MedicalDeviceStatisticsMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao.MedicalOverrunAlarmRecordMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeData;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeDataDetails;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalNormRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalOverrunAlarmRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.MonitorRecordStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.RuleNormStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.RuleNormTypeEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalDeviceHistoryStatisticsParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalOverrunAlarmRecordParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalIotRealTimeDataDetailsService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalIotRealTimeDataService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalOverrunRecordService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.CorrectNormConfigVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.DeviceListVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalDeviceHistoryStatisticsVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalOverrunAlarmRecordVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalRecordStatisticsVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.RuleNormService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-10  10:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalOverrunRecordServiceImpl implements MedicalOverrunRecordService {
    private JdbcTemplate jdbcTemplate;
    private final IotDevicePropClient iotDevicePropClient;
    private final ClickHouseConfigProperties clickHouseConfigProperties;
    private final TenantClient tenantClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final RuleNormService ruleNormService;
    private final MedicalRuleMapper medicalRuleMapper;
    private final MedicalIotRealTimeDataService medicalIotRealTimeDataService;
    private final IotProductClient iotProductClient;
    private final MedicalIotRealTimeDataDetailsService medicalIotRealTimeDataDetailsService;
    private final MedicalRuleService medicalRuleService;
    private final DataDictionaryAdapter dataDictionaryAdapter;
    private final static String EFFECTIVE = "effective";//是否有效属性code
    private final static String AIRCONDITIONER = "airconditioner";//空调开关属性code
    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final DataDictionaryClient dataDictionaryClient;

    public JdbcTemplate getJdbcTemplate() {
        if (null != jdbcTemplate) {
            return jdbcTemplate;
        }
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(clickHouseConfigProperties.getJdbcUrl());
        hikariDataSource.setDriverClassName(clickHouseConfigProperties.getDriverClassName());
        if (StringUtils.isNotEmpty(clickHouseConfigProperties.getUsername())) {
            hikariDataSource.setUsername(clickHouseConfigProperties.getUsername());
            hikariDataSource.setPassword(clickHouseConfigProperties.getPassword());
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
        this.jdbcTemplate = jdbcTemplate;
        return jdbcTemplate;
    }

    /**
     * 采集设备指标数据
     */
    public void medicalOverrunAlarm(LocalDateTime date) {
        this.getJdbcTemplate();
        List<TenantDTO> tenants = tenantClient.listTenants("");
        for (TenantDTO tenant : tenants) {
            SessionContextHolder.requestTenant(tenant.getCode());
            List<RuleVO> ruleVOList = medicalRuleService.queryByAll();
            if (ObjectUtils.isEmpty(ruleVOList)) {
                continue;
            }
            List<Long> deviceIds = new ArrayList<>();
            ruleVOList.forEach(rule -> {
                if (ObjectUtils.isNotEmpty(rule.getDeviceIds())) {
                    String[] ids = rule.getDeviceIds().split(",");
                    for (String id : ids) {
                        deviceIds.add(Long.parseLong(id));
                    }
                }
            });
            if (ObjectUtils.isEmpty(deviceIds)) {
                continue;
            }
            Integer total = 999;
            DeviceFilterPageParam deviceFilterPageParam = new DeviceFilterPageParam();
            deviceFilterPageParam.setPageSize(999);
            deviceFilterPageParam.setPageNumber(1);
            deviceFilterPageParam.setMatchedDeviceIds(deviceIds);
            Pagination<DevicePageInfoVO> deviceVoPagination = null;
            do {
                total = deviceFilterPageParam.getPageNumber() * deviceFilterPageParam.getPageSize();
                deviceVoPagination = deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam);
                this.saveHistory(deviceVoPagination.getRecords(), date, tenant);
                deviceFilterPageParam.setPageNumber(deviceFilterPageParam.getPageNumber() + 1);
            } while (deviceVoPagination.getTotal() > total);
        }
    }

    /**
     * 采集实时数据
     */
    public void medicalCollectRealTimeData() {
        List<TenantDTO> tenants = tenantClient.listTenants("");
        for (TenantDTO tenant : tenants) {
            SessionContextHolder.requestTenant(tenant.getCode());
            List<RuleVO> ruleVOList = medicalRuleService.queryByAll();
            if (ObjectUtils.isEmpty(ruleVOList)) {
                continue;
            }
            List<Long> deviceIds = new ArrayList<>();
            ruleVOList.forEach(rule -> {
                if (ObjectUtils.isNotEmpty(rule.getDeviceIds())) {
                    String[] ids = rule.getDeviceIds().split(",");
                    for (String id : ids) {
                        deviceIds.add(Long.parseLong(id));
                    }
                }
            });
            if (ObjectUtils.isEmpty(deviceIds)) {
                continue;
            }
            Integer total = 999;
            DeviceFilterPageParam deviceFilterPageParam = new DeviceFilterPageParam();
            deviceFilterPageParam.setPageSize(999);
            deviceFilterPageParam.setPageNumber(1);
            deviceFilterPageParam.setMatchedDeviceIds(deviceIds);
            Pagination<DevicePageInfoVO> deviceVoPagination = null;
            do {
                total = deviceFilterPageParam.getPageNumber() * deviceFilterPageParam.getPageSize();
                deviceVoPagination = deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam);
                this.saveReal(deviceVoPagination.getRecords(), tenant);
                deviceFilterPageParam.setPageNumber(deviceFilterPageParam.getPageNumber() + 1);
            } while (deviceVoPagination.getTotal() > total);
            log.info("{}：采集实时数据结束", tenant);
        }
    }

    @Override
    public Pagination<MedicalOverrunAlarmRecordVO> medicalOverrunAlarmPage(MedicalOverrunAlarmRecordParam param) {

        var startLine = param.getPageNumber() * param.getPageSize() - param.getPageSize();
        String sql = "";
        if (ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getBegin())) {
            sql += " where monitoring_time between '" + DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd mm:hh:ss") + "' and '" +
                   DateUtils.format(param.getPeriod().getEnd(), "yyyy-MM-dd mm:hh:ss") + "'";
        }
        if (ObjectUtils.isNotEmpty(param.getTenant())) {
            sql += sql.equals("") ? " where " : " and ";
            sql += "tenant=" + param.getTenant();
        }
        if (ObjectUtils.isNotEmpty(param.getDepId())) {
            sql += sql.equals("") ? " where " : " and ";
            sql += " dep_id=" + param.getDepId();
        }
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            sql += sql.equals("") ? " where " : " and ";
            sql += " device_id in (";
            for (int i = 0; i < param.getDeviceIds().size(); i++) {
                sql += param.getDeviceIds().get(i);
                if (param.getDeviceIds().size() - 1 != i) {
                    sql += ",";
                }
            }
            sql += " )";
        }
        if (ObjectUtils.isNotEmpty(param.getDeviceName())) {
            sql += sql.equals("") ? " where " : " and ";
            sql += " device_name like '%" + param.getDeviceName() + "%'";
        }
        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            sql += sql.equals("") ? " where " : " and ";
            sql += " status= '" + param.getStatus() + "'";
        }
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
                if (ObjectUtils.isNotEmpty(organizationDTOList)) {
                    sql += sql.equals("") ? " where " : " and ";
                    sql += " dep_id in (";
                    for (int i = 0; i < organizationDTOList.size(); i++) {
                        OrganizationDTO organizationDTO = organizationDTOList.get(i);
                        if (i > 0) {
                            sql += ",";
                        }
                        sql += organizationDTO.getId();
                    }
                    sql += ")";
                }
            }
        }

        Long total = this.getJdbcTemplate()
                .queryForObject("select ifNull(count(1),0) from medical_iot.monitoring_records" + sql, Long.class);
        if (ObjectUtils.isEmpty(total) || total < 1) {
            return Pagination.empty();
        }
        sql += " limit ";
        sql += startLine;
        sql += ",";
        sql += param.getPageSize();
        List<MedicalOverrunAlarmRecord> query = this.getJdbcTemplate()
                .query("select id,monitoring_time,dep_id,status,tenant,device_id,record_data,dep_name,device_name,tenant_name,prod_id from medical_iot.monitoring_records " +
                       sql, new MedicalOverrunAlarmRecordMapper());
        List<MedicalOverrunAlarmRecordVO> data = new ArrayList<>();
        query.forEach(record -> {
            MedicalOverrunAlarmRecordVO alarmRecordVO = ConvertUtils.convert(MedicalOverrunAlarmRecordVO.class, record);
            alarmRecordVO.setRecordData(JsonUtils.deserialize(record.getRecordData(), List.class));
            data.add(alarmRecordVO);
        });
        Pagination<MedicalOverrunAlarmRecordVO> pagination = new Pagination<>(total, (long) startLine, data);

        List<String> prodIds = this.getJdbcTemplate()
                .queryForList("select prod_id from medical_iot.monitoring_records group by prod_id", String.class);
        if (ObjectUtils.isEmpty(prodIds)) {
            return pagination;
        }
        List<RuleNormVO> ruleNormVOList = medicalRuleMapper.getAllRule(1, null, null);
        List<ProductDTO> productDTOList = iotProductClient.prodListDetail(prodIds);
        Map<String, ProductDTO> ruleNormVOMap = productDTOList.stream()
                .collect(Collectors.toMap(ProductDTO::getProdId, ProductDTO -> ProductDTO));
        for (RuleNormVO ex : ruleNormVOList) {
            ProductDTO productDTO = ruleNormVOMap.get(ex.getProdId());
            if (ObjectUtils.isNotEmpty(productDTO)) {
                List<ProductPropDTO> propList = productDTO.getProductPropVoList();
                for (ProductPropDTO propDTO : propList) {
                    ex.setNormName(propDTO.getPropName());
                    ex.setUnit(propDTO.getUnitName());
                }
            }
        }
        return ObjectUtils.isEmpty(pagination) ? Pagination.empty() : pagination;
    }

    @Override
    public Boolean deleteRecord(List<Long> ids) {
        String sql = "ALTER TABLE medical_iot.monitoring_records DELETE WHERE id in (";
        for (Long id : ids) {
            sql += id + ",";
        }
        sql += ")";
        int i = this.getJdbcTemplate().update(sql);
        return i > 0;
    }

    @Override
    public Pagination<MedicalRecordStatisticsVO> medicalDeviceStatistics() {
        String whereSql = "";
        Set<Long> organizationIds = null;
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
        if (ObjectUtils.isNotEmpty(organizationIds)) {
            whereSql += whereSql.equals("") ? " where " : " and ";
            whereSql += " dep_id in (";
            for (int i = 0; i < organizationIds.size(); i++) {
                if (i > 0) {
                    whereSql += ",";
                }
                whereSql += organizationIds;
            }
            whereSql += ")";
        }
        String sql =
                "select toDate(monitoring_time) as monitoring_time,status,device_id,device_name,count(1) as number from medical_iot.monitoring_records " +
                whereSql +
                " group by device_id,device_name,monitoring_time,status ";
        List<MedicalOverrunAlarmRecord> data = this.getJdbcTemplate().query(sql, new MedicalDeviceStatisticsMapper());
        List<MedicalRecordStatisticsVO> recordData = new ArrayList<>();
        Map<Long, MedicalRecordStatisticsVO> recordMap = new HashMap<>();
        data.forEach(ex -> {
            MedicalRecordStatisticsVO recordVO = recordMap.get(ex.getDeviceId());
            if (ObjectUtils.isEmpty(recordVO)) {
                recordVO = MedicalRecordStatisticsVO.builder()
                        .deviceId(ex.getDeviceId())
                        .deviceName(ex.getDeviceName())
                        .monitoringTime(ex.getMonitoringTime())
                        .total(1)
                        .normalTotal(MonitorRecordStatusEnum.NORMAL.name().equals(ex.getStatus()) ? 1 : 0)
                        .abnormalTotal(MonitorRecordStatusEnum.ABNORMAL.name().equals(ex.getStatus()) ? 1 : 0)
                        .invalidTotal(MonitorRecordStatusEnum.INVALID.name().equals(ex.getStatus()) ? 1 : 0)
                        .build();
            } else {
                recordVO.setTotal(recordVO.getTotal() + 1);
                if (MonitorRecordStatusEnum.NORMAL.name().equals(ex.getStatus())) {
                    recordVO.setTotal(recordVO.getTotal() + 1);
                } else if (MonitorRecordStatusEnum.ABNORMAL.name().equals(ex.getStatus())) {
                    recordVO.setAbnormalTotal(recordVO.getAbnormalTotal() + 1);
                }
                if (MonitorRecordStatusEnum.INVALID.name().equals(ex.getStatus())) {
                    recordVO.setInvalidTotal(recordVO.getInvalidTotal() + 1);
                }
            }
            recordMap.put(ex.getDeviceId(), recordVO);
        });
        recordData.addAll(recordMap.values());
        Pagination<MedicalRecordStatisticsVO> pagination = new Pagination<>(1L, 1L, recordData);
        return pagination;
    }

    @Override
    public Pagination<MedicalDeviceHistoryStatisticsVO> deviceHistoryStatistics(MedicalDeviceHistoryStatisticsParam param) {
        String sqlWhere = "";

        if (ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getBegin()) &&
            ObjectUtils.isNotEmpty(param.getPeriod().getEnd())) {
            sqlWhere =
                    " (select id,monitoring_time,dep_id,status,tenant,device_id,record_data,dep_name,device_name,tenant_name,prod_id from medical_iot.monitoring_records where monitoring_time between '" +
                    DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd mm:hh:ss") + "' and '" +
                    DateUtils.format(param.getPeriod().getEnd(), "yyyy-MM-dd mm:hh:ss") + "'";
        }
        if (ObjectUtils.isNotEmpty(param.getDepIds())) {
            sqlWhere += sqlWhere.equals("")
                        ? " (select id,monitoring_time,dep_id,status,tenant,device_id,record_data,dep_name,device_name,tenant_name,prod_id from medical_iot.monitoring_records where  "
                        : " and ";
            sqlWhere += " device_id in (";
            for (int i = 0; i < param.getDepIds().size(); i++) {
                sqlWhere += param.getDepIds().get(i);
                if (param.getDepIds().size() - 1 != i) {
                    sqlWhere += ",";
                }
            }
            sqlWhere += " )";
        }
        if (ObjectUtils.isNotEmpty(param.getDeviceName())) {
            sqlWhere += sqlWhere.equals("")
                        ? " (select id,monitoring_time,dep_id,status,tenant,device_id,record_data,dep_name,device_name,tenant_name,prod_id from medical_iot.monitoring_records where  "
                        : " and ";
            sqlWhere += " device_name like '%" + param.getDeviceName() + "%'";
        }
        sqlWhere += ObjectUtils.isNotEmpty(sqlWhere) ? ")" : "medical_iot.monitoring_records";
        Long total = this.getJdbcTemplate().queryForObject(
                "select count(1) from (select formatDateTime(monitoring_time, '%Y-%m-%d') as monitoring_time, device_id,device_name " +
                "from " + sqlWhere +
                "    group by monitoring_time,device_id,device_name" +
                ") a", Long.class);
        if (ObjectUtils.isEmpty(total) || total < 1) {
            return Pagination.empty();
        }
        Integer startLine = param.getPageNumber() * param.getPageSize() - param.getPageSize();
        String sql =
                "select formatDateTime(monitoring_time, '%Y-%m-%d') as monitoring_time, device_id,device_name,status,count(1) as number " +
                "from " + sqlWhere +
                " group by monitoring_time,device_id,device_name,status" +
                " limit " + startLine + "," + param.getPageSize();
        List<MedicalDeviceHistoryStatisticsVO> data = this.getJdbcTemplate().query(sql, new MedicalDeviceHistoryStatisticsMapper());
        List<MedicalDeviceHistoryStatisticsVO> recordData = new ArrayList<>();
        Map<String, MedicalDeviceHistoryStatisticsVO> recordMap = new HashMap<>();
        data.forEach(ex -> {
            MedicalDeviceHistoryStatisticsVO recordVO = recordMap.get(ex.getDeviceId() + ex.getMonitoringTime());
            if (ObjectUtils.isEmpty(recordVO)) {
                recordVO = new MedicalDeviceHistoryStatisticsVO();
                recordVO.setDeviceId(ex.getDeviceId());
                recordVO.setDeviceName(ex.getDeviceName());
                recordVO.setMonitoringTime(ex.getMonitoringTime());
                recordVO.setTotal(1);
                recordVO.setNormalTotal(MonitorRecordStatusEnum.NORMAL.name().equals(ex.getStatus()) ? 1 : 0);
                recordVO.setAbnormalTotal(MonitorRecordStatusEnum.ABNORMAL.name().equals(ex.getStatus()) ? 1 : 0);
                recordVO.setInvalidTotal(MonitorRecordStatusEnum.INVALID.name().equals(ex.getStatus()) ? 1 : 0);
            } else {
                recordVO.setTotal(recordVO.getTotal() + 1);
                if (MonitorRecordStatusEnum.NORMAL.name().equals(ex.getStatus())) {
                    recordVO.setTotal(recordVO.getTotal() + 1);
                } else if (MonitorRecordStatusEnum.ABNORMAL.name().equals(ex.getStatus())) {
                    recordVO.setAbnormalTotal(recordVO.getAbnormalTotal() + 1);
                }
                if (MonitorRecordStatusEnum.INVALID.name().equals(ex.getStatus())) {
                    recordVO.setInvalidTotal(recordVO.getInvalidTotal() + 1);
                }
            }
            recordMap.put(ex.getDeviceId() + ex.getMonitoringTime(), recordVO);
        });
        recordData.addAll(recordMap.values());
        Pagination<MedicalDeviceHistoryStatisticsVO> pagination = new Pagination<>(total, Long.parseLong(param.getPageNumber()
                                                                                                                 .toString()), recordData);
        return pagination;
    }

    /**
     * 保存历史数据
     */
    private void saveHistory(List<DevicePageInfoVO> record, LocalDateTime date, TenantDTO tenant) {
        record.forEach(device -> {
            try {
                DeviceSamplingPageParam deviceSamplingPageParam = new DeviceSamplingPageParam();
                deviceSamplingPageParam.setDeviceId(device.getId());
                deviceSamplingPageParam.setStartTs(DateUtils.toTimestamp(DateUtils.beginOfDay(date)));
                deviceSamplingPageParam.setEndTs(DateUtils.toTimestamp(DateUtils.endOfDay(date)));
                Pagination<DeviceSamplingTSDTO> data = iotDevicePropClient.deviceHistorySamplingPage(deviceSamplingPageParam);
                data.getRecords().forEach(deviceSamplingTSDTO -> {
                    Set<String> keys = deviceSamplingTSDTO.getPropValues().keySet();
                    List<MedicalNormRecord> recordList = new ArrayList<>();
                    for (String key : keys) {
                        KeyValueWithTsDTO withTsDTO = deviceSamplingTSDTO.getPropValues().get(key);
                        RuleNormVO ruleNormVO = ruleNormService.queryByCode(key);
                        MedicalNormRecord normRecord = MedicalNormRecord.builder()
                                .normCode(key)
                                .normName(ruleNormVO.getNormName())
                                .unit(ruleNormVO.getUnit())
                                .value(withTsDTO.getValue().toString())
                                .build();
                        this.correctNorm(normRecord, device.getId(), date);
                        recordList.add(normRecord);
                    }
                    Map<String, String> ruleNormMap = ruleNormService.getNormStatus(recordList, tenant.getCode(), device.getId());
                    String sql = "insert into medical_iot.monitoring_records(id,device_id,device_name,tenant,tenant_name,dep_id,dep_name,monitoring_time,record_data,status,prod_id) values (?,?,?,?,?,?,?,?,?,?,?)";
                    int i = jdbcTemplate.update(sql, (ps) -> {
                        ps.setLong(1, IdUtils.next());
                        ps.setLong(2, device.getId());
                        ps.setNString(3, device.getName());
                        ps.setLong(4, tenant.getCode());
                        ps.setNString(5, tenant.getName());
                        ps.setLong(6, Optional.ofNullable(device.getDepartment()).orElse(new IdToName()).getId());
                        ps.setNString(7, Optional.ofNullable(device.getDepartment()).orElse(new IdToName()).getName());
                        ps.setTimestamp(8, new java.sql.Timestamp(deviceSamplingTSDTO.getTs()));
                        ps.setNString(9, ruleNormMap.get("valus"));
                        ps.setNString(10, ruleNormMap.get("status"));
                        ps.setNString(11, device.getProductInfo().getId().toString());
                    });
                    log.info("{}：洁净医疗历史数据采集成功", tenant);
                });
            } catch (Exception e) {
                log.error("{}：洁净医疗历史数据采集失败", tenant.getCode(), e);
            }
        });
    }

    /**
     * 保存实时数据
     */
    private void saveReal(List<DevicePageInfoVO> record, TenantDTO tenant) {
        DataDictionaryaramParam dictionaryaramParam = new DataDictionaryaramParam();
        dictionaryaramParam.setItemCode("OPERATING_ROOM_ADMIN_SPECIAL_NORM_SET");
        List<DataDictionaryDTO> dictionaryList = dataDictionaryClient.getDataDictionaryByItem(dictionaryaramParam);
        record.forEach(device -> {
            try {
                MedicalIotRealTimeData param = new MedicalIotRealTimeData();
                IdToName department = Optional.ofNullable(device.getDepartment()).orElse(new IdToName());
                param.setId(IdUtils.next());
                param.setDeviceId(device.getId());
                param.setDeviceName(device.getName());
                param.setTenant(tenant.getCode());
                param.setTenantName(tenant.getName());
                param.setDepId(department.getId());
                param.setDepName(department.getName());
                param.setStatus(MonitorRecordStatusEnum.NORMAL.name());
                param.setNormType(RuleNormTypeEnum.NORMAL.name());
                MultiDevicePropParam deviceSamplingPageParam = new MultiDevicePropParam();
                deviceSamplingPageParam.setDevIdAndPropCodes(device.getId().toString());
                Map<String, List<KeyValueWithTsDTO>> data = iotDevicePropClient.multiProp(deviceSamplingPageParam);
                List<MedicalIotRealTimeDataDetails> dataDetailsList = new ArrayList<>();
                data.keySet().forEach(withTsDTO -> {
                    List<KeyValueWithTsDTO> valueData = data.get(withTsDTO);
                    if (CollectionUtils.isEmpty(valueData)) {
                        return;
                    }
                    //空调属性值
                    Optional<KeyValueWithTsDTO> airconditionerOpt = valueData.stream()
                            .filter(KeyValue -> AIRCONDITIONER.equals(KeyValue.getKey()))
                            .findFirst();
                    valueData.forEach(value -> {
                        if (EFFECTIVE.equals(value.getKey()) && "0".equals(value.getValue().toString())) {
                            param.setStatus(MonitorRecordStatusEnum.INVALID.name());
                            medicalIotRealTimeDataDetailsService.deleteNorm(device.getId());
                            return;
                        }
                        MedicalIotRealTimeDataDetails normRecord = new MedicalIotRealTimeDataDetails();
                        normRecord.setId(IdUtils.next());
                        normRecord.setDeviceId(device.getId());
                        normRecord.setDeviceName(device.getName());
                        normRecord.setValue(new BigDecimal(value.getValue().toString()));
                        normRecord.setRuleNormCode(value.getKey());
                        this.correctNorm(normRecord);
                        medicalIotRealTimeDataDetailsService.getNormStatus(normRecord, dictionaryList);
                        boolean airConditionerState =
                                !airconditionerOpt.isPresent() || "1".equals(airconditionerOpt.get().getValue().toString());
                        if (airConditionerState && MonitorRecordStatusEnum.NORMAL.name().equals(param.getStatus()) &&
                            !RuleNormStatusEnum.NORMAL.name().equals(normRecord.getStatus())) {
                            param.setStatus(MonitorRecordStatusEnum.ABNORMAL.name());
                        }
                        if (RuleNormTypeEnum.NORMAL.name().equals(param.getNormType()) &&
                            RuleNormTypeEnum.SPECIAL.name().equals(normRecord.getType())) {
                            param.setNormType(RuleNormTypeEnum.SPECIAL.name());
                        }
                        dataDetailsList.add(normRecord);
                    });
                });
                medicalIotRealTimeDataService.saveOrUpdate(param);
                medicalIotRealTimeDataDetailsService.saveBatch(dataDetailsList);
            } catch (Exception e) {
                log.error("{}：采集实时数据异常", tenant.getCode(), e);
            }
        });
    }

    /**
     * 修正指标,采集的指标需要加、减修正指标的值
     */
    public void correctNorm(MedicalIotRealTimeDataDetails normRecord) {
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode(DictionaryItemEnum.OPERATING_ROOM_ADMIN_CORRECT_NORM_SET.name());
        List<DataDictionaryDTO> dataDictionary = dataDictionaryAdapter.getDataDictionaryByItem(dataDictionaryaramParam);
        if (ObjectUtils.isNotEmpty(dataDictionary)) {
            end:
            for (int i = dataDictionary.size() - 1; i >= 0; i--) {
                DataDictionaryDTO dictionary = dataDictionary.get(i);
                if (ObjectUtils.isNotEmpty(dictionary.getVariable())) {
                    CorrectNormConfigVO correctNormConfig = JsonUtils.deserialize(dictionary.getVariable(), CorrectNormConfigVO.class);
                    for (DeviceListVO device : correctNormConfig.getDevice().getDeviceList()) {
                        if (device.getId().equals(normRecord.getDeviceId()) &&
                            normRecord.getRuleNormCode().equals(correctNormConfig.getNormCode())) {
                            normRecord.setValue(normRecord.getValue().add(correctNormConfig.getCorrectValue()));
                            break end;
                        }
                    }
                }
            }
        }
    }

    public void correctNorm(MedicalNormRecord normRecord, Long deviceId, LocalDateTime date) {
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode(DictionaryItemEnum.OPERATING_ROOM_ADMIN_CORRECT_NORM_SET.name());
        List<DataDictionaryDTO> dataDictionary = dataDictionaryAdapter.getDataDictionaryByItem(dataDictionaryaramParam);
        if (ObjectUtils.isNotEmpty(dataDictionary)) {
            end:
            for (int i = dataDictionary.size() - 1; i >= 0; i--) {
                DataDictionaryDTO dictionary = dataDictionary.get(i);
                if (ObjectUtils.isNotEmpty(dictionary.getVariable())) {
                    CorrectNormConfigVO correctNormConfig = JsonUtils.deserialize(dictionary.getVariable(), CorrectNormConfigVO.class);
                    for (DeviceListVO device : correctNormConfig.getDevice().getDeviceList()) {
                        Boolean bool = device.getId().equals(deviceId)
                                       && normRecord.getNormCode().equals(correctNormConfig.getNormCode())
                                       && DateUtils.compare(correctNormConfig.getHandleTime(), date) <= 0;
                        if (bool) {
                            BigDecimal value = new BigDecimal(normRecord.getValue()).add(correctNormConfig.getCorrectValue());
                            normRecord.setValue(value.toString());
                            break end;
                        }
                    }
                }
            }
        }
    }
}

package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.converts.PatrolLocationConvertToVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.NfcDetailsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.NfcDetails;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlan;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlanLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.NfcStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationByBluetoothParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationTemplateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolRouteService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.MaintainSelectSheetWriteHandler;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.StrUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationAreaStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationDistributeStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 巡更点配置
 *
 * @author : lijie
 * @date : 2023-5-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolLocationServiceImpl extends ServiceImpl<PatrolLocationMapper, PatrolLocation> implements PatrolLocationService {
    private final AreaClient areaClient;
    private final NfcDetailsMapper nfcDetailsMapper;
    private final PatrolRouteService patrolRouteService;
    private final PatrolPlanMapper patrolPlanMapper;
    private final PatrolPlanLocationMapper patrolPlanLocationMapper;

    @Override
    public PatrolLocationVO queryById(Long id) {
        PatrolLocation location = this.baseMapper.selectById(id);
        PatrolLocationVO patrolLocation = new PatrolLocationVO();
        if (ObjectUtils.isNotEmpty(location)) {
            patrolLocation = ConvertUtils.convert(PatrolLocationVO.class, location);
            if (ObjectUtils.isNotEmpty(location.getAreaId())) {
                List<AreaDTO> area = areaClient.getByIds(StrUtils.StrToList(location.getAreaId()));
                patrolLocation.setArea(area);
            }
            NfcDetails nfcDetails = nfcDetailsMapper.selectOne(new LambdaQueryWrapper<NfcDetails>().eq(NfcDetails::getId, location.getDeviceId()));
            if (ObjectUtils.isNotEmpty(nfcDetails)) {
                patrolLocation.setDeviceCode(nfcDetails.getDeviceCode());
                patrolLocation.setNfcCode(nfcDetails.getNfcCode());
                patrolLocation.setBluetoothCode(nfcDetails.getBluetoothCode());
            }
        }
        return patrolLocation;
    }

    @Override
    public List<PatrolLocationIdTypeDTO> getPatrolPointIdTypes(PatrolLocationPageParam param) {
        var list = baseMapper.selectList(new LambdaQueryWrapper<PatrolLocation>()
                                                 .select(PatrolLocation::getId, PatrolLocation::getPatrolLocationName)
                                                 .in(CollectionUtils.isNotEmpty(param.getIdSet()), PatrolLocation::getId, param.getIdSet())
                                                 .like(ObjectUtils.isNotEmpty(param.getKeyword()), PatrolLocation::getPatrolLocationName, param.getKeyword()));
        return list.stream().map(po -> new PatrolLocationIdTypeDTO(po.getId(), po.getPatrolLocationName(), null))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<PatrolLocationVO> queryLocationPage(PatrolLocationPageParam param) {
        Page<PatrolLocation> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.selectPage(page, new LambdaQueryWrapper<PatrolLocation>()
                .in(CollectionUtils.isNotEmpty(param.getIdSet()), PatrolLocation::getId, param.getIdSet())
                .like(ObjectUtils.isNotEmpty(param.getKeyword()), PatrolLocation::getPatrolLocationName, param.getKeyword())
                .like(ObjectUtils.isNotEmpty(param.getAreaId()), PatrolLocation::getAreaId, param.getAreaId())
                .eq(PatrolLocation::getStatus, DeleteStatusEnum.NO_REMOVE.name())
                .orderByDesc(PatrolLocation::getCreateTime));
        Pagination<PatrolLocationVO> data = PagingUtils.convert(page, PatrolLocationVO.class);
        data.getRecords().forEach(ex -> {
            ex.setArea(areaClient.getByIds(StrUtils.StrToList(ex.getAreaId())));
            NfcDetails nfcDetails = nfcDetailsMapper.selectOne(new LambdaQueryWrapper<NfcDetails>().eq(NfcDetails::getId, ex.getDeviceId()));
            if (ObjectUtils.isNotEmpty(nfcDetails)) {
                ex.setDeviceCode(nfcDetails.getDeviceCode());
                ex.setNfcCode(nfcDetails.getNfcCode());
                ex.setBluetoothCode(nfcDetails.getBluetoothCode());
            }
        });
        return data;
    }

    @Override
    public List<PatrolLocationVO> queryLocationBatch(PatrolLocationPageParam param) {
        List<PatrolLocation> patrolLocationList = baseMapper.selectList(new LambdaQueryWrapper<PatrolLocation>()
                                                                                .like(ObjectUtils.isNotEmpty(param.getKeyword()), PatrolLocation::getPatrolLocationName, param.getKeyword())
                                                                                .like(ObjectUtils.isNotEmpty(param.getAreaId()), PatrolLocation::getAreaId, param.getAreaId())
                                                                                .eq(PatrolLocation::getStatus, DeleteStatusEnum.NO_REMOVE.name())
                                                                                .in(CollectionUtils.isNotEmpty(param.getIdSet()), PatrolLocation::getId, param.getIdSet())
                                                                                .orderByDesc(PatrolLocation::getCreateTime));
        if (CollectionUtils.isEmpty(patrolLocationList)) {
            return new ArrayList<>();
        }
        Set<Long> areaIds = new HashSet<>();
        Set<Long> deviceIds = new HashSet<>();
        patrolLocationList.forEach(ex -> {
            if (StringUtils.isNotBlank(ex.getAreaId())) {
                areaIds.addAll(StrUtils.StrToList(ex.getAreaId()));
            }
            if (ObjectUtils.isNotEmpty(ex.getDeviceId())) {
                deviceIds.add(ex.getDeviceId());
            }
        });
        List<AreaDTO> areaList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(areaIds)) {
            areaList = areaClient.getByIds(areaIds);
        }
        List<NfcDetails> nfcDetailsList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            nfcDetailsList = nfcDetailsMapper.selectList(new LambdaQueryWrapper<NfcDetails>().in(NfcDetails::getId, deviceIds));
        }
        return PatrolLocationConvertToVO.toPatrolLocationVO(patrolLocationList, areaList, nfcDetailsList);
    }

    @Override
    public List<PatrolLocationVO> queryLocationByPlan(Long planId) {
        List<PatrolLocationVO> location = baseMapper.queryLocationByPlan(planId);
        location.forEach(ex -> {
            List<AreaDTO> area = areaClient.getByIds(StrUtils.StrToList(ex.getAreaId()));
            ex.setArea(area);
        });
        return location;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(PatrolLocationParam param) {
        param.setId(null);
        NfcDetails nfcDetails = checkPatrolLocation(param);
        if (ObjectUtils.isNotEmpty(nfcDetails)) {
            Assert.isConflict(NfcStatusEnum.BOUND.name().equals(nfcDetails.getStatus()), "该设备已经绑定巡更点");
            nfcDetails.setStatus(NfcStatusEnum.BOUND.name());
            nfcDetailsMapper.updateById(nfcDetails);
        }
        PatrolLocation location = ConvertUtils.convert(PatrolLocation.class, param);
        location.setId(null);
        location.setAreaId(String.join(",", param.getAreaIds()));
        location.setStatus(DeleteStatusEnum.NO_REMOVE.name());
        boolean insert = this.baseMapper.insert(location) > 0;
        return insert;
    }

    /**
     * @Author wql
     * @Description 检查巡更点信息
     * @Date 17:10 2023/10/26
     **/
    private NfcDetails checkPatrolLocation(PatrolLocationParam param) {
        PatrolLocation patrolLocation = this.baseMapper.selectOne(new LambdaQueryWrapper<PatrolLocation>()
                                                                          .eq(StringUtils.isNotBlank(param.getBimCode()), PatrolLocation::getBimCode, param.getBimCode())
                                                                          .eq(PatrolLocation::getStatus, DeleteStatusEnum.NO_REMOVE.name())
                                                                          .ne(param.getId() != null, PatrolLocation::getId, param.getId())
                                                                          .last("limit 1"));
        var modelCodeExists = ObjectUtils.isNotNull(patrolLocation) && !patrolLocation.getId().equals(param.getId())
                              && StringUtils.isNotBlank(param.getBimCode());
        Assert.isConflict(modelCodeExists, param.getBimCode() + "模型编码已存在");
        return nfcDetailsMapper.selectOne(new LambdaQueryWrapper<NfcDetails>()
                                                  .eq(NfcDetails::getId, param.getDeviceId()));
    }

    @Override
    public Boolean update(PatrolLocationParam param) {
        NfcDetails nfcDetails = checkPatrolLocation(param);
        PatrolLocation location = this.getById(param.getId());
        if (ObjectUtils.isNotEmpty(nfcDetails)) {
            Assert.isConflict(!nfcDetails.getId().equals(location.getDeviceId()), "该设备已经绑定巡更点");
            if (!param.getDeviceId().equals(location.getDeviceId())) {
                // 更新之前绑定的设备状态
                NfcDetails old = nfcDetailsMapper.selectById(location.getDeviceId());
                old.setStatus(NfcStatusEnum.UNBOUND.name());
                nfcDetailsMapper.updateById(nfcDetails);

                // 更新现在绑定的芯片的状态
                NfcDetails newDetail = nfcDetailsMapper.selectById(param.getDeviceId());
                newDetail.setStatus(NfcStatusEnum.BOUND.name());
                nfcDetailsMapper.updateById(newDetail);
            }
        }
        location.setDeviceId(param.getDeviceId());
        location.setPatrolLocationName(param.getPatrolLocationName());
        location.setPatrolLongitude(param.getPatrolLongitude());
        location.setPatrolDimension(param.getPatrolDimension());
        location.setAreaId(String.join(",", param.getAreaIds()));
        location.setRemarks(param.getRemarks());
        location.setBimCode(param.getBimCode());
        return baseMapper.updateById(location) > 0;
    }

    public Boolean update(String pastNfcCode, String nfcCode) {
        return this.lambdaUpdate().set(PatrolLocation::getNfcCode, nfcCode)
                .eq(PatrolLocation::getNfcCode, pastNfcCode).update();
    }

    /**
     * 删除校验
     *
     * @param id 巡更点id
     *
     * @return
     */
    private String checkDeleteById(Long id) {
        var result = new StringBuffer();
        // 查询包含巡更点的线路
        var routeList = patrolRouteService.lambdaQuery().eq(PatrolRoute::getPatrolLocationId, id)
                .ne(PatrolRoute::getStatus, DeleteStatusEnum.REMOVE).groupBy(PatrolRoute::getUuid).list();
        if (CollectionUtils.isEmpty(routeList)) {
            return result.toString();
        }
        // 获取包含线路的计划
        var planLocationList = patrolPlanLocationMapper.selectList(Wrappers.<PatrolPlanLocation>lambdaQuery()
                                                                           .select(PatrolPlanLocation::getPlanId)
                                                                           .eq(PatrolPlanLocation::getLocationId, id)
                                                                           .groupBy(PatrolPlanLocation::getPlanId));
        var planIdList = Optional.ofNullable(planLocationList).orElse(new ArrayList<>()).stream().map(PatrolPlanLocation::getPlanId)
                .collect(Collectors.toSet());
        List<PatrolPlan> planList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(planIdList)) {
            planList = patrolPlanMapper.selectList(Wrappers.<PatrolPlan>lambdaQuery()
                                                           .in(PatrolPlan::getId, routeList.stream().map(PatrolRoute::getUuid)
                                                                   .collect(Collectors.toList()))
                                                           .eq(PatrolPlan::getStatus, PlanStatusEnum.ENABLE.name()));
        }
        if (CollectionUtils.isNotEmpty(planList)) {
            result.append("计划：");
            planList.forEach(ex -> result.append(ex.getPlanName()).append(","));
            result.append("正在使用，请先移除后再删除。");
            return result.toString();
        }
        result.append("线路：");
        routeList.forEach(ex -> result.append(ex.getPatrolRouteName()).append(","));
        result.append("正在使用，请先移除后再删除。");
        return result.toString();
    }

    @Override
    public Boolean deleteById(Long id) {
        var check = this.checkDeleteById(id);
        Assert.isConflict(StringUtils.isNotBlank(check), check);
        PatrolLocation location = this.baseMapper.selectById(id);
        location.setStatus(NfcStatusEnum.REMOVE.name());
        boolean bool = baseMapper.updateById(location) > 0;
        if (ObjectUtils.isNotEmpty(location.getDeviceId())) {
            // 解绑nfc
            NfcDetails nfcDetails = nfcDetailsMapper.selectById(location.getDeviceId());
            nfcDetails.setStatus(NfcStatusEnum.UNBOUND.name());
            bool = nfcDetailsMapper.updateById(nfcDetails) > 0;
        }
        // 删除巡更路线关系
        patrolRouteService.deleteByLocationId(id);
        return bool;
    }

    @Override
    public List<PatrolLocationVO> listByBluetooth(PatrolLocationByBluetoothParam param) {
        List<PatrolLocation> list = new ArrayList<>();
        if (ObjectUtils.isEmpty(param.getBluetoothCodes())) {
            return new ArrayList<>();
        }
        List<NfcDetails> nfcDetails = nfcDetailsMapper.selectList(new LambdaQueryWrapper<NfcDetails>().in(NfcDetails::getBluetoothCode, param.getBluetoothCodes()));
        if (ObjectUtils.isNotEmpty(nfcDetails)) {
            List<Long> collect = nfcDetails.stream().map(NfcDetails::getId).collect(Collectors.toList());
            list = this.lambdaQuery().in(PatrolLocation::getDeviceId, collect).list();
        }
        return ObjectUtils.copying(list, PatrolLocationVO.class);
    }

    @Override
    public LocationDistributeStatisticsVO locationDistributeStatistics(StatisticsQueryParam param) {
        long total = this.lambdaQuery().between(ObjectUtils.isNotNull(param.getPeriod()), PatrolLocation::getCreateTime,
                                                ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .count();
        List<LocationAreaStatisticsVO> locationAreaList = this.baseMapper.locationDistributeStatistics(param.getPeriod());
        if (ObjectUtils.isNotEmpty(locationAreaList)) {
            locationAreaList.forEach(location -> {
                if (StringUtils.isNotBlank(location.getAreaId())) {
                    String[] split = location.getAreaId().split(",");
                    location.setAreaName(areaClient.getAreaNamesById(Long.parseLong(split[split.length - 1])));
                }
                location.setProportion(MathUtils.calculateProportionPercentage(location.getNum(), total).floatValue());
            });
        }
        return LocationDistributeStatisticsVO.builder()
                .locationAreaList(locationAreaList)
                .total(total)
                .build();
    }

    @Override
    public List<PatrolLocationVO> listByRoute(Long routeId) {
        List<PatrolLocation> locationList = this.baseMapper.listByRoute(routeId);
        List<PatrolLocationVO> vos = ObjectUtils.copying(locationList, PatrolLocationVO.class);
        vos.forEach(ex -> {
            ex.setArea(areaClient.getByIds(StrUtils.StrToList(ex.getAreaId())));
            NfcDetails nfcDetails = nfcDetailsMapper.selectOne(new LambdaQueryWrapper<NfcDetails>().eq(NfcDetails::getId, ex.getDeviceId()));
            if (ObjectUtils.isNotEmpty(nfcDetails)) {
                ex.setDeviceCode(nfcDetails.getDeviceCode());
                ex.setNfcCode(nfcDetails.getNfcCode());
                ex.setBluetoothCode(nfcDetails.getBluetoothCode());
            }
        });
        return vos;
    }

    @Override
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = null;
        try {
            fileNameEncode = URLEncoder.encode("巡更点导入模板", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        // 下拉框内容
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        // 区域列表
        List<AreaDTO> areaList = areaClient.listAreas();
        if (ObjectUtils.isNotEmpty(areaList)) {
            for (AreaDTO area : areaList) {
                List<Long> parentIds = Arrays.stream(area.getSequence().split("-")).map(Long::parseLong)
                        .collect(Collectors.toList());
                List<String> pathAreaNames = new ArrayList<>();
                parentIds.forEach(parentId -> {
                    Optional<AreaDTO> optionalArea = areaList.stream()
                            .filter(parentArea -> parentArea.getId().equals(parentId)).findFirst();
                    optionalArea.ifPresent(parentArea ->
                                                   pathAreaNames.add(parentArea.getName())
                                          );
                });
                area.setName(String.join("-", pathAreaNames));
            }
            List<String> areaNameList = areaList.stream().map(AreaDTO::getName).collect(Collectors.toList());
            dropdownData.put(1, areaNameList);
        }
        // 关联设备列表
        List<NfcDetails> nfcDetails = nfcDetailsMapper.selectList(new LambdaQueryWrapper<NfcDetails>()
                                                                          .eq(NfcDetails::getStatus, NfcStatusEnum.UNBOUND.name())
                                                                          .orderByDesc(NfcDetails::getCreateTime));
        if (ObjectUtils.isNotEmpty(nfcDetails)) {
            List<String> areaNameList = nfcDetails.stream().map(NfcDetails::getDeviceCode).collect(Collectors.toList());
            dropdownData.put(3, areaNameList);
        }

        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new MaintainSelectSheetWriteHandler(dropdownData))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(PatrolLocationTemplateParam.class)
                    .sheet("巡更点导入模板")
                    .doWrite(new ArrayList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importTemplate(MultipartFile file) {
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 读取第一个sheet的数据
        List<PatrolLocationTemplateParam> datas = reader.sheet(0).head(PatrolLocationTemplateParam.class).doReadSync();
        Assert.parameterIsNull(datas, "无法添加空的excel");
        List<PatrolLocation> patrolLocationsList = new ArrayList<>();
        Map<String, List<String>> areaMap = new HashMap<>();
        // 区域列表
        List<AreaDTO> areaList = areaClient.listAreas();
        if (ObjectUtils.isNotEmpty(areaList)) {
            for (AreaDTO area : areaList) {
                List<Long> parentIds = Arrays.stream(area.getSequence().split("-")).map(Long::parseLong)
                        .collect(Collectors.toList());
                List<String> pathAreaIds = new ArrayList<>();
                List<String> pathAreaNames = new ArrayList<>();
                parentIds.forEach(parentId -> {
                    Optional<AreaDTO> optionalArea = areaList.stream()
                            .filter(parentArea -> parentArea.getId().equals(parentId)).findFirst();
                    optionalArea.ifPresent(parentArea ->
                                                   pathAreaIds.add(parentArea.getId().toString())
                                          );
                    optionalArea.ifPresent(parentArea ->
                                                   pathAreaNames.add(parentArea.getName())
                                          );
                });
                area.setName(String.join("-", pathAreaNames));
                areaMap.put(area.getName(), pathAreaIds);
            }
        }
        datas.forEach(ex -> {
            PatrolLocationParam patrolLocation = ConvertUtils.convert(PatrolLocationParam.class, ex);
            List<String> ids = new ArrayList<>();
            if (StringUtils.isNotBlank(ex.getAreaName())) {
                ids = areaMap.get(ex.getAreaName());
            }
            if (ObjectUtils.isNotEmpty(ids)) {
                patrolLocation.setAreaIds(ids);
            }
            if (ObjectUtils.isNotEmpty(ex.getDeviceCode())) {
                NfcDetails nfcDetails = nfcDetailsMapper.selectOne(new LambdaQueryWrapper<NfcDetails>()
                                                                           .eq(NfcDetails::getDeviceCode, ex.getDeviceCode())
                                                                           .last("limit 1"));
                if (ObjectUtils.isNotEmpty(nfcDetails)) {
                    patrolLocation.setNfcCode(nfcDetails.getNfcCode());
                    patrolLocation.setDeviceId(nfcDetails.getId());
                    patrolLocation.setBluetoothCode(nfcDetails.getBluetoothCode());
                    patrolLocation.setPatrolDimension(nfcDetails.getNfcDimension());
                    patrolLocation.setPatrolLongitude(nfcDetails.getNfcLongitude());
                }
            }
            this.insert(patrolLocation);
        });
        log.info("导入结果");
    }

    @Override
    public Map<Long, Long> countByAreaIds(List<Long> gridAreaIds) {
        Map<Long, Long> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return resultMap;
        }

        // 初始化结果Map
        for (Long areaId : gridAreaIds) {
            resultMap.put(areaId, 0L);
        }

        // 批量查询所有巡更点（只查询areaId字段，提高效率）
        List<PatrolLocation> allPatrolLocations = this.list(
                Wrappers.<PatrolLocation>lambdaQuery()
                        .select(PatrolLocation::getAreaId)
                        .isNotNull(PatrolLocation::getAreaId)
                        .ne(PatrolLocation::getAreaId, "")
                                                           );

        // 统计每个区域的巡更点数
        // areaId可能是层级格式（如"1-2-3"）或逗号分隔（如"1,2,3"），需要检查是否包含给定的区域ID
        for (PatrolLocation patrolLocation : allPatrolLocations) {
            if (StringUtils.isBlank(patrolLocation.getAreaId())) {
                continue;
            }

            // 尝试解析逗号分隔的区域ID
            String[] areaIdArray = patrolLocation.getAreaId().split(",");
            Set<Long> patrolAreaIds = new HashSet<>();
            for (String areaIdStr : areaIdArray) {
                if (StringUtils.isNotBlank(areaIdStr)) {
                    try {
                        patrolAreaIds.add(Long.parseLong(areaIdStr.trim()));
                    } catch (NumberFormatException e) {
                        // 如果不是数字，可能是层级格式，尝试用"-"分隔
                        String[] levelArray = areaIdStr.trim().split("-");
                        for (String levelId : levelArray) {
                            if (StringUtils.isNotBlank(levelId)) {
                                try {
                                    patrolAreaIds.add(Long.parseLong(levelId.trim()));
                                } catch (NumberFormatException ex) {
                                    log.warn("解析巡更点区域ID失败: {}", levelId, ex);
                                }
                            }
                        }
                    }
                }
            }

            // 统计匹配的区域
            for (Long areaId : gridAreaIds) {
                if (patrolAreaIds.contains(areaId)) {
                    resultMap.put(areaId, resultMap.getOrDefault(areaId, 0L) + 1);
                }
            }
        }

        return resultMap;
    }
}

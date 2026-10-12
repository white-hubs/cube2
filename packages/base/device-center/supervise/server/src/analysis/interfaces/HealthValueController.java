package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.biz.mdata.client.SceneTypeClient;
import com.guhecloud.cube.biz.mdata.client.dto.SceneTypeDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceAccountQueryService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query.DeviceHealthValueQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query.HealthValueTrendQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query.HistoricalHealthValueQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceHealthValueService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceLifeCycleService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthProblemMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthValueMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthProblemPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthValuePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.job.ComputingHealthValue;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DegradationVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceAvgHealthVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceHealthValueVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthLevelVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthPreviewVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthProblemVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthValuePageVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthValueTrendVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HistoricalHealthValueVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.PageWithHealthValueVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author pengjian
 * @date 2022/8/1 17:40
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthValueController {

    private final DeviceAccountQueryService deviceAccountQueryService;
    private final DeviceHealthValueService healthValueService;
    private final DeviceHealthProblemMapper problemMapper;
    private final ComputingHealthValue computingHealthValue;
    private final DeviceHealthValueMapper healthValueMapper;
    private final SceneTypeClient sceneTypeClient;
    private final DeviceHealthValueService deviceHealthValueService;
    private final DeviceLifeCycleService deviceLifeCycleService;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final DeviceBimSceneStdClient deviceBimSceneStdClient;
    private final ProductClient productClient;

    @GetMapping("/device/instance/analysis/health/page")
    public Pagination<HealthValuePageVO> page(DeviceBaseInfoPageQry query) {
        // 直接调用设备列表然后进行计算
        Pagination<DeviceBaseArchivesInfoDTO> pagination = deviceCenterStdClient.pageOfBaseInfo(query);

        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return new Pagination<>();
        }

        Map<Long, DeviceHealthValuePO> healthValueMap = healthValueMapper.selectLast(pagination.getRecords().stream()
                                                                                             .map(DeviceBaseArchivesInfoDTO::getId)
                                                                                             .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(DeviceHealthValuePO::getDeviceId, v -> v));

        List<HealthValuePageVO> result = new ArrayList<>();

        Map<Long, DeviceProductInfoWithDeviceDTO> productInfoMap = deviceCenterStdClient
                .getProductInfoByDeviceId(pagination.getRecords().stream().map(DeviceBaseArchivesInfoDTO::getId)
                                                  .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, o -> o));

        pagination.getRecords().forEach(deviceAccountPageVO -> {
            HealthValuePageVO healthValuePageVO = ObjectUtils.copying(deviceAccountPageVO, HealthValuePageVO.class);
            DeviceProductInfoWithDeviceDTO deviceProductInfoWithDeviceDTO = productInfoMap.get(deviceAccountPageVO.getId());
            if (Objects.nonNull(deviceProductInfoWithDeviceDTO) && Objects.nonNull(deviceProductInfoWithDeviceDTO.getProductInfo())) {
                healthValuePageVO.setProductCategoryName(productInfoMap.get(deviceAccountPageVO.getId()).getProductInfo()
                                                                 .getProductCategory().getName());
            }
            DeviceHealthValuePO deviceHealthValuePO = healthValueMap.get(deviceAccountPageVO.getId());
            // 区域可能为空
            healthValuePageVO.setAreaName(Objects.nonNull(deviceAccountPageVO.getArea()) ? deviceAccountPageVO.getArea().getVal() : null);

            if (Objects.nonNull(deviceHealthValuePO)) {
                healthValuePageVO.setHealthValue(deviceHealthValuePO.getHealthValue());
                healthValuePageVO.setCreateTime(deviceHealthValuePO.getCreateTime());
            }
            result.add(healthValuePageVO);
        });
        return new Pagination<>(pagination.getTotal(), pagination.getPages(), result);
    }

    /**
     * 最新检测的健康问题
     *
     * @param deviceInstanceId
     */
    @GetMapping("/device/instance/analysis/health/problems/latest/{deviceId}")
    public HealthProblemVO healthProblems(@PathVariable("deviceId") Long deviceInstanceId) {
        Page<DeviceHealthValuePO> page = healthValueService.page(
                new Page<>(0, 1),
                Wrappers.<DeviceHealthValuePO>lambdaQuery()
                        .eq(DeviceHealthValuePO::getDeviceId, deviceInstanceId)
                        .orderByDesc(DeviceHealthValuePO::getCreateTime)
                                                                );
        Optional<DeviceHealthValuePO> optional = page.getRecords().stream().findFirst();

        AtomicReference<List<String>> result = new AtomicReference<>();
        AtomicReference<Integer> healthValue = new AtomicReference<>();
        AtomicReference<LocalDateTime> createTime = new AtomicReference<>();
        optional.ifPresent(deviceHealthValuePO -> {
            healthValue.set(deviceHealthValuePO.getHealthValue());
            createTime.set(deviceHealthValuePO.getCreateTime());
            List<DeviceHealthProblemPO> deviceHealthProblemPOS = problemMapper.selectList(
                    Wrappers.<DeviceHealthProblemPO>lambdaQuery()
                            .eq(DeviceHealthProblemPO::getDeviceHealthValueId, deviceHealthValuePO.getId())
                            .orderByDesc(DeviceHealthProblemPO::getCreateTime)
                                                                                         );

            result.set(
                    deviceHealthProblemPOS.stream().map(DeviceHealthProblemPO::getDesc).collect(Collectors.toList())
                      );

        });

        List<String> problems = result.get();
        return new HealthProblemVO(8, Objects.isNull(problems) ? 0 : problems.size(), problems, healthValue.get(), createTime.get());
    }

    /**
     * 历史健康值
     *
     * @param query
     */
    @GetMapping("/device/instance/analysis/health/historical/page")
    public Pagination<HistoricalHealthValueVO> historicalHealthValue(HistoricalHealthValueQuery query) {
        Page<DeviceHealthValuePO> page = healthValueService.page(PagingUtils.convert(query),
                                                                 Wrappers.<DeviceHealthValuePO>lambdaQuery()
                                                                         .eq(DeviceHealthValuePO::getDeviceId, query.getDeviceId())
                                                                         .between(Objects.nonNull(query.getPeriod()), DeviceHealthValuePO::getCreateTime,
                                                                                  Objects.nonNull(query.getPeriod()) ? query.getPeriod()
                                                                                          .getBegin() : null,
                                                                                  Objects.nonNull(query.getPeriod()) ? query.getPeriod()
                                                                                          .getEnd() : null)
                                                                         .orderByDesc(DeviceHealthValuePO::getCreateTime)
                                                                );

        List<DeviceHealthValuePO> records = page.getRecords();
        List<HistoricalHealthValueVO> historicalHealthValueVOS = ObjectUtils.copying(records, HistoricalHealthValueVO.class);
        return new Pagination<>(page.getTotal(), page.getPages(), historicalHealthValueVOS);

    }

    /**
     * 健康值的趋势
     *
     * @param query
     */
    @GetMapping("/device/instance/analysis/health/trend")
    public List<HealthValueTrendVO> healthValueTrend(HealthValueTrendQuery query) {
        Map<String, Integer> dateMap = new HashMap<>();
        if (Objects.nonNull(query.getPeriod())) {
            var begin = query.getPeriod().getBegin();
            var end = query.getPeriod().getEnd();
            LocalDate beginDate = begin.toLocalDate();
            LocalDate endDate = end.toLocalDate();

            // 计算天数
            List<LocalDate> dates = Stream.iterate(beginDate, date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(beginDate, endDate))
                    .toList();

            dateMap = dates.stream().collect(Collectors.toMap(LocalDate::toString, date -> 0));

        }

        List<HealthValueTrendVO> result = new ArrayList<>();
        List<DeviceHealthValuePO> list = healthValueService.list(
                Wrappers.<DeviceHealthValuePO>lambdaQuery()
                        .eq(DeviceHealthValuePO::getDeviceId, query.getDeviceInstanceId())
                        .between(DeviceHealthValuePO::getCreateTime,
                                 Objects.nonNull(query.getPeriod()) ? query.getPeriod().getBegin() : null,
                                 Objects.nonNull(query.getPeriod()) ? query.getPeriod().getEnd() : null)
                        .orderByAsc(DeviceHealthValuePO::getCreateTime)
                                                                );

        Map<String, List<DeviceHealthValuePO>> map = list.stream().collect(Collectors.groupingBy(v -> {
            return DateUtils.formatDate(v.getCreateTime());
        }));

        Iterator<Map.Entry<String, List<DeviceHealthValuePO>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<DeviceHealthValuePO>> next = iterator.next();
            String key = next.getKey();
            List<DeviceHealthValuePO> value = next.getValue();
            OptionalDouble average = value.stream().map(DeviceHealthValuePO::getHealthValue)
                    .mapToLong(Number::intValue).average();
            if (average.isPresent()) {
                dateMap.put(key, (int) average.getAsDouble());
            }
        }

        Iterator<Map.Entry<String, Integer>> dateMapIterator = dateMap.entrySet().stream().iterator();
        while (dateMapIterator.hasNext()) {
            Map.Entry<String, Integer> next = dateMapIterator.next();
            String key = next.getKey();
            Integer value = next.getValue();
            result.add(new HealthValueTrendVO(key, value));
        }

        Collections.sort(result);
        return result;
    }

    @PostMapping("/device/instance/{deviceId}/analysis/health/computing")
    public void computingHealthValue(@PathVariable("deviceId") Long deviceInstanceId) {
        DeviceDetailDTO device = deviceCenterStdClient.getDeviceById(deviceInstanceId);
        DeviceBaseArchivesInfoDTO baseArchivesInfo = device.getBaseArchivesInfo();
        baseArchivesInfo.setTenant(SessionContextHolder.getRequester().getTenant());
        computingHealthValue.executeComputing(Arrays.asList(baseArchivesInfo), true);
    }

    @GetMapping("/device/instance/analysis/page-with-healthvalue")
    public Pagination<PageWithHealthValueVO> pageWithHealthValue(DeviceBaseInfoPageQry query) {
        List<PageWithHealthValueVO> result = new ArrayList<>();
        Pagination<DeviceBaseArchivesInfoDTO> page = deviceCenterStdClient.pageOfBaseInfo(query);

        if (page.getRecords().isEmpty()) {
            return new Pagination<>(page.getTotal(), page.getPages(), Collections.EMPTY_LIST);
        }

        // 批量填充产品信息
        Map<Long, DeviceProductInfoWithDeviceDTO> map = deviceCenterStdClient
                .getProductInfoByDeviceId(page.getRecords().stream().map(DeviceBaseArchivesInfoDTO::getId)
                                                  .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, o -> o));
        page.getRecords().forEach(deviceBaseArchivesInfoDTO -> {
            PageWithHealthValueVO pageWithHealthValueVO = new PageWithHealthValueVO();
            pageWithHealthValueVO.setBaseArchivesInfo(deviceBaseArchivesInfoDTO);
            pageWithHealthValueVO.setProductInfo(map.get(deviceBaseArchivesInfoDTO.getId()).getProductInfo());
            result.add(pageWithHealthValueVO);
        });

        List<PageWithHealthValueVO> pageWithHealthValueVOS = result;

        Map<Long, Integer> healthValueMap = healthValueMapper.selectLast(
                        pageWithHealthValueVOS.stream()
                                .map(pageWithHealthValueVO -> pageWithHealthValueVO.getBaseArchivesInfo().getId()).collect(Collectors.toSet())
                                                                        ).stream()
                .collect(Collectors.toMap(DeviceHealthValuePO::getDeviceId, DeviceHealthValuePO::getHealthValue));

        pageWithHealthValueVOS.forEach(pageWithHealthValueVO -> {
            pageWithHealthValueVO.setHealthValue(healthValueMap.get(pageWithHealthValueVO.getBaseArchivesInfo().getId()));
        });
        return new Pagination<>(page.getTotal(), page.getPages(), pageWithHealthValueVOS);
    }

    /**
     * 系统的专业健康度
     * 传入id 获取所有设备
     * <p>
     * 健康度: 是所有设备当天健康度的平均值，还是加上历史的
     * 健康等级各设备数  设备总数
     */
    @GetMapping("/device/instance/analysis/health/preview/scene-type/{sceneType}")
    public HealthPreviewVO getHealthPreviewBySceneType(@PathVariable("sceneType") Long sceneType) {

        return healthValueService.getHealthPreviewBySceneType(sceneType);
    }

    @GetMapping("/device/instance/analysis/health/preview/all")
    public HealthPreviewVO getHealthPreview() {

        /**
         * 获取所有场景绑定的设备
         */

        Set<Long> deviceIds = new HashSet<>();

        List<SceneTypeDTO> sceneTypeDTOS = sceneTypeClient.page();
        if (CollectionUtils.isNotEmpty(sceneTypeDTOS)) {
            sceneTypeDTOS.forEach(sceneTypeDTO -> {
                Set<Long> bimDeviceIds = deviceBimSceneStdClient.getBimDeviceIds(sceneTypeDTO.getId(), null);
                if (CollectionUtils.isNotEmpty(bimDeviceIds)) {
                    deviceIds.addAll(bimDeviceIds);
                }

            });

            if (CollectionUtils.isEmpty(deviceIds)) {
                return null;
            }

            HealthLevelVO healthLevelRate = deviceHealthValueService.getHealthLevelRate(deviceIds);

            // 昨日开始时间
            LocalDate yesterday = LocalDate.now().plusDays(-1);
            // 昨日结束时间

            List<DeviceHealthValuePO> deviceHealthValuePOS = healthValueMapper.selectLast(deviceIds);
            int sum = deviceHealthValuePOS.stream().mapToInt(value -> value.getHealthValue()).sum();

            HealthPreviewVO healthPreviewVO = ObjectUtils.copying(healthLevelRate, HealthPreviewVO.class);

            if (CollectionUtils.isNotEmpty(deviceHealthValuePOS)) {
                healthPreviewVO.setHealthValue(sum / deviceHealthValuePOS.size());
            }

            return healthPreviewVO;
        }

        return null;
    }

    @GetMapping("/device/instance/analysis/health/all")
    public Integer getHealthValue() {
        /**
         * 获取所有的sceneType
         */
        AtomicReference<Integer> sum = new AtomicReference<>(0);
        List<SceneTypeDTO> sceneTypeDTOS = sceneTypeClient.page();
        if (CollectionUtils.isNotEmpty(sceneTypeDTOS)) {
            sceneTypeDTOS.forEach(sceneTypeDTO -> {
                Integer healthValueBySceneType = getHealthValueBySceneType(sceneTypeDTO.getId());
                if (Objects.isNull(healthValueBySceneType)) {
                    sum.updateAndGet(v -> v);
                } else {
                    sum.updateAndGet(v -> v + healthValueBySceneType);
                }

            });

            return sum.get() / sceneTypeDTOS.size();
        }

        return null;
    }

    @GetMapping("/device/instance/analysis/health/value/scene-type")
    public Integer getHealthValueBySceneType(@RequestParam("sceneType") Long sceneType) {
        return healthValueService.getHealthValueBySceneType(sceneType);
    }

    @PostMapping("/device/instance/analysis/health/value/last/batch")
    public List<DeviceHealthValueVO> getHealthValue(@RequestBody DeviceHealthValueQuery query) {
        List<DeviceHealthValuePO> deviceHealthValuePOS = healthValueMapper.selectLast(query.getDeviceIds());
        List<DeviceHealthValueVO> deviceHealthValueVOS = ObjectUtils.copying(deviceHealthValuePOS, DeviceHealthValueVO.class);
        return deviceHealthValueVOS;
    }

    @GetMapping("/device/instance/analysis/health/avg-value")
    public DeviceAvgHealthVO getHealthValueByTag(@RequestParam("sceneType") String deviceTag) {
        return healthValueService.getHealthValueByDeviceTag(deviceTag);
    }

    /**
     * 设备劣化趋势
     * 目前是根据使用寿命逐渐下降
     */
    @GetMapping("/device/instance/{deviceId}/analysis/health/degradation/trend")
    public List<DegradationVO> degradationTrend(@PathVariable("deviceId") Long deviceInstanceId) {

        /**
         * 从设备创建时间开始计算，每3个月一个数据
         *
         * 1. 计算循环几次（有几个三个月）
         * 2. 计算每循环一次的数值
         *
         */
        List<DegradationVO> result = new ArrayList<>();
        DeviceLifeCycleDTO deviceLifeCycleDTO = deviceLifeCycleService.get(deviceInstanceId);
        if (Objects.isNull(deviceLifeCycleDTO)) {
            return Collections.emptyList();
        }

        var now = LocalDateTime.now();
        var createTime = deviceLifeCycleDTO.getCreateTime();
        long day = DateUtils.between(createTime, now, ChronoUnit.DAYS);
        long allNumber = DateUtils.between(createTime, deviceLifeCycleDTO.getStopTime(), ChronoUnit.DAYS);
        long month = allNumber / 30;
        long allCycle = month / 2;
        float factor = allCycle * 1.0f / 100;
        long cycleNumber = day / 30 / 2;

        for (int i = 0; i <= cycleNumber; i++) {
            String time = String.valueOf((i * factor) + 1);
            time = new BigDecimal(time).setScale(1, RoundingMode.HALF_UP).toString();
            if (time.substring(time.indexOf(".") + 1).startsWith("0")) {
                time = time.substring(0, time.indexOf("."));
            }
            DegradationVO degradationVO = new DegradationVO(
                    (100 - (i * factor * 10)),
                    time
            );
            result.add(degradationVO);
        }
        return result;
    }
}

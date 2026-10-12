package com.guhecloud.cube.biz.metric.server.metric.library.service.command;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.metric.server.metric.library.command.ChangeMetricLibraryCommand;
import com.guhecloud.cube.biz.metric.server.metric.library.command.DelMetricLibraryCommand;
import com.guhecloud.cube.biz.metric.server.metric.library.command.IncreaseMetricLibraryCommand;
import com.guhecloud.cube.biz.metric.server.metric.library.enumeration.MetricComputeType;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.MetricLibraryRepository;
import com.guhecloud.cube.biz.metric.server.metric.library.repository.po.MetricLibraryPO;
import com.guhecloud.cube.biz.metric.server.metric.library.utils.FormulaPreGenerateTool;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.AlarmBaseMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.BaseElectronMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.flow.BaseOrderMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadBaseDeviceMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadBaseLooseWorksMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadBaseMaintainMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersAlarmBaseMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.BaseInspectionMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.BaseLooseWorksMetricService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MetricLibraryCommandService {

    private final MetricLibraryRepository metricLibraryRepository;

    private final FormulaPreGenerateTool formulaPreGenerateTool;

    public MetricLibraryCommandService(MetricLibraryRepository metricLibraryRepository, FormulaPreGenerateTool formulaPreGenerateTool
                                      ) {
        this.metricLibraryRepository = metricLibraryRepository;
        this.formulaPreGenerateTool = formulaPreGenerateTool;
    }

    public Long increaseMetricLibrary(IncreaseMetricLibraryCommand command) {
        MetricLibraryPO libraryRepositoryByName = metricLibraryRepository.findByName(command.getName());
        if (Objects.nonNull(libraryRepositoryByName)) {
            throw new ServiceException("指标名称已存在");
        }
        MetricLibraryPO metricLibraryPO = ObjectUtils.copying(command, MetricLibraryPO.class);
        metricLibraryPO.setCode(IdWorker.getIdStr());
        metricLibraryPO.setInitialized(false);
        if (command.getComputeType() == MetricComputeType.COMBINATION_RESULT) {
            String executableFormula = formulaPreGenerateTool.resolveFormula(metricLibraryPO);
            metricLibraryPO.setExecutableFormula(executableFormula);
        } else {
            metricLibraryPO.setExecutableFormula(command.getComputeFormula());
            metricLibraryPO.setAllBaseMetricIds(command.getComputeFormulaMetrics());
        }
        return metricLibraryRepository.save(metricLibraryPO);
    }

    public Long changeMetricLibrary(ChangeMetricLibraryCommand command) {
        MetricLibraryPO metricLibraryPO = metricLibraryRepository.findById(command.getId());
        if (Objects.isNull(metricLibraryPO)) {
            return 0L;
        }
        if (Boolean.TRUE.equals(metricLibraryPO.getInitialized())) {
            return 0L;
        }
        MetricLibraryPO libraryRepositoryByName = metricLibraryRepository.findByName(command.getName());
        if (Objects.nonNull(libraryRepositoryByName) && !libraryRepositoryByName.getId().equals(metricLibraryPO.getId())) {
            throw new ServiceException("指标名称已存在");
        }
        ObjectUtils.copying(command, metricLibraryPO);
        if (command.getComputeType() == MetricComputeType.COMBINATION_RESULT) {
            String executableFormula = formulaPreGenerateTool.resolveFormula(metricLibraryPO);
            metricLibraryPO.setExecutableFormula(executableFormula);
        } else {
            metricLibraryPO.setExecutableFormula(command.getComputeFormula());
            metricLibraryPO.setAllBaseMetricIds(command.getComputeFormulaMetrics());
        }
        return metricLibraryRepository.update(metricLibraryPO);
    }

    public void deleteMetricLibrary(DelMetricLibraryCommand command) {
        MetricLibraryPO metricLibraryPO = metricLibraryRepository.findById(command.getId());
        if (Objects.isNull(metricLibraryPO)) {
            return;
        }
        if (Boolean.TRUE.equals(metricLibraryPO.getInitialized())) {
            return;
        }
        metricLibraryRepository.removeById(command.getId());
    }

    public void initMetricLibrary(String type) {
        List<Object> values = new ArrayList<>();
        if ("MARKET".equals(type)) {
            values.add(ApplicationContextHolder.getBean(AlarmBaseMetricService.class));
            values.add(ApplicationContextHolder.getBean(BaseElectronMetricService.class));
            values.add(ApplicationContextHolder.getBean(BaseInspectionMetricService.class));
            values.add(ApplicationContextHolder.getBean(BaseOrderMetricService.class));
            values.add(ApplicationContextHolder.getBean(BaseLooseWorksMetricService.class));
        } else if ("GROUP".equals(type)) {
            values.add(ApplicationContextHolder.getBean(HeadBaseLooseWorksMetricService.class));
            values.add(ApplicationContextHolder.getBean(HeadBaseMaintainMetricService.class));
            values.add(ApplicationContextHolder.getBean(HeadquartersAlarmBaseMetricService.class));
            values.add(ApplicationContextHolder.getBean(HeadBaseDeviceMetricService.class));
        }
        Map<String, String> metricTopicMap = new HashMap<>();
        for (Object bean : values) {
            Class<?> clazz = AopUtils.getTargetClass(bean);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(MetricTopic.class)) {
                    method.setAccessible(true);
                    MetricTopic metricTopic = method.getAnnotation(MetricTopic.class);
                    metricTopicMap.put(metricTopic.topic(), metricTopic.description());
                }
            }
        }
        for (Map.Entry<String, String> entry : metricTopicMap.entrySet()) {
            MetricLibraryPO metricLibraryPO = new MetricLibraryPO();
            metricLibraryPO.setId(IdWorker.getId());
            metricLibraryPO.setCode(entry.getKey());
            metricLibraryPO.setName(entry.getValue());
            metricLibraryPO.setComputeType(MetricComputeType.BASE);
            metricLibraryPO.setInitialized(true);
            metricLibraryPO.setComputeFormula(Collections.singleton(metricLibraryPO.getId()).toString());
            metricLibraryPO.setComputeFormulaMetrics(metricLibraryPO.getId().toString());
            metricLibraryPO.setExecutableFormula(entry.getKey());
            metricLibraryPO.setAllBaseMetricIds(entry.getKey());
            metricLibraryRepository.save(metricLibraryPO);
        }
    }
}

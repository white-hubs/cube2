package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseTemperatureTrendConfigMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCable;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendLayerCable;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendLayerConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAggType;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCableService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendLayerCableService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendLayerConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendConfigVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseTemperatureTrendConfigServiceImpl
        extends ServiceImpl<WarehouseTemperatureTrendConfigMapper, WarehouseTemperatureTrendConfig>
        implements WarehouseTemperatureTrendConfigService {

    private static final List<LayerDefinition> LAYER_DEFINITIONS = List.of(
            new LayerDefinition("UPPER", "上层", "粮面侧", 1),
            new LayerDefinition("MIDDLE", "中层", "粮堆中部", 2),
            new LayerDefinition("LOWER", "下层", "仓底侧", 3));
    private static final Map<String, LayerDefinition> LAYER_DEFINITION_BY_CODE = LAYER_DEFINITIONS.stream()
            .collect(Collectors.toMap(LayerDefinition::code, item -> item, (a, b) -> b, LinkedHashMap::new));

    private final WarehouseTemperatureTrendLayerConfigService layerConfigService;
    private final WarehouseTemperatureTrendLayerCableService layerCableService;
    private final WarehouseCableService warehouseCableService;
    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehousePageApiClient warehousePageApiClient;

    @Override
    public WarehouseTemperatureTrendConfigVO detail(Long warehouseId) {
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        assertWarehouseBoundAndExists(warehouseId);
        WarehouseTemperatureTrendConfig config = lambdaQuery()
                .eq(WarehouseTemperatureTrendConfig::getWarehouseId, warehouseId)
                .one();
        if (config == null) {
            return defaultDetail(warehouseId);
        }
        List<WarehouseTemperatureTrendLayerConfig> layers = layerConfigService.list(
                Wrappers.<WarehouseTemperatureTrendLayerConfig>lambdaQuery()
                        .eq(WarehouseTemperatureTrendLayerConfig::getConfigId, config.getId())
                        .orderByAsc(WarehouseTemperatureTrendLayerConfig::getSortNo));
        Map<String, WarehouseTemperatureTrendLayerConfig> layerByCode = ListUtils.isEmpty(layers) ? Map.of()
                : layers.stream()
                        .filter(layer -> layer != null && layer.getLayerCode() != null)
                        .collect(Collectors.toMap(WarehouseTemperatureTrendLayerConfig::getLayerCode,
                                                  item -> item, (a, b) -> b));
        Set<Long> layerConfigIds = ListUtils.isEmpty(layers) ? Set.of()
                : SetUtils.listToSet(layers, WarehouseTemperatureTrendLayerConfig::getId);
        Map<Long, List<Long>> cableIdsByLayerConfigId = loadCableIdsByLayerConfigId(config.getId(), layerConfigIds);

        var vo = new WarehouseTemperatureTrendConfigVO();
        vo.setConfigId(config.getId());
        vo.setWarehouseId(config.getWarehouseId());
        vo.setHighAlarmThreshold(config.getHighAlarmThreshold());
        vo.setLowAlarmThreshold(config.getLowAlarmThreshold());
        vo.setWarehouseAggType(config.getWarehouseAggType());
        List<WarehouseTemperatureTrendConfigVO.Layer> outLayers = new ArrayList<>(LAYER_DEFINITIONS.size());
        for (LayerDefinition definition : LAYER_DEFINITIONS) {
            WarehouseTemperatureTrendLayerConfig layer = layerByCode.get(definition.code());
            outLayers.add(toLayerVO(definition, layer, cableIdsByLayerConfigId));
        }
        vo.setLayers(outLayers);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveConfig(WarehouseTemperatureTrendConfigSaveParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        assertWarehouseBoundAndExists(warehouseId);
        WarehouseTemperatureAggType warehouseAggType = requireAggType(param.getWarehouseAggType());
        Assert.isParameterError(param.getHighAlarmThreshold() == null, MsgBinding.TEMP_TREND_ALARM_THRESHOLD_REQUIRED);
        Assert.isParameterError(param.getLowAlarmThreshold() == null, MsgBinding.TEMP_TREND_ALARM_THRESHOLD_REQUIRED);
        Map<String, WarehouseTemperatureTrendConfigSaveParam.Layer> layerByCode = validateAndNormalizeLayers(param);

        WarehouseTemperatureTrendConfig config = lambdaQuery()
                .eq(WarehouseTemperatureTrendConfig::getWarehouseId, warehouseId)
                .one();
        if (config == null) {
            config = new WarehouseTemperatureTrendConfig();
            config.setId(IdWorker.getId());
            config.setWarehouseId(warehouseId);
            config.setHighAlarmThreshold(param.getHighAlarmThreshold());
            config.setLowAlarmThreshold(param.getLowAlarmThreshold());
            config.setWarehouseAggType(warehouseAggType);
            save(config);
        } else {
            config.setHighAlarmThreshold(param.getHighAlarmThreshold());
            config.setLowAlarmThreshold(param.getLowAlarmThreshold());
            config.setWarehouseAggType(warehouseAggType);
            updateById(config);
        }

        List<WarehouseTemperatureTrendLayerConfig> existingLayers = layerConfigService.list(
                Wrappers.<WarehouseTemperatureTrendLayerConfig>lambdaQuery()
                        .eq(WarehouseTemperatureTrendLayerConfig::getConfigId, config.getId()));
        Map<String, WarehouseTemperatureTrendLayerConfig> existingLayerByCode = ListUtils.isEmpty(existingLayers) ? Map.of()
                : existingLayers.stream()
                        .filter(layer -> layer != null && layer.getLayerCode() != null)
                        .collect(Collectors.toMap(WarehouseTemperatureTrendLayerConfig::getLayerCode,
                                                  item -> item, (a, b) -> b));

        List<WarehouseTemperatureTrendLayerConfig> layersToSave = new ArrayList<>();
        List<WarehouseTemperatureTrendLayerConfig> layersToUpdate = new ArrayList<>();
        Set<Long> allCableIds = validateCableIds(warehouseId, layerByCode);
        for (LayerDefinition definition : LAYER_DEFINITIONS) {
            WarehouseTemperatureTrendConfigSaveParam.Layer incoming = layerByCode.get(definition.code());
            WarehouseTemperatureTrendLayerConfig layer = existingLayerByCode.get(definition.code());
            boolean created = false;
            if (layer == null) {
                layer = new WarehouseTemperatureTrendLayerConfig();
                layer.setId(IdWorker.getId());
                created = true;
            }
            fillLayerConfig(layer, config, definition, incoming);
            if (created) {
                layersToSave.add(layer);
            } else {
                layersToUpdate.add(layer);
            }
        }
        if (ListUtils.isNotEmpty(layersToSave)) {
            layerConfigService.saveBatch(layersToSave);
        }
        if (ListUtils.isNotEmpty(layersToUpdate)) {
            layerConfigService.updateBatchById(layersToUpdate);
        }

        List<WarehouseTemperatureTrendLayerConfig> allLayers = new ArrayList<>(layersToSave.size() + layersToUpdate.size());
        allLayers.addAll(layersToSave);
        allLayers.addAll(layersToUpdate);
        Map<String, WarehouseTemperatureTrendLayerConfig> savedLayerByCode = allLayers.stream()
                .collect(Collectors.toMap(WarehouseTemperatureTrendLayerConfig::getLayerCode, item -> item, (a, b) -> b));

        layerCableService.remove(Wrappers.<WarehouseTemperatureTrendLayerCable>lambdaQuery()
                .eq(WarehouseTemperatureTrendLayerCable::getConfigId, config.getId()));
        if (SetUtils.isNotEmpty(allCableIds)) {
            List<WarehouseTemperatureTrendLayerCable> cableRows = buildLayerCableRows(config, savedLayerByCode, layerByCode);
            if (ListUtils.isNotEmpty(cableRows)) {
                layerCableService.saveBatch(cableRows);
            }
        }
        return true;
    }

    private WarehouseTemperatureTrendConfigVO defaultDetail(Long warehouseId) {
        var vo = new WarehouseTemperatureTrendConfigVO();
        vo.setWarehouseId(warehouseId);
        vo.setHighAlarmThreshold(null);
        vo.setLowAlarmThreshold(null);
        vo.setWarehouseAggType(WarehouseTemperatureAggType.MAX);
        vo.setLayers(LAYER_DEFINITIONS.stream()
                .map(definition -> toLayerVO(definition, null, Map.of()))
                .toList());
        return vo;
    }

    private WarehouseTemperatureTrendConfigVO.Layer toLayerVO(LayerDefinition definition,
                                                             WarehouseTemperatureTrendLayerConfig layer,
                                                             Map<Long, List<Long>> cableIdsByLayerConfigId
                                                            ) {
        var vo = new WarehouseTemperatureTrendConfigVO.Layer();
        vo.setLayerConfigId(layer == null ? null : layer.getId());
        vo.setLayerCode(definition.code());
        vo.setLayerName(layer == null || StringUtils.isBlank(layer.getLayerName()) ? definition.layerName() : layer.getLayerName());
        vo.setPositionName(layer == null || StringUtils.isBlank(layer.getPositionName()) ? definition.positionName() : layer.getPositionName());
        vo.setSelectedCableIds(layer == null ? List.of()
                : cableIdsByLayerConfigId.getOrDefault(layer.getId(), List.of()));
        vo.setHighAlarmThreshold(layer == null ? null : layer.getHighAlarmThreshold());
        vo.setLowAlarmThreshold(layer == null ? null : layer.getLowAlarmThreshold());
        vo.setLayerAggType(layer == null || layer.getLayerAggType() == null ? WarehouseTemperatureAggType.MAX : layer.getLayerAggType());
        vo.setSortNo(layer == null ? definition.sortNo() : layer.getSortNo());
        return vo;
    }

    private Map<Long, List<Long>> loadCableIdsByLayerConfigId(Long configId, Set<Long> layerConfigIds) {
        if (SetUtils.isEmpty(layerConfigIds)) {
            return Map.of();
        }
        List<WarehouseTemperatureTrendLayerCable> rows = layerCableService.list(
                Wrappers.<WarehouseTemperatureTrendLayerCable>lambdaQuery()
                        .select(WarehouseTemperatureTrendLayerCable::getLayerConfigId,
                                WarehouseTemperatureTrendLayerCable::getCableId)
                        .eq(WarehouseTemperatureTrendLayerCable::getConfigId, configId)
                        .in(WarehouseTemperatureTrendLayerCable::getLayerConfigId, layerConfigIds)
                        .orderByAsc(WarehouseTemperatureTrendLayerCable::getCableId));
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        return rows.stream()
                .collect(Collectors.groupingBy(WarehouseTemperatureTrendLayerCable::getLayerConfigId,
                                               HashMap::new,
                                               Collectors.mapping(WarehouseTemperatureTrendLayerCable::getCableId,
                                                                  Collectors.toList())));
    }

    private Map<String, WarehouseTemperatureTrendConfigSaveParam.Layer> validateAndNormalizeLayers(
            WarehouseTemperatureTrendConfigSaveParam param
    ) {
        Assert.isParameterError(!(ListUtils.isNotEmpty(param.getLayers())), MsgBinding.TEMP_TREND_LAYER_REQUIRED);
        Map<String, WarehouseTemperatureTrendConfigSaveParam.Layer> layerByCode = new LinkedHashMap<>();
        for (WarehouseTemperatureTrendConfigSaveParam.Layer layer : param.getLayers()) {
            Assert.isParameterError(layer == null, MsgBinding.TEMP_TREND_LAYER_REQUIRED);
            Assert.isParameterError(!(StringUtils.isNotBlank(layer.getLayerCode())), MsgBinding.TEMP_TREND_LAYER_CODE_REQUIRED);
            String layerCode = layer.getLayerCode().trim();
            Assert.isParameterError(!(LAYER_DEFINITION_BY_CODE.containsKey(layerCode)), MsgBinding.TEMP_TREND_LAYER_UNSUPPORTED);
            Assert.isParameterError(layerByCode.containsKey(layerCode), MsgBinding.TEMP_TREND_LAYER_DUPLICATED);
            layer.setLayerCode(layerCode);
            layer.setLayerAggType(requireAggType(layer.getLayerAggType()));
            Assert.isParameterError(layer.getHighAlarmThreshold() == null, MsgBinding.TEMP_TREND_ALARM_THRESHOLD_REQUIRED);
            Assert.isParameterError(layer.getLowAlarmThreshold() == null, MsgBinding.TEMP_TREND_ALARM_THRESHOLD_REQUIRED);
            layerByCode.put(layerCode, layer);
        }
        Assert.isParameterError(!(layerByCode.keySet().containsAll(LAYER_DEFINITION_BY_CODE.keySet())), MsgBinding.TEMP_TREND_LAYER_INCOMPLETE);
        return layerByCode;
    }

    private WarehouseTemperatureAggType requireAggType(WarehouseTemperatureAggType aggType) {
        Assert.isParameterError(aggType == null, MsgBinding.TEMP_TREND_AGG_TYPE_REQUIRED);
        return aggType;
    }

    private Set<Long> validateCableIds(Long warehouseId, Map<String, WarehouseTemperatureTrendConfigSaveParam.Layer> layerByCode) {
        Set<Long> allCableIds = new HashSet<>();
        for (WarehouseTemperatureTrendConfigSaveParam.Layer layer : layerByCode.values()) {
            List<Long> cableIds = layer.getSelectedCableIds() == null ? List.of() : layer.getSelectedCableIds();
            Set<Long> currentLayerCableIds = new HashSet<>();
            for (Long cableId : cableIds) {
                Assert.isParameterError(cableId == null, MsgBinding.CABLE_ID_REQUIRED);
                Assert.isParameterError(!(currentLayerCableIds.add(cableId)), MsgBinding.TEMP_TREND_CABLE_DUPLICATED);
                Assert.isParameterError(!(allCableIds.add(cableId)), MsgBinding.TEMP_TREND_CABLE_CROSS_LAYER_DUPLICATED);
            }
            layer.setSelectedCableIds(new ArrayList<>(currentLayerCableIds));
        }
        if (SetUtils.isEmpty(allCableIds)) {
            return allCableIds;
        }
        List<WarehouseCable> cables = warehouseCableService.list(Wrappers.<WarehouseCable>lambdaQuery()
                .select(WarehouseCable::getId)
                .eq(WarehouseCable::getWarehouseId, warehouseId)
                .in(WarehouseCable::getId, allCableIds));
        Set<Long> existingCableIds = ListUtils.isEmpty(cables) ? Set.of()
                : SetUtils.listToSet(cables, WarehouseCable::getId);
        Assert.isParameterError(!(existingCableIds.containsAll(allCableIds)), MsgBinding.TEMP_TREND_CABLE_NOT_FOUND);
        return allCableIds;
    }

    private void fillLayerConfig(WarehouseTemperatureTrendLayerConfig layer, WarehouseTemperatureTrendConfig config,
                                 LayerDefinition definition, WarehouseTemperatureTrendConfigSaveParam.Layer incoming
                                ) {
        layer.setWarehouseId(config.getWarehouseId());
        layer.setConfigId(config.getId());
        layer.setLayerCode(definition.code());
        layer.setLayerName(StringUtils.isBlank(incoming.getLayerName()) ? definition.layerName() : incoming.getLayerName().trim());
        layer.setPositionName(StringUtils.isBlank(incoming.getPositionName()) ? definition.positionName() : incoming.getPositionName().trim());
        layer.setHighAlarmThreshold(incoming.getHighAlarmThreshold());
        layer.setLowAlarmThreshold(incoming.getLowAlarmThreshold());
        layer.setLayerAggType(incoming.getLayerAggType());
        layer.setSortNo(definition.sortNo());
    }

    private List<WarehouseTemperatureTrendLayerCable> buildLayerCableRows(
            WarehouseTemperatureTrendConfig config,
            Map<String, WarehouseTemperatureTrendLayerConfig> savedLayerByCode,
            Map<String, WarehouseTemperatureTrendConfigSaveParam.Layer> layerByCode
    ) {
        List<WarehouseTemperatureTrendLayerCable> cableRows = new ArrayList<>();
        for (LayerDefinition definition : LAYER_DEFINITIONS) {
            WarehouseTemperatureTrendLayerConfig layer = savedLayerByCode.get(definition.code());
            WarehouseTemperatureTrendConfigSaveParam.Layer incoming = layerByCode.get(definition.code());
            List<Long> cableIds = incoming.getSelectedCableIds() == null ? List.of() : incoming.getSelectedCableIds();
            for (Long cableId : cableIds.stream().sorted(Comparator.naturalOrder()).toList()) {
                var row = new WarehouseTemperatureTrendLayerCable();
                row.setId(IdWorker.getId());
                row.setWarehouseId(config.getWarehouseId());
                row.setConfigId(config.getId());
                row.setLayerConfigId(layer.getId());
                row.setCableId(cableId);
                cableRows.add(row);
            }
        }
        return cableRows;
    }

    private void assertWarehouseBoundAndExists(Long warehouseId) {
        boolean bound = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, warehouseId)
                .exists();
        Assert.isParameterError(!(bound), MsgBinding.WAREHOUSE_NOT_BOUND);
        Assert.isParameterError(!(warehousePageApiClient.fetchPageItemById(warehouseId).isPresent()), MsgBinding.WAREHOUSE_INFO_NOT_FOUND);
    }

    private record LayerDefinition(String code, String layerName, String positionName, Integer sortNo) {
    }
}

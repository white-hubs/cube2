package com.guhecloud.cube.customization.xialing.server.communal.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.customization.xialing.server.communal.constants.DeviceStatisticsDictionary;
import com.guhecloud.cube.customization.xialing.server.communal.service.XiaNingDeviceStatService;
import com.guhecloud.cube.customization.xialing.server.communal.vo.DeviceStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class XiaNingDeviceStatServiceImpl implements XiaNingDeviceStatService {
    private static final int DEVICE_PAGE_SIZE = 1000;
    private static final String DEVICE_STAT_PRODUCT_CONFIG_MISSING = "设备统计产品配置不存在";
    private static final String DEVICE_STAT_PRODUCT_CONFIG_EMPTY = "设备统计产品配置 variable 不能为空";
    private static final String DEVICE_STAT_PRODUCT_EMPTY = "设备统计产品 id 不能为空";

    private final DataDictionaryClient dataDictionaryClient;
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Override
    public DeviceStatisticsVO statistics() {
        Set<Long> productIds = loadProductIds();
        DeviceStatisticsVO result = DeviceStatisticsVO.empty();
        for (Long productId : productIds) {
            appendProductStatistics(result, productId);
        }
        return result;
    }

    private Set<Long> loadProductIds() {
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode(DeviceStatisticsDictionary.ITEM_THIRD_CONFIG);
        param.setCode(DeviceStatisticsDictionary.CODE_DEVICE_STAT_PRODUCT);
        var dictionary = dataDictionaryClient.getDataDictionaryByItemAndCode(param);
        Assert.isNotFound(dictionary, DEVICE_STAT_PRODUCT_CONFIG_MISSING);
        Assert.isParameterError(StringUtils.isBlank(dictionary.getVariable()), DEVICE_STAT_PRODUCT_CONFIG_EMPTY);

        List<Long> productIds = parseProductIds(dictionary.getVariable());
        Assert.isParameterError(ListUtils.isEmpty(productIds), DEVICE_STAT_PRODUCT_EMPTY);
        Set<Long> result = productIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Assert.isParameterError(SetUtils.isEmpty(result), DEVICE_STAT_PRODUCT_EMPTY);
        return result;
    }

    private List<Long> parseProductIds(String variable) {
        try {
            return JsonUtils.deserialize(variable, new TypeReference<List<Long>>() {});
        } catch (RuntimeException e) {
            log.error("设备统计产品配置解析出错：", e);
            return List.of();
        }
    }

    private void appendProductStatistics(DeviceStatisticsVO result, Long productId) {
        int pageNumber = 1;
        while (true) {
            DeviceFilterPageParam param = new DeviceFilterPageParam();
            param.setProductId(productId);
            param.setPageNumber(pageNumber);
            param.setPageSize(DEVICE_PAGE_SIZE);
            Pagination<DevicePageInfoVO> page = deviceCenterStdClient.deviceInfoPage(param);
            List<DevicePageInfoVO> records = page == null ? List.of() : page.getRecords();
            if (ListUtils.isEmpty(records)) {
                break;
            }

            appendRecords(result, records);
            Long pages = page.getPages();
            if (pages == null || pageNumber >= pages) {
                break;
            }
            pageNumber++;
        }
    }

    private void appendRecords(DeviceStatisticsVO result, List<DevicePageInfoVO> records) {
        long onlineCount = records.stream()
                .map(DevicePageInfoVO::getStatus)
                .filter(Objects::nonNull)
                .filter(DeviceBaseArchivesInfoDTO.Status::isOnlineState)
                .count();
        result.add(records.size(), onlineCount);
    }
}

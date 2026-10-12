package com.guhecloud.cube.biz.adapter.park.server.application.oasis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.oasis.client.OasisClient;
import com.guhecloud.cube.biz.adapter.park.server.application.oasis.config.OasisConfig;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OasisParkingService extends AbstractParkingService {
    private static final String MANUFACTURER = "oasis";
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int PARKING_INFO_TOKEN_CODE = 23;
    private static final int PARKING_IN_TOKEN_CODE = 37;
    private static final int PARKING_OUT_TOKEN_CODE = 40;
    private static final int PARKING_PAY_TOKEN_CODE = 41;
    private static final int QUERY_PAGE_SIZE = 1000;

    private final OasisClient oasisClient;
    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() {
        OasisConfig config = config();
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("Status", "1");
        List<JsonNode> records = page(config, PARKING_INFO_TOKEN_CODE,
                                      "/api/E8/sys-park-info/get-park-tree-table", query);
        List<ParkingInfoVO> result = new ArrayList<>();
        for (JsonNode record : records) {
            String id = text(record, "Id");
            if (!includesParking(config, id)) {
                continue;
            }
            int total = integer(record, "TotalParkingLot");
            int remain = integer(record, "RemainParkingLot");
            result.add(new ParkingInfoVO(id, text(record, "Name"), total, Math.max(total - remain, 0), remain));
        }
        return result;
    }

    @Override
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        OasisConfig config = config();
        return forEachParking(config, query -> {
            put(query, "PlateNO", param.getVehicleNo());
            put(query, "EnterTimeStart", param.getInBeginTime());
            put(query, "EnterTimeEnd", param.getInEndTime());
            List<ParkingInRecordVO> result = new ArrayList<>();
            for (JsonNode record : page(config, PARKING_IN_TOKEN_CODE,
                                        "/api/E8/park-enter-record/get-page-list", query)) {
                result.add(toParkingInRecord(record));
            }
            return result;
        });
    }

    @Override
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        OasisConfig config = config();
        return forEachParking(config, query -> {
            put(query, "PlateNO", param.getVehicleNo());
            put(query, "OutTimeStart", param.getOutBeginTime());
            put(query, "OutTimeEnd", param.getOutEndTime());
            List<ParkingOutRecordVO> result = new ArrayList<>();
            for (JsonNode record : page(config, PARKING_OUT_TOKEN_CODE,
                                        "/api/E8/park-out-record/get-page-list-complete-record", query)) {
                result.add(toParkingOutRecord(record));
            }
            return result;
        });
    }

    @Override
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        OasisConfig config = config();
        return forEachParking(config, query -> {
            put(query, "PlateNO", param.getVehicleNo());
            put(query, "PayTimeStart", param.getBeginTime());
            put(query, "PayTimeEnd", param.getBndTime());
            query.put("Status", "1");
            List<ParkingPayRecordVO> result = new ArrayList<>();
            for (JsonNode record : page(config, PARKING_PAY_TOKEN_CODE,
                                        "/api/E8/park-pay-bill/get-page-list-temp-pay-bill", query)) {
                result.add(toParkingPayRecord(record));
            }
            return result;
        });
    }

    private List<JsonNode> page(OasisConfig config, int tokenCode, String path, Map<String, Object> filters) {
        Map<String, Object> query = new LinkedHashMap<>(filters);
        query.put("pageNum", 1);
        query.put("pageSize", QUERY_PAGE_SIZE);
        JsonNode records = oasisClient.get(config, tokenCode, path, query).path("records");
        List<JsonNode> result = new ArrayList<>();
        if (records.isArray()) {
            records.forEach(result::add);
        }
        return result;
    }

    private ParkingInRecordVO toParkingInRecord(JsonNode source) {
        ParkingInRecordVO target = new ParkingInRecordVO();
        target.setId(longValue(source, "Id"));
        target.setIdentifyId(text(source, "RecordId"));
        target.setVehicleNo(text(source, "PlateNO"));
        target.setParkingNo(text(source, "ParkId"));
        target.setParkingName(text(source, "ParkName"));
        target.setChannelNo(text(source, "ChannelId"));
        target.setChannelName(text(source, "ChannelName"));
        target.setInTime(dateTime(source, "EnterTime"));
        target.setInImageUrl(firstNotBlank(text(source, "EnterPictureUrl"), text(source, "EnterPicture")));
        target.setVehicleType(text(source, "CarType"));
        target.setParkingInfoCode(text(source, "ParkId"));
        target.setParkingInfoName(text(source, "ParkName"));
        target.setTenant(tenant());
        return target;
    }

    private ParkingOutRecordVO toParkingOutRecord(JsonNode source) {
        ParkingOutRecordVO target = new ParkingOutRecordVO();
        target.setId(longValue(source, "Id"));
        target.setIdentifyId(firstNotBlank(text(source, "EnterRecordId"), text(source, "RecordId")));
        target.setVehicleNo(text(source, "PlateNO"));
        target.setParkingNo(text(source, "ParkId"));
        target.setParkingName(text(source, "ParkName"));
        target.setChannelNo(text(source, "OutChannelId"));
        target.setChannelName(text(source, "OutChannelName"));
        target.setOutTime(dateTime(source, "OutTime"));
        target.setOutImageUrl(firstNotBlank(text(source, "OutPictureUrl"), text(source, "OutPicture")));
        target.setVehicleType(text(source, "CarType"));
        target.setParkingInfoCode(text(source, "ParkId"));
        target.setParkingInfoName(text(source, "ParkName"));
        target.setTenant(tenant());
        return target;
    }

    private ParkingPayRecordVO toParkingPayRecord(JsonNode source) {
        ParkingPayRecordVO target = new ParkingPayRecordVO();
        target.setId(longValue(source, "Id"));
        target.setIdentifyId(firstNotBlank(text(source, "EnterRecordId"), text(source, "OutRecordId")));
        target.setVehicleNo(text(source, "PlateNO"));
        target.setParkingNo(text(source, "ParkId"));
        target.setParkingName(text(source, "ParkName"));
        LocalDateTime inTime = dateTime(source, "InTime");
        LocalDateTime payTime = dateTime(source, "PayTime");
        target.setParkingTimes(durationMinutes(inTime, payTime == null ? dateTime(source, "FeesTime") : payTime));
        target.setReceivableAmount(decimal(source, "TotalFee"));
        target.setDiscountAmount(decimal(source, "BenefitFee").add(decimal(source, "DerateFee"))
                                         .add(decimal(source, "FreeFee")));
        target.setFreeAmount(decimal(source, "AccountReceived"));
        target.setPayType(firstNotBlank(text(source, "PayTypeName"), text(source, "PayTypeDesc")));
        target.setPayTime(payTime);
        target.setTenant(tenant());
        return target;
    }

    private OasisConfig config() {
        String cacheKey = SubsystemTypeConstants.PARKING_SERVER + ":" + MANUFACTURER + ":" + tenant();
        SubsystemAdapterConfig adapter = subsystemRedisUtils.getSubAdapterRefreshConfig(cacheKey);
        if (adapter == null) {
            throw new ServiceException("未配置绿洲停车厂家信息");
        }
        OasisConfig config = JsonUtils.deserialize(adapter.getManufacturerJson(), OasisConfig.class);
        if (config == null) {
            throw new ServiceException("绿洲停车厂家配置格式错误");
        }
        if (StringUtils.isBlank(config.getApiBaseUrl())) {
            config.setApiBaseUrl(adapterBaseUrl(adapter));
        }
        if (StringUtils.isBlank(config.getTokenBaseUrl()) || StringUtils.isBlank(config.getApiBaseUrl())) {
            throw new ServiceException("绿洲停车Token或API服务地址未配置");
        }
        if (StringUtils.isBlank(config.getAppId())) {
            config.setAppId("datauser");
        }
        return config;
    }

    private static String adapterBaseUrl(SubsystemAdapterConfig adapter) {
        if (StringUtils.isBlank(adapter.getServiceAddr())) {
            return null;
        }
        String url = adapter.getServiceAddr();
        if (StringUtils.isNotBlank(adapter.getServicePort()) && !url.matches(".*:\\d+/?$")) {
            url += ":" + adapter.getServicePort();
        }
        if (StringUtils.isNotBlank(adapter.getServiceAddrSuffix())) {
            url = url.replaceFirst("/+$", "") + "/" + adapter.getServiceAddrSuffix().replaceFirst("^/+", "");
        }
        return url;
    }

    private <T> List<T> forEachParking(OasisConfig config, Function<Map<String, Object>, List<T>> loader) {
        if (config.getParkingIds() == null || config.getParkingIds().isEmpty()) {
            return loader.apply(new LinkedHashMap<>());
        }
        List<T> result = new ArrayList<>();
        for (String parkingId : config.getParkingIds()) {
            Map<String, Object> query = new LinkedHashMap<>();
            query.put("ParkId", parkingId);
            result.addAll(loader.apply(query));
        }
        return result;
    }

    private static boolean includesParking(OasisConfig config, String parkingId) {
        return config.getParkingIds() == null || config.getParkingIds().isEmpty()
               || config.getParkingIds().contains(parkingId);
    }

    private static void put(Map<String, Object> target, String key, Object value) {
        if (value != null && (!(value instanceof String text) || StringUtils.isNotBlank(text))) {
            target.put(key, value);
        }
    }

    private static String text(JsonNode source, String field) {
        JsonNode value = source.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    private static int integer(JsonNode source, String field) {
        String value = text(source, field);
        return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
    }

    private static Long longValue(JsonNode source, String field) {
        String value = text(source, field);
        return StringUtils.isBlank(value) ? null : Long.valueOf(value);
    }

    private static BigDecimal decimal(JsonNode source, String field) {
        String value = text(source, field);
        return StringUtils.isBlank(value) ? BigDecimal.ZERO : new BigDecimal(value);
    }

    private static LocalDateTime dateTime(JsonNode source, String field) {
        String value = text(source, field);
        return StringUtils.isBlank(value) ? null : LocalDateTime.parse(value, DATETIME_FORMATTER);
    }

    private static String firstNotBlank(String first, String second) {
        return StringUtils.isNotBlank(first) ? first : second;
    }

    private static Integer durationMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || end.isBefore(start)) {
            return 0;
        }
        long minutes = Duration.between(start, end).toMinutes();
        return minutes > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) minutes;
    }

    private static Integer tenant() {
        return SessionContextHolder.getRequester().getTenant();
    }
}

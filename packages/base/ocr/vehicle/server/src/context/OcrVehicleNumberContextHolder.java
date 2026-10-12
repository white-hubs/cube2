package com.guhecloud.base.ocr.vehicle.server.context;

import com.guhecloud.base.ocr.vehicle.server.enums.VehicleOcrManufactureEnum;
import com.guhecloud.base.ocr.vehicle.server.third.VehicleOcr;
import com.guhecloud.base.ocr.vehicle.server.third.VehicleOcrManufacture;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OcrVehicleNumberContextHolder {
    private final static Map<VehicleOcrManufactureEnum, VehicleOcr> vehicleOcrMap = new HashMap<>();
    private final List<VehicleOcrManufacture> vehicleOcrManufactureList;

    @PostConstruct
    public void init() {
        if (ListUtils.isNotEmpty(vehicleOcrManufactureList)) {
            vehicleOcrMap.putAll(MapUtils.listToMap(vehicleOcrManufactureList, VehicleOcrManufacture::getManufacture, vehicleOcrManufacture -> vehicleOcrManufacture));
        }
    }

    public VehicleOcr get(@NotNull VehicleOcrManufactureEnum vehicleOcrManufactureEnum) {
        return vehicleOcrMap.get(vehicleOcrManufactureEnum);
    }
}

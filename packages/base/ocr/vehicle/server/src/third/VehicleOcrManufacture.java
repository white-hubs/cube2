package com.guhecloud.base.ocr.vehicle.server.third;

import com.guhecloud.base.ocr.vehicle.server.enums.VehicleOcrManufactureEnum;

public abstract class VehicleOcrManufacture implements VehicleOcr {
    public abstract VehicleOcrManufactureEnum getManufacture();
}

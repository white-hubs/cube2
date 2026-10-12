package com.guhecloud.base.ocr.vehicle.server.third;

import com.guhecloud.base.ocr.vehicle.server.third.dto.OcrVehicleDTO;
import com.guhecloud.base.ocr.vehicle.server.third.dto.res.OcrVehicleRes;

public interface VehicleOcr {
    OcrVehicleRes ocr(OcrVehicleDTO ocrVehicleDTO);

}

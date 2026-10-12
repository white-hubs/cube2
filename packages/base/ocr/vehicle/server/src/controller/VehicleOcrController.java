package com.guhecloud.base.ocr.vehicle.server.controller;

import com.guhecloud.base.ocr.vehicle.server.service.VehicleOcrService;
import com.guhecloud.base.ocr.vehicle.server.third.dto.req.OcrVehicleReq;
import com.guhecloud.base.ocr.vehicle.server.third.dto.res.OcrVehicleRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 车牌ocr识别
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class VehicleOcrController {
    private final VehicleOcrService vehicleOcrService;

    /**
     * 车牌号码识别
     */
    @PostMapping("ocr/vehicle-number")
    public OcrVehicleRes ocrVehicleNumber(@RequestBody @Valid OcrVehicleReq req) {

        return vehicleOcrService.ocrVehicleNumber(req);
    }
}

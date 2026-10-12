package com.guhecloud.base.ocr.vehicle.server.service;

import com.guhecloud.base.ocr.vehicle.server.context.OcrVehicleNumberContextHolder;
import com.guhecloud.base.ocr.vehicle.server.enums.OcrFileProvideType;
import com.guhecloud.base.ocr.vehicle.server.third.dto.OcrVehicleDTO;
import com.guhecloud.base.ocr.vehicle.server.third.dto.req.OcrVehicleReq;
import com.guhecloud.base.ocr.vehicle.server.third.dto.res.OcrVehicleRes;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleOcrService {
    private final OcrVehicleNumberContextHolder vehicleNumberContextHolder;
    private final SysDictionaryAdapter sysDictionaryAdapter;
    private final static String VEHICLE_NUMBER_OCR_DICT_ITEM_CODE = "VEHICLE_NUMBER_OCR";

    public OcrVehicleRes ocrVehicleNumber(OcrVehicleReq req) {
        //参数检查
        Assert.isParameterError(StringUtils.isBlank(req.getImgUrl()) && StringUtils.isBlank(req.getImgBase64()), "请选择识别方式！");
        Assert.isParameterError(req.getOcrFileProvideType().equals(OcrFileProvideType.URL) &&
                                StringUtils.isBlank(req.getImgUrl()), "OCR文件提供类型为URL时，url不能为空！");
        Assert.isParameterError(req.getOcrFileProvideType().equals(OcrFileProvideType.BASE64) &&
                                StringUtils.isBlank(req.getImgBase64()), "OCR文件提供类型为Base64时，Base64不能为空！");
        Assert.isParameterError(req.getOcrFileProvideType().equals(OcrFileProvideType.FILE) &&
                                req.getImgFile() == null, "OCR文件提供类型为FILE时，FILE不能为空！");

        var vehicleOcr = vehicleNumberContextHolder.get(req.getVehicleOcrManufacture());
        Assert.isParameterError(vehicleOcr == null, String.format("车牌识别厂家未找到,厂家:%s", req.getVehicleOcrManufacture()));
        var sysDictionary = sysDictionaryAdapter.getSysDictionaryByCodeAndItemCode(VEHICLE_NUMBER_OCR_DICT_ITEM_CODE, req
                .getVehicleOcrManufacture().name());
        Assert.isParameterError(sysDictionary == null, String.format("车牌识别厂家配置未找到,厂家:%s", req.getVehicleOcrManufacture()));
        return vehicleOcr.ocr(OcrVehicleDTO
                                      .builder().ocrConfig(sysDictionary.getVariable()).ocrFileProvideType(req.getOcrFileProvideType())
                                      .imgUrl(req.getImgUrl())
                                      .imgFile(req.getImgFile()).imgBase64(req.getImgBase64())
                                      .imgFileVO(req.getImgFileVO())
                                      .build());
    }
}

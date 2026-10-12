package com.guhecloud.base.ocr.vehicle.server.third.dto.req;

import com.guhecloud.base.ocr.vehicle.server.enums.OcrFileProvideType;
import com.guhecloud.base.ocr.vehicle.server.enums.VehicleOcrManufactureEnum;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OcrVehicleReq {
    /**
     * 车牌识别厂家
     */
    @NotNull(message = "厂家不能为空")
    private VehicleOcrManufactureEnum vehicleOcrManufacture;
    /**
     * 文件提供类型
     */
    @NotNull(message = "识别文件提供类型不能为空")
    private OcrFileProvideType ocrFileProvideType;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 图片base64编码
     */
    private String imgBase64;
    /**
     * 图片文件方式
     */
    private MultipartFile imgFile;

    /**
     * 图片文件 fileVO
     */
    private FileVO imgFileVO;
}

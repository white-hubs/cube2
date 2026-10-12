package com.guhecloud.base.ocr.vehicle.server.third.dto;

import com.guhecloud.base.ocr.vehicle.server.enums.OcrFileProvideType;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class OcrVehicleDTO {
    private OcrFileProvideType ocrFileProvideType;
    private String ocrConfig;
    private String imgUrl;
    private MultipartFile imgFile;
    private String imgBase64;
    private FileVO imgFileVO;
}

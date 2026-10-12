package com.guhecloud.base.ocr.vehicle.server.third.ali;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.ocr_api20210707.AsyncClient;
import com.aliyun.sdk.service.ocr_api20210707.models.RecognizeCarNumberRequest;
import com.aliyun.sdk.service.ocr_api20210707.models.RecognizeCarNumberResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.ocr.vehicle.server.enums.OcrFileProvideType;
import com.guhecloud.base.ocr.vehicle.server.enums.VehicleOcrManufactureEnum;
import com.guhecloud.base.ocr.vehicle.server.third.VehicleOcrManufacture;
import com.guhecloud.base.ocr.vehicle.server.third.dto.OcrVehicleDTO;
import com.guhecloud.base.ocr.vehicle.server.third.dto.res.OcrVehicleRes;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class AliVehicleOcr extends VehicleOcrManufacture {
    private static final List<String> SUPPORT_FILE_TYPE = List.of("jpg", "jpeg", "png", "bmp", "gif", "tiff", "webp");

    @Override
    public OcrVehicleRes ocr(OcrVehicleDTO ocrVehicleDTO) {
        var fileVo = ocrVehicleDTO.getImgFileVO();
        if (Objects.nonNull(fileVo)) {
            var suffix = fileVo.getSuffix();
            Assert.isUnsupported(!SUPPORT_FILE_TYPE.contains(suffix), "阿里云车牌识别不支持该图片类型！");
        }
        var ocrConfig = JsonUtils.deserialize(ocrVehicleDTO.getOcrConfig(), AliDictConfig.class);
        Assert.isParameterError(ocrConfig == null, "阿里云车牌识别配置不能为空！");
        ocrConfig.check();
        String accessKey = ocrConfig.getAccessKey();
        String secretKey = ocrConfig.getSecretKey();
        var credential = Credential.builder().accessKeyId(accessKey).accessKeySecret(secretKey).build();
        StaticCredentialProvider provider = StaticCredentialProvider.
                create(credential);
        try (AsyncClient client = AsyncClient.builder()
                .region(ocrConfig.getRegion()) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(ocrConfig.getEndpoint())
                                .setConnectTimeout(Duration.ofSeconds(30))
                                      )
                .build()) {

            RecognizeCarNumberResponse resp;
            try {
                var recognizeCarNumberRequestBuilder = RecognizeCarNumberRequest.builder();
                if (ocrVehicleDTO.getOcrFileProvideType().equals(OcrFileProvideType.URL)) {
                    recognizeCarNumberRequestBuilder.url(ocrVehicleDTO.getImgUrl());
                } else if (ocrVehicleDTO.getOcrFileProvideType().equals(OcrFileProvideType.FILE)) {
                    recognizeCarNumberRequestBuilder.body(ocrVehicleDTO.getImgFile().getInputStream());
                } else {
                    throw new ServiceException("阿里云车牌识别暂不支持Base64！");
                }

                CompletableFuture<RecognizeCarNumberResponse> response = client.recognizeCarNumber(recognizeCarNumberRequestBuilder.build());
                resp = response.get();
                var dataMap = JsonUtils.deserialize(resp.getBody().getData(), new TypeReference<Map<String, Object>>() {});
                var vehicleDataMap = JsonUtils.deserialize(dataMap.get("data"), new TypeReference<Map<String, Object>>() {});
                var vehicleTypeInfo = JsonUtils.deserialize(vehicleDataMap.get("secondary_cls"), new TypeReference<Map<String, Object>>() {});
                var vehicleType = (String) vehicleTypeInfo.get("type_str");
                var vehicleNumberInfo = JsonUtils.deserialize(vehicleDataMap.get("data"), new TypeReference<Map<String, Object>>() {});
                var vehicleNumber = (String) vehicleNumberInfo.get("车牌");
                return OcrVehicleRes.builder().vehicleNumber(vehicleNumber).vehicleType(vehicleType).build();
            } catch (Exception e) {
                log.error("阿里云车牌识别异常！", e);
                throw new ServiceException("该图片不符合识别类型，请重新上传！");
            }
        }
    }

    @Override
    public VehicleOcrManufactureEnum getManufacture() {
        return VehicleOcrManufactureEnum.ALI;
    }
}
package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.converter;

import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeTypeRequest;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;

import java.util.Collection;
import java.util.List;

public class CloudQrCodeTypeConverter {

    private CloudQrCodeTypeConverter() {
    }

    /**
     * 构建创建请求
     */
    public static CreateQrCodeTypeRequest convertPOToCreateRequest(QrcodeEntityTypePO po) {
        var request = new CreateQrCodeTypeRequest();
        request.setCode(po.getEntityType());
        request.setName(po.getTypeName());
        request.setRemark("实体类型：" + po.getEntityType());
        return request;
    }

    /**
     * 构建创建请求
     */
    public static List<CreateQrCodeTypeRequest> convertPOToCreateRequests(Collection<QrcodeEntityTypePO> pos) {
        return pos.stream().map(CloudQrCodeTypeConverter::convertPOToCreateRequest).toList();
    }

    /**
     * 构建更新请求
     */
    public static UpdateQrCodeTypeRequest convertPOToUpdateRequest(QrcodeEntityTypePO po) {
        var request = new UpdateQrCodeTypeRequest();
        request.setCode(po.getEntityType());
        request.setName(po.getTypeName());
        request.setRemark("实体类型：" + po.getEntityType());
        return request;
    }

    public static List<UpdateQrCodeTypeRequest> convertPOToUpdateRequests(Collection<QrcodeEntityTypePO> pos) {
        return pos.stream().map(CloudQrCodeTypeConverter::convertPOToUpdateRequest).toList();
    }
}

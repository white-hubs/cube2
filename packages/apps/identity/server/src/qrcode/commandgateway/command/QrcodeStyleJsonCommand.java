package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bq
 * @date 2022/7/20 13:30
 * @description
 */
@Data
public class QrcodeStyleJsonCommand {

    private String name;

    private String width;

    private String height;

    private String backgroundColor;

    private String backgroundImage;

    private Long imgId;

    private List<BoxInfo> layers;

    @Data
    public static class BoxInfo {
        private String id;
        private Long imgId;
        private String type;
        private String textType;
        private String entityType;
        private String entityAttributesVlaue;
        private String text;
        private String url;
        private Object option;
        private String index;
    }

    public List<Long> getImageIds() {
        List<Long> fileIds = new ArrayList<>();
        // 整体背景图
        Long backgroundId = this.imgId;
        if (null != backgroundId) {
            fileIds.add(backgroundId);
        }
        // 小图
        List<QrcodeStyleJsonCommand.BoxInfo> layers = this.layers;
        if (layers != null) {
            layers.forEach(boxInfo -> {
                if (QrcodeEnum.imgBox.name().equals(boxInfo.getType())) {
                    Long imageId = boxInfo.getImgId();
                    if (null != imageId) {
                        fileIds.add(imageId);
                    }
                }
            });
        }
        return fileIds;
    }

}

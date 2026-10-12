package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import java.math.BigDecimal;

/**
 * 海康门禁远程核验人脸框。
 *
 * @param height 人脸框高度，占图片高度比例
 * @param width 人脸框宽度，占图片宽度比例
 * @param x 人脸框左上角横坐标，占图片宽度比例
 * @param y 人脸框左上角纵坐标，占图片高度比例
 */
public record HikRemoteCheckFaceRectRequest(
        BigDecimal height,
        BigDecimal width,
        BigDecimal x,
        BigDecimal y
) {
}

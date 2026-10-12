package com.guhecloud.platform.base.util;

import com.google.common.collect.ImmutableMap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 图形码工具类
 *
 * @author Woody
 * @date 2021/7/27
 */
public final class BarcodeUtils {
    /**
     * 默认文件格式名称
     */
    public static final String DEFAULT_FORMAT_NAME = "png";

    /**
     * 默认base64字符串前缀
     */
    public static final String DEFAULT_BASE64_PREFIX = String.format("data:image/%s;base64,", DEFAULT_FORMAT_NAME);

    private BarcodeUtils() {
    }

    /**
     * 二维码内容编码
     *
     * @param content 二维码内容
     *
     * @return 图片对象
     */
    public static BufferedImage qrcode(String content) {
        return qrcode(content, 200, 200);
    }

    /**
     * 二维码内容编码
     *
     * @param content 图片内容
     * @param width   图片宽度
     * @param height  图片高度
     *
     * @return 图片对象
     */
    public static BufferedImage qrcode(String content, int width, int height) {
        return encode(content, BarcodeFormat.QR_CODE, width, height);
    }

    /**
     * 将内容编码
     *
     * @param content 图片内容
     * @param format  图片格式
     * @param width   图片宽度
     * @param height  图片高度
     *
     * @return 图片对象
     */
    private static BufferedImage encode(String content, BarcodeFormat format, int width, int height) {
        AssertUtils.assertTrue(width > 0, "width must be greater than 0");
        AssertUtils.assertTrue(height > 0, "height must be greater than 0");
        Map<EncodeHintType, Object> hints =
                ImmutableMap.of(EncodeHintType.CHARACTER_SET, "utf-8");//大写的UTF-8生成的二维码，扫描枪扫描时，可能出现一个固定前缀]Q2\000026，导致程序出错
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(content, format, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 内容解码
     *
     * @param image 图片对象
     *
     * @return 图形内容
     */
    public static String decode(BufferedImage image) {
        Map<DecodeHintType, Object> hints =
                ImmutableMap.of(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        try {
            return new MultiFormatReader().decode(bitmap, hints).getText();
        } catch (ReaderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将图片对象转换成base64字符串
     *
     * @param image 图片对象
     *
     * @return base64字符串
     */
    public static String image2base64(BufferedImage image) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, DEFAULT_FORMAT_NAME, stream);
            return DEFAULT_BASE64_PREFIX + Base64.encodeBase64String(stream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

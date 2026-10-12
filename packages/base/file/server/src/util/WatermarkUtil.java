package com.guhecloud.cube.base.file.server.util;

import com.guhecloud.platform.base.standard.exception.Assert;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-25  19:15
 * @Description: 图片添加水印
 * @Version: 1.0
 */
public class WatermarkUtil {
    /**
     * 添加水印
     *
     * @param watermarkType 水印类型（1-文字水印 2-图片水印）
     * @param path          图片地址
     * @param watermark     水印内容（文字水印内容/图片水印的存放路径）
     */
    public static MultipartFile addWatermark(Integer watermarkType, String path, String watermark) {
        if (null == watermarkType) {
            throw new RuntimeException("水印类型不能为空");
        }
        // 读取网络图片
        Image image = readNetworkPicture(path);
        return manageWatermark("", image, watermarkType, watermark);
    }

    /**
     * 添加水印
     *
     * @param watermarkType 水印类型（1-文字水印 2-图片水印）
     * @param file          文件信息
     */
    public static MultipartFile addWatermark(MultipartFile file, Integer watermarkType, String watermark) {
        String fileName = file.getOriginalFilename();
        Image image = FileConversionUtil.convertToBufferedImage(file);
        if (null == image) {
            return file;
        }
        return manageWatermark(fileName, image, watermarkType, watermark);
    }

    /**
     * 读取网络图片
     *
     * @param path 网络图片地址
     */
    public static Image readNetworkPicture(String path) {
        Assert.parameterIsNull(path, "图片地址不能为空");
        if (null == path) {
            throw new RuntimeException("网络图片路径不能为空");
        }
        try {
            URL url = new URL(path);
            BufferedImage bugImg = ImageIO.read(url.openStream());
            if (null == bugImg) {
                throw new RuntimeException("网络图片地址不正确");
            }
            return bugImg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 图片水印处理
     *
     * @param fileName  文件名称
     * @param image     图片对象
     * @param type      水印类型（1-文字水印 2-图片水印）
     * @param watermark 水印内容（文字水印内容/图片水印的存放路径）
     */
    public static MultipartFile manageWatermark(String fileName, Image image, Integer type, String watermark) {
        if (null == image) {
            return null;
        }
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufImg.createGraphics();
        // 绘制原始图片
        graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);
        // 校验水印的类型
        if (type == 1) {
            Assert.parameterIsNull(watermark, "水印内容不能为空");
            // 添加文字水印，设置水印背景颜色、透明度
            graphics.setColor(new Color(192, 192, 192));
            // 设置字体  画笔字体样式为微软雅黑，加粗，文字大小为45pt
            int fontSize = BigDecimal.valueOf(imgWidth).divide(new BigDecimal("300"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("12")).intValue();
            if (fontSize == 0) {
                fontSize = 12;
            }
            graphics.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            List<String> waterMarkContentList = new ArrayList<>();
            // 单字符长度
            int tempCharLen = 0;
            // 单行字符总长度临时计算
            int tempLineLen = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < watermark.length(); i++) {
                char tempChar = watermark.charAt(i);
                tempCharLen = getCharLen(tempChar, graphics);
                tempLineLen += tempCharLen;
                // 文字长度大于图片宽度-2*间距-字体大小
                if (tempLineLen > imgWidth - tempCharLen) {
                    // 长度已经满一行
                    waterMarkContentList.add(sb.toString());
                    // 清空内容,重新追加
                    sb.delete(0, sb.length());
                    tempLineLen = 0;
                }
                // 追加字符
                sb.append(tempChar);
            }
            waterMarkContentList.add(sb.toString());
            Collections.reverse(waterMarkContentList);
            // 绘制水印
            for (int i = 0; i < waterMarkContentList.size(); i++) {
                String s = waterMarkContentList.get(i);
                graphics.drawString(s, 0, (waterMarkContentList.size() - i) * fontSize);
            }
            graphics.dispose();
        } else {
            Assert.parameterIsNull(watermark, "水印地址不能为空");
            // 添加图片水印：
            Image srcWatermark = readNetworkPicture(watermark);
            int watermarkWidth = srcWatermark.getWidth(null);
            int watermarkHeight = srcWatermark.getHeight(null);
            // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
            // 绘制水印图片  坐标为中间位置
            graphics.drawImage(srcWatermark, (imgWidth - watermarkWidth) / 2,
                               (imgHeight - watermarkHeight) / 2, watermarkWidth, watermarkHeight, null);
            graphics.dispose();
        }
        try {
            // 将BufferedImage转换为字节数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "png", byteArrayOutputStream);
            // 假设我们以PNG格式保存
            byte[] imageData = byteArrayOutputStream.toByteArray();
            String[] postfix = fileName.split("\\.");
            String mimeType = "application/" + postfix[1];
            return FileConversionUtil.convertByteToMultipartFile(imageData, fileName, mimeType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getCharLen(char c, Graphics2D graphics) {
        return graphics.getFontMetrics(graphics.getFont()).charWidth(c);
    }

    /**
     * 获取字符串占用的宽度
     *
     * @param content 字符串
     * @param metrics 文字大小
     *
     * @return 字符串占用的宽度
     */
    private static int getStrWidth(String content, FontDesignMetrics metrics) {
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }

    /**
     * 字符串拆分
     *
     * @param content
     * @param imgWidth
     *
     * @return
     */
    private static int splitString(String content, int imgWidth, int frontSize) {

        char[] chars = content.toCharArray();
        int fontSize2 = frontSize * 2;

        //字符串索引
        int index = 1;

        for (char c : chars) {
            //所占字符
            int width = 0;
            int len = String.valueOf(c).getBytes().length;
            // 汉字为3,其余1
            // 可能还有一些特殊字符占用2 等等,统统计为汉字·
            if (len != 1) {
                width += fontSize2;
            } else {
                width += frontSize;
            }
            imgWidth = imgWidth - width;
            if (imgWidth <= 0) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * 获取水印文字的长度
     *
     * @param watermarkContent 文字水印内容
     * @param graphics         图像类
     */
    private static int getWatermarkLength(String watermarkContent, Graphics2D graphics) {
        return graphics.getFontMetrics(graphics.getFont()).charsWidth(watermarkContent.toCharArray(), 0, watermarkContent.length());
    }
}

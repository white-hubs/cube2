package com.guhecloud.cube.srv.gen.visitor.management.server.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 生成二维码图片工具
 */
public class QRCodeUtil {

    static Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 将BitMatrix转换为BufferedImage
     *
     * @param matrix BitMatrix对象
     *
     * @return BufferedImage对象
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                                                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 将二维位图写入输出流
     *
     * @param matrix 二维位图对象
     * @param format 图像格式，如"jpg"或"png"等
     * @param stream 输出流对象
     *
     * @throws IOException 当无法向输出流中写入时抛出异常
     */
    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 生成二维码图像字节数组
     *
     * @param text   二维码文本内容
     * @param width  二维码宽度
     * @param height 二维码高度
     * @param format 二维码格式
     *
     * @return 二维码图像字节数组
     *
     * @throws WriterException
     * @throws IOException
     */
    private static ByteArrayOutputStream generateQRCode(String text, int width, int height,
                                                        String format
                                                       ) throws WriterException, IOException {
        // 内容所使用字符集编码
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        QRCodeUtil.writeToStream(bitMatrix, format, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    /**
     * 获取二维码图片字节流
     *
     * @param text 二维码字符串
     *
     * @return
     *
     * @throws Exception
     */
    public static ByteArrayOutputStream generateQRCode(String text) throws Exception {
        return generateQRCode(text, 300, 300, "jpg");
    }

    /**
     * 生成二维码图片
     *
     * @param text      二维码字符串
     * @param width     宽度
     * @param height    高度
     * @param format    图片格式,如jpg,png等
     * @param imagePath 图片地址
     *
     * @throws WriterException
     * @throws IOException
     */
    public static void generateQRCode(String text, int width, int height, String format,
                                      String imagePath
                                     ) throws WriterException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = generateQRCode(text, width, height, format);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imagePath);
            outputStream.write(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}

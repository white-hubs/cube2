package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageUtil {


    /**
     * 巡检图片添加水印
     * @param inputStream
     * @param fileExtName
     * @param date
     * @param userName
     * @param areaNames
     * @param address
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream addInspWatermark(InputStream inputStream, String fileExtName, String date, String userName, String areaNames, String address
    ) throws Exception {
        try {

            //也可以传入文件目录地址读取
//            File file = new File("D:\\1.png");
//            Image srcImg = ImageIO.read(file);
            //把文件转换成图片
            Image srcImg = ImageIO.read(inputStream);
            //获取图片的宽和高
            int srcImgwidth = srcImg.getWidth(null);
            int srcImgheight = srcImg.getHeight(null);

            Font font = new Font("Microsoft Yahei UI", Font.PLAIN, (int)(srcImgheight * 0.02));

            //画水印需要一个画板    创建一个画板
            BufferedImage buffImg = new BufferedImage(srcImgwidth, srcImgheight, BufferedImage.TYPE_INT_RGB);
            //创建一个2D的图像
            Graphics2D g = buffImg.createGraphics();
            setGraphics2D(g);
            g.setFont(font);
            //画出来
            g.drawImage(srcImg, 0, 0, srcImgwidth, srcImgheight, null);


            // 取水印中最长的一条作为水印背景的长度
            int backWidth = getMaxWidthOfTexts(g, font, date, userName, areaNames, address);

            //中间背景颜色
            int x = (int)(srcImgwidth * 0.02);
            int y = (int)(srcImgheight * 0.798);
            Color color = new Color(255,255,255);
            g = buffImg.createGraphics();
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.4F)); // 设置透明度
            g.fillRect(x ,y , backWidth, (int)(srcImgheight * 0.14));


            //内容信息
            Graphics2D fontG = buffImg.createGraphics();
            fontG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // 文字抗锯齿
            setGraphics2D(fontG);
            Color fontColor = new Color(0,0,0);
            fontG.setColor(fontColor);
            fontG.setFont(font);
            String info = "巡检时间：" + " "+ date;
            x = (int)(srcImgwidth * 0.03);
            y = (int)(srcImgheight * 0.83);
            fontG.drawString(info, x, y);

            info  = "巡检人员：" + " "+ userName;
            x = (int)(srcImgwidth * 0.03);
            y = (int)(srcImgheight * 0.86);
            fontG.drawString(info, x, y);

            info  = "所属区域：" + " "+ areaNames;
            x = (int)(srcImgwidth * 0.03);
            y = (int)(srcImgheight * 0.89);
            fontG.drawString(info, x, y);

            info  = "详细地址：" + " "+ address;
            x = (int)(srcImgwidth * 0.03);
            y = (int)(srcImgheight * 0.92);
            fontG.drawString(info, x, y);

//
            //释放画板的资源
            g.dispose();
            fontG.dispose();


//            System.out.println("水印添加完成！");

            //水印文件输出到本地目录
//            FileOutputStream outputStream = new FileOutputStream("D:\\2.png");
//            //创建新的图片
//            ImageIO.write(buffImg, "jpg", outputStream);
//            //刷新流
//            outputStream.flush();
//            //关闭流
//            outputStream.close();

            //输出成流
            //创建一个ByteArrayOutputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //把BufferedImage写入ByteArrayOutputStream
            ImageIO.write(buffImg, fileExtName, os);
            //ByteArrayOutputStream转成InputStream
            ByteArrayInputStream input = new ByteArrayInputStream(os.toByteArray());

            return input;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("添加水印失败");
        }
    }

    /**
     * 获取多个提示文本在图像中最宽的长度
     * @param date
     * @param username
     * @param areaNames
     * @param address
     * @return
     */
    private static int getMaxWidthOfTexts(Graphics2D g, Font font, String date, String username, String areaNames, String address) {
        double multiple = 1.5; // 倍数。在文本长度的基础上将背景稍微长一点
        int maxWidth = getTextWidth(g, font, address);
        int dateWidth = getTextWidth(g, font, date);
        int usernameWidth = getTextWidth(g, font, username);
        int areaNamesWidth = getTextWidth(g, font, areaNames);
        if (dateWidth > maxWidth) {
            maxWidth = dateWidth;
            multiple = 1.8; // 时间是全数字显示，所以倍数要更长一些
        }
        if (usernameWidth > maxWidth) {
            maxWidth = usernameWidth;
        }
        if (areaNamesWidth > maxWidth) {
            maxWidth = areaNamesWidth;
        }
        return (int) (maxWidth * multiple);
    }


    /**
     * 获取文本在图像中的长度
     * @param g
     * @param font
     * @param text
     * @return
     */
    private static int getTextWidth(Graphics2D g, Font font, String text) {
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D stringBounds = font.getStringBounds(text, frc);
        return (int)stringBounds.getWidth();
    }

    /**
     * 获取水印的坐标
     *
     * @param watermarkContent 水印内容
     * @param g          2d图像
     * @return 水印的长度
     */
    public int getwaterMarkLength(String watermarkContent, Graphics2D g) {

        return g.getFontMetrics(g.getFont()).charsWidth(watermarkContent.toCharArray(), 0, watermarkContent.length());

    }

    /**
     * 设置 Graphics2D 属性  （抗锯齿）
     *
     * @param graphics2D
     */
    private static void setGraphics2D(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        graphics2D.setStroke(s);
    }

}

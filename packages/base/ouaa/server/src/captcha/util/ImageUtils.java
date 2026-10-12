package com.guhecloud.cube.biz.ouaa.server.captcha.util;

import com.guhecloud.cube.biz.ouaa.server.captcha.model.common.CaptchaBaseMapEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageUtils {
    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    private static Map<String, String> originalRotateCacheMap = new ConcurrentHashMap();  //旋转底图
    private static Map<String, String> rotateBlockCacheMap = new ConcurrentHashMap(); //旋转块
    private static Map<String, String> originalCacheMap = new ConcurrentHashMap();  //滑块底图
    private static Map<String, String> slidingBlockCacheMap = new ConcurrentHashMap(); //滑块
    private static Map<String, String> picClickCacheMap = new ConcurrentHashMap(); //点选文字
    private static Map<String, String[]> fileNameMap = new ConcurrentHashMap<>();

    public static void cacheImage(String captchaOriginalPathJigsaw, String captchaOriginalPathClick, String captchaOriginalPathRotate) {
        // 旋转拼图
        if (StringUtils.isBlank(captchaOriginalPathRotate)) {
            originalRotateCacheMap.putAll(getResourcesImagesFile("defaultImages/rotate/original"));
            rotateBlockCacheMap.putAll(getResourcesImagesFile("defaultImages/rotate/rotateBlock"));
        } else {
            originalRotateCacheMap.putAll(getImagesFile(captchaOriginalPathRotate + File.separator + "original"));
            rotateBlockCacheMap.putAll(getImagesFile(captchaOriginalPathRotate + File.separator + "rotateBlock"));
        }
        //滑动拼图
        if (StringUtils.isBlank(captchaOriginalPathJigsaw)) {
            originalCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/original"));
            slidingBlockCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/slidingBlock"));
        } else {
            originalCacheMap.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "original"));
            slidingBlockCacheMap.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "slidingBlock"));
        }
        //点选文字
        if (StringUtils.isBlank(captchaOriginalPathClick)) {
            picClickCacheMap.putAll(getResourcesImagesFile("defaultImages/pic-click"));
        } else {
            picClickCacheMap.putAll(getImagesFile(captchaOriginalPathClick));
        }

        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), picClickCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.ROTATE.getCodeValue(), originalRotateCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue(), rotateBlockCacheMap.keySet().toArray(new String[0]));
        logger.info("初始化底图:{}", JsonUtils.serialize(fileNameMap));
    }

    public static void cacheBootImage(Map<String, String> originalMap, Map<String, String> slidingBlockMap,
                                      Map<String, String> picClickMap
                                     ) {
        originalCacheMap.putAll(originalMap);
        slidingBlockCacheMap.putAll(slidingBlockMap);
        picClickCacheMap.putAll(picClickMap);
        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), picClickCacheMap.keySet().toArray(new String[0]));
        logger.info("自定义resource底图:{}", JsonUtils.serialize(fileNameMap));
    }

    public static BufferedImage getRotate() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.ROTATE.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = originalRotateCacheMap.get(strings[randomInt]);
        return getBase64StrToImage(s);
    }

    public static String getRotateBlock() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.ROTATE_BLOCK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = rotateBlockCacheMap.get(strings[randomInt]);
        return s;
    }

    public static BufferedImage getOriginal() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = originalCacheMap.get(strings[randomInt]);
        return getBase64StrToImage(s);
    }

    public static String getslidingBlock() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = slidingBlockCacheMap.get(strings[randomInt]);
        return s;
    }

    public static BufferedImage getPicClick() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue());
        if (null == strings || strings.length == 0) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        String s = picClickCacheMap.get(strings[randomInt]);
        return getBase64StrToImage(s);
    }

    /**
     * 图片转base64 字符串
     *
     * @param templateImage
     *
     * @return
     */
    public static String getImageToBase64Str(BufferedImage templateImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(templateImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(bytes).trim();
    }

    /**
     * base64 字符串转图片
     *
     * @param base64String
     *
     * @return
     */
    public static BufferedImage getBase64StrToImage(String base64String) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(base64String);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:" + path + "/*.png");
            for (Resource resource : resources) {
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String string = Base64Utils.encodeToString(bytes);
                imgMap.put(resource.getFilename(), string);
            }
        } catch (IOException e) {
            logger.error("加载验证码默认图片失败，path={}", path, e);
        }
        return imgMap;
    }

    private static Map<String, String> getImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>();
        File file = new File(path);
        if (!file.exists()) {
            return new HashMap<>();
        }
        File[] files = file.listFiles();
        Arrays.stream(files).forEach(item -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(item);
                byte[] bytes = FileCopyUtils.copyToByteArray(fileInputStream);
                String string = Base64Utils.encodeToString(bytes);
                imgMap.put(item.getName(), string);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return imgMap;
    }

}

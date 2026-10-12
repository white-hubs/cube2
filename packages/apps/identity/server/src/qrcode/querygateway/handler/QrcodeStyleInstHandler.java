package com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.handler;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallCategoryDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDetailDTO;
import com.guhecloud.cube.biz.mdata.client.param.StallPostQueryParam;
import com.guhecloud.cube.property.management.client.property.client.property.PropertyDetailedClient;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedByIdListParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerBatchParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.factory.CloudQrCodeClientFactory;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.EntityInfoCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.dto.QrcodeEntityInfo;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolLocationClient;
import com.guhecloud.cube.srv.gen.maintain.client.ScenePointClient;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/07/28
 * @description 二维码实例化处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeStyleInstHandler {

    private static final String IMG_FORMATE = "png";

    private final DeviceCenterStdClient deviceCenterStdClient;
    private final PatrolLocationClient patrolLocationClient;
    private final StallClient stallClient;
    private final PropertyDetailedClient propertyDetailedClient;
    private final ScenePointClient scenePointClient;
    private final RiskSourceLedgerClient riskSourceLedgerClient;
    private final CloudQrCodeClientFactory cloudQrCodeClientFactory;
    @Value("${qrCode.preUrl}")
    private String BASE_URL;

    /**
     * 实例化二维码样式中的实体
     *
     * @param entityInfo
     * @param code
     * @param json
     *
     * @return
     */
    public String qrcodeStyleToInst(EntityInfoCommand entityInfo, String code, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray layers = jsonObject.getJSONArray("layers");
        layers.forEach(layer -> {
            Map<String, Object> map = (Map) layer;
            String type = (String) map.get("type");
            String textType = (String) map.get("textType");
            String entityType = (String) map.get("entityType");
            String entityAttributesType = (String) map.get("entityAttributesVlaue");
            if (QrcodeEnum.textBox.name().equals(type) && QrcodeEnum.ENTITY_ATTRIBUTES.name().equals(textType)) {
                String attributeValue = this.fillEntityAttribute(entityInfo.getEntityId(), entityInfo.getEntityName(),
                                                                 entityInfo.getBusinessEntity(), entityType, entityAttributesType);
                String textTemplate = (String) map.get("text");
                if (textTemplate.contains("$$" + entityAttributesType + "$$")) {
                    map.put("text", textTemplate.replace("$$" + entityAttributesType + "$$", attributeValue));
                } else {
                    map.put("text", attributeValue);
                }
            }
            if (QrcodeEnum.qrBox.name().equals(type)) {
                map.put("url", null);
            }
        });
        return jsonObject.toJSONString();
    }

    /**
     * 填充二维码图
     */
    private String fillQrcodeInstByCode(Map map, String code) {
        Map component = (Map) map.get("component");
        return this.qrcodeImageBase64(getCloudQrCodeUrl(code), component);
    }

    /**
     * 填充二维码图
     */
    private String fillQrcodeInstByUrl(Map map, String url) {
        Map component = (Map) map.get("component");
        return this.qrcodeImageBase64(url, component);
    }

    /**
     * 获取云二维码url
     *
     * @param code 二维码code
     *
     * @return 云二维码内容，未启用或失败返回null
     */
    private String getCloudQrCodeUrl(String code) {
        try {
            // 获取云二维码客户端
            var client = cloudQrCodeClientFactory.createClient();
            if (client == null) {
                log.debug("当前租户未启用云二维码，使用本地URL");
                return getQrCodeUrl(code);
            }

            // 通过code查询云二维码
            QrCodeDTO qrCode = client.getQrCodeByCode(code);
            if (qrCode == null) {
                log.warn("云二维码不存在， code={}，使用本地URL", code);
                return getQrCodeUrl(code);
            }

            // 返回云二维码内容
            if (StringUtils.isNotBlank(qrCode.getQrContent())) {
                log.debug("成功获取云二维码内容， code={}, content={}", code, qrCode.getQrContent());
                return qrCode.getQrContent();
            }

            log.warn("云二维码内容为空， code={}，使用本地URL", code);
            return getQrCodeUrl(code);

        } catch (Exception e) {
            log.error("获取云二维码失败， code={}，使用本地URL", code, e);
            return getQrCodeUrl(code);
        }
    }

    /**
     * 获取二维码url
     *
     * @param code 二维码code
     *
     * @return 二维码url
     */
    public String getQrCodeUrl(String code) {
        return BASE_URL + code;
    }

    /**
     * 填充base64的二维码图
     */
    public String fillQrcodeBase64(String code, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray layers = jsonObject.getJSONArray("layers");
        layers.forEach(layer -> {
            Map<String, Object> map = (Map) layer;
            String type = (String) map.get("type");
            if (QrcodeEnum.qrBox.name().equals(type)) {
                map.put("url", this.fillQrcodeInstByCode(map, code));
            }
        });
        return jsonObject.toJSONString();
    }

    /**
     * 填充base64的二维码图
     */
    public String fillQrcodeBase64ByUrl(String json, String url) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray layers = jsonObject.getJSONArray("layers");
        layers.forEach(layer -> {
            Map<String, Object> map = (Map) layer;
            String type = (String) map.get("type");
            if (QrcodeEnum.qrBox.name().equals(type)) {
                map.put("url", this.fillQrcodeInstByUrl(map, url));
            }
        });
        return jsonObject.toJSONString();
    }

    /**
     * 批量获取云二维码内容，调用失败或未启用时由调用方回退到本地URL。
     */
    public Map<String, String> batchGetCloudQrCodeContent(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyMap();
        }
        try {
            var client = cloudQrCodeClientFactory.createClient();
            if (client == null) {
                return Collections.emptyMap();
            }
            List<QrCodeDTO> qrCodes = client.getQrCodeByCodes(codes);
            if (CollectionUtils.isEmpty(qrCodes)) {
                return Collections.emptyMap();
            }
            return qrCodes.stream()
                    .filter(qrCode -> StringUtils.isNotBlank(qrCode.getCode()) && StringUtils.isNotBlank(qrCode.getQrContent()))
                    .collect(Collectors.toMap(QrCodeDTO::getCode, QrCodeDTO::getQrContent, (left, right) -> left));
        } catch (Exception e) {
            log.error("批量获取云二维码失败，使用本地URL", e);
            return Collections.emptyMap();
        }
    }

    public byte[] renderQrcodeImage(String json, Map<String, BufferedImage> imageCache) {
        return renderQrcodeImage(json, null, imageCache);
    }

    public byte[] renderQrcodeImage(String json, String qrCodeContent, Map<String, BufferedImage> imageCache) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        int width = toInt(jsonObject.get("width"), 800);
        int height = toInt(jsonObject.get("height"), 400);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setColor(parseColor(jsonObject.getString("backgroundColor"), Color.WHITE));
            graphics.fillRect(0, 0, width, height);
            drawImage(graphics, jsonObject.getString("backgroundImage"), 0, 0, width, height, imageCache);
            JSONArray layers = jsonObject.getJSONArray("layers");
            if (layers != null) {
                layers.stream()
                        .map(layer -> (Map<String, Object>) layer)
                        .sorted((left, right) -> Integer.compare(toInt(left.get("index"), 0), toInt(right.get("index"), 0)))
                        .forEach(layer -> drawLayer(graphics, layer, qrCodeContent, imageCache));
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, IMG_FORMATE, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("二维码图片渲染异常:", e);
            throw new RuntimeException(e);
        } finally {
            graphics.dispose();
        }
    }

    private void drawLayer(Graphics2D graphics, Map<String, Object> layer, String qrCodeContent,
                           Map<String, BufferedImage> imageCache
                          ) {
        Map<String, Object> component = (Map<String, Object>) layer.get("component");
        if (component == null) {
            return;
        }
        String type = (String) layer.get("type");
        int left = toInt(component.get("left"), 0);
        int top = toInt(component.get("top"), 0);
        int width = toInt(component.get("width"), 0);
        int height = toInt(component.get("height"), 0);
        if (width <= 0 || height <= 0) {
            return;
        }
        if (QrcodeEnum.qrBox.name().equals(type) && StringUtils.isNotBlank(qrCodeContent)) {
            drawQrCode(graphics, qrCodeContent, component, left, top, width, height);
        } else if (QrcodeEnum.imgBox.name().equals(type) || QrcodeEnum.qrBox.name().equals(type)) {
            drawImage(graphics, (String) layer.get("url"), left, top, width, height, imageCache);
        } else if (QrcodeEnum.textBox.name().equals(type)) {
            drawText(graphics, layer, left, top, width, height);
        }
    }

    private void drawQrCode(Graphics2D graphics, String content, Map<String, Object> component, int left, int top, int width, int height) {
        QrConfig config = new QrConfig();
        config.setMargin(1);
        config.setWidth(width);
        config.setHeight(height);
        try {
            if (ObjectUtils.isNotNull(component.get("icon")) && (boolean) component.get("icon")) {
                InputStream inputStream = getIconFile((String) component.get("url"));
                if (ObjectUtils.isNotNull(inputStream)) {
                    config.setImg(ImageIO.read(inputStream));
                }
            }
            BufferedImage image = QrCodeUtil.generate(content, config);
            graphics.drawImage(image, left, top, width, height, null);
        } catch (IOException e) {
            log.error("二维码生成异常:", e);
            throw new RuntimeException(e);
        }
    }

    private void drawImage(Graphics2D graphics, String url, int left, int top, int width, int height,
                           Map<String, BufferedImage> imageCache
                          ) {
        if (StringUtils.isBlank(url)) {
            return;
        }
        BufferedImage image = getImage(url, imageCache);
        if (image != null) {
            graphics.drawImage(image, left, top, width, height, null);
        }
    }

    private BufferedImage getImage(String url, Map<String, BufferedImage> imageCache) {
        String imageUrl = getImageUrl(url);
        if (imageCache.containsKey(imageUrl)) {
            return imageCache.get(imageUrl);
        }
        BufferedImage image = null;
        try {
            if (imageUrl.startsWith("data:image")) {
                String base64 = imageUrl.substring(imageUrl.indexOf(',') + 1);
                image = ImageIO.read(new ByteArrayInputStream(Base64Utils.decode(base64)));
            } else {
                HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(5000);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    image = ImageIO.read(connection.getInputStream());
                }
            }
        } catch (Exception e) {
            log.error("二维码图片资源读取异常:{}", imageUrl, e);
        }
        imageCache.put(imageUrl, image);
        return image;
    }

    private String getImageUrl(String url) {
        if (StringUtils.startsWithIgnoreCase(url, "http") || url.startsWith("data:image")) {
            return url;
        }
        try {
            URL baseUrl = new URL(BASE_URL);
            String hostUrl = baseUrl.getProtocol() + "://" + baseUrl.getAuthority();
            return hostUrl + (url.startsWith("/") ? url : "/" + url);
        } catch (Exception e) {
            log.error("二维码图片资源地址拼接异常:{}", url, e);
            return url;
        }
    }

    private void drawText(Graphics2D graphics, Map<String, Object> layer, int left, int top, int width, int height) {
        Map<String, Object> option = (Map<String, Object>) layer.get("option");
        if (option == null) {
            option = Collections.emptyMap();
        }
        String text = Optional.ofNullable((String) layer.get("text")).orElse("");
        Color backgroundColor = parseColor((String) option.get("backgroundColor"), null);
        if (backgroundColor != null) {
            graphics.setColor(backgroundColor);
            graphics.fillRect(left, top, width, height);
        }
        int fontSize = toInt(option.get("fontSize"), 18);
        int fontStyle = "bold".equals(option.get("fontWeight")) ? Font.BOLD : Font.PLAIN;
        String fontFamily = Optional.ofNullable((String) option.get("fontFamily")).filter(StringUtils::isNotBlank).orElse("SansSerif");
        graphics.setFont(new Font(fontFamily, fontStyle, fontSize));
        graphics.setColor(parseColor((String) option.get("color"), Color.BLACK));
        FontMetrics metrics = graphics.getFontMetrics();
        int x = getTextX(left, width, text, metrics, (String) option.get("textAlign"));
        int y = top + Math.max(metrics.getAscent(), (height - metrics.getHeight()) / 2 + metrics.getAscent());
        graphics.drawString(text, x, y);
        if ("underline".equals(option.get("textDecoration"))) {
            graphics.setStroke(new BasicStroke(1));
            graphics.drawLine(x, y + 2, x + metrics.stringWidth(text), y + 2);
        }
    }

    private int getTextX(int left, int width, String text, FontMetrics metrics, String textAlign) {
        int textWidth = metrics.stringWidth(text);
        if ("right".equals(textAlign)) {
            return left + Math.max(0, width - textWidth);
        }
        if ("center".equals(textAlign)) {
            return left + Math.max(0, (width - textWidth) / 2);
        }
        return left;
    }

    private Color parseColor(String color, Color defaultColor) {
        if (StringUtils.isBlank(color)) {
            return defaultColor;
        }
        try {
            return Color.decode(color);
        } catch (NumberFormatException e) {
            return defaultColor;
        }
    }

    private int toInt(Object value, int defaultValue) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String string && StringUtils.isNotBlank(string)) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 获取Base64的二维码图
     *
     * @param content 二维码内容
     *
     * @return 二维码
     */
    private String qrcodeImageBase64(String content, Map component) {
        QrConfig config = new QrConfig();
        config.setMargin(1);
        config.setWidth((int) component.get("width"));
        config.setHeight((int) component.get("height"));
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            if (ObjectUtils.isNotNull(component.get("icon")) && (boolean) component.get("icon")) {
                InputStream inputStream = getIconFile((String) component.get("url"));
                if (ObjectUtils.isNotNull(inputStream)) {
                    config.setImg(ImageIO.read(inputStream));
                }
            }
        } catch (IOException e) {
            log.error("获取二维码logo异常:", e);
        }
        try {
            BufferedImage image = QrCodeUtil.generate(content, config);
            ImageIO.write(image, IMG_FORMATE, os);
        } catch (IOException e) {
            log.error("二维码生成异常:", e);
            throw new RuntimeException(e);
        }
        byte[] bytes = os.toByteArray();
        return "data:image/jpg;base64," + Base64Utils.encode(bytes);
    }

    private InputStream getIconFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            // 总是检查HTTP响应码是否为200.
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 打开输入流以读取URL内容。
                return httpConn.getInputStream();
            } else {
                log.info("二维码icon获取失败:{}", url);
            }
        } catch (IOException e) {
            log.error("二维码icon获取异常:" + fileUrl + ",", e);
        }
        return null;
    }

    /**
     * 实例化实体属性值
     */
    private String fillEntityAttribute(Long entityId, String entityName, Object businessEntity, String entityType,
                                       String entityAttributesType
                                      ) {
        if (entityId == null) {
            return "";
        }
        String entityAttributeValue = "";
        // 查询不同实体类型的属性值，如果添加一种类型的实体则增加一个判断和实现
        if (QrcodeEnum.DEVICE.name().equals(entityType)) {
            entityAttributeValue = this.getDeviceAttributeValue(entityId, entityName, businessEntity, entityAttributesType);
        } else if (QrcodeEnum.STALL.name().equals(entityType)) {
            entityAttributeValue = this.getStallAttributeValue(entityId, entityName, businessEntity, entityAttributesType);
        } else if (QrcodeEnum.PROPERTY.name().equals(entityType)) {
            entityAttributeValue = this.getPropertyAttributeValue(entityId, entityName, businessEntity, entityAttributesType);
        } else if (QrcodeEnum.SITE_POINT.name().equals(entityType)) {
            entityAttributeValue = this.getSitePointAttributeValue(entityId, entityName, businessEntity, entityAttributesType);
        } else if (QrcodeEnum.RISK_SOURCE.name().equals(entityType)) {
            entityAttributeValue = this.getRiskSourceAttributeValue(entityId, entityName, businessEntity, entityAttributesType);
        } else if (QrcodeEnum.PATROL_POINT.name().equals(entityType)) {
            entityAttributeValue = this.getPatrolPointAttributeValue(entityId, entityName, businessEntity, entityAttributesType);
        }
        return entityAttributeValue;
    }

    private String getPatrolPointAttributeValue(Long entityId, String entityName, Object businessEntity, String entityAttributesType) {
        PatrolLocationVO locationVO;
        if (Objects.nonNull(businessEntity) && businessEntity instanceof PatrolLocationVO) {
            locationVO = (PatrolLocationVO) businessEntity;
        } else {
            locationVO = patrolLocationClient.queryById(entityId);
        }
        if (StringUtils.isNotBlank(entityName)) {
            Assert.isNotFound(locationVO, "无对应实体：" + entityName);
        }
        if (ObjectUtils.isNotNull(locationVO)) {
            if (QrcodeEnum.NAME.name().equals(entityAttributesType)) {
                return locationVO.getPatrolLocationName();
            }
            if (QrcodeEnum.AREA.name().equals(entityAttributesType)) {
                List<AreaDTO> area = locationVO.getArea();
                if (ObjectUtils.isNotEmpty(area)) {
                    return area.stream().map(AreaDTO::getName).collect(Collectors.joining("-"));
                }
            }
        }
        return "";
    }

    /**
     * 查询风险源属性信息
     *
     * @param entityId             实体id
     * @param entityName           实体名称
     * @param businessEntity
     * @param entityAttributesType 属性类型
     *
     * @return 属性值
     */
    private String getRiskSourceAttributeValue(Long entityId, String entityName, Object businessEntity, String entityAttributesType) {
        RiskSourceLedgerVO sourceLedgerVO;
        if (Objects.nonNull(businessEntity) && businessEntity instanceof RiskSourceLedgerVO) {
            sourceLedgerVO = (RiskSourceLedgerVO) businessEntity;
        } else {
            sourceLedgerVO = riskSourceLedgerClient.queryById(entityId);
        }
        if (StringUtils.isNotBlank(entityName)) {
            Assert.isNotFound(sourceLedgerVO, "无对应实体：" + entityName);
        }
        if (ObjectUtils.isNotEmpty(sourceLedgerVO)) {
            if (QrcodeEnum.NAME.name().equals(entityAttributesType)) {
                return sourceLedgerVO.getName();
            }
            if (QrcodeEnum.TYPE.name().equals(entityAttributesType)) {
                return sourceLedgerVO.getTypeName();
            }
            if (QrcodeEnum.AREA.name().equals(entityAttributesType)) {
                return sourceLedgerVO.getAreaName();
            }
            if (QrcodeEnum.RECEIVE_DEP_NAME.name().equals(entityAttributesType)) {
                return sourceLedgerVO.getReceiveDepName();
            }
            if (QrcodeEnum.MAINTENANCE_DEP_NAME.name().equals(entityAttributesType)) {
                return sourceLedgerVO.getMaintenanceDepName();
            }
            if (QrcodeEnum.SERIAL_NUMBER.name().equals(entityAttributesType)) {
                return sourceLedgerVO.getSerialNumber();
            }
        }
        return "";
    }

    /**
     * @Author wql
     * @Description 获取现场点扩展属性
     * @Date 17:06 2023/11/22
     **/
    private String getSitePointAttributeValue(Long entityId, String entityName, Object businessEntity, String entityAttributesType) {
        ScenePointVO scenePointVO;
        if (Objects.nonNull(businessEntity) && businessEntity instanceof ScenePointVO) {
            scenePointVO = (ScenePointVO) businessEntity;
        } else {
            scenePointVO = scenePointClient.scenePointDetail(entityId);
        }
        if (StringUtils.isNotEmpty(entityName)) {
            Assert.isNotFound(scenePointVO, "无对应实体：" + entityName);
        }
        if (ObjectUtils.isNotEmpty(scenePointVO)) {
            if (QrcodeEnum.NAME.name().equals(entityAttributesType)) {
                return scenePointVO.getName();
            }
            if (QrcodeEnum.TYPE.name().equals(entityAttributesType)) {
                return scenePointVO.getTypeName();
            }
            if (QrcodeEnum.AREA.name().equals(entityAttributesType)) {
                return scenePointVO.getAreaName();
            }
            if (QrcodeEnum.MANAGE_DEPT.name().equals(entityAttributesType)) {
                return scenePointVO.getDepName();
            }
            if (QrcodeEnum.DERAIL_ADDRESS.name().equals(entityAttributesType)) {
                return scenePointVO.getDetailAddress();
            }
            if (QrcodeEnum.CODE_NUM.name().equals(entityAttributesType)) {
                return scenePointVO.getCodeNum();
            }
        }
        return "";
    }

    /**
     * 获取设备属性值
     * 若加一种属性，需扩展一种
     *
     * @param entityId             实体ID
     * @param entityName           实体名称
     * @param businessEntity
     * @param entityAttributesType 实体属性类型
     *
     * @return 属性值
     */
    private String getDeviceAttributeValue(Long entityId, String entityName, Object businessEntity, String entityAttributesType) {
        DeviceBaseDetailsDTO byDeviceId = null;
        if (Objects.nonNull(businessEntity) && businessEntity instanceof DeviceBaseDetailsDTO) {
            byDeviceId = (DeviceBaseDetailsDTO) businessEntity;
        } else {
            DevicePostByIdSetParam param = new DevicePostByIdSetParam();
            param.setDeviceIdSet(Collections.singleton(entityId));
            List<DeviceBaseDetailsDTO> deviceBaseDetailsDTOS = deviceCenterStdClient.postDetailsListByDeviceId(param);
            if (CollectionUtils.isNotEmpty(deviceBaseDetailsDTOS)) {
                byDeviceId = deviceBaseDetailsDTOS.getFirst();
            }
        }
        if (Objects.nonNull(byDeviceId)) {
            // 如果传了实体名称则校验一下，目前只有在二维码绑定实体新增和修改时
            if (StringUtils.isNotEmpty(entityName)) {
                Assert.isNotFound(byDeviceId, "无对应实体：" + entityName);
            } else if (null == byDeviceId) {
                return "";
            }
            if (QrcodeEnum.NAME.name().equals(entityAttributesType)) {
                return byDeviceId.getDeviceName();
            }
            if (QrcodeEnum.TYPE.name().equals(entityAttributesType)) {
                return byDeviceId.getProductCategoryName();
            }
            if (QrcodeEnum.AREA.name().equals(entityAttributesType)) {
                return byDeviceId.getAreaName();
            }
            if (QrcodeEnum.USE_DEPT.name().equals(entityAttributesType)) {
                return byDeviceId.getDepartmentName();
            }
            if (QrcodeEnum.MANAGER.name().equals(entityAttributesType)) {
                return byDeviceId.getManagerNames();
            }
            if (QrcodeEnum.CODE.name().equals(entityAttributesType)) {
                return byDeviceId.getDeviceCode();
            }
        }
        return "";
    }

    /**
     * 获取档位属性值
     * 若加一种属性，需扩展一种
     *
     * @param entityId             实体ID
     * @param entityName           实体名称
     * @param businessEntity
     * @param entityAttributesType 实体属性类型
     *
     * @return 属性值
     */
    private String getStallAttributeValue(Long entityId, String entityName, Object businessEntity, String entityAttributesType) {
        StallDetailDTO detail;
        if (Objects.nonNull(businessEntity) && businessEntity instanceof StallDetailDTO) {
            detail = (StallDetailDTO) businessEntity;
        } else {
            detail = stallClient.getDetailById(entityId);
        }
        // 如果传了实体名称则校验一下，目前只有在二维码绑定实体新增和修改时
        if (StringUtils.isNotEmpty(entityName)) {
            Assert.isNotFound(detail, "无对应实体：" + entityName);
        }
        if (ObjectUtils.isNotEmpty(detail)) {
            if (QrcodeEnum.NAME.name().equals(entityAttributesType)) {
                return detail.getName();
            }
            if (QrcodeEnum.TYPE.name().equals(entityAttributesType)) {
                if (StringUtils.isEmpty(detail.getType())) {
                    return "";
                }
                List<StallCategoryDTO> categoryDTOS = stallClient.listStallCategory(Collections.singletonList(Long.valueOf(detail.getType())));
                return categoryDTOS.stream().map(StallCategoryDTO::getName).collect(Collectors.joining("/"));
            }
            if (QrcodeEnum.AREA.name().equals(entityAttributesType)) {
                if (null == detail.getAreaNames()) {
                    return "";
                }
                return String.join("/", detail.getAreaNames());
            }
            if (QrcodeEnum.LOCATION.name().equals(entityAttributesType)) {
                return detail.getLocation();
            }
            if (QrcodeEnum.CODE.name().equals(entityAttributesType)) {
                return detail.getCode();
            }
            if (QrcodeEnum.DEPT_NAMES.name().equals(entityAttributesType)) {
                return Optional.ofNullable(detail.getDeptNames()).orElse(new ArrayList<>()).get(0);
            }
        }
        return "";
    }

    /**
     * 获取资产属性值
     * 若加一种属性，需扩展一种
     *
     * @param entityId             实体ID
     * @param entityName           实体名称
     * @param businessEntity
     * @param entityAttributesType 实体属性类型
     *
     * @return 属性值
     */
    private String getPropertyAttributeValue(Long entityId, String entityName, Object businessEntity, String entityAttributesType) {
        PropertyDetailedVO detail;
        if (Objects.nonNull(businessEntity) && businessEntity instanceof PropertyDetailedVO) {
            detail = (PropertyDetailedVO) businessEntity;
        } else {
            detail = propertyDetailedClient.queryById(entityId);
        }
        // 如果传了实体名称则校验一下，目前只有在二维码绑定实体新增和修改时
        if (null == detail) {
            return "";
        } else if (QrcodeEnum.NAME.name().equals(entityAttributesType)) {
            return detail.getName();
        } else if (QrcodeEnum.UNIT.name().equals(entityAttributesType)) {
            return detail.getUnit();
        } else if (QrcodeEnum.NUMBER.name().equals(entityAttributesType)) {
            return ObjectUtils.isNotEmpty(detail.getNumber()) ? detail.getNumber().toString() : "";
        } else if (QrcodeEnum.TYPE_NAME.name().equals(entityAttributesType)) {
            return detail.getTypeName();
        } else if (QrcodeEnum.START_TIME.name().equals(entityAttributesType)) {
            return ObjectUtils.isNotEmpty(detail.getStartTime()) ? DateUtils.format(detail.getStartTime()) : "";
        } else if (QrcodeEnum.RECEIVE_DEP_NAME.name().equals(entityAttributesType)) {
            return detail.getReceiveDepName();
        } else if (QrcodeEnum.PROPERTY_CODE.name().equals(entityAttributesType)) {
            return detail.getSerialNumber();
        } else if (QrcodeEnum.AREA.name().equals(entityAttributesType)) {
            List<AreaDTO> areas = detail.getArea();
            if (ObjectUtils.isNotEmpty(areas)) {
                List<String> names = areas.stream().map(AreaDTO::getName).collect(Collectors.toList());
                return String.join("-", names);
            }
            return "";
        } else if (QrcodeEnum.FINANCE_CODE.name().equals(entityAttributesType)) {
            return detail.getFinanceNumber();
        }
        return "";
    }

    public String createQRCode(EntityInfoCommand entity, String code, String configJson) {
        JSONObject jsonObject = JSONObject.parseObject(configJson);
        JSONArray layers = jsonObject.getJSONArray("layers");
        layers.forEach(layer -> {
            Map<String, Object> map = (Map) layer;
            String type = (String) map.get("type");
            String textType = (String) map.get("textType");
            String entityType = (String) map.get("entityType");
            String entityAttributesType = (String) map.get("entityAttributesVlaue");
            if (QrcodeEnum.textBox.name().equals(type) && QrcodeEnum.ENTITY_ATTRIBUTES.name().equals(textType)) {
                String attributeValue = this.fillEntityAttribute(entity.getEntityId(), entity.getEntityName(), entity.getBusinessEntity(), entityType, entityAttributesType);
                String textTemplate = (String) map.get("text");
                map.put("text", textTemplate.replace("$$" + entityAttributesType + "$$", attributeValue));
            }
            if (QrcodeEnum.qrBox.name().equals(type)) {
                map.put("url", null);
            }
        });
        return jsonObject.toJSONString();
    }

    private String createQRCodeInstance(Map<String, Object> map, String code) {
        Map<String, Object> component = (Map) map.get("component");
        code = BASE_URL + code;
        QrConfig config = new QrConfig();
        config.setMargin(1);
        config.setWidth((Integer) component.get("width"));
        config.setHeight((Integer) component.get("height"));
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            BufferedImage image = QrCodeUtil.generate(code, config);
            ImageIO.write(image, IMG_FORMATE, os);
        } catch (IOException e) {
            log.error("二维码图片生成异常:", e);
            throw new RuntimeException(e);
        }
        byte[] bytes = os.toByteArray();
        return "data:image/jpg;base64," + Base64Utils.encode(bytes);
    }

    /**
     * 能解析出来type的对象直接根据对象类型获取对象信息,没有解析出来type的对象挨个查
     *
     * @param type  对象类型
     * @param objId 对象id
     *
     * @return 对象信息
     */
    public QrcodeEntityInfo getObjInfo(String type, Long objId) {
        try {
            return StringUtils.isNotBlank(type) ? getObjInfoWithType(type, objId) : getObjInfoWithoutType(objId);
        } catch (Exception exception) {
            throw new ResourceNotFoundException("未找到相关的二维码信息");
        }
    }

    private QrcodeEntityInfo getObjInfoWithoutType(Long objId) {
        DeviceDetailDTO device = deviceCenterStdClient.getDeviceById(objId);
        if (ObjectUtils.isNotEmpty(device)) {
            return QrcodeEntityInfo.builder()
                    .styleId(1747068670250586114L)
                    .entityId(device.getBaseArchivesInfo().getId())
                    .entityType(QrcodeEnum.DEVICE.name())
                    .entityName(device.getBaseArchivesInfo().getName())
                    .build();
        }
        ScenePointVO scenePointVO = scenePointClient.scenePointDetail(objId);
        if (ObjectUtils.isNotEmpty(scenePointVO)) {
            return QrcodeEntityInfo.builder()
                    .styleId(1747068670250586115L)
                    .entityId(scenePointVO.getId())
                    .entityType(QrcodeEnum.SITE_POINT.name())
                    .entityName(scenePointVO.getName())
                    .build();
        }
        return null;
    }

    private QrcodeEntityInfo getObjInfoWithType(String type, Long objId) {
        if (QrcodeEnum.DEVICE.name().equals(type)) {
            DeviceDetailDTO device = deviceCenterStdClient.getDeviceById(objId);
            Assert.parameterIsNull(device, objId + "设备不存在!");
            return QrcodeEntityInfo.builder()
                    .styleId(1747068670250586114L)
                    .entityId(device.getBaseArchivesInfo().getId())
                    .entityType(QrcodeEnum.DEVICE.name())
                    .entityName(device.getBaseArchivesInfo().getName())
                    .build();
        }
        if (QrcodeEnum.SITE_POINT.name().equals(type)) {
            ScenePointVO scenePointVO = scenePointClient.scenePointDetail(objId);
            Assert.parameterIsNull(scenePointVO, objId + "设备不存在!");
            return QrcodeEntityInfo.builder()
                    .styleId(1747068670250586115L)
                    .entityId(scenePointVO.getId())
                    .entityType(QrcodeEnum.SITE_POINT.name())
                    .entityName(scenePointVO.getName())
                    .build();
        }
        return null;
    }

    /**
     * 批量获取指定实体类型的信息
     */
    public Map<Long, Object> batchGetEntityInfo(Collection<Long> entityIds, String entityType) {
        Map<Long, Object> result = new HashMap<>();
        if (CollectionUtils.isEmpty(entityIds)) {
            return result;
        }
        if (QrcodeEnum.DEVICE.name().equals(entityType)) {
            DevicePostByIdSetParam param = new DevicePostByIdSetParam();
            param.setDeviceIdSet(new HashSet<>(entityIds));
            List<DeviceBaseDetailsDTO> deviceBaseDetailsDTOS = deviceCenterStdClient.postDetailsListByDeviceId(param);
            for (DeviceBaseDetailsDTO deviceBaseDetailsDTO : deviceBaseDetailsDTOS) {
                result.put(deviceBaseDetailsDTO.getId(), deviceBaseDetailsDTO);
            }
        } else if (QrcodeEnum.STALL.name().equals(entityType)) {
            StallPostQueryParam param = new StallPostQueryParam();
            param.setStallIdList(new ArrayList<>(entityIds));
            List<StallDTO> stallDTOS = stallClient.postStallListByIds(param);
            for (StallDTO stallDTO : stallDTOS) {
                result.put(stallDTO.getId(), stallDTO);
            }
        } else if (QrcodeEnum.PROPERTY.name().equals(entityType)) {
            PropertyDetailedByIdListParam param = new PropertyDetailedByIdListParam();
            param.setIdList(new ArrayList<>(entityIds));
            List<PropertyDetailedVO> propertyDetailedVOS = propertyDetailedClient.queryPropertyDetailBatchPost(param);
            for (PropertyDetailedVO propertyDetailedVO : propertyDetailedVOS) {
                result.put(propertyDetailedVO.getId(), propertyDetailedVO);
            }
        } else if (QrcodeEnum.SITE_POINT.name().equals(entityType)) {
            InspectScenePointQueryParam param = new InspectScenePointQueryParam();
            param.setIdSet(new HashSet<>(entityIds));
            List<ScenePointVO> scenePointVOS = scenePointClient.scenePointListBatch(param);
            for (ScenePointVO scenePointVO : scenePointVOS) {
                result.put(scenePointVO.getId(), scenePointVO);
            }
        } else if (QrcodeEnum.RISK_SOURCE.name().equals(entityType)) {
            RiskSourceLedgerBatchParam param = new RiskSourceLedgerBatchParam();
            param.setIdList(new ArrayList<>(entityIds));
            List<RiskSourceLedgerVO> riskSourceLedgerVOS = riskSourceLedgerClient.queryRiskSourceLedgerBatchPost(param);
            for (RiskSourceLedgerVO riskSourceLedgerVO : riskSourceLedgerVOS) {
                result.put(riskSourceLedgerVO.getId(), riskSourceLedgerVO);
            }
        } else if (QrcodeEnum.PATROL_POINT.name().equals(entityType)) {
            PatrolLocationPageParam param = new PatrolLocationPageParam();
            param.setIdSet(new HashSet<>(entityIds));
            List<PatrolLocationVO> patrolLocationVOS = patrolLocationClient.queryLocationBatch(param);
            for (PatrolLocationVO patrolLocationVO : patrolLocationVOS) {
                result.put(patrolLocationVO.getId(), patrolLocationVO);
            }
        }
        return result;
    }
}

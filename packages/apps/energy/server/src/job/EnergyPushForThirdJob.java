package com.guhecloud.cube.srv.gen.energymanagement.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.DeviceMapMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.DeviceMap;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.AttributeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.DeviceAttribute;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

/**
 * 本任务 负责给三方企业推送
 * 目前用于东部新区
 */

@Slf4j
@Component
public class EnergyPushForThirdJob {

    @Autowired
    private MeterDeviceService meterDeviceService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DeviceMapMapper deviceMapMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @Description 能耗抄表推送
     * 给政府的能耗平台推送数据，目前用于东部新区
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @JobAnnotation(name = "readeDevicePush", cron = "0 0 0 1/1 * ?", desc = "能耗-东部新区-推送数据到政府能耗平台")
    public void readeDevicePush(String param) {
        SessionContextHolder.requestTenant(Integer.valueOf(param));
        try {
            //获取设备列表
            var devices = meterDeviceService.list();
            for (var device : devices) {
                var document = DocumentHelper.createDocument();
                document.setXMLEncoding("UTF-8");
                var root = document.addElement("root");
                var common = root.addElement("common");
                var building_id = common.addElement("building_id");
                var gateway_id = common.addElement("gateway_id");
                var type = common.addElement("type");
                building_id.setText("510111A001");
                gateway_id.setText("510111A00101");
                type.setText("report");
                var data = root.addElement("data");
                data.addAttribute("operation", "report");
                var sequence = data.addElement("sequence");
                sequence.setText("1");
                var parser = data.addElement("parser");
                parser.setText("yes");
                var time = data.addElement("time");
                time.setText(DateUtils.formatDate(LocalDateTime.now(), COMPACT_DATETIME_FORMAT));
                var total = data.addElement("total");
                total.setText("1");
                var current = data.addElement("current");
                current.setText("1");

                var meter = data.addElement("meter");
                var deviceMap = deviceMapMapper.selectOne(Wrappers.<DeviceMap>lambdaQuery()
                                                                  .eq(DeviceMap::getDeviceId, device.getCode()));
                if (Objects.nonNull(deviceMap)) {
                    meter.addAttribute("id", deviceMap.getDeviceCode());
                    var function = meter.addElement("function");
                    function.addAttribute("id", "1");
                    function.addAttribute("coding", "01");
                    function.addAttribute("error", "0");
                    String deviceProperties = "";

                    //属性
                    var keyValueWithTsDTOS = DeviceAttribute.deviceProp(device.getDeviceId(), AttributeTypeEnum.getByDesc(device.getInstrumentType()));
                    var energyProCode = deviceProperties;
                    //表显计算
                    if (!CollectionUtils.isEmpty(keyValueWithTsDTOS)) {
                        var keyValueWithTsDTO = keyValueWithTsDTOS.stream()
                                .filter(attributeRunningDTO -> energyProCode.equals(attributeRunningDTO.getKey())).findFirst().orElse(null);
                        var o = redisTemplate.opsForValue().get("energy:" + device.getDeviceId());
                        if (Objects.nonNull(o) && Objects.nonNull(keyValueWithTsDTO.getValue())) {
                            var bigDecimal = new BigDecimal(keyValueWithTsDTO.getValue().toString());
                            var bigDecimal1 = new BigDecimal(o.toString());
                            var subtract = bigDecimal.subtract(bigDecimal1);
                            function.setText(subtract.toString());
                            redisTemplate.opsForValue().set("energy:" + device.getDeviceId(), bigDecimal);
                        } else {
                            function.setText("0");
                            redisTemplate.opsForValue().set("energy:" + device.getDeviceId(), keyValueWithTsDTO.getValue());
                        }
                    }

                    var s = document.asXML();
                    byte[] key = {
                            (byte) 0xD4, 0x1D, (byte) 0xDD, (byte) 0xDB, 0x63, 0x4D, (byte) 0xF3, 0x4F, (byte) 0x9D, 0x36, (byte) 0x86,
                            0x7C, 0x1D, 0x7C, (byte) 0x98, 0x13
                    };
                    byte[] iv = {
                            0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x12, 0x34, 0x56, 0x78, (byte) 0x90,
                            (byte) 0xAB, (byte) 0xCD, (byte) 0xEF
                    };

                    var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    var skeySpec = new SecretKeySpec(key, "AES");
                    var ivs = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
                    byte[] bytes = cipher.doFinal(s.getBytes());
                    String zeroPadding = Base64Utils.encode(bytes);

                    //获取请求
                    var url = "http://118.122.92.139:7601/Service/EnergyRequestWebSvr.assx/UploadData";
                    var headers = new HttpHeaders();
                    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

                    MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap();
                    requestMap.add("gateway_id", "510111A00101");
                    requestMap.add("building_id", "510111A001");
                    requestMap.add("data", zeroPadding);

                    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestMap, headers);
                    var objectMapper = new ObjectMapper();
                    var similarJSON = objectMapper.writeValueAsString(requestMap);
                    log.info("similarJSON:{}", similarJSON);
                    var jsonObject = restTemplate.postForObject(url, httpEntity, String.class);
                    log.info("body1:{}", objectMapper.writeValueAsString(jsonObject));
                }
            }
        } catch (Exception e) {
            log.error("能耗推送,失败原因:", e);
        }
    }
}

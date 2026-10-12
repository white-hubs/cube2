package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.config.CarSafeConfig;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Slf4j
public class CarSafeParkingParam {

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    /**
     * 用户标识
     */
    private String from;
    /**
     * 时间戳 格式：yyyy-MM-dd hh:mm:ss
     */
    private String timestamp;
    /**
     * 随机数 生成 0 到 99 的随机整数
     */
    private String nonce;
    /**
     * 数据
     */
    private Object data;
    /**
     * 签名
     */
    private String sign;

    private String branchno = "1";
    private String queryFormat = null;

    /**
     * 构建鉴权信息
     *
     * @param config 配置
     */
    public static Optional<CarSafeParkingParam> build(final CarSafeConfig config) {
        return build(null, config);

    }

    /**
     * 构建鉴权信息
     *
     * @param data   查询模型
     * @param config 配置
     */
    public static Optional<CarSafeParkingParam> build(final Object data, final CarSafeConfig config) {
        try {
            CarSafeParkingParam authInfo = new CarSafeParkingParam();
            authInfo.timestamp = DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd hh:mm:ss");
            authInfo.nonce = ThreadLocalRandom.current().nextInt(100) + "";
            authInfo.from = config.getAccount();
            authInfo.data = data;
            authInfo.sign = generateSignature(authInfo, config.getPrivateKey());
            return Optional.of(authInfo);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("build authInfo error", e);
            return Optional.empty();
        }

    }

    private static String generateSignature(final CarSafeParkingParam authInfo,
                                            String privateKey
                                           ) throws NoSuchAlgorithmException, InvalidKeyException {

        // 生成签名的字符串
        StringBuilder signingStr = new StringBuilder();
        signingStr.append(authInfo.getTimestamp());
        signingStr.append(authInfo.getNonce());
        signingStr.append(authInfo.getFrom());
        if (authInfo.getData() != null) {
            signingStr.append(JSONObject.toJSONString(authInfo.getData()));
        }

        // 使用 HMAC-SHA1 算法进行签名
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
        mac.init(keySpec);
        byte[] bytes = mac.doFinal(signingStr.toString().getBytes(StandardCharsets.UTF_8));

        // 将签名转换为 Base64 编码
        return Base64.getEncoder().encodeToString(bytes);
    }
}

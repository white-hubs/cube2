package com.guhecloud.platform.license.manage;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.license.model.LicenseCheckModel;
import com.guhecloud.platform.license.notary.RsaLicenseNotary;
import com.guhecloud.platform.license.util.ServerInfoUtils;
import com.guhecloud.platform.license.vo.LicenseVO;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import lombok.extern.slf4j.Slf4j;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 自定义LicenseManager，用于增加额外的服务器硬件信息校验
 *
 * @author lanchun
 * @date 2025/4/8 10:39
 */
@Slf4j
public class CustomLicenseManager extends LicenseManager {

    private RsaLicenseNotary rsaLicenseNotary;

    private static final String XML_CHARSET = "UTF-8";

    private static final int DEFAULT_BUF_SIZE = 8 * 1024;

    public CustomLicenseManager() {

    }

    public CustomLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * 复写create方法
     *
     * @param
     *
     * @return byte[]
     *
     * @since 1.0.0
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    public final synchronized LicenseContent install(String filePath) throws Exception {
        return install(filePath, getLicenseNotary());
    }

    protected synchronized LicenseContent install(String filePath, LicenseNotary notary) throws Exception {
        return install(loadLicenseKey(filePath), notary);
    }

    /**
     * 复写install方法，其中validate方法调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     *
     * @param
     *
     * @return de.schlichtherle.license.LicenseContent
     *
     * @since 1.0.0
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final var certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final var content = (LicenseContent) this.load(certificate.getEncoded());
        setLicenseKey(key);
        setCertificate(certificate);
        return content;
    }

    /**
     * 复写verify方法，调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     *
     * @param
     *
     * @return de.schlichtherle.license.LicenseContent
     *
     * @since 1.0.0
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary)
            throws Exception {
        final var key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }
        var certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final var content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setCertificate(certificate);
        return content;
    }

    /**
     * 校验生成证书的参数信息
     *
     * @param content 证书正文
     *
     * @since 1.0.0
     */
    protected synchronized void validateCreate(final LicenseContent content) throws LicenseContentException {
        final var now = new Date();
        final var notBefore = content.getNotBefore();
        final var notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final var consumerType = content.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }

    /**
     * 复写validate方法，增加IP地址、Mac地址等其他信息校验
     *
     * @param content LicenseContent
     *
     * @since 1.0.0
     */
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        //1. 首先调用父类的validate方法
        super.validate(content);
        //2. 然后校验自定义的License参数
        //License中可被允许的参数信息
        if (Objects.isNull(content.getExtra())) {
            return;
        }
        var extra = content.getExtra().toString();
        var expectedCheckModel = JsonUtils.deserialize(extra, LicenseCheckModel.class);
        //当前服务器真实的参数信息
        var serverCheckModel = getServerInfos();
        if (expectedCheckModel != null && serverCheckModel != null) {
            //校验IP地址
            if (!checkIpAddress(expectedCheckModel.getIpAddress(), serverCheckModel.getIpAddress())) {
                throw new LicenseContentException("当前服务器的IP没在授权范围内");
            }
            //校验Mac地址
            if (!checkIpAddress(expectedCheckModel.getMacAddress(), serverCheckModel.getMacAddress())) {
                throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
            }
            //校验主板序列号
            if (!checkSerial(expectedCheckModel.getMainBoardSerial(), serverCheckModel.getMainBoardSerial())) {
                throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
            }
            //校验CPU序列号
            if (!checkSerial(expectedCheckModel.getCpuSerial(), serverCheckModel.getCpuSerial())) {
                throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
            }
        } else {
            throw new LicenseContentException("不能获取服务器硬件信息");
        }
    }

    /**
     * 重写XMLDecoder解析XML
     *
     * @param encoded XML类型字符串
     *
     * @return java.lang.Object
     *
     * @since 1.0.0
     */
    private Object load(String encoded) {
        try (var inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));
             var decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUF_SIZE), null, null)) {
            return decoder.readObject();
        } catch (Exception e) {
            log.error("XMLDecoder解析XML失败", e);
        }
        return null;
    }

    /**
     * 获取当前服务器需要额外校验的License参数
     *
     * @return demo.LicenseCheckModel
     *
     * @since 1.0.0
     */
    private LicenseCheckModel getServerInfos() {
        return ServerInfoUtils.getCheckModel();
    }

    /**
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP/Mac地址范围内，则返回true
     *
     * @return boolean
     *
     * @since 1.0.0
     */
    private boolean checkIpAddress(List<String> expectedList, List<String> serverList) {
        if (expectedList != null && !expectedList.isEmpty()) {
            if (serverList != null && !serverList.isEmpty()) {
                for (String expected : expectedList) {
                    if (serverList.contains(expected.trim())) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
     *
     * @return boolean
     *
     * @since 1.0.0
     */
    private boolean checkSerial(String expectedSerial, String serverSerial) {
        if (StringUtils.isNotBlank(expectedSerial)) {
            if (StringUtils.isNotBlank(serverSerial)) {
                return expectedSerial.equals(serverSerial);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取证书内容
     *
     * @return
     */
    public synchronized LicenseVO getLicenseContent() throws Exception {
        final var key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }
        var certificate = getPrivacyGuard().key2cert(key);
        getLicenseNotary().verify(certificate);
        final var content = (LicenseContent) this.load(certificate.getEncoded());
        if (null == content) {
            return null;
        }
        var licenseVO = new LicenseVO();
        licenseVO.setSubject(content.getSubject());
        licenseVO.setIssuedTime(DateUtils.parse(content.getNotBefore()));
        licenseVO.setExpiryTime(DateUtils.parse(content.getNotAfter()));
        var extra = content.getExtra().toString();
        licenseVO.setLicenseCheckModel(JsonUtils.deserialize(extra, LicenseCheckModel.class));
        return licenseVO;
    }

    @Override
    protected synchronized LicenseNotary getLicenseNotary() {
        if (this.rsaLicenseNotary == null) {
            this.rsaLicenseNotary = new RsaLicenseNotary(getLicenseParam().getKeyStoreParam());
        }
        return this.rsaLicenseNotary;
    }

    protected static byte[] loadLicenseKey(final String filePath) throws IOException, URISyntaxException {
        return getLicenseBytes(filePath);
    }

    public static byte[] getLicenseBytes(String path) throws IOException, URISyntaxException {
        if (!SpringFileUtils.isRemoteUrl(path)) {
            return loadLicenseKey(new File(path));
        }
        var downloadFile = SpringFileUtils.downloadFile(path);
        if (downloadFile != null) {
            return downloadFile.fileBytes();
        }
        return null;
    }
}

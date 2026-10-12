package com.guhecloud.platform.license.keystore;

import com.guhecloud.platform.base.util.SpringFileUtils;
import de.schlichtherle.license.AbstractKeyStoreParam;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;

@Getter
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    /**
     * 公钥/私钥在磁盘上的存储路径
     */
    private final String storePath;
    private final String alias;
    private final String storePwd;
    private final String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    /**
     * 复写de.schlichtherle.license.AbstractKeyStoreParam的getStream()方法<br/>
     * 用于将公私钥存储文件存放到其他磁盘位置或远程而不是项目中
     *
     * @param
     *
     * @return java.io.InputStream
     *
     * @since 1.0.0
     */
    @Override
    public InputStream getStream() throws IOException {
        try {
            if (SpringFileUtils.isRemoteUrl(storePath)) {
                return SpringFileUtils.getRemoteInputStream(storePath);
            }
            return Files.newInputStream(new File(storePath).toPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

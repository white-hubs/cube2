package com.guhecloud.platform.license.notary;

import com.guhecloud.platform.base.util.AssertUtils;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseNotaryException;
import de.schlichtherle.license.Policy;
import de.schlichtherle.xml.GenericCertificate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class RsaLicenseNotary extends LicenseNotary {

    private static final String RSA_SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final int BUFSIZE = 5 * 1024;

    private KeyStoreParam param;
    private KeyStore keyStore;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RsaLicenseNotary(KeyStoreParam param) {
        setKeyStoreParam(param);
    }

    public void setKeyStoreParam(KeyStoreParam param) {
        AssertUtils.assertNotNull(param, "KeyStoreParam cannot be null");
        if (param.getAlias() == null) {
            throw new NullPointerException("Alias cannot be null");
        }

        Policy.getCurrent().checkPwd(param.getStorePwd());
        if (param.getKeyPwd() != null) {
            Policy.getCurrent().checkPwd(param.getKeyPwd());
        }

        this.param = param;
        this.keyStore = null;
        this.privateKey = null;
        this.publicKey = null;
    }

    public GenericCertificate sign(Object content) throws Exception {
        GenericCertificate cert = new GenericCertificate();
        sign(cert, content);
        return cert;
    }

    void sign(GenericCertificate certificate, Object content) throws Exception {
        certificate.sign(content, getPrivateKey(), getSignatureEngine());
    }

    public void verify(GenericCertificate certificate) throws Exception {
        certificate.verify(getPublicKey(), getSignatureEngine());
    }

    protected PrivateKey getPrivateKey() throws LicenseNotaryException,
            IOException,
            CertificateException,
            NoSuchAlgorithmException,
            UnrecoverableKeyException {
        if (privateKey == null) {
            String keyPwd = param.getKeyPwd();
            if (keyPwd == null) {
                throw new LicenseNotaryException("Private key password not provided", param.getAlias());
            }
            KeyStore keystore = getKeyStore();
            try {
                privateKey = (PrivateKey) keystore.getKey(param.getAlias(), keyPwd.toCharArray());
            } catch (KeyStoreException e) {
                throw new AssertionError(e);
            }
        }
        return privateKey;
    }

    protected PublicKey getPublicKey() throws LicenseNotaryException,
            IOException,
            CertificateException,
            NoSuchAlgorithmException {
        if (publicKey == null) {
            KeyStore keystore = getKeyStore();
            Certificate cert = null;
            try {
                cert = keystore.getCertificate(param.getAlias());
                publicKey = cert.getPublicKey();
            } catch (KeyStoreException e) {
                throw new AssertionError(e);
            }
        }
        return publicKey;
    }

    protected Signature getSignatureEngine() {
        try {
            return Signature.getInstance(RSA_SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA256withRSA not available", e);
        }
    }

    protected KeyStore getKeyStore() throws IOException,
            CertificateException,
            NoSuchAlgorithmException {
        if (keyStore == null) {
            try {
                keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            } catch (KeyStoreException e) {
                throw new AssertionError(e);
            }
            try (InputStream in = new BufferedInputStream(param.getStream(), BUFSIZE)) {
                keyStore.load(in, param.getStorePwd().toCharArray());
            }
        }
        return keyStore;
    }
}
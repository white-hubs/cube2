package com.guhecloud.platform.license;

import com.guhecloud.platform.license.manage.CustomLicenseManager;
import com.guhecloud.platform.license.verify.LicenseVerify;
import com.guhecloud.platform.license.verify.param.LicenseVerifyParam;

import java.util.Objects;

public class LicenseVerifyHolder {

    private static volatile LicenseVerify LICENSE_VERIFY;

    public static void install(LicenseVerifyParam param) {
        if (LICENSE_VERIFY == null) {
            LICENSE_VERIFY = new LicenseVerify();
        }
        LICENSE_VERIFY.install(param);
    }

    public static LicenseVerify getLicenseVerify() {
        CustomLicenseManager customLicenseManager = LicenseManagerHolder.getInstance();
        if (Objects.isNull(customLicenseManager)) {
            return null;
        }
        return LICENSE_VERIFY;
    }
}

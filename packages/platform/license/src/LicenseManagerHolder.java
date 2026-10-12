package com.guhecloud.platform.license;

import com.guhecloud.platform.license.manage.CustomLicenseManager;
import de.schlichtherle.license.LicenseParam;

public class LicenseManagerHolder {

    private static volatile CustomLicenseManager LICENSE_MANAGER;

    public static CustomLicenseManager getInstance(LicenseParam param) {
        if (LICENSE_MANAGER == null) {
            synchronized (LicenseManagerHolder.class) {
                if (LICENSE_MANAGER == null) {
                    LICENSE_MANAGER = new CustomLicenseManager(param);
                }
            }
        }

        return LICENSE_MANAGER;
    }

    public static CustomLicenseManager getInstance() {
        return LICENSE_MANAGER;
    }
}

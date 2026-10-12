package com.guhecloud.cube.base.message2.server.sat.param.validator.manufacturer;

public interface AccountValidator {

    String getChannelName();

    String getManufacturerName();

    boolean accountIdentityVerify(String firstManufacturerConfig, String secondManufacturerConfig);
}

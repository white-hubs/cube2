package com.guhecloud.cube.base.license.server.cli.command;

import com.guhecloud.cube.base.license.server.cli.CommandOptionParser.CommandOption;
import com.guhecloud.cube.base.license.server.creator.LicenseCreator;
import com.guhecloud.cube.base.license.server.creator.param.LicenseCreatorParam;
import com.guhecloud.platform.license.model.LicenseCheckModel;

public class CreateLicenseCommand {

    public static boolean execute(CommandOption option) {
        LicenseCreator licenseCreator = new LicenseCreator(convertParam(option));
        return licenseCreator.generateLicense();
    }

    private static LicenseCreatorParam convertParam(CommandOption option) {
        LicenseCreatorParam licenseCreatorParam = new LicenseCreatorParam();
        licenseCreatorParam.setSubject(option.getSubject());
        licenseCreatorParam.setPrivateAlias(option.getPrivateAlias());
        licenseCreatorParam.setKeyPass(option.getKeyPass());
        licenseCreatorParam.setStorePass(option.getStorePass());
        licenseCreatorParam.setLicensePath(option.getLicensePath());
        licenseCreatorParam.setPrivateKeysStorePath(option.getPrivateKeysStorePath());
        licenseCreatorParam.setIssuedTime(option.getIssuedTime());
        licenseCreatorParam.setExpiryTime(option.getExpiryTime());
        licenseCreatorParam.setLicenseCheckModel(new LicenseCheckModel());
        return licenseCreatorParam;
    }
}

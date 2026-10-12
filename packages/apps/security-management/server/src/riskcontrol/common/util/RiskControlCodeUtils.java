package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

public final class RiskControlCodeUtils {
    private RiskControlCodeUtils() {
    }

    public static String resolve(String submittedCode, CodeType codeType, Long businessId) {
        if (StringUtils.isNotBlank(submittedCode)) {
            return submittedCode.trim();
        }
        Assert.isParameterError(businessId == null, "编号生成失败");
        return codeType.prefix + "-" + businessId;
    }

    public static String resolveForUpdate(String submittedCode, String existingCode) {
        if (StringUtils.isNotBlank(submittedCode)) {
            return submittedCode.trim();
        }
        return existingCode;
    }

    public enum CodeType {
        WARNING_SIGN("RWS"),
        UNIT_TYPE("RUT"),
        ASSESSMENT_METHOD("RAM"),
        AREA("RA"),
        UNIT("RU"),
        RISK_OBJECT("RO");

        private final String prefix;

        CodeType(String prefix) {
            this.prefix = prefix;
        }
    }
}

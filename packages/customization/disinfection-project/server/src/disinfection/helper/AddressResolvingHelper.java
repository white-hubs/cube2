package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/25 17:46
 * @Description:
 */
@Slf4j
public class AddressResolvingHelper {

    private final static String regex =
            "(?<province>[^省]+省|.+自治区|[^澳门]+澳门|[^香港]+香港|[^市]+市)?" +
            "(?<city>[^自治州]+自治州|[^特别行政区]+特别行政区|[^市]+市|.*?地区|.*?行政单位|.+盟|市辖区|[^县]+县)" +
            "(?<district>[^县]+县|[^市]+市|[^镇]+镇|[^区]+区|[^乡]+乡|.+场|.+旗|.+海域|.+岛)?" +
            "(?<address>.*)";

    public static String getAddressDistrict(String address) {
        if (StringUtils.isBlank(address)) {
            return null;
        }
        try {
            Matcher matcher = Pattern.compile(regex).matcher(address);
            if (matcher.find()) {
                if (StringUtils.isNotBlank(matcher.group("district"))) {
                    return matcher.group("district");
                }
            }
        } catch (Exception e) {
            log.error("解析地址出错{}", e);
        }
        return null;
    }

}

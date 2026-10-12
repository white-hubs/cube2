package org.apache.shardingsphere.infra.database.dm.connector;

import com.google.common.base.Strings;
import org.apache.shardingsphere.infra.database.core.connector.ConnectionProperties;
import org.apache.shardingsphere.infra.database.core.connector.ConnectionPropertiesParser;
import org.apache.shardingsphere.infra.database.core.connector.StandardConnectionProperties;
import org.apache.shardingsphere.infra.database.core.exception.UnrecognizedDatabaseURLException;
import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/9 12:43
 * @Description:
 */
public final class DMConnectionPropertiesParser implements ConnectionPropertiesParser {

    private static final int DEFAULT_PORT = 5236;

    private final Pattern DM_URL_PATTERN = Pattern.compile("jdbc:dm://([\\w\\-\\.]+):?([0-9]*)(/?)([\\w\\-]*)", Pattern.CASE_INSENSITIVE);


    @Override
    public ConnectionProperties parse(String url, String username, String catalog) {
        Matcher matcher = DM_URL_PATTERN.matcher(url);
        ShardingSpherePreconditions.checkState(matcher.find(), () -> new UnrecognizedDatabaseURLException(url, DM_URL_PATTERN.pattern()));

        return new StandardConnectionProperties(matcher.group(1), Strings.isNullOrEmpty(matcher.group(2)) ? DEFAULT_PORT : Integer.parseInt(matcher.group(2)),
                                                matcher.group(4), username, null, new Properties());
    }

    @Override
    public String getDatabaseType() {
        return "DM";
    }
}
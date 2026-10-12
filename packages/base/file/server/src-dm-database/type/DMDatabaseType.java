package org.apache.shardingsphere.infra.database.dm.type;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/9 12:44
 * @Description:
 */

import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.spi.type.typed.TypedSPILoader;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public final class DMDatabaseType implements DatabaseType {
    @Override
    public Collection<String> getJdbcUrlPrefixes() {
        return Collections.singleton("jdbc:dm:");
    }

    @Override
    public Optional<DatabaseType> getTrunkDatabaseType() {
        return Optional.of(TypedSPILoader.getService(DatabaseType.class, "MySQL"));
    }

    @Override
    public String getType() {
        return "DM";
    }
}
package org.apache.shardingsphere.infra.database.dm.metadata.database.system;

import org.apache.shardingsphere.infra.database.core.metadata.database.system.DialectSystemDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/17 17:14
 * @Description:
 */
public class DMSystemDatabase implements DialectSystemDatabase {

    @Override
    public Map<String, Collection<String>> getSystemDatabaseSchemaMap() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<String> getSystemSchemas() {
        return Collections.emptyList();
    }

    @Override
    public String getDatabaseType() {
        return "DM";
    }
}

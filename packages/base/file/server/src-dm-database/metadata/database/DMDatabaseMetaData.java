package org.apache.shardingsphere.infra.database.dm.metadata.database;

import org.apache.shardingsphere.infra.database.core.metadata.database.DialectDatabaseMetaData;
import org.apache.shardingsphere.infra.database.core.metadata.database.enums.NullsOrderType;
import org.apache.shardingsphere.infra.database.core.metadata.database.enums.QuoteCharacter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/17 17:10
 * @Description:
 */
public final class DMDatabaseMetaData implements DialectDatabaseMetaData {

    @Override
    public QuoteCharacter getQuoteCharacter() {
        return QuoteCharacter.BACK_QUOTE;
    }

    @Override
    public NullsOrderType getDefaultNullsOrderType() {
        return NullsOrderType.LOW;
    }

    @Override
    public boolean isInstanceConnectionAvailable() {
        return true;
    }

    @Override
    public boolean isSupportThreeTierStorageStructure() {
        return true;
    }

    @Override
    public String getDatabaseType() {
        return "DM";
    }
}

package org.apache.shardingsphere.infra.database.dm.metadata.data.loader;

import com.guhecloud.platform.base.util.StringUtils;
import org.apache.shardingsphere.infra.database.core.GlobalDataSourceRegistry;
import org.apache.shardingsphere.infra.database.core.metadata.data.loader.DialectMetaDataLoader;
import org.apache.shardingsphere.infra.database.core.metadata.data.loader.MetaDataLoaderMaterial;
import org.apache.shardingsphere.infra.database.core.metadata.data.model.ColumnMetaData;
import org.apache.shardingsphere.infra.database.core.metadata.data.model.ConstraintMetaData;
import org.apache.shardingsphere.infra.database.core.metadata.data.model.IndexMetaData;
import org.apache.shardingsphere.infra.database.core.metadata.data.model.SchemaMetaData;
import org.apache.shardingsphere.infra.database.core.metadata.data.model.TableMetaData;
import org.apache.shardingsphere.infra.database.core.metadata.database.datatype.DataTypeRegistry;
import org.apache.shardingsphere.infra.database.core.metadata.database.enums.TableType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/17 21:43
 * @Description:
 */
public final class DMMetaDataLoader implements DialectMetaDataLoader {
    private static final String ORDER_BY_ORDINAL_POSITION = " ORDER BY ORDINAL_POSITION";

    private static final String TABLE_META_DATA_COLUMN_PK =             "SELECT " +
                                                                        "  WM_CONCAT(B.COLUMN_NAME) PK_COLUMNS " +
                                                                        "FROM" +
                                                                        "        ALL_CONSTRAINTS A," +
                                                                        "        ALL_CONS_COLUMNS B " +
                                                                        "where" +
                                                                        "        A.CONSTRAINT_type='P' " +
                                                                        "    AND A.OWNER          = ? " +
                                                                        "    AND A.TABLE_NAME  IN (%s) " +
                                                                        "    AND B.OWNER   = A.OWNER " +
                                                                        "    AND A.TABLE_NAME =B.TABLE_NAME " +
                                                                        "GROUP BY" +
                                                                        "        A.TABLE_NAME";
    private static final String TABLE_META_DATA_NO_ORDER =
            "SELECT TABLE_NAME,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,DATA_SCALE,COLUMN_ID AS ORDINAL_POSITION,NULLABLE, pck.PK_COLUMNS  FROM ALL_TAB_COLUMNS atc " +
            "LEFT JOIN  (" + TABLE_META_DATA_COLUMN_PK +") pck on pck.PK_COLUMNS = atc.COLUMN_NAME " +
            "WHERE OWNER = ?";

    private static final String TABLE_META_DATA_SQL = TABLE_META_DATA_NO_ORDER + ORDER_BY_ORDINAL_POSITION;

    private static final String TABLE_META_DATA_SQL_IN_TABLES = TABLE_META_DATA_NO_ORDER + " AND TABLE_NAME IN (%s)" + ORDER_BY_ORDINAL_POSITION;

    private static final String INDEX_META_DATA_SQL = "SELECT DIC.TABLE_NAME,  DIC.COLUMN_NAME, DIC.INDEX_NAME, dc.CONSTRAINT_NAME  from DBA_IND_COLUMNS DIC " +
                                                      "LEFT JOIN  DBA_CONSTRAINTS DC " +
                                                      "ON DC.INDEX_NAME = DIC.INDEX_NAME " +
                                                      "AND DC.CONSTRAINT_TYPE = 'P' " +
                                                      "WHERE  DIC.TABLE_OWNER=? " +
                                                      "and DIC.table_name IN (%s) ";

    private static final String VIEW_META_DATA_SQL = "SELECT VIEW_NAME AS TABLE_NAME FROM ALL_VIEWS WHERE OWNER=? AND VIEW_NAME IN (%s)";

    @Override
    public Collection<SchemaMetaData> load(final MetaDataLoaderMaterial material) throws SQLException {
        Collection<TableMetaData> tableMetaDataList = new LinkedList<>();
        Map<String, Collection<ColumnMetaData>> columnMetaDataMap = loadColumnMetaDataMap(material.getDataSource(), material.getActualTableNames());
        Collection<String> viewNames = columnMetaDataMap.isEmpty() ? Collections.emptySet() : loadViewNames(material.getDataSource(), columnMetaDataMap.keySet());
        Map<String, Collection<IndexMetaData>> indexMetaDataMap = columnMetaDataMap.isEmpty() ? Collections.emptyMap() : loadIndexMetaData(material.getDataSource(), columnMetaDataMap.keySet());
        for (Map.Entry<String, Collection<ColumnMetaData>> entry : columnMetaDataMap.entrySet()) {
            Collection<IndexMetaData> indexMetaDataList = indexMetaDataMap.getOrDefault(entry.getKey(), Collections.emptyList());

            tableMetaDataList.add(
                    new TableMetaData(entry.getKey(), entry.getValue(), indexMetaDataList, Collections.emptyList(), viewNames.contains(entry.getKey()) ? TableType.VIEW : TableType.TABLE));
        }
        return Collections.singletonList(new SchemaMetaData(material.getDefaultSchemaName(), tableMetaDataList));
    }

    private Collection<String> loadViewNames(final DataSource dataSource, final Collection<String> tableNames) throws SQLException {
        Collection<String> result = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getViewMetaDataSQL(tableNames))) {
            String databaseName = "".equals(connection.getCatalog()) ? GlobalDataSourceRegistry.getInstance().getCachedDatabaseTables().get(tableNames.iterator().next()) : connection.getCatalog();
            preparedStatement.setString(1, databaseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString(1));
                }
            }
        }
        return result;
    }

    private String getViewMetaDataSQL(final Collection<String> tableNames) {
        return String.format(VIEW_META_DATA_SQL, tableNames.stream().map(each -> String.format("'%s'", each)).collect(Collectors.joining(",")));
    }


    private Map<String, Collection<ColumnMetaData>> loadColumnMetaDataMap(final DataSource dataSource, final Collection<String> tables) throws SQLException {
        Map<String, Collection<ColumnMetaData>> result = new HashMap<>(tables.size(), 1F);
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getTableMetaDataSQL(tables))) {
            String databaseName = "".equals(connection.getCatalog()) ? GlobalDataSourceRegistry.getInstance().getCachedDatabaseTables().get(tables.iterator().next()) : connection.getCatalog();
            preparedStatement.setString(1, databaseName);
            preparedStatement.setString(2, databaseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    ColumnMetaData columnMetaData = loadColumnMetaData(resultSet);
                    if (!result.containsKey(tableName)) {
                        result.put(tableName, new LinkedList<>());
                    }
                    result.get(tableName).add(columnMetaData);
                }
            }
        }
        return result;
    }

    private ColumnMetaData loadColumnMetaData(final ResultSet resultSet) throws SQLException {
        String columnName = resultSet.getString("COLUMN_NAME");
        String dataType = resultSet.getString("DATA_TYPE");
        boolean primaryKey = StringUtils.isNotBlank(resultSet.getString("PK_COLUMNS"));
        boolean nullable = "Y".equals(resultSet.getString("NULLABLE"));
        return new ColumnMetaData(columnName, DataTypeRegistry.getDataType(getDatabaseType(), dataType).orElse(Types.OTHER), primaryKey, false, false, true, false, nullable);
    }

    private String getTableMetaDataSQL(final Collection<String> tables) {
        String tableNames = tables.stream().map(each -> String.format("'%s'", each)).collect(Collectors.joining(","));
        return tables.isEmpty() ? TABLE_META_DATA_SQL : String.format(TABLE_META_DATA_SQL_IN_TABLES, tableNames, tableNames);
    }

    private Map<String, Collection<IndexMetaData>> loadIndexMetaData(final DataSource dataSource, final Collection<String> tableNames) throws SQLException {
        Map<String, Map<String, IndexMetaData>> tableToIndex = new HashMap<>(tableNames.size(), 1F);
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getIndexMetaDataSQL(tableNames))) {
            String databaseName = "".equals(connection.getCatalog()) ? GlobalDataSourceRegistry.getInstance().getCachedDatabaseTables().get(tableNames.iterator().next()) : connection.getCatalog();
            preparedStatement.setString(1, databaseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String indexName = resultSet.getString("INDEX_NAME");
                    String tableName = resultSet.getString("TABLE_NAME");
                    if (!tableToIndex.containsKey(tableName)) {
                        tableToIndex.put(tableName, new HashMap<>());
                    }
                    Map<String, IndexMetaData> indexMap = tableToIndex.get(tableName);
                    if (indexMap.containsKey(indexName)) {
                        indexMap.get(indexName).getColumns().add(resultSet.getString("COLUMN_NAME"));
                    } else {
                        IndexMetaData indexMetaData = new IndexMetaData(indexName, new LinkedList<>(Collections.singleton(resultSet.getString("COLUMN_NAME"))));
                        indexMetaData.setUnique(StringUtils.isNotBlank(resultSet.getString("CONSTRAINT_NAME")));
                        indexMap.put(indexName, indexMetaData);
                    }
                }
            }
        }
        Map<String, Collection<IndexMetaData>> result = new HashMap<>(tableToIndex.size(), 1F);
        for (Map.Entry<String, Map<String, IndexMetaData>> entry : tableToIndex.entrySet()) {
            result.put(entry.getKey(), entry.getValue().values());
        }
        return result;
    }

    private String getIndexMetaDataSQL(final Collection<String> tableNames) {
        return String.format(INDEX_META_DATA_SQL, tableNames.stream().map(each -> String.format("'%s'", each)).collect(Collectors.joining(",")));
    }

    @Override
    public String getDatabaseType() {
        return "DM";
    }
}

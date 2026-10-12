DB_TYPE = {
    "mysql": {
        "driver": "com.mysql.cj.jdbc.Driver",
        "prefix": "jdbc:mysql://",
        "params": "characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true",
    },
    "dm": {
        "driver": "dm.jdbc.driver.DmDriver",
        "prefix": "jdbc:dm://",
        "params": "characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true",
    },
    "hg": {
        "driver": "com.highgo.jdbc.Driver",
        "prefix": "jdbc:highgo://",
        "params": "",
    },
    "clickhouse": {
        "driver": "com.clickhouse.jdbc.ClickHouseDriver",
        "prefix": "jdbc:clickhouse://",
        "params": "",
    },
}

def assert_is_supported_db_type(db_type):
    if db_type not in DB_TYPE:
        fail("[assert_is_supported_db_type] Unsupported db_type: %s" % db_type)

def generate_jdbc_url(db_type = "mysql", domain = "", database = "", namespace = "cube"):
    assert_is_supported_db_type(db_type)
    if db_type == "hg":
        return "%s%s/%s?currentSchema=%s" % (DB_TYPE[db_type]["prefix"], domain, namespace, database)
    if db_type == "clickhouse":
        return "%s%s" % (DB_TYPE[db_type]["prefix"], domain)
    return "%s%s/%s?%s" % (DB_TYPE[db_type]["prefix"], domain, database, DB_TYPE[db_type]["params"])

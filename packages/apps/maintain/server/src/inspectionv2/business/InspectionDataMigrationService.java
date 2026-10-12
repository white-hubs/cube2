package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-26  09:56
 * @Description:
 * @Version: 1.0
 */
public interface InspectionDataMigrationService {
    /**
     * 成农批数据迁移
     */
    Boolean inspectionDataMigration();

    Boolean inspectionItemDataMigration();

    /**
     * 上海数据迁移
     */
    Boolean shInspectionDataMigration();

    /**
     * 天津数据迁移
     */
    void tjInspectionDataMigration();

    void tjInspectionPlanDataMigration();

    void tjInspectionTaskDataMigration();

    void tjInspectionRecordDataMigration();

    void tjInspectionRecordDataItemMigration();

}

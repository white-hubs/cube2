package com.guhecloud.cube.biz.daily.task.migration.controller;

import com.guhecloud.cube.biz.daily.task.migration.service.MigrationBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MigrationDataController {
    private final MigrationBaseService migrationBaseService;

    /**
     * 迁移数据
     *
     * @return 是否成功
     */
    @GetMapping("/inspection-daily/migration")
    public Boolean migrate(@RequestParam(value = "table", required = false) String table) {
        return migrationBaseService.migrateAll(table);
    }
}

package com.guhecloud.cube.biz.daily.task.migration.service.impl;

import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.service.MigrationBaseService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MigrationBaseServiceImpl implements MigrationBaseService {
    private final List<MigrationBaseConvert<?, ?>> migrationBaseConverts;
    private static final List<String> tables = new ArrayList<>();

    /**
     * 迁移所有数据(入口)
     *
     * @return 是否成功
     */
    public boolean migrateAll(String table) {
        if (CollectionUtils.isEmpty(migrationBaseConverts)) {
            log.warn("没有需要迁移的数据源");
            return false;
        }
        tables.clear();
        if (StringUtils.isNotBlank(table)) {
            tables.add(table);
        }
        var startTime = System.currentTimeMillis();
        migrationBaseConverts.forEach(convert -> {
            try {
                this.migrate(convert);
            } catch (Exception e) {
                log.error("数据源{}迁移失败", convert.getClass().getSimpleName(), e);
            }
        });
        log.info("数据源迁移完成,耗时{}ms", System.currentTimeMillis() - startTime);
        return true;
    }

    /**
     * 迁移数据
     *
     * @param convert 转换类
     * @param <S>     源数据
     * @param <T>     目标数据
     */
    private <S, T> void migrate(MigrationBaseConvert<S, T> convert) {
        String name = convert.getClass().getSimpleName();
        if (!tables.isEmpty() && !tables.contains(name)) {
            return;
        }
        log.info("开始迁移数据: {}", name);
        var service = convert.getService();
        var current = 1L;
        var newPage = convert.getAndConvertDataPage(current);
        while (current <= newPage.getPages()) {
            log.info("开始迁移{}第{}页数据", name, current);
            if (CollectionUtils.isNotEmpty(newPage.getRecords())) {
                service.saveBatch(newPage.getRecords());
            }
            current++;
            newPage = convert.getAndConvertDataPage(current);
        }
        convert.mapClear();
    }
}

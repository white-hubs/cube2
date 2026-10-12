package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.ClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 班次管理操作
 * @date 2023/6/13 10:08
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesCommandOperationService extends ServiceImpl<ClassesMapper, ClassesPO> {

    /**
     * @Author wql
     * @Description 新增班次
     * @Date 10:11 2023/6/13
     **/
    @LogRecord(success = "新增:{{#classesPO.name}}", subType = "CLASSES", subTypeName = "班次", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#classesPO.id}}"
    )
    public void insertClassesCommand(ClassesPO classesPO) {
        log.info("新增班次操作...");
        this.save(classesPO);
    }

    /**
     * @Author wql
     * @Description 更新班次
     * @Date 10:12 2023/6/13
     **/
    @LogRecord(success = "更新:{_DIFF{#newPo}}", subType = "CLASSES", subTypeName = "班次", operationType = "UPDATE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPo.id}}"
    )
    public void updateClassesCommand(ClassesPO oldPo, ClassesPO newPo) {
        log.info("更新班次操作...");
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, oldPo);
        this.updateById(newPo);
    }

    /**
     * @Author wql
     * @Description 删除班次
     * @Date 10:13 2023/6/13
     **/
    @LogRecord(success = "删除:{{#classesPO.name}}", subType = "CLASSES", subTypeName = "班次", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#classesPO.id}}"
    )
    public void deleteClassesCommand(ClassesPO classesPO) {
        log.info("删除班次操作...");
        DatabaseContextHolder.softDelete(this, classesPO.getId());
    }
}

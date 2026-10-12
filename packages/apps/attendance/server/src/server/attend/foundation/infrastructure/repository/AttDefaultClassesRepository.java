package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttClassesRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesParam;
import com.guhecloud.platform.base.util.DateUtils;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDefaultClassesRepository implements AttClassesRepository {

    private final AttClassesMapper attClassesMapper;

    /**
     * 编辑考勤班次
     *
     * @param param 班次对象
     */
    @Override
    @LogRecord(success = "更新:{_DIFF{#newPo}}", subType = "CLASSES", subTypeName = "班次", operationType = "UPDATE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPo.id}}"
    )
    public void updateAttClasses(AttClassesParam param, AttClassesPO oldPo, AttClassesPO newPo) {
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, oldPo);
        attClassesMapper.update(null, Wrappers.<AttClassesPO>lambdaUpdate().eq(AttClassesPO::getId, param.getId())
                                        .set(AttClassesPO::getName, param.getName())
                                        .set(AttClassesPO::getNickName, param.getNickName())
                                        .set(AttClassesPO::getClassesType, param.getClassesType())
                                        .set(AttClassesPO::getDeptIds, param.getDeptIds())
                                        .set(AttClassesPO::getEndTimeIsMorrow, param.getEndTimeIsMorrow())

                                        .set(AttClassesPO::getStartWorkTime, param.getStartWorkTime())
                                        .set(AttClassesPO::getStartClockBeforeTime, param.getStartClockBeforeTime())
                                        .set(AttClassesPO::getStartClockAfterTime, param.getStartClockAfterTime())

                                        .set(AttClassesPO::getEndWorkTime, param.getEndWorkTime())
                                        .set(AttClassesPO::getEndClockBeforeTime, param.getEndClockBeforeTime())
                                        .set(AttClassesPO::getEndClockAfterTime, param.getEndClockAfterTime())

                                        .set(AttClassesPO::getRestStartTime, param.getRestStartTime())
                                        .set(AttClassesPO::getRestEndTime, param.getRestEndTime())

                                        .set(AttClassesPO::getUpdateTime, LocalDateTime.now())

                               );
    }

    /**
     * 新增班次
     *
     * @param classesPO 班次
     */
    @Override
    @LogRecord(success = "新增:{{#classesPO.name}}", subType = "CLASSES", subTypeName = "班次", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#classesPO.id}}"
    )
    public void insertAttClasses(AttClassesPO classesPO) {
        attClassesMapper.insert(classesPO);
    }

    /**
     * 删除班次
     *
     * @param classesPO 班次信息
     */
    @Override
    @LogRecord(success = "删除:{{#classesPO.name}}", subType = "CLASSES", subTypeName = "班次", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#classesPO.id}}"
    )
    public void deleteAttClasses(AttClassesPO classesPO) {
        if (classesPO != null) {
            attClassesMapper.removeById(DateUtils.toTimestamp(LocalDateTime.now()), classesPO.getId());
        }
    }
}

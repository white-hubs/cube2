package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.ClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.ClassesCommandOperationService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.ClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.ClassesValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bq
 * @date 2022/11/10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesCommandServiceImpl extends ServiceImpl<ClassesMapper, ClassesPO> implements ClassesCommandService {

    private final ClassesValidator classesValidator;
    private final ClassesCommandOperationService classesCommandOperationService;

    @Override
    public Long saveOrUpdate(SubmitClassesCommand submitClassesCommand) {
        Long id = submitClassesCommand.getId();
        classesValidator.verifyIsExistByName(id, submitClassesCommand.getName());
        classesValidator.verifyIsCrossByDate(submitClassesCommand);
        if (null == id) {
            ClassesPO classesPO = ObjectUtils.copying(submitClassesCommand, ClassesPO.class);
            classesPO.setMark(ClassesMarkEnum.WORK.name());
            classesCommandOperationService.insertClassesCommand(classesPO);
            id = classesPO.getId();
        } else {
            ClassesPO saved = this.getById(id);
            ClassesPO oldPo = new ClassesPO();
            ObjectUtils.copying(saved, oldPo);
            if (saved != null) {
                Assert.isNotAcceptable(ClassesMarkEnum.All_DAY_REST.name().equals(saved.getMark()), "休息班次不支持编辑!");
                Assert.isNotAcceptable((ClassesMarkEnum.DEFAULT_WORK.name().equals(saved.getMark()) &&
                                        !submitClassesCommand.getName().equals(saved.getName())), "默认班次不支持修改名称!");
                ObjectUtils.copying(submitClassesCommand, saved);
                classesCommandOperationService.updateClassesCommand(oldPo, saved);
            }
        }
        return id;
    }

    @Override
    public void delete(Long id) {
        ClassesPO classesPO = this.getById(id);
        if (Objects.isNull(classesPO)) {
            return;
        }
        Assert.isNotAcceptable(ClassesMarkEnum.All_DAY_REST.name().equals(classesPO.getMark()), "休息班次不支持删除");
        Assert.isNotAcceptable(ClassesMarkEnum.DEFAULT_WORK.name().equals(classesPO.getMark()), "默认班次不支持删除");
        classesCommandOperationService.deleteClassesCommand(classesPO);
    }

}

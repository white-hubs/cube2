package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;

/**
 * @author bq
 * @date 2022/11/10
 * @description
 */
public interface ClassesCommandService extends IService<ClassesPO> {

    Long saveOrUpdate(SubmitClassesCommand submitClassesCommand);

    void delete(Long id);

}

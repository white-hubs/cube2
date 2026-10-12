package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.SubmitWorkCommand;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 * @description 排班和值班公共命令
 */
public interface WorkCommandService {

    /**
     * 排班
     *
     * @param command 排班数据
     */
    void generateWorkData(SubmitWorkCommand command);

    /**
     * 删除排班数据
     *
     * @param removeStaffIds 删除的员工IDS
     * @param type           考勤组类型
     */
    void removeWorkData(List<Long> removeStaffIds, String type);

    /**
     * 导入excel模板
     *
     * @param month 月份 格式 yyyy-MM
     * @param file  excel文件
     * @param type  类型（值班、排班）
     */
    void importTemplate(String month, MultipartFile file, String type);

}

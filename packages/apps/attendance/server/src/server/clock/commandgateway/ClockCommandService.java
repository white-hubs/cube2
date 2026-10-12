package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.SubmitClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.SubmitThirdClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.UpdateClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo.ThirdClockResultVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;

/**
 * @author bq
 * @date 2022/11/30
 * @description
 */
public interface ClockCommandService extends IService<ClassesClockPO> {

    /**
     * 手机端打卡
     *
     * @param command 打卡数据
     */
    StaffDetailDTO saveByPhone(SubmitClockCommand command);

    /**
     * 更新单个打卡结果为正常（目前主要是加班和补卡需要用到）
     *
     * @param command
     */
    void updateClockToNormal(UpdateClockCommand command);

    /**
     * 更新时间段内的打卡结果为正常（目前主要是请假用）
     *
     * @param command
     */
    void updateClockToNormalByPeriod(UpdateClockCommand command);

    /**
     * 保存第三方打卡数据
     *
     * @param command
     */
    ThirdClockResultVO saveByThird(SubmitThirdClockCommand command);

}

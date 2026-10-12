package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTaskCheckIn;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.p.TaskCheckInPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskCheckInVO;
import com.guhecloud.platform.base.model.Pagination;

public interface DisinfectionTaskCheckInService extends IService<DisinfectionTaskCheckIn> {
    Pagination<TaskCheckInVO> pageCheckIn(TaskCheckInPageParam param);
}

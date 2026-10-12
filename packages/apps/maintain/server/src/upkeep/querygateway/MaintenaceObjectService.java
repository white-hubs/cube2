package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObject;
import com.guhecloud.platform.base.model.Period;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：维保对象服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
public interface MaintenaceObjectService extends IService<MaintenanceObject> {

    Integer isUpkeepValid(@RequestParam("deviceId") String deviceId, Period period);
}
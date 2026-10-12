package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyAlarmRule;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyAlarmRuleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyAlarmRuleQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyAlarmRuleVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：报警规则设备绑定服务层
 *
 * @author lrj
 * @date 2022-07-07 15:12:36
 */
public interface EnergyAlarmRuleService extends IService<EnergyAlarmRule> {

    Long add(EnergyAlarmRuleParam param);

    void update(Long id, EnergyAlarmRuleParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EnergyAlarmRuleVO getById(Long id);

    Pagination<EnergyAlarmRuleVO> getPage(EnergyAlarmRuleQueryParam param);

}
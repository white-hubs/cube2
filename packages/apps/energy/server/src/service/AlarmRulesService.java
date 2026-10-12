package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AlarmRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.AlarmRulesParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.AlarmRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AlarmRulesVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：报警规则配置服务层
 *
 * @author lrj
 * @date 2021-12-30 15:12:36
 */
public interface AlarmRulesService extends IService<AlarmRules> {

    Long add(AlarmRulesParam param);

    void update(Long id, AlarmRulesParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    AlarmRulesVO getById(Long id);

    Pagination<AlarmRulesVO> getPage(AlarmRulesQueryParam param);

    /**
     * 修改告警配置状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Boolean status);

    /**
     * @Description 设备规则
     * @Author lrj
     */
    List<AlarmRules> getRules(Long id, Integer tenant);

}
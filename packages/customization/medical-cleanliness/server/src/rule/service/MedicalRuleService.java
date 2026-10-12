package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleAllParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RulePageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RulePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Set;

/**
 * (rule)服务接口
 *
 * @author : lijie
 * @date : 2024-3-20
 */
public interface MedicalRuleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     * @author : lijie
     */
    RuleVO queryById(Long id);

    /**
     * 查询下发iot联动规则信息
     */
    RuleVO queryByIotRuleId(Long iotRuleId);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     * @return 查询结果
     * @author : lijie
     */
    Pagination<RulePageVO> queryByPage(RulePageParam param);

    List<RuleVO> queryByAll(RuleAllParam param);
    List<RuleVO> queryByAll();

    /**
     * 新增数据
     *
     * @param param 新增数据
     * @return 新增结果
     * @author : lijie
     */
    Long insert(RuleParam param);

    /**
     * 更新数据
     *
     * @param param 修改数据
     * @return 修改结果
     * @author : lijie
     */
    Boolean update(RuleParam param);
    Boolean updateByIotRuleId(RuleParam param);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 删除结果
     * @author : lijie
     */
    Boolean deleteById(Long id);

    /**
     * 获取规则主键集合
     *
     * @return
     */
    Set<Long> getMedicalRuleIds();

    void getNormAllByDevice();
}

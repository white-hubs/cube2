package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalNormRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleNormPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleNormParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * 指标设置(rule_norm)服务接口
 * @author : lijie
 * @date : 2024-3-20
 */
public interface RuleNormService{

    /**
     * 通过ID查询单条数据
     * @author : lijie
     * @param id 主键
     * @return 实例对象
     */
    RuleNormVO queryById(Long id);
    RuleNormVO queryByCode(String code);

    /**
     * 分页查询
     * @author : lijie
     * @param param: 筛选条件
     * @return 查询结果
     */
    Pagination<RuleNormVO> queryByPage(RuleNormPageParam param);
    List<RuleNormVO> queryAllByRuleId(Long ruleId);

    /**
     * 新增数据
     * @author : lijie
     * @param param 新增数据
     * @return 上下限是否修改
     */
    Boolean insertBatch(List<RuleNormParam> param,Long ruleId);

    /**
     * 通过规则id删除数据
     * @author : lijie
     * @param ruleId 主键
     * @return 删除结果
     */
    Boolean deleteByRuleId(Long ruleId);

    /**判断指标是否异常*/
    Map<String,String> getNormStatus(List<MedicalNormRecord> recordList,Integer tenant,Long deviceId);
}
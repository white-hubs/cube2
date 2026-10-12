package com.guhecloud.cube.biz.mdata.server.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.RiskLevel;
import com.guhecloud.cube.biz.mdata.server.supplier.param.RiskLevelCreateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.RiskLevelUpdateParam;

import java.util.List;

public interface RiskLevelService extends IService<RiskLevel> {

    /**
     * 风险级别新增
     *
     * @param params 风险级别新增参数
     */
    void save(List<RiskLevelCreateParam> params);

    /**
     * 风险级别初始化
     *
     * @param tenant 市场租户编号
     */
    void initRiskLevel(Integer tenant);

    /**
     * 风险级别编辑
     *
     * @param params 风险级别编辑参数
     */
    void update(List<RiskLevelUpdateParam> params);

    /**
     * 根据数值查询风险级别
     *
     * @param value 风险数值
     *
     * @return 风险级别信息
     */
    RiskLevel getByValue(Integer value, List<RiskLevel> riskLevels);

    /**
     * 根据主键集合查询风险级别
     *
     * @param ids 风险级别主键
     *
     * @return 风险级别信息
     */
    List<RiskLevel> getByIds(List<Long> ids);

    List<RiskLevel> listRiskLevels();

}

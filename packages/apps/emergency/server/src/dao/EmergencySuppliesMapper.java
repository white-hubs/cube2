package com.guhecloud.cube.biz.emergency.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySupplies;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesStatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：应急物资管理 Repository接口
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
@Repository
public interface EmergencySuppliesMapper extends BaseMapper<EmergencySupplies> {

    List<EmergencySuppliesStatVO> getEmergencySuppliesStatForStock();
}
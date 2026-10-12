package com.guhecloud.cube.biz.emergency.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUser;
import com.guhecloud.cube.biz.emergency.server.vo.CommonStatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：应急人员管理 Repository接口
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
@Repository
public interface EmergencyUserMapper extends BaseMapper<EmergencyUser> {

    List<CommonStatVO> getEmergencyUserStat();
}
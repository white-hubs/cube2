package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUser;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急人员管理服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
public interface EmergencyUserService extends IService<EmergencyUser> {

    Long add(EmergencyUserParam param);

    void update(Long id, EmergencyUserParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyUserVO getById(Long id);

    Pagination<EmergencyUserVO> getPage(EmergencyUserQueryParam param);

    List<CommonStatVO> getEmergencyUserStat();
}
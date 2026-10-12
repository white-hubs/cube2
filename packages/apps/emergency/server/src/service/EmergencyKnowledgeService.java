package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyKnowledge;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyKnowledgeParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyKnowledgeQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyKnowledgeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急知识（技术）管理服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface EmergencyKnowledgeService extends IService<EmergencyKnowledge> {

    Long add(EmergencyKnowledgeParam param);

    void update(Long id, EmergencyKnowledgeParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyKnowledgeVO getById(Long id);

    Pagination<EmergencyKnowledgeVO> getPage(EmergencyKnowledgeQueryParam param);

}
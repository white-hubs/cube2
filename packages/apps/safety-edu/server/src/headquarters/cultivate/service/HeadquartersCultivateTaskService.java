package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 培训任务服务层
 *
 * @author lanchun
 * @date 2024/4/10 14:54
 */
public interface HeadquartersCultivateTaskService extends IService<HeadquartersCultivateTask> {

    Long add(HeadquartersCultivateTaskParam param);

    void update(Long id, HeadquartersCultivateTaskParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    HeadquartersCultivateTaskDetailVO getById(Long id);

    Pagination<HeadquartersCultivateTaskVO> getPage(HeadquartersCultivateTaskQueryParam param);

    void updateStatus(Long id, String status);
}
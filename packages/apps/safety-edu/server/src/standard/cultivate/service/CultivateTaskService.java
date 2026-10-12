package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateSignPublicParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskPublicQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCommonStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateMyTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskDetailVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskWithStatVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateUserStatVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Set;

/**
 * 描述：培训任务服务层
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
public interface CultivateTaskService extends IService<CultivateTask> {

    Long add(CultivateTaskParam param);

    void update(Long id, CultivateTaskParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    CultivateTaskDetailVO getById(Long id);

    Pagination<CultivateTaskVO> getPage(CultivateTaskQueryParam param);

    Pagination<CultivateMyTaskVO> getMyTaskPage(CultivateTaskQueryParam param);

    Pagination<CultivateMyTaskVO> getPublicMyTaskPage(CultivateTaskPublicQueryParam param);

    Pagination<CultivateTaskWithStatVO> getPageExtendStat(CultivateTaskQueryParam param);

    List<CultivateTaskPageVO> getCultivateStandardList(CultivateTaskQueryParam param);

    void exportCultivateExtendStat(CultivateTaskQueryParam param,
                                   HttpServletRequest request,
                                   HttpServletResponse response
                                  );

    void updateStatus(Long id, String status);

    void updatePublicStatus(Long id, String status);

    void cultivateSign(Long id);

    void cultivatePublicSign(Long id, CultivateSignPublicParam cultivateSignPublicParam);

    CultivateTaskStatVO getCultivateTaskStat(CultivateTaskQueryParam param);

    CultivateTaskStatVO getCultivateTaskPermissionStat(CultivateTaskQueryParam param);

    List<CultivateCommonStatVO> getCultivateThemeStat(CultivateTaskQueryParam param);

    List<CultivateCommonStatVO> getCultivateTrendStat(CultivateTaskQueryParam param);

    CultivateUserStatVO getTrainUserStat(CultivateTaskQueryParam param);

    List<CultivateCommonStatVO> getCultivateTargetStat(CultivateTaskQueryParam param);

    List<CultivateUserStatVO> getUserSignStatByTask(CultivateTaskQueryParam param);

    void deleteBySourceKeys(Set<Long> sourceKeys);
}
package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskCoursewareLibrary;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskCoursewarLibraryQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCoursewarVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：培训课件管理服务层
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
public interface CultivateTaskCoursewarLibraryService extends IService<CultivateTaskCoursewareLibrary> {

    Long add(CultivateCoursewarParam param);

    void update(Long id, CultivateCoursewarParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    CultivateCoursewarVO getById(Long id);

    Pagination<CultivateCoursewarVO> getPage(CultivateTaskCoursewarLibraryQueryParam param);

    List<CultivateCoursewarVO> getCoursewarsByCultivateId(Long cultivateId);

}
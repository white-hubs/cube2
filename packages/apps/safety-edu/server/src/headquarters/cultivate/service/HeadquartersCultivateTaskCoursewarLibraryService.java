package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskCoursewareLibrary;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskCoursewarLibraryQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：培训课件管理服务层
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
public interface HeadquartersCultivateTaskCoursewarLibraryService extends IService<HeadquartersCultivateTaskCoursewareLibrary> {

    Long add(HeadquartersCultivateCoursewarParam param);

    void update(Long id, HeadquartersCultivateCoursewarParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    HeadquartersCultivateCoursewarVO getById(Long id);

    Pagination<HeadquartersCultivateCoursewarVO> getPage(HeadquartersCultivateTaskCoursewarLibraryQueryParam param);

    List<HeadquartersCultivateCoursewarVO> getCoursewarsByCultivateId(Long cultivateId);

}
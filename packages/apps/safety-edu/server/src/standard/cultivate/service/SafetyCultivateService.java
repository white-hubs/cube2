package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.SafetyCultivate;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.SafetyCultivateParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.SafetyCultivateQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.SafetyCultivateStatisticVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.SafetyCultivateVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @author wql
 * @describe 安全培训(二维码中转页)
 * @date 2024/3/19 19:34
 */
public interface SafetyCultivateService extends IService<SafetyCultivate> {

    Long saveSafetyCultivate(SafetyCultivateParam param);

    Pagination<SafetyCultivateVO> safetyCultivateList(SafetyCultivateQueryParam param);

    SafetyCultivateStatisticVO safetyCultivateStatistics(SafetyCultivateQueryParam param);
}

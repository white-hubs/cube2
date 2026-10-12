package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskUserVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 描述：培训人员服务层
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
public interface CultivateTaskUserService extends IService<CultivateTaskUser> {

    Long add(CultivateTaskUserParam param);

    void update(Long id, CultivateTaskUserParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    CultivateTaskUserVO getById(Long id);

    Pagination<CultivateTaskUserVO> getPage(CultivateTaskUserQueryParam param);

    void exportCultivateUser(CultivateTaskUserQueryParam param,
                             HttpServletRequest request,
                             HttpServletResponse response
                            );

    List<CultivateTaskUserVO> getCultivateUsersByCultivateId(Long cultivateId);

}
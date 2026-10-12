package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.AreaPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.AreaUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.AreaInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.AreaPageVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.validation.Valid;

import java.util.List;

public interface BusinessDisinfectionAreaService {

    /**
     * 获取项目区域列表
     *
     * @param projectId 项目id
     *
     * @return 区域列表
     */
    List<AreaInfoVO> listAll(long projectId, String keyword);

    /**
     * 新增区域
     *
     * @param param 参数
     *
     * @return 新增结果
     */
    boolean insertArea(@Valid AreaUpsertParam param, long projectId);

    /**
     * 修改区域
     *
     * @param param 修改参数
     *
     * @return 修改结果
     */
    boolean updateArea(@Valid AreaUpsertParam param, long projectId);

    /**
     * 删除区域
     *
     * @param areaId 区域id
     *
     * @return 删除结果
     */
    boolean deleteArea(long areaId);

    /**
     * 区域分页
     *
     * @param param 区域分页参数
     *
     * @return 区域分页结果
     */
    Pagination<AreaPageVO> pageArea(AreaPageParam param, String projectId);

    boolean singleUpdateArea(AreaUpsertParam.AreaParam param, Long projectId, Long id);
}

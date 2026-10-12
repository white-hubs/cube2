package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.MaterialInsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.MaterialPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.MaterialInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.ProjectMaterialInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.MaterialPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface BusinessDisinfectionMaterialService {
    /**
     * 获取项目下的资料
     *
     * @param projectId 项目id
     * @param keyword   关键字
     *
     * @return 项目下的资料
     */

    List<ProjectMaterialInfoVO> listAll(long projectId, String keyword);

    /**
     * 新增项目下的资料
     *
     * @param param 参数
     *
     * @return 是否成功
     */
    boolean insertMaterial(MaterialInsertParam param);

    /**
     * 删除项目下的资料
     *
     * @param ids 项目下的资料ids
     *
     * @return 是否成功
     */
    boolean deleteMaterial(String ids);

    /**
     * 获取项目下的资料列表
     *
     * @param fileTypeId 文件类型id
     * @param projectId  项目id
     * @param keyword    关键字
     *
     * @return 项目下的资料列表
     */
    List<MaterialInfoVO> listMaterials(long fileTypeId, Long projectId, String keyword);

    /**
     * 获取项目下的资料分页列表
     *
     * @param param 项目下的资料分页列表参数
     *
     * @return 项目下的资料分页列表
     */
    Pagination<MaterialPageVO> pageMaterials(MaterialPageParam param);
}

package com.guhecloud.cube.base.visualization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.visualization.server.entity.ModelScene;
import com.guhecloud.cube.base.visualization.server.param.ModelSceneParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.vo.ModelSceneVO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 15:15
 * @Description:
 */
public interface ModelSceneService extends IService<ModelScene> {

    /**
     * 根据项目id查询模型场景配置
     *
     * @param param
     *
     * @return
     */
    List<ModelSceneVO> queryModelSceneByProjectId(VisualizationBaseQueryParam param);

    /**
     * 新增模型场景配置
     *
     * @param modelSceneParam
     *
     * @return
     */
    Boolean saveOrUpdateModelScene(ModelSceneParam modelSceneParam);

    /**
     * 自动关联区域和模型
     *
     * @param file
     * @param param
     */
    void autoRelateAreaAndModel(MultipartFile file, ModelSceneParam param);

    /**
     * 自动关联模板和模型
     *
     * @param response
     */
    void autoRelateTemplateWrite(HttpServletResponse response);
}

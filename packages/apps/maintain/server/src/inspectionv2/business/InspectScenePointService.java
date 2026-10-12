package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointExcelTemplate;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectScenePointParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointPO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author wql
 * @describe 现场点
 * @date 2023/6/5 15:54
 */
public interface InspectScenePointService extends IService<InspectScenePointPO> {
    /**
     * @Author wql
     * @Description 新增现场点
     * @Date 17:14 2023/6/5
     **/
    void addPoint(InspectScenePointParam point);

    /**
     * @Author wql
     * @Description 现场点列表
     * @Date 18:11 2023/6/5
     **/
    Pagination<ScenePointVO> scenePointList(InspectScenePointQueryParam param);

    /**
     * @Author wql
     * @Description 更新现场点
     * @Date 9:21 2023/6/6
     **/
    void updatePoint(InspectScenePointParam point);

    /**
     * @Author wql
     * @Description 删除现场点
     * @Date 9:35 2023/6/6
     **/
    void deletePoint(Long pointId);

    /**
     * @Author wql
     * @Description 获取现场点详情
     * @Date 15:13 2023/11/9
     **/
    ScenePointVO getScenePointDetail(Long objId);

    /**
     * 现场点列表(不分页)
     */
    List<ScenePointVO> scenePointListAll(InspectScenePointQueryParam param);

    /**
     * 根据名称查询现场点
     *
     * @param name 现场点名称
     *
     * @return 现场点详情
     */
    ScenePointVO scenePointDetailByName(String name);

    List<String> excelSave(List<ScenePointExcelTemplate> param, Map<String, ExcelColumnSelectionProvider> providerMap);

    /**
     * 获取现场点ID列表
     *
     * @param param 查询参数
     *
     * @return 现场点ID列表
     */
    List<ScenePointIdTypeDTO> getScenePointIdTypes(InspectScenePointQueryParam param);

    /**
     * 根据区域id列表批量获取现场点数统计
     *
     * @param areaIds 区域id列表
     * @return 区域id与现场点数的映射，key为区域id，value为现场点数
     */
    Map<Long, Long> countByAreaIds(List<Long> areaIds);
}

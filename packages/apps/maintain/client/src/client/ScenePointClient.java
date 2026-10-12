package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author wql
 * @describe 现场点查询
 * @date 2023/11/22 17:20
 */
@FeignClient(ServiceRoute.TAG)
public interface ScenePointClient {

    /**
     * @Author wql
     * @Description 现场点详情查询
     * @Date 17:20 2023/11/22
     **/
    @GetMapping("/inspect/detail/{id}")
    ScenePointVO scenePointDetail(@PathVariable("id") Long id);

    /**
     * 现场点列表(不分页)
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @GetMapping("/inspect/pointListAll")
    List<ScenePointVO> scenePointListAll(@SpringQueryMap InspectScenePointQueryParam param);

    /**
     * 现场点列表(分页)
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @PostMapping("/inspect/pointPage")
    Pagination<ScenePointVO> scenePointList(@RequestBody InspectScenePointQueryParam param);

    /**
     * 批量获取现场点
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @PostMapping("/inspect/point/batch")
    List<ScenePointVO> scenePointListBatch(@RequestBody InspectScenePointQueryParam param);

    /**
     * 根据名称查询现场点
     *
     * @param name 现场点名称
     *
     * @return 现场点详情
     */
    @GetMapping("/inspect/detailByName")
    ScenePointVO scenePointDetailByName(@RequestParam("name") String name);

    /**
     * 获取现场点ID列表
     *
     * @param param 查询参数
     *
     * @return 现场点ID列表
     */
    @PostMapping("/inspect/point/idTypes")
    List<ScenePointIdTypeDTO> getScenePointIdTypes(@RequestBody InspectScenePointQueryParam param);

    @PostMapping("/inspect/count/by/areaIds")
    Map<Long, Long> countByAreaIds(@RequestBody List<Long> gridAreaIds);
}

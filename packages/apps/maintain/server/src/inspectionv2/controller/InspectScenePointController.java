package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.common.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl.ScenePointTempleHandlerImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointExcelTemplate;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.ScenePointIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectScenePointParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PointDeleteBatchParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.BatchOptResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.nacos.common.http.param.MediaType.APPLICATION_OCTET_STREAM;

/**
 * @author wql
 * @describe 现场点
 * @date 2023/6/5 15:56
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectScenePointController {

    private final InspectScenePointService inspectScenePointService;
    private final ScenePointTempleHandlerImpl scenePointTempleHandler;

    /**
     * 新增现场点
     *
     * @param point 现场点信息
     */
    @PutMapping("/inspect/addPoint")
    public void addPoint(@Valid @RequestBody InspectScenePointParam point) {
        inspectScenePointService.addPoint(point);
    }

    /**
     * 现场点列表
     *
     * @param param 现场点列表查询参数
     *
     * @return 现场点列表
     */
    @GetMapping("/inspect/pointList")
    public Pagination<ScenePointVO> scenePointList(InspectScenePointQueryParam param) {
        return inspectScenePointService.scenePointList(param);
    }

    /**
     * 现场点列表
     *
     * @param param 现场点列表查询参数
     *
     * @return 现场点列表
     */
    @PostMapping("/inspect/pointPage")
    public Pagination<ScenePointVO> scenePointPage(@RequestBody InspectScenePointQueryParam param) {
        return inspectScenePointService.scenePointList(param);
    }

    /**
     * 现场点列表(不分页)
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @GetMapping("/inspect/pointListAll")
    public List<ScenePointVO> scenePointListAll(InspectScenePointQueryParam param) {
        return inspectScenePointService.scenePointListAll(param);
    }

    /**
     * 批量获取现场点
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @PostMapping("/inspect/point/batch")
    List<ScenePointVO> scenePointListBatch(@RequestBody InspectScenePointQueryParam param) {
        return inspectScenePointService.scenePointListAll(param);
    }

    /**
     * @Author wql
     * @Description 现场点详情
     * @Date 17:16 2023/11/22
     **/
    @GetMapping("/inspect/detail/{id}")
    public ScenePointVO scenePointDetail(@PathVariable("id") Long id) {
        return inspectScenePointService.getScenePointDetail(id);
    }

    /**
     * 更新现场点
     *
     * @param point 现场点信息
     */
    @PutMapping("/inspect/updatePoint")
    public void updatePoint(@Valid @RequestBody InspectScenePointParam point) {
        inspectScenePointService.updatePoint(point);
    }

    /**
     * 删除现场点
     *
     * @param pointId 现场点id
     */
    @DeleteMapping("/inspect/deletePoint/{pointId}")
    public void deletePoint(@PathVariable("pointId") Long pointId) {
        inspectScenePointService.deletePoint(pointId);
    }

    /**
     * 根据名称查询现场点
     *
     * @param name 现场点名称
     *
     * @return 现场点详情
     */
    @GetMapping("/inspect/detailByName")
    public ScenePointVO scenePointDetailByName(@RequestParam("name") String name) {
        return inspectScenePointService.scenePointDetailByName(name);
    }

    /**
     * 现场点导表模板
     *
     * @param response 响应对象
     */
    @GetMapping("/inspect/point/download-template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        List<ScenePointExcelTemplate> excelList = new ArrayList<>();
        //write to excel
        response.setContentType(APPLICATION_OCTET_STREAM);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=批量导入模板.xlsx");
        ExcelWriter<ScenePointExcelTemplate> excelWriter = new ExcelWriter<>();
        excelWriter.writeData(excelList, scenePointTempleHandler.buildExcelColumnSelectionProvider(), ScenePointExcelTemplate.class, response
                .getOutputStream(), false, 9999);
    }

    /**
     * 现场点批量导入
     *
     * @param file 现场点
     *
     * @return 错误信息
     *
     * @throws Exception
     */
    @PostMapping("/inspect/point/import-template")
    public BatchOptResult importTemplate(@RequestPart("file") MultipartFile file) throws Exception {
        return scenePointTempleHandler.importFromTemplate(file.getInputStream());
    }

    /**
     * 现场点批量删除
     *
     * @param param idList 现场点id集合
     *
     * @return 是否成功
     */
    @PutMapping("/inspect/point/delete-batch")
    public Boolean deleteBatch(@RequestBody PointDeleteBatchParam param) {
        if (CollectionUtils.isEmpty(param.getIdList())) {
            return true;
        }
        return inspectScenePointService.removeByIds(param.getIdList());
    }

    /**
     * 获取现场点ID列表
     *
     * @param param 查询参数
     *
     * @return 现场点ID列表
     */
    @PostMapping("/inspect/point/idTypes")
    public List<ScenePointIdTypeDTO> getScenePointIdTypes(@RequestBody @Valid InspectScenePointQueryParam param) {
        return inspectScenePointService.getScenePointIdTypes(param);
    }

    /**
     * 根据区域id列表批量获取现场点数统计
     *
     * @param areaIds 区域id列表
     * @return 区域id与现场点数的映射，key为区域id，value为现场点数
     */
    @PostMapping("/inspect/count/by/areaIds")
    public Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds) {
        return inspectScenePointService.countByAreaIds(areaIds);
    }

}

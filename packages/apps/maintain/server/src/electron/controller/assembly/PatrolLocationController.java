package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.assembly;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.guhecloud.cube.srv.gen.maintain.server.common.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationByBluetoothParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolLocationParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 巡更点配置
 *
 * @author : lijie
 * @date : 2023-5-25
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolLocationController {
    private final PatrolLocationService patrolLocationService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    @GetMapping("/patrol/location/{id}")
    public PatrolLocationVO queryById(@PathVariable("id") Long id) {
        return patrolLocationService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    @GetMapping("/patrol/location/page")
    public Pagination<PatrolLocationVO> queryLocationPage(PatrolLocationPageParam param) {
        return patrolLocationService.queryLocationPage(param);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    @PostMapping("/patrol/points")
    public Pagination<PatrolLocationVO> points(@RequestBody @Valid PatrolLocationPageParam param) {
        return patrolLocationService.queryLocationPage(param);
    }

    /**
     * 批量获取巡更点详情
     */
    @PostMapping("/patrol/location/batch")
    List<PatrolLocationVO> queryLocationBatch(@RequestBody PatrolLocationPageParam param) {
        return patrolLocationService.queryLocationBatch(param);
    }

    /**
     * 根据蓝牙编号查询巡更点列表
     *
     * @Author wql
     * @Description 根据蓝牙编号查询巡更点列表
     * @Date 13:59 2023/10/25
     **/
    @GetMapping("/patrol/location/listByBluetooth")
    public List<PatrolLocationVO> listByBluetooth(PatrolLocationByBluetoothParam param) {
        return patrolLocationService.listByBluetooth(param);
    }

    /**
     * 根据巡更路线id查询巡更点列表
     *
     * @param routeId 巡更路线id
     *
     * @return 巡更点列表
     */
    @GetMapping("/patrol/location/listByRoute")
    public List<PatrolLocationVO> listByRoute(Long routeId) {
        return patrolLocationService.listByRoute(routeId);
    }

    /**
     * 新增数据
     *
     * @param param nfc信息
     *
     * @return 新增结果
     */
    @PostMapping("/patrol/location/insert")
    public Boolean insert(@RequestBody PatrolLocationParam param) {
        return patrolLocationService.insert(param);
    }

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    @PutMapping("/patrol/location/update")
    public Boolean update(@RequestBody PatrolLocationParam param) {
        return patrolLocationService.update(param);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/patrol/location/{id}")
    public Boolean deleteById(@PathVariable("id") Long id) {
        return patrolLocationService.deleteById(id);
    }

    /**
     * 下载巡更点导入模板
     *
     * @author: lijie
     * @date: 2024/05/14 09:38
     * @param: void
     * @return: 模板
     **/
    @PostMapping("/patrol/location/getTemplateExcel")
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            patrolLocationService.getTemplateExcel(request, response);
        } catch (Exception e) {
            log.error("巡更点导入模板下载失败", e);
            throw new ServiceException("巡更点导入模板下载失败，请联系管理员");
        }
    }

    /**
     * 巡更点导入
     *
     * @author: lijie
     * @date: 2024/05/14 09:40
     * @param: 文件
     * @return: void
     **/
    @PostMapping("/patrol/location/import/template")
    public void importTemplate(@RequestParam("file") MultipartFile file) {
        patrolLocationService.importTemplate(file);
    }

    /**
     * 获取巡更点ID列表
     *
     * @param param 查询参数
     *
     * @return 巡更点ID列表
     */
    @PostMapping("/patrol/location/idTypes")
    public List<PatrolLocationIdTypeDTO> getPatrolPointIdTypes(@RequestBody @Valid PatrolLocationPageParam param) {
        return patrolLocationService.getPatrolPointIdTypes(param);
    }

    /**
     * 根据区域id列表批量获取巡更点数统计
     *
     * @param gridAreaIds 区域id列表
     *
     * @return 区域id与巡更点数的映射，key为区域id，value为巡更点数
     */
    @PostMapping("/patrol/count/by/areaIds")
    public Map<Long, Long> countByAreaIds(@RequestBody List<Long> gridAreaIds) {
        return patrolLocationService.countByAreaIds(gridAreaIds);
    }

}

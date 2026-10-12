package com.guhecloud.cube.customization.xialing.server.drone.controller;

import com.guhecloud.cube.customization.xialing.server.drone.service.DroneService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneResultQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneRouteCommandParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneRouteQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskCreateParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskFinishParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneResultVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneRouteVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneTaskRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneTaskVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 霞凝无人机控制器
 * <p>
 * 提供无人机航迹、任务、任务记录和成果数据的RESTful API接口。
 * 所有接口都委托给 {@link com.guhecloud.cube.customization.xialing.server.drone.service.DroneService} 处理业务逻辑。
 * </p>
 *
 * @author system
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DroneController {
    private final DroneService droneService;

    /**
     * 分页查询航迹列表
     * <p>
     * 支持按航迹名称、机巢SN、类型等条件筛选。
     * </p>
     *
     * @param param 查询参数，包含分页信息和筛选条件
     *
     * @return 分页的航迹数据
     */
    @PostMapping("/drone/routes/page")
    public Pagination<DroneRouteVO> pageRoutes(@RequestBody(required = false) DroneRouteQueryParam param) {
        // 控制层仅负责参数接收与路由分发，具体查询逻辑在服务层实现。
        return droneService.pageRoutes(param);
    }

    /**
     * 创建航迹
     * <p>
     * 调用霞凝平台API创建航迹，然后同步到本地数据库。
     * </p>
     *
     * @param param 航迹创建参数，包含航迹文件和配置信息
     *
     * @return 新创建的航迹ID
     */
    @PostMapping("/drone/routes")
    public Long createRoute(@RequestBody DroneRouteCommandParam param) {
        // 创建后由服务层负责触发本地同步。
        return droneService.createRoute(param);
    }

    /**
     * 更新航迹
     * <p>
     * 调用霞凝平台API更新航迹，然后同步到本地数据库。
     * </p>
     *
     * @param param 航迹更新参数，必须包含航迹ID
     *
     * @return 是否更新成功
     */
    @PutMapping("/drone/routes")
    public Boolean updateRoute(@RequestBody DroneRouteCommandParam param) {
        // 更新航迹并返回统一布尔结果。
        return droneService.updateRoute(param);
    }

    /**
     * 删除航迹
     * <p>
     * 调用霞凝平台API删除航迹，然后同步到本地数据库（标记为已删除）。
     * </p>
     *
     * @param routeId 航迹ID
     *
     * @return 是否删除成功
     */
    @DeleteMapping("/drone/routes/{routeId}")
    public Boolean deleteRoute(@PathVariable("routeId") Long routeId) {
        // 根据路径参数删除指定航迹。
        return droneService.deleteRoute(routeId);
    }

    /**
     * 分页查询任务列表
     * <p>
     * 支持按任务类型、机巢SN、航迹ID、任务名称等条件筛选。
     * </p>
     *
     * @param param 查询参数，包含分页信息和筛选条件
     *
     * @return 分页的任务数据
     */
    @PostMapping("/drone/tasks/page")
    public Pagination<DroneTaskVO> pageTasks(@RequestBody(required = false) DroneTaskQueryParam param) {
        // 透传分页与筛选参数到服务层。
        return droneService.pageTasks(param);
    }

    /**
     * 查询异常任务列表
     * <p>
     * 返回天气冲突或结果状态异常的任务（如失败、取消等）。
     * </p>
     *
     * @return 异常任务列表
     */
    @GetMapping("/drone/tasks/abnormal")
    public List<DroneTaskVO> listAbnormalTasks() {
        // 查询异常任务聚合视图。
        return droneService.listAbnormalTasks();
    }

    /**
     * 分页查询任务记录列表
     * <p>
     * 任务记录是任务的执行实例，一个任务可以有多次执行记录。
     * </p>
     *
     * @param param 查询参数，包含分页信息和任务ID筛选
     *
     * @return 分页的任务记录数据
     */
    @PostMapping("/drone/task-records/page")
    public Pagination<DroneTaskRecordVO> pageTaskRecords(@RequestBody(required = false) DroneTaskRecordQueryParam param) {
        // 查询任务执行记录分页。
        return droneService.pageTaskRecords(param);
    }

    /**
     * 创建任务
     * <p>
     * 调用霞凝平台API创建任务，然后同步任务和任务记录到本地数据库。
     * </p>
     *
     * @param param 任务创建参数，包含任务配置和调度信息
     *
     * @return 创建的任务记录列表
     */
    @PostMapping("/drone/tasks")
    public List<DroneTaskRecordVO> createTask(@RequestBody DroneTaskCreateParam param) {
        // 创建任务后返回第三方返回的任务记录。
        return droneService.createTask(param);
    }

    /**
     * 删除任务
     * <p>
     * 调用霞凝平台API删除任务，然后同步到本地数据库（标记为已删除）。
     * </p>
     *
     * @param taskId 任务ID
     *
     * @return 是否删除成功
     */
    @DeleteMapping("/drone/tasks/{taskId}")
    public Boolean deleteTask(@PathVariable("taskId") Long taskId) {
        // 根据第三方任务ID删除任务。
        return droneService.deleteTask(taskId);
    }

    /**
     * 结束任务
     * <p>
     * 调用霞凝平台API强制结束正在执行的任务。
     * </p>
     *
     * @param param 任务结束参数，包含机巢SN和任务ID
     *
     * @return 是否结束成功
     */
    @PostMapping("/drone/tasks/finish")
    public Boolean finishTask(@RequestBody DroneTaskFinishParam param) {
        // 强制结束任务执行。
        return droneService.finishTask(param);
    }

    /**
     * 分页查询成果数据列表
     * <p>
     * 成果数据包括照片、视频等任务执行产生的文件。
     * 支持按文件类型、通道类型、计划ID等条件筛选。
     * </p>
     *
     * @param param 查询参数，包含分页信息和筛选条件
     *
     * @return 分页的成果数据
     */
    @PostMapping("/drone/results/page")
    public Pagination<DroneResultVO> pageResults(@RequestBody(required = false) DroneResultQueryParam param) {
        // 查询任务成果分页。
        return droneService.pageResults(param);
    }

    /**
     * 下载成果文件。
     *
     * @param fileKey 文件密钥
     * @param response 当前HTTP响应
     */
    @GetMapping("/drone/results/download")
    public void downloadResult(@RequestParam("fileKey") String fileKey, HttpServletResponse response) throws IOException {
        droneService.downloadResultFile(fileKey, response);
    }
}

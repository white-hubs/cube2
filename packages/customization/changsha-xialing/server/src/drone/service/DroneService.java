package com.guhecloud.cube.customization.xialing.server.drone.service;

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

import java.io.IOException;
import java.util.List;

/**
 * 霞凝无人机服务接口
 * <p>
 * 提供无人机航迹、任务、任务记录和成果数据的管理功能，
 * 包括查询、创建、更新、删除以及全量同步等操作。
 * </p>
 *
 * @author system
 */
public interface DroneService {
    /**
     * 分页查询航迹列表
     *
     * @param param 查询参数，包含分页信息和筛选条件
     * @return 分页的航迹数据
     */
    Pagination<DroneRouteVO> pageRoutes(DroneRouteQueryParam param);

    /**
     * 创建航迹
     * <p>
     * 调用霞凝平台API创建航迹，然后同步到本地数据库。
     * </p>
     *
     * @param param 航迹创建参数
     * @return 新创建的航迹ID
     */
    Long createRoute(DroneRouteCommandParam param);

    /**
     * 更新航迹
     * <p>
     * 调用霞凝平台API更新航迹，然后同步到本地数据库。
     * </p>
     *
     * @param param 航迹更新参数
     * @return 是否更新成功
     */
    Boolean updateRoute(DroneRouteCommandParam param);

    /**
     * 删除航迹
     * <p>
     * 调用霞凝平台API删除航迹，然后同步到本地数据库（标记为已删除）。
     * </p>
     *
     * @param routeId 航迹ID
     * @return 是否删除成功
     */
    Boolean deleteRoute(Long routeId);

    /**
     * 分页查询任务列表
     *
     * @param param 查询参数，包含分页信息和筛选条件
     * @return 分页的任务数据
     */
    Pagination<DroneTaskVO> pageTasks(DroneTaskQueryParam param);

    /**
     * 查询异常任务列表
     * <p>
     * 返回天气冲突或结果状态异常的任务。
     * </p>
     *
     * @return 异常任务列表
     */
    List<DroneTaskVO> listAbnormalTasks();

    /**
     * 分页查询任务记录列表
     *
     * @param param 查询参数，包含分页信息和筛选条件
     * @return 分页的任务记录数据
     */
    Pagination<DroneTaskRecordVO> pageTaskRecords(DroneTaskRecordQueryParam param);

    /**
     * 创建任务
     * <p>
     * 调用霞凝平台API创建任务，然后同步到本地数据库。
     * </p>
     *
     * @param param 任务创建参数
     * @return 创建的任务记录列表
     */
    List<DroneTaskRecordVO> createTask(DroneTaskCreateParam param);

    /**
     * 删除任务
     * <p>
     * 调用霞凝平台API删除任务，然后同步到本地数据库（标记为已删除）。
     * </p>
     *
     * @param taskId 任务ID
     * @return 是否删除成功
     */
    Boolean deleteTask(Long taskId);

    /**
     * 结束任务
     * <p>
     * 调用霞凝平台API强制结束正在执行的任务。
     * </p>
     *
     * @param param 任务结束参数
     * @return 是否结束成功
     */
    Boolean finishTask(DroneTaskFinishParam param);

    /**
     * 分页查询成果数据列表
     *
     * @param param 查询参数，包含分页信息和筛选条件
     * @return 分页的成果数据
     */
    Pagination<DroneResultVO> pageResults(DroneResultQueryParam param);

    /**
     * 下载成果文件。
     *
     * @param fileKey 文件密钥
     * @param response 当前HTTP响应
     */
    void downloadResultFile(String fileKey, HttpServletResponse response) throws IOException;

    /**
     * 全量同步当前租户的无人机数据
     * <p>
     * 依次同步航迹、任务、任务记录和成果数据。
     * 通常在定时任务中调用，确保本地数据与霞凝平台保持一致。
     * </p>
     */
    void fullSyncCurrentTenant();
}

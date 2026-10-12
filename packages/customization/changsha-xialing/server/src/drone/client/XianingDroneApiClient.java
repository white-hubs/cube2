package com.guhecloud.cube.customization.xialing.server.drone.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.customization.xialing.server.drone.auth.XianingBizTokenProvider;
import com.guhecloud.cube.customization.xialing.server.drone.dto.DroneQuickTaskResult;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneQuickTaskStartParam;
import com.guhecloud.cube.customization.xialing.server.drone.util.Gcj02CoordinateConverter;
import com.guhecloud.cube.customization.xialing.server.drone.util.Gcj02CoordinateConverter.Coordinate;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 霞凝无人机API客户端
 * <p>
 * 封装与霞凝无人机平台的HTTP交互，提供航迹、任务、任务记录和成果数据的API调用。
 * 主要功能包括：
 * <ul>
 *   <li>航迹的增删改查</li>
 *   <li>任务的增删改查和异常任务查询</li>
 *   <li>任务记录的分页查询</li>
 *   <li>成果数据的分页查询</li>
 *   <li>自动处理Token过期重试</li>
 *   <li>JSON响应到VO对象的转换</li>
 * </ul>
 * </p>
 *
 * @author system
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XianingDroneApiClient {
    /**
     * 航迹列表接口路径
     */
    private static final String ROUTE_LIST_PATH = "/prod-api/task/track/getTracklist";
    /**
     * 航迹创建接口路径
     */
    private static final String ROUTE_CREATE_PATH = "/prod-api/task/track/addTrack";
    /**
     * 航迹更新接口路径
     */
    private static final String ROUTE_UPDATE_PATH = "/prod-api/task/track/edit";
    /**
     * 航迹删除接口路径
     */
    private static final String ROUTE_DELETE_PATH = "/prod-api/task/track/{trackId}";
    /**
     * 任务列表接口路径
     */
    private static final String TASK_LIST_PATH = "/prod-api/task/task/listByType";
    /**
     * 异常任务接口路径
     */
    private static final String TASK_ABNORMAL_PATH = "/prod-api/task/task/abnormalTasks";
    /**
     * 任务记录分页接口路径
     */
    private static final String TASK_RECORD_PATH = "/prod-api/task/scheduled/page/v2";
    /**
     * 任务创建接口路径
     */
    private static final String TASK_CREATE_PATH = "/prod-api/task/task/add";
    /**
     * 快速任务启动接口路径
     */
    private static final String QUICK_TASK_START_PATH = "/prod-api/task/quickTask/startQuickTask";
    /**
     * 云台控制接口路径
     */
    private static final String GIMBAL_CONTROL_PATH = "/prod-api/nest/drone/cmd/camera/{sn}/{ytType}/{type}/{category}";
    /**
     * 任务删除接口路径
     */
    private static final String TASK_DELETE_PATH = "/prod-api/task/task/{taskId}";
    /**
     * 任务结束接口路径
     */
    private static final String TASK_FINISH_PATH = "/prod-api/nest/nest/cmd/task/end/{sn}/{taskId}";
    /**
     * 成果数据分页接口路径
     */
    private static final String RESULT_PAGE_PATH = "/prod-api/dataCenter/dataCenter/getTaskResultFilesByScheduledId";
    /**
     * 成果文件下载接口路径
     */
    private static final String RESULT_DOWNLOAD_PATH = "/prod-api/file/minio/download";
    /**
     * Token过期的错误码
     */
    private static final String AUTH_EXPIRED_CODE = "UAP-AUTH-402";

    private final RestTemplate restTemplate;
    private final XianingBizTokenProvider tokenProvider;

    /**
     * 分页查询航迹列表
     * <p>
     * 调用霞凝平台的航迹列表接口，将响应转换为分页的VO对象。
     * </p>
     *
     * @param param 查询参数，包含分页信息和筛选条件
     *
     * @return 分页的航迹数据
     */
    public Pagination<DroneRouteVO> pageRoutes(DroneRouteQueryParam param) {
        // 1. 发送分页查询请求。
        String url = tokenProvider.getBaseUrl() + ROUTE_LIST_PATH;
        String body = exchangeForString(HttpMethod.POST, url, jsonEntity(buildRoutePageRequest(param)));
        // 2. 解析响应并做成功性校验。
        JsonNode root = JsonUtils.deserialize(body);
        if (!isSuccess(root)) {
            return Pagination.empty();
        }
        // 3. 读取分页字段并逐行映射为VO。
        long total = asLong(root.get("total"));
        long currentPage = asLong(root.get("currentPage"));
        List<DroneRouteVO> records = new ArrayList<>();
        JsonNode rows = root.get("rows");
        if (rows != null && rows.isArray()) {
            for (JsonNode row : rows) {
                DroneRouteVO vo = new DroneRouteVO();
                vo.setId(asLongObj(row.get("id")));
                vo.setFileUrl(asText(row.get("fileUrl")));
                vo.setWebFileUrl(asText(row.get("webFileUrl")));
                vo.setFileMd5(asText(row.get("fileMd5")));
                vo.setFileName(asText(row.get("fileName")));
                vo.setTrackName(asText(row.get("trackName")));
                vo.setProjectName(asText(row.get("projectName")));
                vo.setStatus(asIntObj(row.get("status")));
                vo.setTrackLength(asBigDecimal(row.get("trackLength")));
                vo.setWaypointCount(asIntObj(row.get("waypointCount")));
                vo.setEstimatedTime(asIntObj(row.get("estimatedTime")));
                vo.setType(asIntObj(row.get("type")));
                vo.setModel(asIntObj(row.get("model")));
                vo.setCreateType(asIntObj(row.get("createType")));
                vo.setPhotoTotal(asIntObj(row.get("photoTotal")));
                vo.setAltitudeMode(asIntObj(row.get("altitudeMode")));
                vo.setNestSn(asText(row.get("nestSn")));
                vo.setNestName(asText(row.get("nestName")));
                vo.setHeight(asIntObj(row.get("height")));
                vo.setSpeed(asIntObj(row.get("speed")));
                vo.setPitchAngle(asIntObj(row.get("pitchAngle")));
                vo.setCreateTime(asDateTime(row.get("createTime")));
                vo.setUpdateTime(asDateTime(row.get("updateTime")));
                records.add(vo);
            }
        }
        return new Pagination<>(total, currentPage, records);
    }

    /**
     * 创建航迹
     * <p>
     * 调用霞凝平台的航迹创建接口，使用multipart/form-data格式上传航迹文件。
     * </p>
     *
     * @param param 航迹创建参数
     *
     * @return 新创建的航迹ID
     *
     * @throws IllegalStateException 如果创建失败
     */
    public Long createRoute(DroneRouteCommandParam param) {
        // 以multipart方式提交航迹创建请求（含可能的文件内容）。
        String url = tokenProvider.getBaseUrl() + ROUTE_CREATE_PATH;
        String body = exchangeForString(HttpMethod.POST, url, multipartEntity(buildRouteMultipart(param, false)));
        JsonNode root = JsonUtils.deserialize(body);
        Assert.isParameterError(!(isSuccess(root)), "创建航迹失败: " + errorMessage(root));
        return asLongObj(root.get("data"));
    }

    /**
     * 更新航迹
     * <p>
     * 调用霞凝平台的航迹更新接口，使用multipart/form-data格式上传航迹文件。
     * </p>
     *
     * @param param 航迹更新参数
     *
     * @return 是否更新成功
     *
     * @throws IllegalStateException 如果更新失败
     */
    public Boolean updateRoute(DroneRouteCommandParam param) {
        // 编辑航迹使用PUT语义，参数结构与创建一致。
        String url = tokenProvider.getBaseUrl() + ROUTE_UPDATE_PATH;
        String body = exchangeForString(HttpMethod.PUT, url, multipartEntity(buildRouteMultipart(param, true)));
        JsonNode root = JsonUtils.deserialize(body);
        Assert.isParameterError(!(isSuccess(root)), "编辑航迹失败: " + errorMessage(root));
        return Boolean.TRUE;
    }

    /**
     * 删除航迹
     *
     * @param routeId 航迹ID
     *
     * @return 是否删除成功
     *
     * @throws IllegalStateException 如果删除失败
     */
    public Boolean deleteRoute(Long routeId) {
        // 将路径变量替换为具体航迹ID后调用删除接口。
        String url = tokenProvider.getBaseUrl() + ROUTE_DELETE_PATH.replace("{trackId}", String.valueOf(routeId));
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.DELETE, url, authEntity()));
        Assert.isParameterError(!(isSuccess(root)), "删除航迹失败: " + errorMessage(root));
        return Boolean.TRUE;
    }

    /**
     * 分页查询任务列表
     *
     * @param param 查询参数
     *
     * @return 分页的任务数据
     */
    public Pagination<DroneTaskVO> pageTasks(DroneTaskQueryParam param) {
        // 动态构建查询参数，未传值的字段不下发。
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tokenProvider.getBaseUrl() + TASK_LIST_PATH);
        if (param != null) {
            if (param.getPageNumber() != null) {
                builder.queryParam("pageNum", param.getPageNumber());
            }
            if (param.getPageSize() != null) {
                builder.queryParam("pageSize", param.getPageSize());
            }
            if (param.getType() != null) {
                builder.queryParam("type", param.getType());
            }
            if (StringUtils.hasText(param.getNestSn())) {
                builder.queryParam("nestSn", param.getNestSn());
            }
            if (param.getTrackId() != null) {
                builder.queryParam("trackId", param.getTrackId());
            }
            if (StringUtils.hasText(param.getTaskName())) {
                builder.queryParam("taskName", param.getTaskName());
            }
        }
        // 请求任务列表并做响应判定。
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.GET, builder.build(true).toUriString(), authEntity()));
        if (!isSuccess(root)) {
            return Pagination.empty();
        }
        // 组装分页信息与记录列表。
        long total = asLong(root.get("total"));
        long pages = calculatePages(total, param == null ? null : param.getPageSize(), root.get("currentPage"));
        List<DroneTaskVO> records = new ArrayList<>();
        JsonNode rows = root.get("rows");
        if (rows != null && rows.isArray()) {
            for (JsonNode row : rows) {
                records.add(toTaskVO(row));
            }
        }
        return new Pagination<>(total, pages, records);
    }

    /**
     * 查询异常任务列表
     *
     * @return 异常任务列表
     */
    public List<DroneTaskVO> listAbnormalTasks() {
        // 异常任务接口返回是多层嵌套结构，需要逐层展开。
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.GET,
                                                                tokenProvider.getBaseUrl() + TASK_ABNORMAL_PATH, authEntity()));
        List<DroneTaskVO> list = new ArrayList<>();
        if (!isSuccess(root)) {
            return list;
        }
        JsonNode tasks = root.path("data").path("tasks");
        if (tasks.isArray()) {
            for (JsonNode dayNode : tasks) {
                JsonNode nestTasks = dayNode.path("nestTasks");
                if (nestTasks.isArray()) {
                    for (JsonNode nestTaskNode : nestTasks) {
                        JsonNode taskDetails = nestTaskNode.path("taskDetails");
                        if (taskDetails.isArray()) {
                            for (JsonNode detailNode : taskDetails) {
                                // 复用常规任务映射，再补齐当前层级的机巢信息。
                                DroneTaskVO vo = toTaskVO(detailNode);
                                vo.setNestSn(asText(detailNode.get("nestSn")));
                                vo.setNestName(asText(detailNode.get("nestName")));
                                list.add(vo);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 分页查询任务记录列表
     *
     * @param param 查询参数
     *
     * @return 分页的任务记录数据
     */
    public Pagination<DroneTaskRecordVO> pageTaskRecords(DroneTaskRecordQueryParam param) {
        // 构建任务记录查询URL与分页参数。
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tokenProvider.getBaseUrl() + TASK_RECORD_PATH);
        if (param != null) {
            if (param.getTaskId() != null) {
                builder.queryParam("taskId", param.getTaskId());
            }
            if (param.getPageNumber() != null) {
                builder.queryParam("pageNum", param.getPageNumber());
            }
            if (param.getPageSize() != null) {
                builder.queryParam("pageSize", param.getPageSize());
            }
        }
        // 调用第三方分页接口。
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.GET, builder.build(true).toUriString(), authEntity()));
        if (!isSuccess(root)) {
            return Pagination.empty();
        }
        // 任务记录分页数据位于data节点下。
        JsonNode data = root.path("data");
        long total = asLong(data.get("total"));
        long pages = calculatePages(total, param == null ? null : param.getPageSize(), data.get("pages"));
        List<DroneTaskRecordVO> records = new ArrayList<>();
        JsonNode rows = data.get("records");
        if (rows != null && rows.isArray()) {
            for (JsonNode row : rows) {
                DroneTaskRecordVO vo = new DroneTaskRecordVO();
                vo.setId(asLongObj(row.get("id")));
                vo.setTaskId(asLongObj(row.get("taskId")));
                vo.setNestSn(asText(row.get("nestSn")));
                vo.setStartTime(asLongObj(row.get("startTime")));
                vo.setEndTime(asLongObj(row.get("endTime")));
                vo.setResultStatus(asIntObj(row.get("resultStatus")));
                vo.setTaskType(asIntObj(row.get("taskType")));
                vo.setRemark(asText(row.get("remark")));
                vo.setSortieNum(asIntObj(row.get("sortieNum")));
                vo.setSumPhotoTotal(asIntObj(row.get("sumPhotoTotal")));
                vo.setFinishPhotoTotal(asIntObj(row.get("finishPhotoTotal")));
                vo.setLastPoint(asIntObj(row.get("lastPoint")));
                records.add(vo);
            }
        }
        return new Pagination<>(total, pages, records);
    }

    /**
     * 创建任务
     *
     * @param param 任务创建参数
     *
     * @return 创建的任务记录列表
     *
     * @throws IllegalStateException 如果创建失败
     */
    public List<DroneTaskRecordVO> createTask(DroneTaskCreateParam param) {
        // 任务创建完成后，第三方会返回对应的执行计划记录列表。
        String url = tokenProvider.getBaseUrl() + TASK_CREATE_PATH;
        String body = exchangeForString(HttpMethod.POST, url, jsonEntity(param));
        JsonNode root = JsonUtils.deserialize(body);
        Assert.isParameterError(!(isSuccess(root)), "创建任务失败: " + errorMessage(root));
        List<DroneTaskRecordVO> list = new ArrayList<>();
        JsonNode data = root.get("data");
        if (data != null && data.isArray()) {
            for (JsonNode row : data) {
                DroneTaskRecordVO vo = new DroneTaskRecordVO();
                vo.setId(asLongObj(row.get("id")));
                vo.setTaskId(asLongObj(row.get("taskId")));
                vo.setTrackId(asLongObj(row.get("trackId")));
                vo.setTrackName(asText(row.get("trackName")));
                vo.setTaskName(asText(row.get("taskName")));
                vo.setStartTime(asLongObj(row.get("startTime")));
                vo.setEndTime(asLongObj(row.get("endTime")));
                vo.setStartingPoint(asIntObj(row.get("startingPoint")));
                vo.setLastPoint(asIntObj(row.get("lastPoint")));
                vo.setStatus(asIntObj(row.get("status")));
                vo.setResultStatus(asIntObj(row.get("resultStatus")));
                vo.setExecutionTime(asLongObj(row.get("executionTime")));
                list.add(vo);
            }
        }
        return list;
    }

    /**
     * 启动快速任务。
     *
     * @param param 快速任务启动参数
     *
     * @return 快速任务启动结果，包含三方原始响应和航迹字段
     */
    public DroneQuickTaskResult startQuickTask(DroneQuickTaskStartParam param) {
        String url = tokenProvider.getBaseUrl() + QUICK_TASK_START_PATH;
        Coordinate wgs84Coordinate = Gcj02CoordinateConverter.toWgs84(param.getLongitude(), param.getLatitude());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("longitude", wgs84Coordinate.longitude());
        body.put("latitude", wgs84Coordinate.latitude());
        body.put("nestSn", param.getNestSn());
        body.put("taskName", param.getTaskName());
        put(body, "sceneId", param.getSceneId());
        put(body, "sceneCode", param.getSceneCode());
        put(body, "finishAction", param.getFinishAction());
        put(body, "flyHeight", param.getFlyHeight());
        String response = exchangeForString(HttpMethod.POST, url, jsonEntity(body));
        JsonNode root = JsonUtils.deserialize(response);
        DroneQuickTaskResult result = new DroneQuickTaskResult();
        result.setRawResponse(response);
        result.setSuccess(isSuccess(root));
        if (!Boolean.TRUE.equals(result.getSuccess())) {
            result.setRawResponse("{\"failReason\":\"" + errorMessage(root) + "\"}");
            result.setErrorMessage("无人机请求失败！");
            return result;
        }
        JsonNode data = root.path("data");
        result.setNestSn(asText(data.get("nestSn")));
        result.setNestName(asText(data.get("nestName")));
        result.setTrackFileType(asText(data.get("trackFileType")));
        result.setTrackId(asText(data.get("trackId")));
        result.setTrackUrl(asText(data.get("trackUrl")));
        return result;
    }

    /**
     * 调用云台控制接口执行拍照、录像开始或录像结束。
     *
     * @return 三方原始响应体
     */
    public String gimbalControl(String nestSn, Integer ytType, String type, Integer category) {
        Assert.isParameterError(!StringUtils.hasText(nestSn), "云台控制机库编号不能为空");
        Assert.isParameterError(ytType == null, "云台控制挂载类型不能为空");
        Assert.isParameterError(!StringUtils.hasText(type), "云台控制类型不能为空");
        Assert.isParameterError(category == null, "云台控制开始结束标识不能为空");
        String path = GIMBAL_CONTROL_PATH.replace("{sn}", UriUtils.encodePathSegment(nestSn, StandardCharsets.UTF_8))
                .replace("{ytType}", String.valueOf(ytType))
                .replace("{type}", UriUtils.encodePathSegment(type, StandardCharsets.UTF_8))
                .replace("{category}", String.valueOf(category));
        log.info("XIANINGDRONE_GIMBAL_CONTROL_REQUEST nestSn={} ytType={} type={} category={} path={}", nestSn, ytType, type, category, path);
        String body = exchangeForString(HttpMethod.GET, tokenProvider.getBaseUrl() + path, authEntity());
        log.info("XIANINGDRONE_GIMBAL_CONTROL_RESPONSE nestSn={} ytType={} type={} category={} response={}", nestSn, ytType, type, category,
                 body);
        JsonNode root = JsonUtils.deserialize(body);
        Assert.isParameterError(root == null, "云台控制响应无法解析");
        Assert.isParameterError(!isSuccess(root), "云台控制失败: " + errorMessage(root));
        return body;
    }

    /**
     * 删除任务
     *
     * @param taskId 任务ID
     *
     * @return 是否删除成功
     *
     * @throws IllegalStateException 如果删除失败
     */
    public Boolean deleteTask(Long taskId) {
        // 通过路径参数传递要删除的第三方任务ID。
        String url = tokenProvider.getBaseUrl() + TASK_DELETE_PATH.replace("{taskId}", String.valueOf(taskId));
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.DELETE, url, authEntity()));
        Assert.isParameterError(!(isSuccess(root)), "删除任务失败: " + errorMessage(root));
        return Boolean.TRUE;
    }

    /**
     * 结束任务
     *
     * @param param 任务结束参数
     *
     * @return 是否结束成功
     *
     * @throws IllegalStateException 如果结束失败
     */
    public Boolean finishTask(DroneTaskFinishParam param) {
        // 结束任务接口要求机巢SN与任务ID都在路径中。
        String url = tokenProvider.getBaseUrl() + TASK_FINISH_PATH
                .replace("{sn}", param.getNestSn())
                .replace("{taskId}", String.valueOf(param.getTaskId()));
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.GET, url, authEntity()));
        Assert.isParameterError(!(isSuccess(root)), "结束任务失败: " + errorMessage(root));
        return root.path("data").asBoolean(false);
    }

    /**
     * 分页查询成果数据列表
     *
     * @param param 查询参数
     *
     * @return 分页的成果数据
     */
    public Pagination<DroneResultVO> pageResults(DroneResultQueryParam param) {
        // 成果接口为GET查询，按传入条件拼接查询参数。
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tokenProvider.getBaseUrl() + RESULT_PAGE_PATH);
        if (param != null) {
            if (param.getPageNumber() != null) {
                builder.queryParam("pageNum", param.getPageNumber());
            }
            if (param.getPageSize() != null) {
                builder.queryParam("pageSize", param.getPageSize());
            }
            if (param.getFileType() != null) {
                builder.queryParam("fileType", param.getFileType());
            }
            if (param.getChannelType() != null) {
                builder.queryParam("channelType", param.getChannelType());
            }
            if (param.getScheduledId() != null) {
                builder.queryParam("scheduledId", param.getScheduledId());
            }
            if (param.getPType() != null) {
                builder.queryParam("pType", param.getPType());
            }
        }
        // 请求并校验结果。
        JsonNode root = JsonUtils.deserialize(exchangeForString(HttpMethod.GET, builder.build(true).toUriString(), authEntity()));
        if (!isSuccess(root)) {
            return Pagination.empty();
        }
        // 解析分页字段与成果列表。
        long total = asLong(root.get("total"));
        long pages = calculatePages(total, param == null ? null : param.getPageSize(), root.get("currentPage"));
        List<DroneResultVO> records = new ArrayList<>();
        JsonNode rows = root.get("rows");
        if (rows != null && rows.isArray()) {
            for (JsonNode row : rows) {
                DroneResultVO vo = new DroneResultVO();
                vo.setId(asLongObj(row.get("id")));
                vo.setTrackName(asText(row.get("trackName")));
                vo.setTaskName(asText(row.get("taskName")));
                vo.setNestSn(asText(row.get("nestSn")));
                vo.setDroneSn(asText(row.get("droneSn")));
                vo.setTaskId(asText(row.get("taskId")));
                vo.setScheduledId(asText(row.get("scheduledId")));
                vo.setFileKey(asText(row.get("fileKey")));
                vo.setThumbKey(asText(row.get("thumbKey")));
                vo.setFileName(asText(row.get("fileName")));
                vo.setFileMd5(asText(row.get("fileMd5")));
                vo.setAnalysisStatus(asBooleanObj(row.get("analysisStatus")));
                vo.setFileSize(asLongObj(row.get("fileSize")));
                vo.setUploadType(asIntObj(row.get("uploadType")));
                vo.setCoordValue(asText(row.get("coordValue")));
                vo.setFileType(asIntObj(row.get("fileType")));
                vo.setPodType(asText(row.get("podType")));
                vo.setChannelType(asIntObj(row.get("channelType")));
                vo.setLongitude(asDoubleObj(row.get("longitude")));
                vo.setLatitude(asDoubleObj(row.get("latitude")));
                vo.setAltitude(asDoubleObj(row.get("altitude")));
                vo.setJudgeStatus(asText(row.get("judgeStatus")));
                vo.setType(asText(row.get("type")));
                vo.setPtype(asIntObj(row.get("ptype")));
                vo.setCreateTime(asLongObj(row.get("createTime")));
                records.add(vo);
            }
        }
        return new Pagination<>(total, pages, records);
    }

    /**
     * 下载成果文件。
     *
     * @param fileKey  文件密钥
     * @param response 当前HTTP响应
     */
    public void downloadResultFile(String fileKey, HttpServletResponse response) throws IOException {
        Assert.isParameterError(!StringUtils.hasText(fileKey), "文件密钥不能为空");
        String url = tokenProvider.getBaseUrl() + RESULT_DOWNLOAD_PATH + "/"
                     + UriUtils.encodePathSegment(fileKey.trim(), StandardCharsets.UTF_8);
        if (!executeDownload(url, response)) {
            tokenProvider.evictToken();
            executeDownload(url, response);
        }
    }

    /**
     * 将任务JSON节点转换为任务VO。
     */
    private DroneTaskVO toTaskVO(JsonNode row) {
        // 将第三方字段逐项映射到统一的任务VO。
        DroneTaskVO vo = new DroneTaskVO();
        vo.setId(asLongObj(row.get("id")));
        vo.setTaskId(asLongObj(row.get("taskId")));
        vo.setTaskName(asText(row.get("taskName")));
        vo.setTaskType(asIntObj(row.get("taskType")));
        vo.setStatus(asIntObj(row.get("status")));
        vo.setRemark(asText(row.get("remark")));
        vo.setTrackId(asLongObj(row.get("trackId")));
        vo.setTrackName(asText(row.get("trackName")));
        vo.setFileUrl(asText(row.get("fileUrl")));
        vo.setNestSn(asText(row.get("nestSn")));
        vo.setNestName(asText(row.get("nestName")));
        vo.setStartTime(asLongObj(row.get("startTime")));
        vo.setEndTime(asLongObj(row.get("endTime")));
        vo.setResultStatus(asIntObj(row.get("resultStatus")));
        vo.setWeatherConflict(asBooleanObj(row.get("weatherConflict")));
        vo.setStartingPoint(asIntObj(row.get("startingPoint")));
        vo.setLastPoint(asIntObj(row.get("lastPoint")));
        vo.setExecutionTime(asLongObj(row.get("executionTime")));
        vo.setExecutionStatus(asIntObj(row.get("executionStatus")));
        return vo;
    }

    /**
     * 构建仅含鉴权请求头的HTTP实体。
     */
    private HttpEntity<?> authEntity() {
        // 无请求体场景仅需要请求头。
        return new HttpEntity<>(buildAuthHeaders(MediaType.APPLICATION_JSON));
    }

    /**
     * 构建JSON请求体实体。
     */
    private HttpEntity<String> jsonEntity(Object body) {
        // JSON请求统一走平台序列化工具。
        return new HttpEntity<>(JsonUtils.serialize(body), buildAuthHeaders(MediaType.APPLICATION_JSON));
    }

    /**
     * 构建multipart请求体实体。
     */
    private HttpEntity<MultiValueMap<String, Object>> multipartEntity(MultiValueMap<String, Object> body) {
        // 文件上传请求使用multipart编码。
        return new HttpEntity<>(body, buildAuthHeaders(MediaType.MULTIPART_FORM_DATA));
    }

    /**
     * 将平台分页字段转换为第三方航迹接口字段。
     */
    private Map<String, Object> buildRoutePageRequest(DroneRouteQueryParam param) {
        Map<String, Object> body = new LinkedHashMap<>();
        if (param == null) {
            return body;
        }
        body.put("trackName", param.getTrackName());
        body.put("nestSn", param.getNestSn());
        body.put("types", param.getTypes());
        body.put("pageNum", param.getPageNumber());
        body.put("pageSize", param.getPageSize());
        return body;
    }

    /**
     * 统一构建鉴权请求头，包含Token、租户和客户端标识。
     */
    private HttpHeaders buildAuthHeaders(MediaType mediaType) {
        // 每次请求都携带最新Token与租户上下文，避免跨租户串数据。
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenProvider.getToken());
        headers.set("Tenant-Id", tokenProvider.getTenantId());
        headers.set("clientid", tokenProvider.getClientId());
        return headers;
    }

    /**
     * 构建文件下载请求头。
     */
    private HttpHeaders buildDownloadHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setBearerAuth(tokenProvider.getToken());
        headers.set("Tenant-Id", tokenProvider.getTenantId());
        headers.set("clientid", tokenProvider.getClientId());
        return headers;
    }

    /**
     * 执行一次文件下载。返回false表示token过期且响应尚未写出，可刷新后重试。
     */
    private boolean executeDownload(String url, HttpServletResponse servletResponse) throws IOException {
        Boolean result = restTemplate.execute(url, HttpMethod.GET, request -> request.getHeaders().putAll(buildDownloadHeaders()),
                                              response -> {
                                                  MediaType contentType = response.getHeaders().getContentType();
                                                  if (isJsonMediaType(contentType)) {
                                                      byte[] bodyBytes = StreamUtils.copyToByteArray(response.getBody());
                                                      String body = new String(bodyBytes, StandardCharsets.UTF_8);
                                                      if (isAuthExpired(body)) {
                                                          return Boolean.FALSE;
                                                      }
                                                      JsonNode root = JsonUtils.deserialize(body);
                                                      Assert.isParameterError(true, "下载文件失败: " + errorMessage(root));
                                                      return Boolean.TRUE;
                                                  }
                                                  servletResponse.setStatus(response.getStatusCode().value());
                                                  if (contentType != null) {
                                                      servletResponse.setContentType(contentType.toString());
                                                  } else {
                                                      servletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                                                  }
                                                  long contentLength = response.getHeaders().getContentLength();
                                                  if (contentLength >= 0) {
                                                      servletResponse.setContentLengthLong(contentLength);
                                                  }
                                                  String disposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
                                                  if (StringUtils.hasText(disposition)) {
                                                      servletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition);
                                                  }
                                                  StreamUtils.copy(response.getBody(), servletResponse.getOutputStream());
                                                  servletResponse.flushBuffer();
                                                  return Boolean.TRUE;
                                              });
        return Boolean.TRUE.equals(result);
    }

    /**
     * 判断响应是否为JSON错误报文。
     */
    private boolean isJsonMediaType(MediaType contentType) {
        return contentType != null && (MediaType.APPLICATION_JSON.includes(contentType)
                                       || contentType.getSubtype().toLowerCase().contains("json"));
    }

    /**
     * 组装航迹新增/编辑接口所需的multipart参数。
     */
    private MultiValueMap<String, Object> buildRouteMultipart(DroneRouteCommandParam param, boolean update) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (update && param.getId() != null) {
            // 编辑模式下补充航迹ID。
            body.add("id", String.valueOf(param.getId()));
        }
        // 组装航迹相关业务字段。
        put(body, "trackName", param.getTrackName());
        put(body, "type", param.getType());
        put(body, "createType", param.getCreateType());
        put(body, "altitudeMode", param.getAltitudeMode());
        put(body, "nestSn", param.getNestSn());
        put(body, "routeMode", param.getRouteMode());
        put(body, "height", param.getHeight());
        put(body, "speed", param.getSpeed());
        put(body, "pitchAngle", param.getPitchAngle());
        put(body, "finishAction", param.getFinishAction());
        put(body, "lostAction", param.getLostAction());
        put(body, "continueFly", param.getContinueFly());
        put(body, "estimatedTime", param.getEstimatedTime());
        put(body, "waypointCount", param.getWaypointCount());
        put(body, "photoTotal", param.getPhotoTotal());
        put(body, "droneType", param.getDroneType());
        put(body, "podType", param.getPodType());
        put(body, "trackFileType", param.getTrackFileType());
        put(body, "taskid", param.getTaskId());
        put(body, "trackLength", param.getTrackLength());
        if (StringUtils.hasText(param.getTrackFileBase64())) {
            // 有文件内容时，将base64转二进制并包装成上传资源。
            byte[] bytes = java.util.Base64.getDecoder().decode(param.getTrackFileBase64());
            String fileName = StringUtils.hasText(param.getTrackFileName()) ? param.getTrackFileName() : "track.gz";
            String contentType = StringUtils.hasText(param.getTrackFileContentType()) ? param.getTrackFileContentType()
                                                                                      : MediaType.APPLICATION_OCTET_STREAM_VALUE;
            body.add("trackFile", new NamedByteArrayResource(fileName, bytes, contentType));
        }
        return body;
    }

    /**
     * 仅在值非空时写入multipart参数。
     */
    private void put(LinkedMultiValueMap<String, Object> body, String key, Object value) {
        if (value != null) {
            body.add(key, String.valueOf(value));
        }
    }

    /**
     * 仅在值非空时写入JSON请求参数。
     */
    private void put(Map<String, Object> body, String key, Object value) {
        if (value != null) {
            body.put(key, value);
        }
    }

    /**
     * 发起HTTP请求并处理一次Token过期重试。
     */
    private String exchangeForString(HttpMethod method, String url, HttpEntity<?> entity) {
        // 首次请求。
        String body = restTemplate.exchange(url, method, entity, String.class).getBody();
        if (isAuthExpired(body)) {
            // token过期时清缓存并仅重试一次，避免无限重试。
            tokenProvider.evictToken();
            HttpEntity<?> retryEntity = rebuildEntity(entity);
            body = restTemplate.exchange(url, method, retryEntity, String.class).getBody();
        }
        return body == null ? "" : body;
    }

    /**
     * 使用最新Token重建请求实体。
     */
    private HttpEntity<?> rebuildEntity(HttpEntity<?> entity) {
        // 保留原请求体与其他请求头，仅替换Bearer Token。
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(entity.getHeaders());
        headers.setBearerAuth(tokenProvider.getToken());
        return new HttpEntity<>(entity.getBody(), headers);
    }

    /**
     * 判断响应是否为认证过期错误。
     */
    private boolean isAuthExpired(String body) {
        if (!StringUtils.hasText(body)) {
            return false;
        }
        // 按平台约定的errorCode判断鉴权过期。
        JsonNode root = JsonUtils.deserialize(body);
        return root != null && AUTH_EXPIRED_CODE.equals(asText(root.get("errorCode")));
    }

    /**
     * 判断响应是否成功（code=200）。
     */
    private boolean isSuccess(JsonNode root) {
        return root != null && root.has("code") && root.get("code").asInt() == 200;
    }

    /**
     * 提取响应错误信息。
     */
    private String errorMessage(JsonNode root) {
        if (root == null) {
            return "空响应";
        }
        // 优先返回msg，无msg时退化为errorCode。
        String msg = asText(root.get("msg"));
        if (StringUtils.hasText(msg)) {
            return msg;
        }
        return asText(root.get("errorCode"));
    }

    /**
     * 计算总页数，优先使用响应中的页数字段。
     */
    private long calculatePages(long total, Integer pageSize, JsonNode pagesNode) {
        // 接口返回页数时直接使用，避免重复计算误差。
        if (pagesNode != null && pagesNode.isNumber()) {
            return pagesNode.asLong();
        }
        if (pageSize == null || pageSize <= 0) {
            return total > 0 ? 1L : 0L;
        }
        return (total + pageSize - 1) / pageSize;
    }

    /**
     * JSON节点安全转字符串。
     */
    private String asText(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        // 过滤空字符串，统一返回null。
        String text = node.asText();
        return StringUtils.hasText(text) ? text : null;
    }

    /**
     * JSON节点安全转Long对象。
     */
    private Long asLongObj(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isNumber()) {
            // 数值节点直接取long，避免字符串转换开销。
            return node.longValue();
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? Long.valueOf(text) : null;
    }

    /**
     * JSON节点安全转long基础类型。
     */
    private long asLong(JsonNode node) {
        Long value = asLongObj(node);
        return value == null ? 0L : value;
    }

    /**
     * JSON节点安全转Integer对象。
     */
    private Integer asIntObj(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isNumber()) {
            // 数值节点直接取int。
            return node.intValue();
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? Integer.valueOf(text) : null;
    }

    /**
     * JSON节点安全转布尔对象。
     */
    private Boolean asBooleanObj(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isBoolean()) {
            // 布尔节点直接返回。
            return node.booleanValue();
        }
        String text = node.asText();
        if (!StringUtils.hasText(text)) {
            return null;
        }
        return Boolean.valueOf(text);
    }

    /**
     * JSON节点安全转Double对象。
     */
    private Double asDoubleObj(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isNumber()) {
            // 数值节点直接取double。
            return node.doubleValue();
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? Double.valueOf(text) : null;
    }

    /**
     * JSON节点安全转BigDecimal对象。
     */
    private BigDecimal asBigDecimal(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isNumber()) {
            // 数值节点直接转decimal，保留精度。
            return node.decimalValue();
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? new BigDecimal(text) : null;
    }

    /**
     * JSON节点时间戳（毫秒）转本地时间。
     */
    private LocalDateTime asDateTime(JsonNode node) {
        // 三方时间字段为毫秒时间戳。
        Long value = asLongObj(node);
        if (value == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
    }

    /**
     * 可指定文件名与Content-Type的内存资源，供multipart上传使用。
     */
    private static class NamedByteArrayResource extends ByteArrayResource {
        private final String fileName;
        private final String contentType;

        private NamedByteArrayResource(String fileName, byte[] byteArray, String contentType) {
            super(byteArray);
            // 保存上传时使用的文件元信息。
            this.fileName = fileName;
            this.contentType = contentType;
        }

        @Override
        public String getFilename() {
            return fileName;
        }

        @Override
        public long contentLength() {
            return getByteArray().length;
        }

        @Override
        public String getDescription() {
            return contentType;
        }
    }
}

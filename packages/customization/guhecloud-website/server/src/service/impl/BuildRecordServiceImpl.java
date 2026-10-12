package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.BuildRecordConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.BuildRecordMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSitePublishConfigMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.BuildRecord;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSitePublishConfig;
import com.guhecloud.cube.customized.guhecloudwebsite.param.BuildRecordQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.BuildRecordService;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SiteJsonService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.BuildRecordDataVO;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.BuildRecordVO;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.DeploysRecordDataVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建记录Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BuildRecordServiceImpl extends ServiceImpl<BuildRecordMapper, BuildRecord>
        implements BuildRecordService {

    private final BuildRecordMapper buildRecordMapper;
    private final RestTemplate restTemplate;
    private final SiteJsonService siteJsonService;
    private final SysSitePublishConfigMapper sysSitePublishConfigMapper;
    private final FileOperateClient fileOperateClient;

    @Value("${generator.service.url:http://192.168.0.133:4000}")
    private String generatorServiceUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void build() {
        long startTime = System.currentTimeMillis();
        BuildRecord record = new BuildRecord();
        String artifactName = "Products-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        try {
            // 1. 生成 ZIP 文件
            String fileName = siteJsonService.getSiteJsonUrl();
            Path zipPath = Path.of(System.getProperty("user.dir"), "resources", "static", fileName);
            byte[] zipBytes = Files.readAllBytes(zipPath);

            // 2. 上传到文件服务器
            MultipartFile multipartFile = SpringFileUtils.fileByteConvertMultipartFile(fileName, zipBytes);
            FileUploadParam uploadParam = new FileUploadParam();
            uploadParam.setSceneCode("OUAA::TENANT");
            uploadParam.setTenant(SessionContextHolder.getRequester().getTenant());
            uploadParam.setFile(multipartFile);
            FileVO fileVO = fileOperateClient.uploadSustain(uploadParam);
            String fileUrl = fileVO.getPath();

            // 3. 发送给 Generator Service
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("file", new ByteArrayResource(zipBytes) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            });
            requestBody.add("tenantCode", SessionContextHolder.getRequester().getTenant());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<BuildRecordDataVO> response = restTemplate.postForEntity(
                    generatorServiceUrl + "/tasks/byBusinessData", requestEntity, BuildRecordDataVO.class);

            BuildRecordDataVO.OutputDataInfo data = response.getBody().getData();
            record.setTaskId(data.getId());
            record.setArtifactName(artifactName);
            record.setFileUrl(fileUrl);
            record.setStatus("RUNNING");
            record.setDeployStatus(null);
            record.setReleaseStatus("unpublished");
            buildRecordMapper.insert(record);

            log.info("构建任务已创建，等待轮询: taskId={}, recordId={}, fileUrl={}", data.getId(), record.getId(), fileUrl);
        } catch (Exception e) {
            log.error("构建失败: {}", e.getMessage(), e);
            record.setArtifactName(artifactName);
            record.setStatus("FAILED");
            record.setDurationMs(System.currentTimeMillis() - startTime);
            record.setErrorMessage(e.getMessage());
            buildRecordMapper.insert(record);
        }
    }
    @Override
    public void pollBuildTasks() {
        LambdaQueryWrapper<BuildRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuildRecord::getStatus, "RUNNING");
        List<BuildRecord> pendingList = buildRecordMapper.selectList(wrapper);

        if (pendingList.isEmpty()) {
            return;
        }
        SessionContextHolder.requestTenant(pendingList.getFirst().getTenant());

        log.info("开始轮询 {} 条生成中任务...", pendingList.size());
        for (BuildRecord record : pendingList) {
            try {
                pollBuildSingleTask(record);
            } catch (Exception e) {
                log.error("轮询构建任务 {} 异常: {}", record.getTaskId(), e);
            }
        }
    }

    private void pollBuildSingleTask(BuildRecord record) {
        String taskId = record.getTaskId();
        if (taskId == null || taskId.isEmpty()) {
            return;
        }

        String url = generatorServiceUrl + "/tasks/" + taskId;
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("查询构建任务 {} 失败: HTTP {}", taskId, response.getStatusCode());
            return;
        }

        Map responseBody = response.getBody();
        Boolean success = (Boolean) responseBody.get("success");
        if (!Boolean.TRUE.equals(success)) {
            log.warn("查询构建任务 {} 返回失败: {}", taskId, responseBody);
            return;
        }

        Map data = (Map) responseBody.get("data");
        String status = (String) data.get("status");
        String outputPath = (String) data.get("outputPath");
        String error = (String) data.get("error");

        log.info("构建任务 {} 状态: {}", taskId, status);

        if ("SUCCESS".equals(status)) {
            record.setStatus("SUCCESS"); // 成功
            record.setArtifactUrl(outputPath);
            record.setDurationMs(System.currentTimeMillis() - getEpochMilli(record.getCreateTime()));
            buildRecordMapper.updateById(record);
            log.info("构建任务 {} 构建成功, artifactUrl={}", taskId, outputPath);
        } else if ("FAILED".equals(status)) {
            record.setStatus("FAILED"); // 失败
            record.setErrorMessage(error);
            record.setDurationMs(System.currentTimeMillis() - getEpochMilli(record.getCreateTime()));
            buildRecordMapper.updateById(record);
            log.info("构建任务 {} 构建失败: {}", taskId, error);
        }
    }

    /**
     * 定时轮询部署中的任务，更新状态到数据库
     * 每5秒执行一次
     */
    public void pollDeployTasks() {
        LambdaQueryWrapper<BuildRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BuildRecord::getStatus, "SUCCESS") // 构建成功
                .eq(BuildRecord::getDeployStatus, "RUNNING"); // 部署中
        List<BuildRecord> deployingList = buildRecordMapper.selectList(wrapper);
        if (deployingList.isEmpty()) {
            return;
        }
        SessionContextHolder.requestTenant(deployingList.getFirst().getTenant());

        log.info("开始轮询 {} 条部署中任务...", deployingList.size());
        for (BuildRecord record : deployingList) {
            try {
                pollDeploySingleTask(record);
            } catch (Exception e) {
                log.error("轮询部署任务 {} 异常: {}", record.getDeployId(), e.getMessage());
            }
        }
    }

    private void pollDeploySingleTask(BuildRecord record) {
        String deployId = record.getDeployId();
        if (deployId == null || deployId.isEmpty()) {
            return;
        }

        String url = generatorServiceUrl + "/deploys/" + deployId;
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("查询部署任务 {} 失败: HTTP {}", deployId, response.getStatusCode());
            return;
        }

        Map responseBody = response.getBody();
        Boolean success = (Boolean) responseBody.get("success");
        if (!Boolean.TRUE.equals(success)) {
            log.warn("查询部署任务 {} 返回失败: {}", deployId, responseBody);
            return;
        }

        Map data = (Map) responseBody.get("data");
        String status = (String) data.get("status");
        String output = (String) data.get("output");
        String error = (String) data.get("error");
        Number progress = (Number) data.get("progress");

        log.info("部署任务 {} 状态: {}, 进度: {}%", deployId, status, progress);

        if ("SUCCESS".equals(status)) {
            record.setDeployStatus("SUCCESS"); // 部署成功
            record.setArtifactUrl(output != null ? output : record.getArtifactUrl());
            buildRecordMapper.updateById(record);
            log.info("部署任务 {} 部署成功, output={}", deployId, output);
        } else if ("FAILED".equals(status)) {
            record.setDeployStatus("FAILED"); // 部署失败
            record.setErrorMessage(error);
            buildRecordMapper.updateById(record);
            log.info("部署任务 {} 部署失败: {}", deployId, error);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean publish(Long id) {
        BuildRecord entity = buildRecordMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("构建记录不存在");
        }
        if (!"SUCCESS".equals(entity.getStatus())) {
            throw new ServiceException("只有构建成功的记录才能发布");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("taskId", entity.getTaskId());
        // 查询发布配置
        SysSitePublishConfig pubConfig = sysSitePublishConfigMapper.selectList(null).stream().findFirst()
                .orElseThrow(() -> new ServiceException("未找到发布配置"));
        JsonNode providerConfigJson = JsonUtils.deserialize(pubConfig.getProviderConfig());

        requestBody.put("provider", pubConfig.getProvider());
        requestBody.put("providerConfig", providerConfigJson);

        //        for (Iterator<String> it = providerConfigJson.fieldNames(); it.hasNext(); ) {
        //            String key = it.next();
        //            requestBody.put(key, providerConfigJson.get(key).asText());
        //        }
        requestBody.put("tenantCode", entity.getTenant());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        log.info("调用 Generator Service 发布: {}/deploys, provider={}", generatorServiceUrl);
        ResponseEntity<DeploysRecordDataVO> response = restTemplate.postForEntity(
                generatorServiceUrl + "/deploys", requestEntity, DeploysRecordDataVO.class);

        DeploysRecordDataVO.OutputDataInfo data = response.getBody().getData();

        entity.setStatus("SUCCESS"); // 构建成功触发发布
        entity.setDeployId(data.getId());
        entity.setDeployStatus("SUCCESS".equals(data.getStatus()) ? "SUCCESS" : "RUNNING");
        entity.setReleaseStatus("published");
        buildRecordMapper.updateById(entity);

        log.info("发布任务已创建: deployId={}, status={}", data.getId(), data.getStatus());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProduct(Long id) {
        BuildRecord entity = buildRecordMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("构建记录不存在");
        }
        buildRecordMapper.deleteById(id);
        return true;
    }

    @Override
    public Pagination<BuildRecordVO> page(BuildRecordQueryParam param) {
        Page<BuildRecord> page = new Page<>(param.getPageNumber(), param.getPageSize());
        LambdaQueryWrapper<BuildRecord> wrapper = new LambdaQueryWrapper<>();

        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            wrapper.eq(BuildRecord::getStatus, param.getStatus());
        }
        if (ObjectUtils.isNotEmpty(param.getArtifactName())) {
            wrapper.like(BuildRecord::getArtifactName, param.getArtifactName());
        }
        if (ObjectUtils.isNotEmpty(param.getReleaseStatus())) {
            wrapper.eq(BuildRecord::getReleaseStatus, param.getReleaseStatus());
        }

        wrapper.orderByDesc(BuildRecord::getCreateTime);
        Page<BuildRecord> result = buildRecordMapper.selectPage(page, wrapper);

        Page<BuildRecordVO> pageVo = new Page<>();
        pageVo.setCurrent(result.getCurrent());
        pageVo.setTotal(result.getTotal());
        pageVo.setRecords(BuildRecordConverts.toVOList(result.getRecords()));
        return PagingUtils.convert(pageVo);
    }

    private long getEpochMilli(java.time.LocalDateTime ldt) {
        if (ldt == null) {
            return System.currentTimeMillis();
        }
        return ldt.toInstant(java.time.ZoneOffset.of("+8")).toEpochMilli();
    }
}

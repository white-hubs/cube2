package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.param.FileRefRemoveParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.KnowledgeBaseCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.KnowledgeBaseCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.KnowledgeBaseMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.AiLibraryConfigVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeBatchVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeBase;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.AiLibraryQueryService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 知识库
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KnowledgeBaseCommandServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseCommandService {
    private static final String FILE_SCENE_CODE = "KNOWLEDGE_BASE_FILE";

    private final FileRefClient fileRefClient;
    private final FileClient fileClient;

    /**
     * 添加知识文档
     *
     * @param command 文档
     *
     * @return 主键
     */
    @Override
    public Long addCommand(KnowledgeBaseCommand command) {
        Long id = IdWorker.getId();
        KnowledgeBase po = ObjectUtils.copying(command, KnowledgeBase.class);
        po.setId(id);
        //        po.setCreateBy(SecurityContextHolder.getPrincipal().getAccount());
        this.save(po);
        this.fileInduce(id, command.getFileIds());
        this.uploadAiLibrary(command.getFileIds(), id, AiLibraryQueryService.getAiLibraryConfig());
        return id;
    }

    /**
     * 编辑
     *
     * @param command 文档
     *
     * @return 主键
     */
    @Override
    public Long editCommand(KnowledgeBaseCommand command) {
        Assert.isParameterError(command.getId() == null, "id 不能为空!");
        KnowledgeBase po = this.getById(command.getId());
        Assert.isNotFound(po);
        po.setName(command.getName());
        po.setTypeId(command.getTypeId());
        po.setDeptId(command.getDeptId());
        po.setProductId(command.getProductId());
        po.setUpdateTime(DateUtils.now());
        this.updateById(po);
        this.removeByBusinessKey(command.getId(), command.getFileIds());
        this.fileInduce(command.getId(), command.getFileIds());
        return po.getId();
    }

    /**
     * 批量移除
     *
     * @param command idList
     *
     * @return 是否成功
     */
    @Override
    public boolean removeBatch(KnowledgeBaseCommand command) {
        if (CollectionUtils.isEmpty(command.getIdList())) {
            return true;
        }
        Map<String, String> documentMap = new HashMap<>();
        List<KnowledgeBase> poList = this.listByIds(command.getIdList());
        Optional.ofNullable(poList).orElse(new ArrayList<>()).forEach(po -> {
            if (StringUtils.isNotBlank(po.getAiDocumentId()) && StringUtils.isNotBlank(po.getAiLibraryId())) {
                documentMap.put(po.getAiDocumentId(), po.getAiLibraryId());
            }
        });
        this.removeByIds(command.getIdList());
        // 移除AI知识文档
        if (!documentMap.isEmpty()) {
            this.removeAiDocumentBatch(documentMap);
        }
        return true;
    }

    /**
     * 批量添加
     *
     * @param batchCommand 文档集合
     *
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeBatchVO addBatch(List<KnowledgeBaseCommand> batchCommand) {
        KnowledgeBatchVO result = new KnowledgeBatchVO();
        // 校验
        List<String> errorList = this.knowledgeBatchCheck(batchCommand);
        // 如果没有合格的就直接返回
        if (CollectionUtils.isEmpty(batchCommand)) {
            result.setErrorCount(errorList.size());
            result.setErrorList(errorList);
            return result;
        }
        List<KnowledgeBase> insertList = new ArrayList<>();
        Map<Long, List<Long>> fileRefMap = new HashMap<>();
        Map<Long, List<Long>> idFileMap = new HashMap<>();
        List<Long> fileIds = new ArrayList<>();
        batchCommand.forEach(command -> {
            KnowledgeBase po = ObjectUtils.copying(command, KnowledgeBase.class);
            po.setId(IdWorker.getId());
            insertList.add(po);
            fileRefMap.put(po.getId(), command.getFileIds());
            idFileMap.put(po.getId(), command.getFileIds());
            fileIds.addAll(command.getFileIds());
        });
        result.setSuccessCount(insertList.size());
        result.setErrorCount(errorList.size());
        result.setErrorList(errorList);
        this.saveBatch(insertList);
        this.fileInduce(fileRefMap);
        this.batchUploadAiDocument(fileIds, idFileMap);
        return result;
    }

    /**
     * 批量校验文档
     * 不合格的会移除
     *
     * @param batchCommands 文档集合
     *
     * @return 结果集合
     */
    private List<String> knowledgeBatchCheck(List<KnowledgeBaseCommand> batchCommands) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(batchCommands)) {
            Iterator<KnowledgeBaseCommand> iterator = batchCommands.iterator();
            while (iterator.hasNext()) {
                KnowledgeBaseCommand command = iterator.next();
                if (StringUtils.isBlank(command.getName()) || command.getTypeId() == null ||
                    CollectionUtils.isEmpty(command.getFileIds())) {
                    result.add(command.getName());
                    iterator.remove();
                }
            }
        }
        return result;
    }

    /**
     * 文件持久化
     *
     * @param id      业务主键
     * @param fileIds 文件id集合
     */
    private void fileInduce(Long id, List<Long> fileIds) {
        FileRefCreateParam param = new FileRefCreateParam();
        param.setFileIdList(fileIds);
        param.setBusinessKey(id);
        param.setSceneCode(FILE_SCENE_CODE);
        fileRefClient.refInduce(param);
    }

    /**
     * 文件批量持久化
     *
     * @param fileRefMap 文件主键和fileId集合的map
     */
    private void fileInduce(Map<Long, List<Long>> fileRefMap) {
        if (fileRefMap == null || fileRefMap.isEmpty()) {
            return;
        }
        List<FileRefCreateParam> fileRefs = new ArrayList<>();
        fileRefMap.forEach((k, v) -> {
            FileRefCreateParam param = new FileRefCreateParam();
            param.setFileIdList(v);
            param.setBusinessKey(k);
            param.setSceneCode(FILE_SCENE_CODE);
            fileRefs.add(param);
        });
        fileRefClient.refInduceBatch(fileRefs);
    }

    /**
     * 根据业务主键移除文件
     *
     * @param businessKey 业务主键id
     * @param fileIds     不需要移除的文件id
     */
    private void removeByBusinessKey(Long businessKey, List<Long> fileIds) {
        // 获取所有文件
        List<FileVO> fileVOList = fileClient.getByBusinessKey(businessKey);
        if (CollectionUtils.isEmpty(fileVOList)) {
            return;
        }
        List<Long> removeIds = fileVOList.stream().map(FileVO::getId).collect(Collectors.toList());
        // 处理不需要移除的文件
        removeIds.removeIf(fileIds::contains);
        if (CollectionUtils.isEmpty(removeIds)) {
            return;
        }
        // 根据文件id移除文件
        FileRefRemoveParam removeParam = new FileRefRemoveParam();
        removeParam.setBusinessKey(businessKey);
        removeParam.setFileIdList(removeIds);
        fileRefClient.removeByBusinessKeyAndIds(removeParam);
    }

    /**
     * 上传AI知识文档(通过文件id)
     *
     * @param fileIds     文件id集合
     * @param businessKey 业务对主键
     * @param configVO    配置对象
     */
    private void uploadAiLibrary(List<Long> fileIds, Long businessKey, AiLibraryConfigVO configVO) {
        if (configVO == null || businessKey == null || CollectionUtils.isEmpty(fileIds)) {
            log.warn("上传AI知识库文件：文件id为空或知识库id为空！");
            return;
        }
        if (StringUtils.isBlank(configVO.getLibraryBaseUrl()) || StringUtils.isBlank(configVO.getLibraryApiKey())) {
            log.error("上传AI知识库文件：配置内容为空");
            return;
        }

        List<FileVO> fileVOList = fileClient.getByIds(fileIds);
        if (CollectionUtils.isEmpty(fileVOList)) {
            log.error("上传AI知识库文件：获取的文件为空{}", fileIds);
            return;
        }
        this.uploadAiDocument(fileVOList, businessKey, configVO);
    }

    /**
     * 上传AI知识文档(通过文件对象集合)
     *
     * @param fileList    文件对象集合
     * @param businessKey 业务主键
     * @param configVO    配置对象
     */
    private void uploadAiDocument(List<FileVO> fileList, Long businessKey, AiLibraryConfigVO configVO) {
        if (CollectionUtils.isEmpty(fileList) || configVO == null || businessKey == null) {
            return;
        }
        AtomicReference<String> id = new AtomicReference<>();
        String url = configVO.getLibraryBaseUrl() + "/datasets/" + configVO.getLibraryId() + "/document/create-by-file";
        fileList.forEach(fileVO -> {
            // 其他参数
            String data = configVO.getLibraryRule();
            // 下载
            File file = this.fileVoToFile(fileVO);
            if (file == null) {
                return;
            }
            try (HttpResponse response = HttpRequest.post(url)
                    .form("data", data)
                    .form("file", file, fileVO.getName()) // 添加文件
                    .timeout(30000) // 设置超时时间为 30 秒
                    .charset("UTF-8") // 设置编码，默认UTF-8
                    .header("Authorization", "Bearer " + configVO.getLibraryApiKey())
                    .execute()) { // 执行请求
                // 获取并输出服务器响应结果
                String result = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readValue(result, JsonNode.class);
                if (jsonNode != null && jsonNode.get("document") != null) {
                    id.set(jsonNode.get("document").get("id").asText());
                }
                log.info("上传AI知识库文件：结果！{}", result);
            } catch (Exception e) {
                log.error("上传AI知识库文件：出错！", e);
            } finally {
                SpringFileUtils.del(file);
            }
        });
        if (StringUtils.isNotBlank(id.get())) {
            this.update(Wrappers.<KnowledgeBase>lambdaUpdate()
                                .set(KnowledgeBase::getUpdateTime, DateUtils.now())
                                .set(KnowledgeBase::getAiDocumentId, id.get())
                                .set(KnowledgeBase::getAiLibraryId, configVO.getLibraryId())
                                .eq(KnowledgeBase::getId, businessKey)
                       );
        }

    }

    /**
     * 批量上传AI知识文档
     *
     * @param fileIds     文件id集合
     * @param idFileIdMap key业务主键,value文件id集合(业务和文件关系map)
     */
    private void batchUploadAiDocument(List<Long> fileIds, Map<Long, List<Long>> idFileIdMap) {
        if (CollectionUtils.isEmpty(fileIds) || idFileIdMap == null || idFileIdMap.isEmpty()) {
            return;
        }
        Map<Long, FileVO> fileMap = new HashMap<>();
        List<FileVO> fileVOList = fileClient.getByIds(fileIds);
        if (CollectionUtils.isEmpty(fileVOList)) {
            return;
        }
        fileVOList.forEach(fileVO -> fileMap.put(fileVO.getId(), fileVO));
        AiLibraryConfigVO configVO = AiLibraryQueryService.getAiLibraryConfig();
        if (StringUtils.isBlank(configVO.getLibraryBaseUrl()) || StringUtils.isBlank(configVO.getLibraryApiKey())) {
            log.error("批量添加AI知识库文件：配置内容为空");
            return;
        }
        idFileIdMap.forEach((businessKey, ids) -> {
            List<FileVO> fileVOS = ids.stream().map(fileMap::get).filter(Objects::nonNull).collect(Collectors.toList());
            this.uploadAiDocument(fileVOS, businessKey, configVO);
        });
    }

    /**
     * 批量移除AI知识文档
     *
     * @param documentMap key文档id,value知识库id
     */
    private void removeAiDocumentBatch(Map<String, String> documentMap) {
        if (documentMap == null || documentMap.isEmpty()) {
            return;
        }
        AiLibraryConfigVO configVO = AiLibraryQueryService.getAiLibraryConfig();
        if (StringUtils.isBlank(configVO.getLibraryBaseUrl()) || StringUtils.isBlank(configVO.getLibraryApiKey())) {
            log.error("移除AI知识库文件：配置内容为空");
            return;
        }
        documentMap.forEach((documentId, libraryId) -> {

            try (HttpResponse response = HttpRequest
                    .delete(configVO.getLibraryBaseUrl() + "/datasets/" + libraryId + "/documents/" + documentId)
                    .charset("UTF-8") // 设置编码，默认UTF-8
                    .header("Authorization", "Bearer " + configVO.getLibraryApiKey()).execute()) { // 执行请求
                // 获取并输出服务器响应结果
                String result = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readValue(result, JsonNode.class);
                if (jsonNode == null || jsonNode.get("code") != null) {
                    log.error("移除AI知识库文件：失败！{}", result);
                }
            } catch (Exception e) {
                log.error("移除AI知识库文件：出错！", e);
            }
        });
    }

    /**
     * fileVO转file
     *
     * @param fileVO vo
     *
     * @return 文件
     */
    private File fileVoToFile(FileVO fileVO) {
        // 下载
        File file = null;
        try {
            file = File.createTempFile("download", "." + fileVO.getSuffix());
            HttpUtil.downloadFileFromUrl(fileVO.getEndpointPath(), file);
        } catch (IOException e) {

            SpringFileUtils.del(file);
            log.error("AI知识库，下载文件失败：", e);
        }
        return file;
    }
}

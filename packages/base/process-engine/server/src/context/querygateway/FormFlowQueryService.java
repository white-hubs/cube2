package com.guhecloud.cube.base.flow.context.querygateway;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileModifyParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.flow.context.commandgateway.FormFlowGroupService;
import com.guhecloud.cube.base.flow.context.dao.FormFlowBusinessMapper;
import com.guhecloud.cube.base.flow.context.dao.FormFlowGroupMapper;
import com.guhecloud.cube.base.flow.context.dao.FormFlowMapper;
import com.guhecloud.cube.base.flow.context.enumeration.ApplicationEnum;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFieldVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowBusinessPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowGroupPO;
import com.guhecloud.cube.base.flow.context.po.FormFlowPO;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowQueryParam;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.application.querygateway.ProcessDefinitionQueryService;
import com.guhecloud.cube.base.flow.form.dao.FormControlBusinessMapper;
import com.guhecloud.cube.base.flow.form.interfaces.vo.FormDefinitionVO;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.cube.base.flow.form.querygateway.FormDefinitionQueryService;
import com.guhecloud.cube.base.flow.webhooks.service.WebhookConfigService;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;
import vo.FormBizFieldMapVO;
import vo.FormFlowBaseVO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowQueryService extends ServiceImpl<FormFlowMapper, FormFlowPO> {
    private static final String IMG_FORMATE = "png";
    private final FormDefinitionQueryService formDefinitionQueryService;
    private final ProcessDefinitionQueryService processDefinitionQueryService;
    private final ApplicationClient applicationClient;
    private final FormControlBusinessMapper formControlBusinessMapper;
    private final WebhookConfigService webhookConfigService;
    private final FileClient fileClient;
    private final FormFlowBusinessMapper formFlowBusinessMapper;
    private final FormFlowGroupMapper formFlowGroupMapper;
    private final FormFlowGroupService formFlowGroupService;

    /**
     * ID查询
     *
     * @param id 表单ID
     *
     * @return
     */
    public FormFlowDetailVO get(Long id) {
        FormFlowPO formFlowPO = this.getById(id);
        Assert.isNotFound(formFlowPO, "表单不存在");
        FormFlowDetailVO detailVO = ObjectUtils.copying(formFlowPO, FormFlowDetailVO.class);
        this.extend(detailVO);
        return detailVO;
    }

    /**
     * 列表查询
     *
     * @param param 查询参数
     *
     * @return
     */
    public List<FormFlowDetailVO> getList(FormFlowQueryParam param) {
        List<FormFlowPO> formFlowPOS = this.getQueryChainWrapper(param).list();
        List<FormFlowDetailVO> detailVOS = ObjectUtils.copying(formFlowPOS, FormFlowDetailVO.class);
        setExtendInfo(detailVOS);
        return detailVOS;
    }

    public FormFlowDetailVO getFromInfo(String businessType) {
        FormFlowDetailVO formFlowDetailVO = this.baseMapper.selectAutoRemoveDuplication(businessType);
        Assert.isNotFound(formFlowDetailVO, "表单不存在");
        FormDefinitionVO formDefinitionVO = formDefinitionQueryService.getLastVersionByKey(formFlowDetailVO.getFormKey());
        formFlowDetailVO.setFormJson(formDefinitionVO.getJsonSchema());
        return formFlowDetailVO;
    }

    /**
     * 列表查询（包含flowJson、formJson）
     *
     * @param param 查询参数
     *
     * @return
     */
    public List<FormFlowDetailVO> getDetailList(FormFlowQueryParam param) {
        List<FormFlowPO> formFlowPOS = this.getQueryChainWrapper(param).list();
        List<FormFlowDetailVO> detailVOS = ObjectUtils.copying(formFlowPOS, FormFlowDetailVO.class);
        List<String> formKeys = detailVOS.stream().map(FormFlowDetailVO::getFormKey).collect(Collectors.toList());
        List<String> applications = detailVOS.stream().map(FormFlowDetailVO::getApplication).collect(Collectors.toList());

        //关联数据
        List<FormDefinitionVO> formDefinitionVOS = formDefinitionQueryService.getLastVersionByKeys(formKeys);
        List<ApplicationDTO> applicationByCodes = applicationClient.getApplicationsByCodes(applications);

        Map<String, FormDefinitionVO> formDefinitionVOMap = formDefinitionVOS.stream()
                .collect(Collectors.toMap(FormDefinitionVO::getKey, Function.identity()));
        Map<String, ApplicationDTO> applicationDTOMap = applicationByCodes.stream()
                .collect(Collectors.toMap(ApplicationDTO::getCode, Function.identity()));

        detailVOS.forEach(detailVO -> {
            // 获取最新的表单定义json
            FormDefinitionVO lastVersionByKey = formDefinitionVOMap.get(detailVO.getFormKey());
            detailVO.setFormJson(lastVersionByKey == null ? StringUtils.EMPTY_STRING : lastVersionByKey.getJsonSchema());

            //获取最新的流程定义json(activiti)
            ProcessDefinitionDTO definitionDTO = processDefinitionQueryService.get(detailVO.getFlowKey());
            detailVO.setFlowJson(definitionDTO == null ? StringUtils.EMPTY_STRING : definitionDTO.getProcessFormJson());

            //获取最新的流程定义json
            ApplicationDTO applicationDTO = applicationDTOMap.get(detailVO.getApplication());
            detailVO.setApplicationName(applicationDTO == null ? StringUtils.EMPTY_STRING : applicationDTO.getName());
        });
        return detailVOS;
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     *
     * @return
     */
    public Pagination<FormFlowDetailVO> getPage(FormFlowQueryParam param) {
        Page<FormFlowPO> page = this.getQueryChainWrapper(param)
                .page(PagingUtils.convert(param));
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Pagination<FormFlowDetailVO> pagination = PagingUtils.convert(page, FormFlowDetailVO.class);
        setExtendInfo(pagination.getRecords());
        return pagination;
    }

    private void setExtendInfo(List<FormFlowDetailVO> recordList) {
        Set<String> applicationCodes = new HashSet<>();
        Set<Long> formFlowIds = new HashSet<>();
        Set<Long> formGroupIds = new HashSet<>();
        Set<Long> fileIds = new HashSet<>();
        for (FormFlowDetailVO record : recordList) {
            if (StringUtils.isNotEmpty(record.getApplication())) {
                applicationCodes.add(record.getApplication());
            }
            if (Objects.nonNull(record.getId())) {
                formFlowIds.add(record.getId());
            }
            if (Objects.nonNull(record.getFormGroupId())) {
                formGroupIds.add(record.getFormGroupId());
            }
            if (Objects.nonNull(record.getQrImage())) {
                fileIds.add(record.getQrImage());
            }
        }
        Map<String, String> applicationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(applicationCodes)) {
            applicationMap = Optional.ofNullable(applicationClient.getApplicationsByCodes(new ArrayList<>(applicationCodes)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(ApplicationDTO::getCode, ApplicationDTO::getName, (k1, k2) -> k1));
        }
        Map<Long, String> formFlowMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(formFlowIds)) {
            formFlowMap = Optional.ofNullable(formFlowBusinessMapper.selectList(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                                                                        .in(FormFlowBusinessPO::getFormFlowId, formFlowIds)))
                    .orElse(new ArrayList<>())
                    .stream()
                    .collect(Collectors.toMap(FormFlowBusinessPO::getFormFlowId, FormFlowBusinessPO::getBusinessType, (k1, k2) -> k1));
        }
        Map<Long, String> formGroupMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(formGroupIds)) {
            formGroupMap = Optional.ofNullable(formFlowGroupMapper.selectList(Wrappers.<FormFlowGroupPO>lambdaQuery()
                                                                                      .in(FormFlowGroupPO::getId, formGroupIds)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FormFlowGroupPO::getId, FormFlowGroupPO::getName));
        }
        Map<Long, FileVO> fileMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(fileIds)) {
            fileMap = Optional.ofNullable(fileClient.getByIds(new ArrayList<>(fileIds)))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));

        }

        for (FormFlowDetailVO record : recordList) {
            record.setApplicationName(applicationMap.get(record.getApplication()));
            record.setBusinessType(formFlowMap.get(record.getId()));
            record.setFormGroupName(formGroupMap.get(record.getFormGroupId()));
            record.setFileVO(fileMap.get(record.getQrImage()));
        }
    }

    /**
     * 获取应用列表
     *
     * @return
     */
    public List<ApplicationDTO> getApplicationList() {
        //获取租户下的应用列表
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<ApplicationDTO> tenantApplications = applicationClient.getTenantApplications(tenant);
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return new ArrayList<>();
        }
        List<ApplicationDTO> applicationCodeList = new ArrayList<>();
        Map<String, String> applicationMap = ApplicationEnum.getMap();
        tenantApplications.forEach(ex -> {
            if (ObjectUtils.isNotEmpty(applicationMap.get(ex.getCode()))) {
                applicationCodeList.add(ex);
            }
        });
        return applicationCodeList;
    }

    private LambdaQueryChainWrapper<FormFlowPO> getQueryChainWrapper(FormFlowQueryParam param) {

        LambdaQueryChainWrapper<FormFlowPO> queryWrapper = this.lambdaQuery()
                .ne(FormFlowPO::getStatus, FormFlowStatusEnum.DELETED)
                .like(StringUtils.isNotEmpty(param.getKeyword()), FormFlowPO::getName, param.getKeyword())
                .eq(StringUtils.isNotEmpty(param.getApplication()), FormFlowPO::getApplication, param.getApplication())
                .eq(StringUtils.isNotEmpty(param.getType()), FormFlowPO::getType, param.getType())
                .in(CollectionUtils.isNotEmpty(param.getTypeList()), FormFlowPO::getType, param.getTypeList())
                .eq(StringUtils.isNotEmpty(param.getStatus()), FormFlowPO::getStatus, param.getStatus())
                .eq(null != param.getEnable(), FormFlowPO::getEnable, param.getEnable());
        if (Objects.nonNull(param.getFromGroupId())) {
            List<Long> groupIds = formFlowGroupService.listLeafGroupIds(param.getFromGroupId());
            if (CollectionUtils.isEmpty(groupIds)) {
                queryWrapper.eq(FormFlowPO::getFormGroupId, -1L);
            } else {
                queryWrapper.in(FormFlowPO::getFormGroupId, groupIds);
            }
        }
        return queryWrapper.orderByDesc(BasicEntity::getUpdateTime);
    }

    private void extend(FormFlowDetailVO detailVO) {
        // 获取最新的表单定义json
        FormDefinitionVO lastVersionByKey = formDefinitionQueryService.getLastVersionByKey(detailVO.getFormKey());
        detailVO.setFormJson(lastVersionByKey.getJsonSchema());
        //解析表单
        List<Map<String, Object>> fieldList = JsonUtils.deserialize(detailVO.getFormJson(), new TypeReference<List<Map<String, Object>>>() {});
        detailVO.setFormFieldList(fieldList.stream().map(field -> {
            String id = field.get("id").toString();
            String name = field.get("title").toString();
            return FormFieldVO.builder().id(id).name(name).build();
        }).collect(Collectors.toList()));

        detailVO.setFormDefinitionVersion(lastVersionByKey.getVersion());
        detailVO.setFormDefinitionId(lastVersionByKey.getId());
        //获取最新的流程定义json
        ProcessDefinitionDTO definitionDTO = processDefinitionQueryService.get(detailVO.getFlowKey());
        detailVO.setFlowJson(definitionDTO.getProcessFormJson());
        //查询应用名称
        this.setExtendInfo(Collections.singletonList(detailVO));
        //查询绑定的字段映射
        List<FormControlBusinessPO> formControlBusinessPOS = formControlBusinessMapper.selectList(Wrappers
                                                                                                          .<FormControlBusinessPO>lambdaQuery()
                                                                                                          .eq(FormControlBusinessPO::getFormDefinitionId, lastVersionByKey.getId()));
        List<FormBizFieldMapVO> formBizFieldMapList = ObjectUtils.copying(formControlBusinessPOS, FormBizFieldMapVO.class);
        detailVO.setFormBizFieldMapList(formBizFieldMapList);
        detailVO.setWebhookConfigVOList(webhookConfigService.getWebhookList(detailVO.getId()));
    }

    private void extendApplicationName(FormFlowDetailVO detailVO) {
        ApplicationDTO application = applicationClient.getApplicationByCode(detailVO.getApplication());
        if (Objects.nonNull(detailVO.getQrImage())) {
            detailVO.setFileVO(fileClient.getById(detailVO.getQrImage()));
        }
        List<FormFlowBusinessPO> formFlowBusinessPO = formFlowBusinessMapper.selectList(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                                                                                .eq(FormFlowBusinessPO::getFormFlowId, detailVO.getId())
                                                                                                .select(FormFlowBusinessPO::getBusinessType));
        detailVO.setBusinessType(
                CollectionUtils.isNotEmpty(formFlowBusinessPO) ? formFlowBusinessPO.get(0).getBusinessType() : StringUtils.EMPTY_STRING);
        detailVO.setApplicationName(application == null ? StringUtils.EMPTY_STRING : application.getName());
    }

    /**
     * 生成表单流程的二维码
     *
     * @param id
     * @param qrCodeUrl
     * @param businessType
     *
     * @return
     */
    public FileVO getQrCode(Long id, String qrCodeUrl, String businessType) {
        FormFlowPO formFlowPO = this.getById(id);
        Assert.isNotFound(formFlowPO, "表单不存在");
        //获取业务类型
        List<FormFlowBusinessPO> formFlowBusinessPO = formFlowBusinessMapper.selectList(Wrappers.<FormFlowBusinessPO>lambdaQuery()
                                                                                                .eq(FormFlowBusinessPO::getFormFlowId, formFlowPO.getId())
                                                                                                .select(FormFlowBusinessPO::getBusinessType));
        String bindBusinessType =
                CollectionUtils.isNotEmpty(formFlowBusinessPO) ? formFlowBusinessPO.get(0).getBusinessType() : StringUtils.EMPTY_STRING;
        //业务类型不变则直接返回
        if (Objects.nonNull(formFlowPO.getQrImage()) && StringUtils.equals(businessType, bindBusinessType)) {
            return fileClient.getById(formFlowPO.getQrImage());
        }
        QrConfig config = new QrConfig();
        config.setMargin(1);
        config.setWidth(200);
        config.setHeight(200);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        MultipartFile multipartFile;
        try {
            BufferedImage image = QrCodeUtil.generate(qrCodeUrl, config);
            ImageIO.write(image, IMG_FORMATE, os);
            multipartFile = SpringFileUtils.fileByteConvertMultipartFile(
                    formFlowPO.getId().toString() + "." + IMG_FORMATE,
                    os.toByteArray(), ContentType.APPLICATION_OCTET_STREAM.toString());
        } catch (IOException e) {
            log.error("二维码图片生成异常:", e);
            throw new RuntimeException(e);
        } finally {
            os.close();
        }
        FileModifyParam fileModifyParam = new FileModifyParam();
        fileModifyParam.setFile(multipartFile);
        fileModifyParam.setTenant(SessionContextHolder.getRequester().getTenant());
        fileModifyParam.setBusinessKey("maintain");
        Long fileId = fileClient.upload(fileModifyParam).getId();
        formFlowPO.setQrImage(fileId);
        this.updateById(formFlowPO);
        return fileClient.getById(fileId);
    }

    /**
     * 根据流程表单id获取信息client调用
     *
     * @param formFlowId
     *
     * @return
     */
    public FormFlowBaseVO getFormFlowBaseInfoById(Long formFlowId) {
        FormFlowPO formFlowPO = this.getById(formFlowId);
        Assert.isNotFound(formFlowPO, "表单不存在");
        FormFlowBaseVO formFlowBaseVO = ObjectUtils.copying(formFlowPO, FormFlowBaseVO.class);
        // 获取最新的表单定义json
        FormDefinitionVO lastVersionByKey = formDefinitionQueryService.getLastVersionByKey(formFlowBaseVO.getFormKey());
        formFlowBaseVO.setFormDefinitionVersion(lastVersionByKey.getVersion());
        formFlowBaseVO.setFormDefinitionId(lastVersionByKey.getId());
        List<FormControlBusinessPO> formControlBusinessPOS = formControlBusinessMapper.selectList(Wrappers
                                                                                                          .<FormControlBusinessPO>lambdaQuery()
                                                                                                          .eq(FormControlBusinessPO::getFormDefinitionId, lastVersionByKey.getId()));
        List<FormBizFieldMapVO> formBizFieldMapList = ObjectUtils.copying(formControlBusinessPOS, FormBizFieldMapVO.class);
        formFlowBaseVO.setFormBizFieldMapList(formBizFieldMapList);
        //查询应用名称
        ApplicationDTO application = applicationClient.getApplicationByCode(formFlowBaseVO.getApplication());
        formFlowBaseVO.setApplicationName(application == null ? StringUtils.EMPTY_STRING : application.getName());
        return formFlowBaseVO;
    }
}

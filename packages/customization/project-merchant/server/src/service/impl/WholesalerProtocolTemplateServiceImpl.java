package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolTemplateDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocolTemplate;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectMerchantAreaEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerProtocalTemplateMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerProtocolService;
//import com.guhecloud.cube.deliver.project.merchant.service.WholesalerProtocolService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerProtocolTemplateService;
import com.guhecloud.cube.deliver.project.merchant.utils.FilesUtil;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerProtocolTemplateServiceImpl extends ServiceImpl<WholesalerProtocalTemplateMapper,
        WholesalerProtocolTemplate> implements WholesalerProtocolTemplateService {

    private final WholesalerProtocalTemplateMapper mapper;
    private final FileClient fileClient;
    private final WholesalerProtocolService wpService;
    private final FileRefClient fileRefClient;

    @Override
    public WholesalerProtocolTemplateDTO getTemplateById(String id) {
        WholesalerProtocolTemplate template = this.lambdaQuery().eq(WholesalerProtocolTemplate::getId, id).one();
        if (template == null) {
            throw new ServiceException("系统中没有找到对应的模板详情");
        }
        WholesalerProtocolTemplateDTO dto = new WholesalerProtocolTemplateDTO(template);
        dto.setTypeName(ProjectMerchantAreaEnum.getCodeByName(dto.getType()));
        this.getMarkFile(dto);
        return dto;
    }

    @Override
    public String save(WholesalerProtocolTemplateDTO dto) {
        WholesalerProtocolTemplate template = dto.convert();
        template.setCode(BizCodeUtils.generate("TEMP"));
        template.setCreateTime(LocalDateTime.now());
        template.setCreateBy(SecurityContextHolder.getPrincipal().getName());
        mapper.insert(template);
        String templateId = template.getId();
        dto.setId(templateId);
        //        this.markFile(dto);
        if (null != dto.getProtocolFileIds() && !dto.getProtocolFileIds().isEmpty()) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(template.getId(), dto.getProtocolFileIds());
            fileRefClient.saveBatch(files);
        }
        return templateId;
    }

    @Override
    public String update(WholesalerProtocolTemplateDTO dto) {
        WholesalerProtocolTemplate template = this.lambdaQuery().eq(WholesalerProtocolTemplate::getId, dto.getId()).one();
        if (template == null) {
            throw new ServiceException("该模板不存在");
        }
        mapper.updateById(dto.convert());
        fileRefClient.removeByBusinessKey(Long.parseLong(dto.getId()));
        if (null != dto.getProtocolFileIds() && !dto.getProtocolFileIds().isEmpty()) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(dto.getId(), dto.getProtocolFileIds());
            fileRefClient.saveBatch(files);
        }
        // 修改引用名称
        wpService.lambdaUpdate().eq(WholesalerProtocol::getTemplateId, dto.getId()).set(WholesalerProtocol::getTemplateType, dto.getType())
                .set(WholesalerProtocol::getTemplateName, dto.getName()).update();
        return dto.getId();
    }

    @Override
    public void removeById(String id) {
        //如果该模板下面有协议，就不让删除
        List<WholesalerProtocol> protocols = wpService.lambdaQuery().eq(WholesalerProtocol::getTemplateId, id).list();
        if (!protocols.isEmpty()) {
            throw new ServiceException("该模板下还有合同，不能删除该模板");
        }
        //        fileApi.removeBatch(PROTOCOL_FILE, id);
        mapper.deleteById(id);
    }

    @Override
    public List<WholesalerProtocolTemplate> getTemplateList(String projectId) {
        return this.lambdaQuery().eq(WholesalerProtocolTemplate::getProjectId, projectId).list();
    }

    private void markFile(WholesalerProtocolTemplateDTO dto) {
        List<Long> fileIds = dto.getProtocolFileIds();
        if (fileIds != null && !fileIds.isEmpty()) {
            //            fileIds.forEach(fileId -> fileApi.mark(fileId, PROTOCOL_FILE, dto.getId()));
        }
    }

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(WholesalerProtocolTemplateDTO dto) {
        List<FileVO> FileVOs = null;
        try {
            FileVOs = fileClient.getByBusinessKey(Long.parseLong(dto.getId()));
        } catch (Exception e) {
            log.error("获取文件异常", e);
        }
        assert FileVOs != null;
        dto.setProtocolFileIds(FileVOs.stream().map(FileVO::getId).collect(Collectors.toList()));
        dto.setProtocolFileDTOS(FileVOs);
    }
}

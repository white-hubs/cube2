package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerFirstPart;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerSignatory;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerFirstPartService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerFirstPartDTO;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerFirstPartMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerSignatoryService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerFirstPartServiceImpl extends ServiceImpl<WholesalerFirstPartMapper, WholesalerFirstPart> implements WholesalerFirstPartService {

    private final WholesalerFirstPartMapper mapper;
    private final WholesalerSignatoryService signatoryService;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    @Override
    public WholesalerFirstPartDTO getFirstPart(String projectId) {

        WholesalerFirstPart part = this.lambdaQuery().eq(WholesalerFirstPart::getProjectId, projectId).one();
        if (part == null) {
            return null;
        }
        List<WholesalerSignatory> signatories = signatoryService.lambdaQuery()
                .eq(WholesalerSignatory::getFirstPartId, part.getId()).list();
        WholesalerFirstPartDTO dto = new WholesalerFirstPartDTO(part);
        dto.setSignatoryList(signatories);
        this.getMarkFile(dto);
        return dto;
    }

    @Override
    public Long saveOrUpdate(WholesalerFirstPartDTO dto) {
        WholesalerFirstPart part = dto.convert();
        //获取签约人信息
        List<WholesalerSignatory> signatoryList = dto.getSignatoryList();
        long count = this.lambdaQuery().eq(WholesalerFirstPart::getProjectId, dto.getProjectId()).count();
        boolean exist =
                dto.getId() != null && (this.lambdaQuery().eq(WholesalerFirstPart::getId, dto.getId()).one() != null);
        if (exist) {
            List<WholesalerSignatory> signatories = signatoryService.lambdaQuery()
                    .eq(WholesalerSignatory::getFirstPartId, dto.getId()).list();
            signatories.forEach(signatory -> signatoryService.removeById(signatory.getId()));
            mapper.updateById(part);
        } else if (count > 0) {
            throw new ServiceException("项目中只能有一条甲方信息，如需对其修改，请确保传入正确的ID");
        } else {
            mapper.insert(part);
            dto.setId(part.getId());
        }
        signatoryList.forEach(signatory -> {
            signatory.setFirstPartId(part.getId());
            signatoryService.save(signatory);
        });
        //mark电子章
        markFile(dto, exist);
        return part.getId();
    }

    private void markFile(WholesalerFirstPartDTO dto, boolean exist) {
        Set<Long> fileIds = dto.getFileIds();
        if (fileIds != null && !fileIds.isEmpty()) {
            if (exist) {
                fileRefClient.ref(dto.getId(), fileIds);
            }
        }
    }

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(WholesalerFirstPartDTO dto) {
        List<FileVO> fileDtos = null;
        try {
            fileDtos = fileClient.getByBusinessKey(dto.getId());
            assert fileDtos != null;
            dto.setFileDtos(fileDtos);
        } catch (Exception e) {
            log.error("获取文件异常", e);
        }
        assert fileDtos != null;
        dto.setFileIds(fileDtos.stream().map(FileVO::getId).collect(Collectors.toSet()));
        dto.setFileDtos(fileDtos);
    }
}

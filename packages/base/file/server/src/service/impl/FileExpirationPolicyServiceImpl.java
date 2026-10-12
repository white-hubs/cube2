package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.server.dao.FileExpirationPolicyMapper;
import com.guhecloud.cube.base.file.server.dto.FileExpirationPolicyDTO;
import com.guhecloud.cube.base.file.server.entity.FileExpirationPolicy;
import com.guhecloud.cube.base.file.server.entity.SceneInfo;
import com.guhecloud.cube.base.file.server.enumeration.ExpirationPolicyEnum;
import com.guhecloud.cube.base.file.server.param.FileExpirationPolicyPageParam;
import com.guhecloud.cube.base.file.server.service.FileExpirationPolicyService;
import com.guhecloud.cube.base.file.server.service.SceneInfoService;
import com.guhecloud.cube.base.file.server.service.SpecificExpirationPolicyService;
import com.guhecloud.cube.base.file.server.vo.FileExpirationPolicyVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijie
 * @date 2023/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileExpirationPolicyServiceImpl extends ServiceImpl<FileExpirationPolicyMapper, FileExpirationPolicy> implements FileExpirationPolicyService {
    private final FileExpirationPolicyMapper fileExpirationPolicyMapper;
    private final SpecificExpirationPolicyService specificExpirationPolicyService;
    private final SceneInfoService sceneInfoService;

    @Override
    public Pagination<FileExpirationPolicyVO> getExpirationPolicyPage(FileExpirationPolicyPageParam param) {
        Page<FileExpirationPolicy> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = fileExpirationPolicyMapper.selectPage(page,
                                                     new LambdaQueryWrapper<FileExpirationPolicy>()
                                                             .like(ObjectUtils.isNotEmpty(param.getSceneCode()), FileExpirationPolicy::getSceneCode, param.getSceneCode()));
        List<FileExpirationPolicyVO> list = ObjectUtils.copying(page.getRecords(), FileExpirationPolicyVO.class);
        if (ObjectUtils.isNotEmpty(list)) {
            List<SceneInfo> sceneInfoList = sceneInfoService.getSceneInfoAll();
            Map<String, String> stringStringMap = sceneInfoList.stream()
                    .collect(Collectors.toMap(SceneInfo::getSceneCode, SceneInfo::getSceneName));
            list.forEach(ex -> {
                String sceneName = stringStringMap.get(ex.getSceneCode());
                if (ObjectUtils.isNotEmpty(sceneName)) {
                    ex.setSceneName(sceneName);
                }
            });
        }
        return PagingUtils.convert(page, list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addExpirationPolicy(FileExpirationPolicyDTO param) {
        this.checkSceneCode(param.getSceneCode(), null);
        FileExpirationPolicy policy = ConvertUtils.convert(FileExpirationPolicy.class, param);
        policy.setId(IdWorker.getId());
        return fileExpirationPolicyMapper.insert(policy) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateExpirationPolicy(FileExpirationPolicyDTO param) {
        FileExpirationPolicy fileExpirationPolicy = this.checkSceneCode(param.getSceneCode(), param.getId());
        fileExpirationPolicy.setSceneCode(param.getSceneCode());
        fileExpirationPolicy.setExpirationPolicy(param.getExpirationPolicy());
        fileExpirationPolicy.setConfigInfo(param.getConfigInfo());
        return fileExpirationPolicyMapper.updateById(fileExpirationPolicy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteExpirationPolicy(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    /**
     * 定期删除文件
     *
     * @return 是否有删除
     */
    @Override
    public void scheduledDelete() {
        log.info("定期删除文件：scheduledDelete");
        // 判断桶是否配置删除策略
        List<FileExpirationPolicy> policyList = fileExpirationPolicyMapper.selectList(
                new LambdaQueryWrapper<>());
        if (ObjectUtils.isNotEmpty(policyList)) {
            for (FileExpirationPolicy ex : policyList) {
                SessionContextHolder.requestTenant(ex.getTenant());
                log.info("开始执行删除策略：{}", ex.getId());
                if (ExpirationPolicyEnum.BASED_ON_TIME_EXPIRATION.name().equals(ex.getExpirationPolicy())) {
                    if ("".equals(ex.getSceneCode())) {
                        // 清理临时文件

                    }
                    specificExpirationPolicyService.basedTimeTag(ex);
                    specificExpirationPolicyService.basedTime(ex);
                } else if (ExpirationPolicyEnum.BASED_ON_SIZE_EXPIRATION.name().equals(ex.getExpirationPolicy())) {
                    specificExpirationPolicyService.basedSize(ex);
                } else if (ExpirationPolicyEnum.BASED_ON_LEAST_RECENTLY_USED_EXPIRATION.name().equals(ex.getExpirationPolicy())) {
                    specificExpirationPolicyService.basedFrequency(ex);
                }
            }
        }
    }

    /**
     * 新增、编辑场景编码不能重复
     */
    private FileExpirationPolicy checkSceneCode(String sceneCode, Long id) {
        FileExpirationPolicy fileExpirationPolicy = fileExpirationPolicyMapper.selectOne(new LambdaQueryWrapper<FileExpirationPolicy>()
                                                                                                 .eq(FileExpirationPolicy::getSceneCode, sceneCode)
                                                                                                 .last("limit 1"));

        var bool = ObjectUtils.isNotEmpty(id) ? ObjectUtils.isNotEmpty(fileExpirationPolicy) && !id.equals(fileExpirationPolicy.getId())
                                              : ObjectUtils.isNotEmpty(fileExpirationPolicy);
        Assert.isConflict(bool, "场景编码已存在");
        return fileExpirationPolicy;
    }
}

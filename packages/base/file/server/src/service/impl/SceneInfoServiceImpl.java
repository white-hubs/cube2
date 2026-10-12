package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.server.dao.FileConfigMapper;
import com.guhecloud.cube.base.file.server.dao.SceneInfoMapper;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.SceneInfo;
import com.guhecloud.cube.base.file.server.service.SceneInfoService;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  10:44
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SceneInfoServiceImpl extends ServiceImpl<SceneInfoMapper, SceneInfo> implements SceneInfoService {
    private final SceneInfoMapper sceneInfoMapper;
    private final FileConfigMapper fileConfigMapper;

    @Override
    public List<SceneInfo> getSceneInfoAll() {
        return sceneInfoMapper.selectList(null);
    }

    @Override
    public FileConfig getConfig(String sceneCode, Integer tenant) {
        SceneInfo sceneInfo = sceneInfoMapper.selectOne(new LambdaQueryWrapper<SceneInfo>()
                                                                .eq(SceneInfo::getSceneCode, sceneCode));
        Assert.parameterIsNull(sceneInfo, "请先配置场景");
        return fileConfigMapper.selectById(sceneInfo.getConfigId());
    }
}

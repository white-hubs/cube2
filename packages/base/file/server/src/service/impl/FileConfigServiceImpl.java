package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.server.dao.FileConfigMapper;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.enumeration.FileStoreType;
import com.guhecloud.cube.base.file.server.minio.dao.FileStoreSignUpMapper;
import com.guhecloud.cube.base.file.server.minio.entity.FileStoreSignUp;
import com.guhecloud.cube.base.file.server.param.FileConfigModifyParam;
import com.guhecloud.cube.base.file.server.service.FileConfigService;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lzy
 * @date 2022/2/24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileConfigServiceImpl extends ServiceImpl<FileConfigMapper, FileConfig> implements FileConfigService {
    private final FileStoreSignUpMapper fileStoreSignUpMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFileConfig(@NonNull FileConfigModifyParam param) {
        FileConfig fileConfig = ObjectUtils.copying(param, FileConfig.class);
        this.save(fileConfig);
        FileStoreSignUp fileStoreSignUp = ConvertUtils.convert(FileStoreSignUp.class, param);
        fileStoreSignUp.setFileStore(fileConfig.getId());
        fileStoreSignUp.setType(FileStoreType.getFileStoreType(param.getStorageObjectType()));
        fileStoreSignUpMapper.insert(fileStoreSignUp);
    }

    @Override
    public FileConfig getByTenant(@NonNull Integer tenant) {
        return this.lambdaQuery().eq(FileConfig::getTenant, tenant).last("limit 1").one();
    }
}

package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.server.minio.dao.FileStoreSignUpMapper;
import com.guhecloud.cube.base.file.server.minio.entity.FileStoreSignUp;
import com.guhecloud.cube.base.file.server.minio.service.impl.MinIoFileStore;
import com.guhecloud.cube.base.file.server.service.FileStore;
import com.guhecloud.cube.base.file.server.service.FileStoreRouteStrategy;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/7/19 11:20
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SingleFileStore extends ServiceImpl<FileStoreSignUpMapper, FileStoreSignUp> implements FileStoreRouteStrategy {
    private final FileStoreSignUpMapper fileStoreSignUpMapper;

    /**
     * 在注册的FileStore里面寻找一个可用的FileStore
     * 目前暂时规定只允许注册1个
     *
     * @return
     */
    @Override
    public FileStore getFileStore() {
        List<FileStoreSignUp> fileStoreSignUps = fileStoreSignUpMapper.selectList(Wrappers.lambdaQuery());
        /**
         * 兼容之前旧数据，等数据转移后，取消此代码
         */
        if (CollectionUtils.isEmpty(fileStoreSignUps)) {
            return ApplicationContextHolder.getBean(FileServiceImpl.class);
        }
        FileStoreSignUp fileStoreSignUp = fileStoreSignUps.stream().findFirst().get();
        switch (fileStoreSignUp.getType()) {
            case MINIO:
                return ApplicationContextHolder.getBean(MinIoFileStore.class);
            case OSS:
                return ApplicationContextHolder.getBean(FileServiceImpl.class);
        }
        throw new ServiceException("未找到file store");
    }

    @Override
    public FileStore getFileStore(Long configId) {
        FileStoreSignUp fileStoreSignUp = fileStoreSignUpMapper.selectOne(new LambdaQueryWrapper<FileStoreSignUp>()
                                                                                  .eq(FileStoreSignUp::getFileStore, configId)
                                                                                  .last(" limit 1"));
        Assert.parameterIsNull(fileStoreSignUp, "未找到file store");
        switch (fileStoreSignUp.getType()) {
            case MINIO:
                return ApplicationContextHolder.getBean(MinIoFileStore.class);
            case OSS:
                return ApplicationContextHolder.getBean(FileServiceImpl.class);
        }
        throw new ServiceException("file store未实现");
    }
}

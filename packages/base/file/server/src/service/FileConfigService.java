package com.guhecloud.cube.base.file.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.param.FileConfigModifyParam;

/**
 * @author lzy
 * @date 2022/2/24
 */
public interface FileConfigService extends IService<FileConfig> {

    void addFileConfig(FileConfigModifyParam param);

    FileConfig getByTenant(Integer tenant);

}

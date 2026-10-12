package com.guhecloud.cube.base.file.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.file.server.entity.FileRef;
import com.guhecloud.cube.base.file.server.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.server.param.FileRefRemoveParam;

import java.util.List;

public interface FileRefService extends IService<FileRef> {

    List<FileRef> getByBusinessKey(Long businessKey);

    List<FileRef> getByBusinessKeys(List<Long> businessKeys);

    void save(Long businessKey, Long fileId);

    void saveBatch(List<FileRefCreateParam> params);

    void remove(Long id);

    void removeByBusinessKey(Long businessKey);

    void removeByBusinessKeyAndIds(FileRefRemoveParam fileRefRemoveParam);

    /**
     * 批量关联并持久化
     *
     * @param fileRefs 文件关联关系
     */
    void refInduceBatch(List<FileRefCreateParam> fileRefs);

    void removeByBusinessKeyAndScene(Long businessKey, String sceneCode);
}

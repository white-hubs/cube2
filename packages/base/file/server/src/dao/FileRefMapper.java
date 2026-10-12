package com.guhecloud.cube.base.file.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.file.server.entity.FileRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileRefMapper extends BaseMapper<FileRef> {

    void addFileRef(@Param("fileRef") FileRef fileRef, @Param("index") Long index);

    void addBatchFileRef(@Param("list") List<FileRef> fileRefs, @Param("index") Long index);

    List<FileRef> listFileRefByBusinessKey(@Param("businessKey") Long businessKey, @Param("index") Long index);

    List<FileRef> listFileRefByBusinessKeys(@Param("businessKeys") List<Long> businessKeys, @Param("index") Long index);

    //    void deleteByBusinessKey(Long businessKey);

    void deleteByBusinessKey(@Param("businessKey") Long businessKey, @Param("index") Long index, @Param("fileIds") List<Long> fileIds);

}

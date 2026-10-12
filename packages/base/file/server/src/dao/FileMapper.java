package com.guhecloud.cube.base.file.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.server.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<File> {
    void addFile(@Param("file") File file);

    void updateFile(@Param("file") File file);

    void updateRefCount(@Param("flag") Boolean flag, @Param("id") Long id);

    void updateRefCount(@Param("flag") Boolean flag, @Param("id") Long id, @Param("index") Long index);

    void updateBatchRefCount(@Param("flag") Boolean flag, @Param("ids") List<Long> ids, @Param("index") Long index);

    void remove(@Param("id") Long id, @Param("time") Long time, @Param("operationTime") LocalDateTime operationTime);

    File getById(@Param("id") Long id);

    List<File> listByIds(@Param("ids") List<Long> ids);

    @InterceptorIgnore(tenantLine = "on")
    Page<File> listAll(@Param("page") Page<?> page, @Param("expirationTime") LocalDateTime expirationTime,
                       @Param("sceneCode") String sceneCode,
                       @Param("configId") Long configId
                      );

    List<File> listTenantAll(@Param("expirationTime") LocalDateTime expirationTime, @Param("index") Integer index,
                             @Param("sceneCode") String sceneCode
                            );

    @InterceptorIgnore(tenantLine = "on")
    Integer deleteByIds(@Param("ids") List<Long> ids);

    Integer removeTag(@Param("sceneCode") String sceneCode, @Param("index") Integer index,
                      @Param("cleaningTime") LocalDateTime cleaningTime,
                      @Param("operationTime") LocalDateTime operationTime, @Param("time") Long time
                     );

    Integer persistentFile(@Param("id") Long id, @Param("index") Long index);

    Page<File> selectListBySceneCodeAndExpirationTime(@Param("page") Page<?> page, @Param("sceneCode") String sceneCode,
                                                      @Param("expirationTime") LocalDateTime expirationTime
                                                     );
}

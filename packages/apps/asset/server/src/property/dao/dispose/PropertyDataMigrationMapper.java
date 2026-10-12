package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.NcpDevice;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.TjDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-19  11:24
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface PropertyDataMigrationMapper {
    @Select("select * from cnp_device where type_id in ('1336606724756434945','1336497905900548097')")
    List<NcpDevice> getAll();

    @Select("select * from tj_dept")
    List<TjDept> getAllDep();

}

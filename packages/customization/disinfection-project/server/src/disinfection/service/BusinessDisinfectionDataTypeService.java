package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DataTypePageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.DataTypeUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.DataTypeVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.DataTypePageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface BusinessDisinfectionDataTypeService {
    /**
     * @return 是否插入成功
     */
    boolean insertDataType(DataTypeUpsertParam dataTypeUpsertParam);

    /**
     * @param id 数据类型id
     *
     * @return 是否删除成功
     */
    boolean deleteDataType(long id);

    /**
     * @param id 数据类型id
     *
     * @return 是否修改成功
     */
    boolean updateDataType(long id, DataTypeUpsertParam dataTypeUpsertParam);

    /**
     * @param dataTypePageParam 分页参数
     *
     * @return 分页数据
     */
    Pagination<DataTypePageVO> list(DataTypePageParam dataTypePageParam);

    /**
     * @param disinfectionDataTypeEnum 数据类型枚举
     *
     * @return 数据列表
     */
    List<DataTypeVO> listAll(DisinfectionDataTypeEnum disinfectionDataTypeEnum, Long projectId);
}

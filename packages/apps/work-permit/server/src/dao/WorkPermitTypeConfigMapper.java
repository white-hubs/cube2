package com.guhecloud.cube.srv.gen.work.permit.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.work.permit.server.entity.WorkPermitTypeConfig;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitAssociatedItemQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitAssociatedItemPageVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigNameVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigPageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业类型配置 Mapper。
 */
@Repository
public interface WorkPermitTypeConfigMapper extends BaseMapper<WorkPermitTypeConfig> {
    /**
     * 分页查询作业类型配置。
     */
    Page<WorkPermitTypeConfigPageVO> selectConfigPage(Page<WorkPermitTypeConfigPageVO> page, @Param("param") WorkPermitTypeConfigQueryParam param);

    /**
     * 查询作业类型配置导出列表。
     */
    List<WorkPermitTypeConfigPageVO> selectConfigList(@Param("param") WorkPermitTypeConfigQueryParam param);

    /**
     * 查询作业等级列表。
     */
    List<String> selectLevels(@Param("param") WorkPermitTypeConfigQueryParam param);

    /**
     * 查询作业名称列表。
     */
    List<WorkPermitTypeConfigNameVO> selectNames(@Param("param") WorkPermitTypeConfigQueryParam param);

    /**
     * 分页查询作业关联项。
     */
    Page<WorkPermitAssociatedItemPageVO> selectAssociatedItemPage(Page<WorkPermitAssociatedItemPageVO> page,
                                                                  @Param("param") WorkPermitAssociatedItemQueryParam param,
                                                                  @Param("itemType") String itemType);

    /**
     * 查询作业关联项ID列表。
     */
    List<Long> selectAssociatedItemIds(@Param("param") WorkPermitAssociatedItemQueryParam param,
                                       @Param("itemType") String itemType);
}

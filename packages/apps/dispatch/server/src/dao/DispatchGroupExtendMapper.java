package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchGroupExtend;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：调度设备-分组-扩展 Repository接口
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Repository
public interface DispatchGroupExtendMapper extends BaseMapper<DispatchGroupExtend> {

    void insertOrUpdateGroupExtendBatch(List<DispatchGroupExtend> dataList);
}
package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchGroup;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupQueryParam;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupTreeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：调度设备-分组 Repository接口
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Repository
public interface DispatchGroupMapper extends BaseMapper<DispatchGroup> {

    void insertOrUpdateGroupBatch(List<DispatchGroup> datas);

    List<DispatchGroupTreeVO> getGroupWithDevice(@Param("param") DispatchGroupQueryParam param);

    List<DispatchGroupTreeVO> getGroupWithDeviceDetail(@Param("param") DispatchGroupQueryParam param);
}
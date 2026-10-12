package com.guhecloud.cube.srv.gen.loose.works.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectApplyPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wql
 * @describe 工程请示
 * @date 2023/9/14 18:01
 */
@Mapper
public interface ProjectApplyMapper extends BaseMapper<ProjectApplyPO> {
    /**
     * @Author wql
     * @Description 获取可以发起签约单的申请单列表
     * @Date 19:56 2023/9/21
     **/
    List<ProjectApplyPO> getProjectNameList();

}

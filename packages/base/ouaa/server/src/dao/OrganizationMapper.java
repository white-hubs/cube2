package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 组织机构数据操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

    List<Organization> listOrganizations(@Param("sequences") Collection<String> sequences);

    /**
     * 查询当前节点及其子节点
     *
     * @param id
     *
     * @return
     */
    LinkedList<OrganizationVO> parentListById(@Param("id") Long id);
}

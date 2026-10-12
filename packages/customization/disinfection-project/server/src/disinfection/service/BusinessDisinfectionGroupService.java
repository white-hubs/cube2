package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.GroupPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.GroupQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.GroupUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.GroupInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.GroupUserPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface BusinessDisinfectionGroupService {

    /**
     * 查询指定产品下指定数据类型的分组信息
     */
    List<GroupInfoVO> listAll(GroupQueryParam param);

    /**
     * 新增分组
     */
    boolean insert(GroupUpsertParam param);

    /**
     * 删除分组信息
     */
    boolean delete(long groupId);

    /**
     * 修改分组信息
     */
    boolean update(GroupUpsertParam groupUpsertParam);

    /**
     * 查询分组下的人员信息
     */
    List<Long> groupMembers(long groupId);

    /**
     * 添加分组人员
     */
    boolean addMember(long groupId, long userId);

    /**
     * 删除分组人员
     */
    boolean deleteMember(long groupId, long userId);

    /**
     * 分组人员分页
     */
    Pagination<GroupUserPageVO> pageGroup(String projectId, GroupPageParam param);

    /**
     * 分组角色ids
     */
    List<UserDTO.Role> roles(Long groupId);

    /**
     * 单个分组更新
     */
    boolean singleUpdate(GroupUpsertParam.GroupParam groupParam);
}

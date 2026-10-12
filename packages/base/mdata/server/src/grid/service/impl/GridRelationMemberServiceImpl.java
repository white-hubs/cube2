package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.grid.dao.GridRelationMemberMapper;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridRelationMember;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridRelationMemberService;
import org.springframework.stereotype.Service;

/**
 * 网格关联成员表 Service 实现
 */
@Service
public class GridRelationMemberServiceImpl extends ServiceImpl<GridRelationMemberMapper, GridRelationMember> implements GridRelationMemberService {
}


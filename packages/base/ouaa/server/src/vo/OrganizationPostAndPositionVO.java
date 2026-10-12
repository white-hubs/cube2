package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 部门关联的岗位和职位信息
 * @date 2023/12/13 14:12
 */
@Data
public class OrganizationPostAndPositionVO {
    /**
     * 岗位列表
     */
    private List<Position> positionList;
    /**
     * 职位列表
     */
    private List<AppointmentDetailVO> postList;
}

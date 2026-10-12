package com.guhecloud.cube.srv.gen.attendance.management.server.stat.querygateway;

import com.guhecloud.platform.base.util.JsonUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.dao.AttendanceStatMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.vo.po.StaffAttendanceStatPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.vo.req.AttendanceStatParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工统计
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceStatsQueryServiceImpl {
    private final AttendanceStatMapper attendanceStatMapper;

    /**
     * 获取统计集合(不分页
     *
     * @param param 条件对象
     *
     * @return 分页集合
     */
    public List<ArrangeWorkVO> getStaffStatList(AttendanceStatParam param) {
        List<StaffAttendanceStatPO> poList = attendanceStatMapper.selectList(Wrappers.<StaffAttendanceStatPO>lambdaQuery()
                                                                                     .eq(StaffAttendanceStatPO::getMonth, param.getMonth())
                                                                                     .like(StringUtils.isNoneBlank(param.getKeyword()), StaffAttendanceStatPO::getStaffName, param.getKeyword())
                                                                                     .eq(param.getOrganizationId() !=
                                                                                         null, StaffAttendanceStatPO::getStaffOrganizationId, param.getOrganizationId())
                                                                                     .eq(param.getGroupId() !=
                                                                                         null, StaffAttendanceStatPO::getGroupId, param.getGroupId())
                                                                            );
        return this.staffAttendanceStatListToWorkVo(poList);
    }

    /**
     * 获取统计列表(分页
     *
     * @param param 条件对象
     *
     * @return 分页集合
     */
    public Pagination<ArrangeWorkVO> getStaffStatPage(AttendanceStatParam param) {
        Page<StaffAttendanceStatPO> page = attendanceStatMapper.selectPage(new Page<>(param.getPageNumber(), param.getPageSize()), Wrappers
                                                                                   .<StaffAttendanceStatPO>lambdaQuery()
                                                                                   .eq(StaffAttendanceStatPO::getMonth, param.getMonth())
                                                                                   .like(StringUtils.isNoneBlank(param.getKeyword()), StaffAttendanceStatPO::getStaffName, param.getKeyword())
                                                                                   .eq(param.getOrganizationId() != null, StaffAttendanceStatPO::getStaffOrganizationId, param.getOrganizationId())
                                                                                   .eq(param.getGroupId() != null, StaffAttendanceStatPO::getGroupId, param.getGroupId())
                                                                          );
        List<StaffAttendanceStatPO> poList = page.getRecords();
        List<ArrangeWorkVO> voList = this.staffAttendanceStatListToWorkVo(poList);
        return new Pagination<>(page.getTotal(), page.getPages(), voList);
    }

    /**
     * 数据库对象转使用对象
     */
    private List<ArrangeWorkVO> staffAttendanceStatListToWorkVo(List<StaffAttendanceStatPO> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        return poList.stream().map(po -> JsonUtils.deserialize(po.getStatContent(), ArrangeWorkVO.class)).collect(Collectors.toList());
    }

}

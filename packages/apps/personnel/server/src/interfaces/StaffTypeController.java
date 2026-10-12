package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces;

import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffTypeCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.StaffTypeParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffTypeVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffTypePO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/5 19:05
 * @Description: 员工类型Api
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/staff-type")
public class StaffTypeController {
    private final StaffTypeCommandService staffTypeCommandService;
    private final StaffQueryService staffQueryService;

    /**
     * 新增类型
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody StaffTypeVO projectTypeVO) {
        return staffTypeCommandService.saveOrUpdateStaffType(projectTypeVO);
    }

    /**
     * 删除类型
     */
    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Long id) {
        StaffTypePO staffTypePO = staffTypeCommandService.getById(id);
        Assert.isNotFound(Objects.isNull(staffTypePO), "员工类型不存在");
        long integer = staffQueryService.lambdaQuery().eq(StaffPO::getType, staffTypePO.getCode())
                .select(StaffPO::getId)
                .count();
        Assert.isNotAcceptable(integer != 0, "该员工类型下存在用户不能删除");
        DatabaseContextHolder.softDelete(StaffTypePO.class, id);
    }

    /**
     * 获取类型列表（分页）
     */
    @GetMapping("/page")
    public Pagination<StaffTypeVO> getStaffTypePage(StaffTypeParam param) {
        return staffTypeCommandService.getStaffTypePage(param);
    }

    /**
     * 获取类型列表
     */
    @GetMapping("/list")
    public List<StaffTypeVO> getStaffTypeList(StaffTypeParam param) {
        List<StaffTypePO> projectTypePOS = staffTypeCommandService.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), StaffTypePO::getName, param.getName())
                .list();
        return ObjectUtils.copying(projectTypePOS, StaffTypeVO.class);
    }
}

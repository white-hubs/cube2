package com.guhecloud.cube.srv.gen.personnel.management.client;

import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffNotInParam;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.ChangeWorkingStatusCommand;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(ServiceRoute.TAG)
public interface StaffClient {

    /**
     * 根据员工id集合查询员工信息
     * 考勤使用字段不全
     *
     * @param ids id集合
     *
     * @return 员工列表
     */
    @PostMapping("/staff/getStaffByIds")
    List<StaffDetailDTO> getStaffByIds(@RequestBody List<Long> ids);

    @GetMapping("/staff/getStaffVOById")
    StaffDetailDTO getStaffVOById(@RequestParam("staffId") Long staffId);

    @PostMapping("/staff/getStaffPageNotIn")
    Pagination<StaffDetailDTO> getStaffPageNotIn(@RequestBody StaffNotInParam param);

    @GetMapping("/staff/{id}")
    StaffDetailDTO getById(@PathVariable("id") Long id);

    @GetMapping("/staff/user/{userId}")
    StaffDetailDTO getByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/staff/list")
    List<StaffDetailDTO> getList(@SpringQueryMap StaffQuery staffQuery);

    @GetMapping("/staff/filter/working-year/list")
    Set<Long> getListWithWorkingYear(@RequestParam("workingYear") Integer workingYear);

    @GetMapping("/staff/filter/time-of-induction/list")
    Set<Long> getListTimeOfInduction(@RequestParam("timeOfInduction") Integer timeOfInduction);

    @GetMapping("/staff/page")
    Pagination<StaffDetailDTO> getPage(@SpringQueryMap StaffQuery staffQuery);

    @GetMapping("/staff/num")
    Integer getNum();

    @PutMapping("/staff/status/working/change")
    void changeWorkingStatus(@RequestBody List<ChangeWorkingStatusCommand> changeWorkingStatusCommands);

    @GetMapping("/staff/mobile")
    StaffDetailDTO getByMobile(@RequestParam("mobile") String mobile);

}

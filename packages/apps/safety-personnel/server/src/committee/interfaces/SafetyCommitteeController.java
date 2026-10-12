package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param.CommitteeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param.CommitteeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.SafetyCommitteeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeChangeLogVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeDetailVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 安委会管理接口。
 */
@RestController("safetyPersonnelSafetyCommitteeController")
@RequestMapping
@RequiredArgsConstructor
public class SafetyCommitteeController {

    private final SafetyCommitteeService safetyCommitteeService;

    /**
     * 分页查询安委会。
     *
     * @param param 查询参数
     *
     * @return 安委会分页。
     */
    @GetMapping("/committees")
    public Pagination<CommitteeVO> page(CommitteeQueryParam param) {
        return safetyCommitteeService.pageCommittees(param);
    }

    /**
     * 查询安委会详情。
     *
     * @param id 安委会ID
     *
     * @return 安委会详情。
     */
    @GetMapping("/committees/{id}")
    public CommitteeDetailVO detail(@PathVariable("id") Long id) {
        return safetyCommitteeService.detail(id);
    }

    /**
     * 新增安委会。
     *
     * @param param 新增参数
     *
     * @return 安委会详情。
     */
    @PostMapping("/committees")
    public CommitteeDetailVO create(@RequestBody @Valid CommitteeModifyParam param) {
        return safetyCommitteeService.create(param);
    }

    /**
     * 编辑安委会。
     *
     * @param id    安委会ID
     * @param param 编辑参数
     *
     * @return 安委会详情。
     */
    @PutMapping("/committees/{id}")
    public CommitteeDetailVO update(@PathVariable("id") Long id, @RequestBody @Valid CommitteeModifyParam param) {
        return safetyCommitteeService.updateCommittee(id, param);
    }

    /**
     * 逻辑删除安委会。
     *
     * @param id 安委会ID
     */
    @DeleteMapping("/committees/{id}")
    public void delete(@PathVariable("id") Long id) {
        safetyCommitteeService.deleteCommittee(id);
    }

    /**
     * 查询安委会变更记录。
     *
     * @param id 安委会ID
     *
     * @return 变更记录列表。
     */
    @GetMapping("/committees/{id}/change-logs")
    public List<CommitteeChangeLogVO> changeLogs(@PathVariable("id") Long id) {
        return safetyCommitteeService.changeLogs(id);
    }

}

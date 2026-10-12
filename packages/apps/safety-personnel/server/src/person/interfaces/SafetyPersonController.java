package com.guhecloud.cube.srv.gen.safety.personnel.server.person.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonLinkParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonPoolQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.PersonQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.ResumeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonResumeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonPoolUserVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonProfileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.PersonVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeDayGroupVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeItemVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 安全生产统一智管平台 — 安全人员台账接口。
 * <p>
 * 与通用「人员管理」模块区分：本 Controller 仅承载本应用 {@code /persons} 路径下的台账、履历能力。
 */
@RestController("safetyPersonnelSafetyPersonController")
@RequestMapping
@RequiredArgsConstructor
public class SafetyPersonController {

    private final PersonService personService;
    private final PersonResumeService personResumeService;

    /**
     * 分页查询安全人员台账。
     *
     * @param param 查询参数
     *
     * @return 安全人员分页
     */
    @GetMapping("/persons")
    public Pagination<PersonVO> page(PersonQueryParam param) {
        return personService.pagePersons(param);
    }

    /**
     * 导出安全人员台账 Excel，筛选条件与分页列表一致。
     *
     * @param param    查询参数，含 typeCode、keyword、status
     * @param response HTTP响应
     */
    @PostMapping("/persons/export")
    public void export(@RequestBody PersonQueryParam param, HttpServletResponse response) throws IOException {
        personService.exportPersons(param, response);
    }

    /**
     * 查询可关联人员池。
     *
     * @param param 查询参数
     *
     * @return 可关联 OUAA 用户分页
     */
    @GetMapping("/persons/pool")
    public Pagination<PersonPoolUserVO> pool(PersonPoolQueryParam param) {
        return personService.pagePool(param);
    }

    /**
     * 批量关联安全人员。
     *
     * @param param 关联参数
     */
    @PostMapping("/persons/link")
    public void link(@RequestBody @Valid PersonLinkParam param) {
        personService.link(param);
    }

    /**
     * 逻辑移除安全人员。
     *
     * @param id 安全人员 ID
     */
    @DeleteMapping("/persons/{id}")
    public void remove(@PathVariable("id") Long id) {
        personService.removePerson(id);
    }

    /**
     * 查询人员抽屉基础信息。
     *
     * @param id 安全人员 ID
     *
     * @return 人员基础信息
     */
    @GetMapping("/persons/{id}/profile")
    public PersonProfileVO profile(@PathVariable("id") Long id) {
        return personService.profile(id);
    }

    /**
     * 查询人员持有证书列表。
     *
     * @param id 安全人员 ID
     *
     * @return 人员证书列表
     */
    @GetMapping("/persons/{id}/certificates")
    public List<EmployeeCertVO> certificates(@PathVariable("id") Long id) {
        return personService.certificates(id);
    }

    /**
     * 查询履职记录时间线，按日期分组。
     *
     * @param id    安全人员 ID
     * @param param 日期范围参数
     *
     * @return 日期分组履职记录
     */
    @GetMapping("/persons/{id}/resume/timeline")
    public List<ResumeDayGroupVO> resumeTimeline(@PathVariable("id") Long id, ResumeQueryParam param) {
        return personResumeService.timeline(personService.profile(id).getUserId(), param);
    }

    /**
     * 查询指定类型履职明细。
     *
     * @param id    安全人员 ID
     * @param type  履职类型，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.ResumeRecordTypeEnum}
     * @param param 日期范围参数
     *
     * @return 履职明细列表
     */
    @GetMapping("/persons/{id}/resume/{type}")
    public List<ResumeItemVO> resumeByType(@PathVariable("id") Long id, @PathVariable("type") String type, ResumeQueryParam param) {
        return personResumeService.listByType(personService.profile(id).getUserId(), type, param);
    }

    /**
     * 导出人员履职记录，扁平化为时间与任务名称。
     *
     * @param id       安全人员 ID
     * @param param    日期范围及可选履职类型
     * @param response HTTP响应
     */
    @PostMapping("/persons/{id}/resume/export")
    public void exportResume(@PathVariable("id") Long id, @RequestBody ResumeQueryParam param, HttpServletResponse response)
            throws IOException {
        PersonProfileVO profile = personService.profile(id);
        personResumeService.export(profile.getUserId(), profile.getUserName(), param, response);
    }
}

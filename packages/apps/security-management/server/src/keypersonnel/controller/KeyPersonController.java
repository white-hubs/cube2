package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.KeyPersonReportService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.KeyPersonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.AreaOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonAreaCountVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonReportVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.SupervisorOptionVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/key-personnel")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyPersonController {
    private final KeyPersonService keyPersonService;
    private final KeyPersonReportService keyPersonReportService;

    /**
     * 重点人员分页查询。
     *
     * @param param 查询条件，支持 keyword、riskLevel、status、areaSequence 和分页参数
     * @return 重点人员分页结果
     */
    @GetMapping("/persons")
    public Pagination<KeyPersonPageVO> getPersons(KeyPersonPageParam param) {
        return keyPersonService.getPage(param);
    }

    /**
     * 重点人员下拉选项查询。
     *
     * @param keyword 姓名或手机号关键字，可为空
     * @return 在管重点人员选项列表
     */
    @GetMapping("/persons/options")
    public List<KeyPersonOptionVO> getPersonOptions(@RequestParam(required = false) String keyword) {
        return keyPersonService.listOptions(keyword);
    }

    /**
     * 获取重点人员详情。
     *
     * @param id 重点人员主键
     * @return 重点人员档案详情，包含附件信息和监管上报记录
     */
    @GetMapping("/persons/{id}")
    public KeyPersonDetailVO getPersonDetail(@PathVariable Long id) {
        return keyPersonService.getDetail(id);
    }

    /**
     * 新增重点人员。
     *
     * @param param 重点人员保存参数
     * @return 新增重点人员主键
     */
    @PostMapping("/persons")
    public Long createPerson(@Valid @RequestBody KeyPersonSaveParam param) {
        return keyPersonService.create(param);
    }

    /**
     * 编辑重点人员。
     *
     * @param id    重点人员主键
     * @param param 重点人员保存参数
     */
    @PutMapping("/persons/{id}")
    public void updatePerson(@PathVariable Long id, @Valid @RequestBody KeyPersonSaveParam param) {
        keyPersonService.update(id, param);
    }

    /**
     * 删除重点人员。
     *
     * <p>删除为软删除，与撤管状态流转相互独立。</p>
     *
     * @param id 重点人员主键
     */
    @DeleteMapping("/persons/{id}")
    public void deletePerson(@PathVariable Long id) {
        keyPersonService.delete(id);
    }

    /**
     * 撤管重点人员。
     *
     * <p>撤管只将状态变更为 INACTIVE，不删除人员档案。</p>
     *
     * @param id 重点人员主键
     */
    @PutMapping("/persons/{id}/withdraw")
    public void withdrawPerson(@PathVariable Long id) {
        keyPersonService.withdraw(id);
    }

    /**
     * 恢复重点人员在管状态。
     *
     * @param id 重点人员主键
     */
    @PutMapping("/persons/{id}/activate")
    public void activatePerson(@PathVariable Long id) {
        keyPersonService.activate(id);
    }

    /**
     * 查询重点人员监管上报记录。
     *
     * @param personId 重点人员主键
     * @param param    分页参数，支持 {@code period} 按上报时间范围筛选
     * @return 监管上报记录分页结果，按上报时间倒序返回
     */
    @GetMapping("/persons/{personId}/reports")
    public Pagination<KeyPersonReportVO> getReports(@PathVariable Long personId, KeyPersonReportPageParam param) {
        return keyPersonReportService.getReportPage(personId, param);
    }

    /**
     * 新增重点人员监管上报。
     *
     * @param personId 重点人员主键
     * @param param    监管上报保存参数
     * @return 新增监管上报记录主键
     */
    @PostMapping("/persons/{personId}/reports")
    public Long createReport(@PathVariable Long personId, @Valid @RequestBody KeyPersonReportSaveParam param) {
        return keyPersonReportService.create(personId, param);
    }

    /**
     * 获取监管上报详情。
     *
     * @param id 监管上报记录主键
     * @return 监管上报详情
     */
    @GetMapping("/reports/{id}")
    public KeyPersonReportVO getReport(@PathVariable Long id) {
        return keyPersonReportService.getReport(id);
    }

    /**
     * 查询 mdata 区域选项。
     *
     * @param keyword 区域关键字，可为空
     * @return 区域树或区域列表选项
     */
    @GetMapping("/areas/options")
    public List<AreaOptionVO> getAreaOptions(@RequestParam(required = false) String keyword) {
        return keyPersonService.listAreaOptions(keyword);
    }

    /**
     * 查询监管人员选项。
     *
     * <p>监管人员来源为 ouaa 当前租户用户列表，不按角色过滤。</p>
     *
     * @param keyword 用户关键字，可为空
     * @return 监管人员选项列表
     */
    @GetMapping("/supervisors/options")
    public List<SupervisorOptionVO> getSupervisorOptions(@RequestParam(required = false) String keyword) {
        return keyPersonService.listSupervisorOptions(keyword);
    }

    /**
     * 查询重点人员统计看板数据。
     *
     * @return 重点人员总数、风险等级统计、区域监管人员统计和区域高风险统计
     */
    @GetMapping("/statistics/dashboard")
    public KeyPersonStatisticsVO getStatistics() {
        return keyPersonService.getStatistics();
    }

    /**
     * 查询重点人员概览统计。
     *
     * @return 重点人员总数及高、中、低风险人数
     */
    @GetMapping("/statistics/person-summary")
    public KeyPersonStatisticsVO getPersonSummary() {
        return keyPersonService.getStatistics();
    }

    /**
     * 查询各区域监管人员去重数量统计。
     *
     * @return 区域监管人员数量 TOP 列表
     */
    @GetMapping("/statistics/area-supervisors")
    public List<KeyPersonAreaCountVO> getAreaSupervisorStatistics() {
        return keyPersonService.getStatistics().getAreaSupervisorCounts();
    }

    /**
     * 查询各区域高风险重点人员数量统计。
     *
     * @return 区域高风险人员数量 TOP 列表
     */
    @GetMapping("/statistics/area-high-risk")
    public List<KeyPersonAreaCountVO> getAreaHighRiskStatistics() {
        return keyPersonService.getStatistics().getAreaHighRiskCounts();
    }
}

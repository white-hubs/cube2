package com.guhecloud.cube.srv.gen.safety.personnel.server.org.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgNodeMemberSaveParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgNodeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgUserCandidateQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.OrgNodeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeDetailVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeTreeVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgUserCandidateVO;
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
 * 安全管理组织配置接口。
 */
@RestController("safetyPersonnelOrgNodeController")
@RequestMapping
@RequiredArgsConstructor
public class OrgNodeController {

    private final OrgNodeService orgNodeService;

    /**
     * 查询组织配置树。
     *
     * @return 组织配置树；多根时返回所有根节点。
     */
    @GetMapping("/org/tree")
    public List<OrgNodeTreeVO> tree() {
        return orgNodeService.tree();
    }

    /**
     * 查询组织节点详情。
     *
     * @param id 组织节点ID
     *
     * @return 组织节点详情，包含全部成员。
     */
    @GetMapping("/org/nodes/{id}")
    public OrgNodeDetailVO detail(@PathVariable("id") Long id) {
        return orgNodeService.detail(id);
    }

    /**
     * 新增根组织节点。
     *
     * @param param 节点新增参数
     *
     * @return 新增后的节点详情。
     */
    @PostMapping("/org/nodes")
    public OrgNodeDetailVO createRoot(@RequestBody @Valid OrgNodeModifyParam param) {
        return orgNodeService.createRoot(param);
    }

    /**
     * 新增子组织节点。
     *
     * @param id    父级组织节点ID
     * @param param 节点新增参数
     *
     * @return 新增后的节点详情。
     */
    @PostMapping("/org/nodes/{id}/children")
    public OrgNodeDetailVO createChild(@PathVariable("id") Long id, @RequestBody @Valid OrgNodeModifyParam param) {
        return orgNodeService.createChild(id, param);
    }

    /**
     * 编辑组织节点。
     *
     * @param id    组织节点ID
     * @param param 节点编辑参数
     *
     * @return 编辑后的节点详情。
     */
    @PutMapping("/org/nodes/{id}")
    public OrgNodeDetailVO updateNode(@PathVariable("id") Long id, @RequestBody @Valid OrgNodeModifyParam param) {
        return orgNodeService.updateNode(id, param);
    }

    /**
     * 逻辑删除组织节点；存在子节点时拒绝删除，叶子节点删除时自动解绑成员。
     *
     * @param id 组织节点ID
     */
    @DeleteMapping("/org/nodes/{id}")
    public void deleteNode(@PathVariable("id") Long id) {
        orgNodeService.deleteNode(id);
    }

    /**
     * 保存组织节点成员，按传入用户ID列表覆盖当前节点成员。
     *
     * @param id    组织节点ID
     * @param param 成员保存参数
     *
     * @return 保存后的节点详情。
     */
    @PutMapping("/org/nodes/{id}/members")
    public OrgNodeDetailVO saveMembers(@PathVariable("id") Long id, @RequestBody @Valid OrgNodeMemberSaveParam param) {
        return orgNodeService.saveMembers(id, param);
    }

    /**
     * 查询组织配置选人候选用户。
     *
     * @param param 查询参数
     *
     * @return OUAA用户候选分页。
     */
    @GetMapping("/org/user-candidates")
    public Pagination<OrgUserCandidateVO> userCandidates(@Valid OrgUserCandidateQueryParam param) {
        return orgNodeService.pageUserCandidates(param);
    }
}

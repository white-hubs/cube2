package com.guhecloud.cube.srv.gen.safety.personnel.server.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNode;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgNodeMemberSaveParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgNodeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgUserCandidateQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.HomeOrgArchitectureVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeDetailVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeTreeVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgUserCandidateVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 安全管理组织展示节点服务。
 */
public interface OrgNodeService extends IService<OrgNode> {

    /**
     * 查询组织配置树。
     *
     * @return 组织配置树列表；多根时返回所有根节点。
     */
    List<OrgNodeTreeVO> tree();

    /**
     * 查询首页安全组织架构。
     *
     * @return 首页安全组织架构；无配置时根节点为空。
     */
    HomeOrgArchitectureVO homeArchitecture();

    /**
     * 查询组织节点详情。
     *
     * @param id 组织节点ID
     *
     * @return 节点详情
     */
    OrgNodeDetailVO detail(Long id);

    /**
     * 新增根节点。
     *
     * @param param 节点保存参数
     *
     * @return 新增后的节点详情
     */
    OrgNodeDetailVO createRoot(OrgNodeModifyParam param);

    /**
     * 新增子节点。
     *
     * @param parentId 父级节点ID
     * @param param    节点保存参数
     *
     * @return 新增后的节点详情
     */
    OrgNodeDetailVO createChild(Long parentId, OrgNodeModifyParam param);

    /**
     * 编辑组织节点。
     *
     * @param id    组织节点ID
     * @param param 节点保存参数
     *
     * @return 编辑后的节点详情
     */
    OrgNodeDetailVO updateNode(Long id, OrgNodeModifyParam param);

    /**
     * 逻辑删除组织节点。
     *
     * @param id 组织节点ID
     */
    void deleteNode(Long id);

    /**
     * 保存组织节点成员，按传入用户ID列表覆盖当前节点成员。
     *
     * @param id    组织节点ID
     * @param param 成员保存参数
     *
     * @return 节点详情
     */
    OrgNodeDetailVO saveMembers(Long id, OrgNodeMemberSaveParam param);

    /**
     * 查询OUAA用户候选分页。
     *
     * @param param 查询参数
     *
     * @return 候选用户分页
     */
    Pagination<OrgUserCandidateVO> pageUserCandidates(OrgUserCandidateQueryParam param);
}

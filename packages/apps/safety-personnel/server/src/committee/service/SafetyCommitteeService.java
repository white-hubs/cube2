package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.SafetyCommittee;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param.CommitteeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param.CommitteeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeChangeLogVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeDetailVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 安委会服务。
 */
public interface SafetyCommitteeService extends IService<SafetyCommittee> {

    /**
     * 分页查询安委会。
     *
     * @param param 查询参数
     *
     * @return 安委会分页。
     */
    Pagination<CommitteeVO> pageCommittees(CommitteeQueryParam param);

    /**
     * 查询安委会详情。
     *
     * @param id 安委会ID
     *
     * @return 安委会详情。
     */
    CommitteeDetailVO detail(Long id);

    /**
     * 新增安委会。
     *
     * @param param 保存参数
     *
     * @return 安委会详情。
     */
    CommitteeDetailVO create(CommitteeModifyParam param);

    /**
     * 编辑安委会。
     *
     * @param id    安委会ID
     * @param param 保存参数
     *
     * @return 安委会详情。
     */
    CommitteeDetailVO updateCommittee(Long id, CommitteeModifyParam param);

    /**
     * 逻辑删除安委会。
     *
     * @param id 安委会ID
     */
    void deleteCommittee(Long id);

    /**
     * 查询安委会变更记录。
     *
     * @param id 安委会ID
     *
     * @return 变更记录列表。
     */
    List<CommitteeChangeLogVO> changeLogs(Long id);

}

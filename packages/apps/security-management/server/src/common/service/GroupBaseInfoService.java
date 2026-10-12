package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseInfo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.GroupControlQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.PasswordCheckParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface GroupBaseInfoService extends IService<GroupBaseInfo> {
    void addGroupControl(GroupControlParam groupControlParam);

    void updateGroupControl(Long id, GroupControlParam groupControlParam);

    void deleteGroupControl(Long id);

    Pagination<GroupBaseInfoVo> getGroupControlsPage(GroupControlQueryParam param);

    GroupBaseInfoVo getGroupControlsInfo(Long id);

    List<GroupBaseVo> getGroupControlsList(GroupControlQueryParam param);

    Boolean groupControlPasswordCheck(PasswordCheckParam param);

}

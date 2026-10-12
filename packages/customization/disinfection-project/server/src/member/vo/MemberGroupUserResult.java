package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupUserResult extends MemberUserResult {
    /**
     * 用户头像
     */
    private FileVO fileVO;
}

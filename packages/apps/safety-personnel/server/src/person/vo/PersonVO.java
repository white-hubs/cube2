package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 安全人员分页响应。
 */
@Data
public class PersonVO {

    /**
     * 安全人员ID。
     */
    private Long id;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名。
     */
    private String userName;

    /**
     * 手机号。
     */
    private String phone;

    /**
     * 公司名称。
     */
    private String companyName;

    /**
     * 部门名称。
     */
    private String deptName;

    /**
     * 岗位名称。
     */
    private String postName;

    /**
     * 人员状态，取值见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum}：ACTIVE/DISABLED/LEFT。
     */
    private String status;

    /**
     * 人员类型编码列表，取值见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonTypeCodeEnum}：SAFETY/SPECIAL/EMERGENCY。
     */
    private List<String> typeCodes;

    /**
     * 纳入安全人员库时间。
     */
    private LocalDateTime linkedAt;

    public static PersonVO of(Person person, List<String> typeCodes) {
        PersonVO vo = new PersonVO();
        vo.setId(person.getId());
        vo.setUserId(person.getUserId());
        vo.setUserName(person.getUserName());
        vo.setPhone(person.getPhone());
        vo.setCompanyName(person.getCompanyName());
        vo.setDeptName(person.getDeptName());
        vo.setPostName(person.getPostName());
        vo.setStatus(person.getStatus());
        vo.setTypeCodes(typeCodes);
        vo.setLinkedAt(person.getLinkedAt());
        return vo;
    }
}

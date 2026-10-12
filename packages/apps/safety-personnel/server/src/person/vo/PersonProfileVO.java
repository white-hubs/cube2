package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import lombok.Data;

import java.util.List;

/**
 * 安全人员基础信息响应。
 */
@Data
public class PersonProfileVO {

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
     * 人员状态，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.PersonStatusEnum}。
     */
    private String status;

    /**
     * 人员类型编码列表。
     */
    private List<String> typeCodes;

    public static PersonProfileVO of(Person person, List<String> typeCodes) {
        PersonProfileVO vo = new PersonProfileVO();
        vo.setId(person.getId());
        vo.setUserId(person.getUserId());
        vo.setUserName(person.getUserName());
        vo.setPhone(person.getPhone());
        vo.setCompanyName(person.getCompanyName());
        vo.setDeptName(person.getDeptName());
        vo.setPostName(person.getPostName());
        vo.setStatus(person.getStatus());
        vo.setTypeCodes(typeCodes);
        return vo;
    }
}

package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/23 9:30
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisinfectionUserInfoVO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 小组ID
     */
    private Long groupId;

    /**
     * 消杀小组
     */
    private String groupName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 照片
     */
    private List<FileVO> photo;

    /**
     * 所属部门
     */
    private String department;

    /**
     * 岗位
     */
    private String position;

    /**
     * 角色
     */
    private String role;
}

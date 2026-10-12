package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 培训人员
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskUserVO {

    //
    private Long id;
    //培训任务主键
    private Long cultivateTaskId;
    //用户主键
    private Long userId;
    //用户姓名
    private String userName;
    //电话号
    private String userPhone;
    //部门主键
    private Long userDepartmentId;
    //部门名称
    private String userDepartmentName;
    //岗位
    private Long userPostId;
    //岗位名称
    private String userPostName;
    //商户ID
    private Long merchantId;
    //商户名称
    private String merchantName;
    //签到状态
    private String userSignStatus;
    //签到时间
    private LocalDateTime userSignTime;
    //是否迟到
    private Boolean belated;
    //备注
    private String remark;

}
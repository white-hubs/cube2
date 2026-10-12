package com.guhecloud.cube.srv.gen.reportmanagement.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 工作安排
 * @date 2024-01-04 10:08:36
 */
@Data
public class WorkPlanVO {

    //
    private Long id;
    //标题
    private String title;
    //类型
    private Long type;
    private String typeName;
    //发布人ID
    private Long publisherId;
    //发布人
    private String publisherName;
    //发布时间
    private LocalDateTime publishTime;
    //联系电话
    private String phone;
    //发布部门ID
    private Long deptId;
    //发布部门名称
    private String deptName;
    //发布内容
    private String content;
    //附件IDS
    private String fileIds;
    private List<FileVO> fileList;
    //状态
    private String status;

}
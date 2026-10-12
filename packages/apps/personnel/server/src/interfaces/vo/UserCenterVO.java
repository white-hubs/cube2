package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/6 17:10
 * @Description:
 */
@Data
@Builder
public class UserCenterVO {

    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 类别
     */
    private String type;

    /**
     * 类别名称
     */
    private String typeName;

    /**
     * 照片
     */
    private List<FileVO> fileVO;
}

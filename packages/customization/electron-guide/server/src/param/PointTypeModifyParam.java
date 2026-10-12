package com.guhecloud.cube.srv.electron.guide.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 点位分类
 *
 * @author lanchun
 * @date 2024/9/3 11:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointTypeModifyParam {

    /**
     * 分类编号
     */
    @Size(max = 100, message = "编号不能大于100")
    private String code;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能大于100")
    @Size(max = 100, message = "分类名称不能大于100")
    private String name;

    /**
     * 点位附件
     */
    @Size(max = 500, message = "点位附件不能大于500")
    private String filePath;
}

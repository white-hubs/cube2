package com.guhecloud.cube.biz.ouaa.client.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 用户查询参数
 *
 * @author Woody
 * @date 2021/7/20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 用户主键列表
     */
    private List<Long> ids;

    /**
     * 过滤的用户主键集合
     */
    private List<Long> filterUserIds;

    /**
     * 用户姓名
     */
    @Size(max = 100, message = "用户姓名长度不能大于100")
    private String name;

    /**
     * 入职日期
     */
    private Period entryDate;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 组织机构主键
     */
    @Min(value = 1, message = "组织机构主键不能小于1")
    private Long organizationId;
    /**
     * 创建时间
     */
    private Period createTime;
    /**
     * 来源
     */
    private String source;
}

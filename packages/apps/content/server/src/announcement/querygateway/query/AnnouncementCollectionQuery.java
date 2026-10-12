package com.guhecloud.cube.srv.gen.content.management.server.announcement.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2022/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnnouncementCollectionQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 公告类型
     */
    private Long type;

    /**
     * 公告级别
     */
    private String level;

    /**
     * 时间
     */
    private Period period;

    /**
     * 是否隐藏
     * HIDE("已隐藏")
     * DISPLAY("已显示")
     */

    private String status;
}

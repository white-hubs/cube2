package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础参数
 *
 * @Author: zengyadong@guhecloud.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConferenceBaseQueryParam extends Paging {
    private String name;
    private Boolean status;
    private String keyword;
}

package com.guhecloud.cube.biz.fusion.communicate.server.third.base.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountQueryParam extends Paging {

    private static final long serialVersionUID = 1L;
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}

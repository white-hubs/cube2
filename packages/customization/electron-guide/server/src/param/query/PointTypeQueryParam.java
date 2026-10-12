package com.guhecloud.cube.srv.electron.guide.param.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointTypeQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}

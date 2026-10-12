package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatedWaterTypeTrendStatVO {
    //类型名称
    private String waterTypeName;
    //类型趋势数据
    private List<TreatedWaterStatVO> list;
}

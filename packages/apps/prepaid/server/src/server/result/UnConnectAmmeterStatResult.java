package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.util.List;

/**
 * 失联电表统计返回对象
 */
@Data
public class UnConnectAmmeterStatResult {
    // 总数
    private int total;
    // 欠费数
    private int oweMoney;
    // 未欠费数
    private int unOweMoney;
    // 失联数量
    private int unConnect;
    // 正常数量
    private int connect;
    // 失联电表集合
    private List<AmmeterPageResult> unConnectList;
}

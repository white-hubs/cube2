package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

/**
 * 门禁气体联动配置分页查询参数。
 *
 * @param pageNumber 页码，从 1 开始
 * @param pageSize 每页条数
 * @param areaId 所属区域 ID
 * @param gasLinkageEnabled 是否开启气体联动
 */
public record DoorGasLinkageConfigPageRequest(
        Integer pageNumber,
        Integer pageSize,
        Long areaId,
        Boolean gasLinkageEnabled
) {
}

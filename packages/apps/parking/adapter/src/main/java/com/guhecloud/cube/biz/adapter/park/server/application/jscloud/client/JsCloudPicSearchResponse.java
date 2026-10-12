package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 图片查询-返回URL 响应
 * resultCode=0 成功，dataItems 为图片数组，每项含 image 字段(OSS链接)
 */
@Data
public class JsCloudPicSearchResponse {
    private Integer resultCode;
    private String message;
    /** 返回结果，格式为数组，每项含 image 字段 */
    private Object dataItems;

    /**
     * 解析 dataItems 为 List，取第一张图片的 image URL
     */
    public String getFirstImageUrl() {
        if (dataItems == null) {
            return null;
        }
        var list = JsonUtils.deserialize(JsonUtils.serialize(dataItems), new TypeReference<List<Map<String, Object>>>() {});
        if (list == null || list.isEmpty()) {
            return null;
        }
        var first = list.get(0);
        var image = first != null ? first.get("image") : null;
        return image != null ? image.toString() : null;
    }

    /**
     * 解析 dataItems 为 List，返回所有图片 URL（按 filePath 顺序对应）
     */
    public List<String> getAllImageUrls() {
        if (dataItems == null) {
            return List.of();
        }
        var list = JsonUtils.deserialize(JsonUtils.serialize(dataItems), new TypeReference<List<Map<String, Object>>>() {});
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream()
                .map(m -> m != null && m.containsKey("image") ? String.valueOf(m.get("image")) : null)
                .filter(url -> url != null && !url.isEmpty())
                .toList();
    }
}

package com.guhecloud.platform.base.model;

import com.guhecloud.platform.base.util.AssertUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询参数
 *
 * @author Woody
 * @date 2021/5/27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Paging implements Serializable {
    /**
     * 页码
     */
    @Builder.Default
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNumber = 1;

    /**
     * 页面大小
     */
    @Builder.Default
    @NotNull(message = "页面大小不能为空")
    @Min(value = 1, message = "页面大小不能小于1")
    @Max(value = 1000, message = "页面大小不能大于1000")
    private Integer pageSize = 10;

    /**
     * 获取分页列表
     *
     * @param objects    对象列表
     * @param pageNumber 页码
     * @param pageSize   页面大小
     * @param <T>        数据类型
     *
     * @return 分页后的对象列表
     */
    public static <T> List<T> list(List<T> objects, @NonNull Integer pageNumber, @NonNull Integer pageSize) {
        AssertUtils.assertTrue(pageNumber > 0, "pageNumber must be greater than 0");
        AssertUtils.assertTrue(pageSize > 0, "pageSize must be greater than 0");
        if (CollectionUtils.isEmpty(objects)) {
            return objects;
        }
        int index = (pageNumber - 1) * pageSize;
        return objects.subList(Math.min(index, objects.size()), Math.min(index + pageSize, objects.size()));
    }

    /**
     * 获取分页列表
     *
     * @param objects 对象列表
     * @param <T>     数据类型
     *
     * @return 分页后的对象列表
     */
    public <T> List<T> list(List<T> objects) {
        return list(objects, this.pageNumber, this.pageSize);
    }

}

package com.guhecloud.cube.deliver.project.merchant.commons.web;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRequest {

    private int pageNum = 1;
    private int pageSize = 15;
    private List<OrderItem> orderItems = new ArrayList<>();

    //    @ApiIgnore
    //    //    @JsonIgnore
    public <T> Page<T> page() {
        Page<T> page = new Page<T>(pageNum, pageSize);
        for (OrderItem orderItem : orderItems) {
            page.addOrder(orderItem);
        }
        return page;
    }

}

package com.guhecloud.cube.srv.prepaid.server.utils;

import com.guhecloud.platform.base.model.Pagination;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PageResponse<T> {

    protected List<T> records = Collections.emptyList();
    protected long total = 0;
    protected long pages = 0;

    public PageResponse() {
    }

    public static <T> Pagination<T> doPage(List<T> all, int pageNum, int pageSize) {
        List<T> pageRecords = getPageRecords(pageNum, pageSize, all);
        int pages = (int) Math.ceil((double) all.size() / pageSize);
        return new Pagination<>((long) all.size(), (long) pages, pageRecords);
    }

    public static <T> List<T> getPageRecords(int pageNum, int pageSize, List<T> all) {
        int fromIndex = 0;
        if (pageNum > (int) Math.ceil((double) all.size() / pageSize)) {
            return new ArrayList<>();
        }
        if (pageNum < 1) {
            return new ArrayList<>();
        }
        fromIndex = (pageNum - 1) * pageSize;
        int toIndex = fromIndex + pageSize;
        if (toIndex > all.size()) {
            toIndex = all.size();
        }
        return all.subList(fromIndex, toIndex);
    }

}

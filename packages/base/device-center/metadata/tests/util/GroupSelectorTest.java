package com.guhecloud.cube.base.device.center.metadata.util;

import com.google.common.collect.ContiguousSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class GroupSelectorTest {
    @Test
    void groupIntegerTest() {
        int enumSize = 83;
        List<Integer> list = ContiguousSet.closed(1, enumSize).asList();
        GroupSelector<Integer> groupSelector = new GroupSelector<>(2);

        groupSelector.group(list);
        Assertions.assertEquals(42, groupSelector.group(list).size());

    }
}

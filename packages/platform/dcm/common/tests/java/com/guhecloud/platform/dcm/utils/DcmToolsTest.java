package com.guhecloud.platform.dcm.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.ArrayMatching;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DcmToolsTest {

    @Test
    public void testEscape() {
        String parts[] = { "abc", "abc,def", "\"abc\"", "abc\",\"def", "ghi\"", "\"okr" };

        String concated = DcmTools.join(parts);
        System.out.println(concated);

        String resolvedParts[] = DcmTools.split(concated);
        System.out.println("============\n Splitted As:");
        for (String part : resolvedParts) {
            System.out.println(part);
        }
        MatcherAssert.assertThat(resolvedParts, ArrayMatching.arrayContaining(ArrayMatching.asEqualMatchers(parts)));
    }
}

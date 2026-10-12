package com.guhecloud.platform.dcm.matcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.core.IsEqual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestedConfigItemMatcher extends CustomTypeSafeMatcher<RequestedConfigItem> {
    private final RequestedConfigItem expected;
    private final Matcher kwargsMatcher;
    private final Matcher vargsMatcher;

    private final Matcher tenantMatcher;
    private static final YAMLMapper mapper = new YAMLMapper();

    public RequestedConfigItemMatcher(RequestedConfigItem expected) {
        super("RequestedConfigItem [" + expected + "]");
        this.expected = expected;
        this.kwargsMatcher = (expected.getKwargs() == null || expected.getKwargs().isEmpty()) ?
                             Matchers.anyOf(Matchers.anEmptyMap(), Matchers.nullValue())
                                                                                              : mapMatcher(expected.getKwargs());

        if ((expected.getVargs() == null || expected.getVargs().isEmpty())) {
            this.vargsMatcher = Matchers.anyOf(Matchers.empty(), Matchers.nullValue());
        } else {
            List<Matcher> matchers = new ArrayList();
            for (Object varg : expected.getVargs()) {
                matchers.add(mapMatcher((Map) varg));
            }
            Matcher<Map>[] matchersArray = new Matcher[matchers.size()];
            matchers.toArray(matchersArray);
            this.vargsMatcher = IsIterableContainingInAnyOrder.containsInAnyOrder(matchersArray);
            //             this.vargsMatcher = IsIterableContainingInAnyOrder.containsInAnyOrder(matchers); // JAVA 8 不能通过（基于集合的泛型不能正确处理）
        }
        this.tenantMatcher = (expected.getTenants() == null || expected.getTenants().isEmpty()) ?
                             Matchers.anyOf(Matchers.empty(), Matchers.nullValue())
                                                                                                : IsEqual.equalTo(expected.getTenants())
        ;

    }

    public static Matcher<RequestedConfigItem> isMatchedRequestConfigItem(RequestedConfigItem expected) {
        return new RequestedConfigItemMatcher(expected);
    }

    public static Matcher isRequestConfigItemList(
            List<RequestedConfigItem> expected
                                                 ) {
        return (expected == null || expected.isEmpty()) ? Matchers.anyOf(Matchers.empty(), Matchers.nullValue())
                                                        : IsIterableContainingInAnyOrder.containsInAnyOrder(
                                                                expected.stream()
                                                                        .map(RequestedConfigItemMatcher::isMatchedRequestConfigItem)
                                                                        .collect(Collectors.toList()));
    }

    @Override
    protected boolean matchesSafely(RequestedConfigItem item) {
        // 检查基本字段
        if (!Objects.equals(expected.getConfigType(), item.getConfigType())) {
            return false;
        }
        if (!Objects.equals(expected.getVersion(), item.getVersion())) {
            return false;
        }
        if (!Objects.equals(expected.getIsMandatory(), item.getIsMandatory())) {
            return false;
        }
        if (!Objects.equals(expected.getIsSystem(), item.getIsSystem())) {
            return false;
        }
        if (!Objects.equals(expected.getIsRemoval(), item.getIsRemoval())) {
            return false;
        }

        if (!tenantMatcher.matches(item.getTenants())) {
            return false;
        }
        if (!kwargsMatcher.matches(item.getKwargs())) {
            return false;
        }
        List<Object> vargs = item.getVargs();
        if (vargs != null) {
            List<Object> list = new ArrayList<>();
            for (Object varg : vargs) {
                String s = null;
                try {
                    s = mapper.writeValueAsString(varg);
                    list.add(mapper.readValue(s, Object.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            vargs = list;
        }
        return vargsMatcher.matches(vargs);
    }

    private <K, V> Matcher<Map<K, V>> mapMatcher(Map<K, V> expected) {
        return Matchers.allOf(expected.entrySet().stream()
                                      .map(e -> mapNotContainOrContainEmptyValue(e))
                                      .collect(Collectors.toList())
                             );
    }

    <K, V> Matcher<Map<? extends K, ? extends V>> mapNotContainOrContainEmptyValue(Map.Entry<K, V> entry) {
        if (entry.getValue() == null || "".equals(entry.getValue())) {
            return Matchers.anyOf(Matchers.hasKey(entry.getKey()), Matchers.not(Matchers.hasKey(entry.getKey())));
        } else {
            return Matchers.hasEntry(entry.getKey(), entry.getValue());
        }

    }
}

package com.guhecloud.cube.base.dictionary.dcm.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import com.guhecloud.platform.dcm.utils.DcmTools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DictTypeCollectionParam {
    public static final String CONFIG_TYPE_DICT_TYPE = "DictType";
    private Collection<DictTypeParam> dictTypeEntries;

    /**
     * @param requestedConfigItemParam
     *
     * @return
     */
    public static DictTypeCollectionParam parseFrom(RequestedConfigItemParam requestedConfigItemParam) {
        if (!CONFIG_TYPE_DICT_TYPE.equalsIgnoreCase(requestedConfigItemParam.getConfigType())) {
            return DictTypeCollectionParam.builder().dictTypeEntries(Collections.emptyList()).build();
        }
        return DictTypeCollectionParam.builder()
                .dictTypeEntries(DictTypeParam.parseCollectionFrom(requestedConfigItemParam))
                .build();
    }

    /**
     * 等价于业务逻辑中的DictType
     */

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DictTypeParam implements Identifiable {
        String code;
        String remark;

        /**
         * @param requestedConfigItemParam
         *
         * @return
         */
        public static Collection<DictTypeParam> parseCollectionFrom(RequestedConfigItemParam requestedConfigItemParam) {
            if (!CONFIG_TYPE_DICT_TYPE.equalsIgnoreCase(requestedConfigItemParam.getConfigType())) {
                return Collections.emptyList();
            }
            if (requestedConfigItemParam.getVargs() == null || CollectionUtils.isEmpty(requestedConfigItemParam.getVargs())) {
                return Collections.emptyList();
            }
            List<DictTypeParam> ret = new ArrayList<>(requestedConfigItemParam.getVargs().size());
            List<Object> vargs = requestedConfigItemParam.getVargs();
            for (Object varg : vargs) {
                if (varg instanceof DictTypeParam) {
                    ret.add((DictTypeParam) varg);
                } else if (varg instanceof Map) {
                    ret.add(DictTypeParam.from((Map<String, Object>) varg));
                } else if (varg instanceof String) {
                    ret.add(new DictTypeParam((String) varg, ""));
                } else {
                    // ignore all other cases.
                }
            }
            return ret.stream().filter(x -> !StringUtils.isEmpty(x.getCode())).collect(Collectors.toList());
        }

        public static DictTypeParam from(Map<String, Object> map) {
            String code = (String) map.get("code");
            if (StringUtils.isEmpty(code)) {
                return null;
            }
            DictTypeParam dictTypeParam = new DictTypeParam();
            dictTypeParam.setCode(code);
            dictTypeParam.setRemark((String) map.getOrDefault("remark", ""));
            return dictTypeParam;
        }

        @JsonIgnore
        @Override
        public String getId() {
            return CONFIG_TYPE_DICT_TYPE + ":" + getCode();
        }

        @JsonIgnore
        @Override
        public String getFingerPrint() {
            return getCode() + ":" + DcmTools.Escape.escape(getRemark());
        }
    }
}

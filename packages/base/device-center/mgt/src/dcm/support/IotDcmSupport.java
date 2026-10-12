package com.guhecloud.cube.iot.mgt.dcm.support;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.iot.mgt.comm.platform.InfiniteHierarchicalNumbering;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IotDcmSupport {
    public static final String PROVIDER_IOT = "IOT";

    public static final String CONFIG_TYPE_DICT_TYPE = "DictType";

    public static final String CONFIG_TYPE_DICT_ENTRY = "DictEntry";

    public static final String CONFIG_TYPE_PROD_MODEL = "ProdModel";

    public static final String DICT_TYPE_PROD_CAT = "prod_category";

    public static final String KW_KEY_TYPE_CODE = "typeCode";

    public static final String KW_KEY_PARENT = "parent";

    public static final Map<String, List<String>> IOT_DCM_PROVIDER_TYPES= ImmutableMap.of(PROVIDER_IOT,
            Arrays.asList(CONFIG_TYPE_DICT_TYPE,
                    CONFIG_TYPE_DICT_ENTRY,
                    CONFIG_TYPE_PROD_MODEL
                    ));

    /**
     * 截取parent Code Path.
     * @see InfiniteHierarchicalNumbering
     * @param codePath
     * @return 父级的codePath
     */
    public static String parseParent(String codePath) {
        if (codePath == null || codePath.length() <= 6) {
            return "";
        }
        return codePath.substring(0, codePath.length() - 3);
    }


}

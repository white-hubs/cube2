package com.guhecloud.cube.iot.mgt.things;

import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;

public class ProductUtils {

    public static void incrementProductVersion(Long productId, MdProductMapper mdProductMapper) {
        // 查询出产品信息，并且产品版本自增 v1.0.1

        MdProduct mdProduct = mdProductMapper.selectById(productId);
        String version = mdProduct.getProdVersion();

        String[] versionArr = version.split("\\.");
        int versionNum = Integer.parseInt(versionArr[2]);
        versionNum++;
        versionArr[2] = String.valueOf(versionNum);

        String newVersion = String.join(".", versionArr);
        mdProduct.setProdVersion(newVersion);
        mdProductMapper.updateById(mdProduct);
    }
}

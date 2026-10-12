package com.guhecloud.cube.iot.mgt.comm.platform;

import com.guhecloud.platform.base.util.Snowflake;

public class PlatformSnowflake {
    private static final long prod_storage_data_center_id = 2;
    private static final long prod_storage_worker_id = 2;

    private static final long prod_iot_data_center_id = 8;
    private static final long prod_iot_worker_id = 8;

    private static final Snowflake prod_storage_snowflake = new Snowflake(prod_storage_worker_id, prod_storage_data_center_id);
    private static final Snowflake prod_iot_snowflake = new Snowflake(prod_iot_worker_id, prod_iot_data_center_id);

    public static long nextProdStorageId() {
        return prod_storage_snowflake.nextId();
    }

    public static long nextProdIotId() {
        return prod_iot_snowflake.nextId();
    }

}

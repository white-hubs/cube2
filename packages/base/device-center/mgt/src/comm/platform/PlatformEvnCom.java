package com.guhecloud.cube.iot.mgt.comm.platform;

import com.guhecloud.cube.iot.mgt.core.data.lang.env.PlatformEvn;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.guhecloud.cube.iot.mgt.comm.platform.PlatformSnowflake.nextProdIotId;
import static com.guhecloud.cube.iot.mgt.comm.platform.PlatformSnowflake.nextProdStorageId;


/**
 * 此模块代码运行在两套平台上<br/>
 * <p>
 * - IOT平台<br/>
 * - 运营平台<br/>
 * </p>
 */
@Data
@Slf4j
public class PlatformEvnCom {

    public final InfiniteHierarchicalNumbering hierarchicalNumbering;
    private PlatformEvn platformEvn;

    public PlatformEvnCom(PlatformEvn platformEvn) {
        this.platformEvn = platformEvn;

        log.info("平台环境:{}", platformEvn.name);
        hierarchicalNumbering = new InfiniteHierarchicalNumbering(platformEvn.singleCode);
    }

    /**
     * 获取下一个雪花id,与平台高度绑定的ID，此ID生成内容中包含平台信息
     *
     * @return id
     */
    public long nextSnowflakeId() {
        if (platformEvn == PlatformEvn.IOT_PLATFORM) {
            return nextProdIotId();
        } else if (platformEvn == PlatformEvn.STORAGE_PLATFORM) {
            return nextProdStorageId();
        } else {
            throw new RuntimeException("未知平台环境");
        }
    }

    public long nextIotPlatformSnowflakeId() {
        return nextProdIotId();
    }

    public boolean isIotPlatform() {
        return platformEvn == PlatformEvn.IOT_PLATFORM;
    }

}

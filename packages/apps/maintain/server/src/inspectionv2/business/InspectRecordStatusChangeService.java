package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;

public interface InspectRecordStatusChangeService {

    /**
     * 记录生成
     */
    InspectRecordPO changeForRecordGenerate(InspectRecordPO po);

    /**
     * 记录过期
     */
    InspectRecordPO changeForRecordExpire(InspectRecordPO po);

    /**
     * 记录处理
     */
    InspectRecordPO changeForRecordComplete(InspectRecordPO po);

    /**
     * 记录终止
     */
    InspectRecordPO changeForRecordStop(InspectRecordPO po);

}

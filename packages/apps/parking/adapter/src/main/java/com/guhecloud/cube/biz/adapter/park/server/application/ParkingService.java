package com.guhecloud.cube.biz.adapter.park.server.application;

import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayTotalQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayTotalVO;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

public interface ParkingService {

    /**
     * 获取车场信息
     *
     * @return
     */
    List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException;

    /**
     * 获取入场记录
     *
     * @param param
     *
     * @return
     */
    List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param);

    /**
     * 获取出场记录
     *
     * @param param
     *
     * @return
     */
    List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param);

    /**
     * 获取缴费记录
     *
     * @param param
     *
     * @return
     */
    List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param);

    /**
     * 获取缴费统计
     *
     * @param param
     *
     * @return
     */
    ParkingPayTotalVO getParkingPayTotal(ParkingPayTotalQueryParam param) throws Exception;

    /**
     * 订阅
     *
     * @param param
     *
     * @return
     */
    Boolean parkingSubscribe(ParkingSubscribeParam param);

    /**
     * 取消订阅
     *
     * @param param
     *
     * @return
     */
    Boolean parkingUnSubscribe(ParkingSubscribeParam param);

}

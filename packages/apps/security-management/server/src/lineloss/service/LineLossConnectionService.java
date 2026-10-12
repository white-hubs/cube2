package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossConnectionPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossConnectionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossDeviceVO;
import com.guhecloud.platform.base.model.Pagination;

public interface LineLossConnectionService extends IService<LineLossConnection> {

    /**
     * 分页查询线损线路
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    Pagination<LineLossConnectionVO> pageLossConnection(LineLossConnectionPageParam param);

    /**
     * 根据线路 ID 查询线路设备信息
     *
     * @param param 查询参数
     *
     * @return 线路设备信息
     */
    LineLossDeviceVO getLineDevices(LineLossDeviceParam param);
}

package com.guhecloud.cube.biz.mdata.server.video.service;

import com.guhecloud.cube.biz.mdata.server.video.param.VideoPhotographModifyParam;
import com.guhecloud.cube.biz.mdata.server.video.param.VideotapeModifyParam;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideoPhotographVO;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideoSessionVO;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideotapeVO;

public interface VideoPtzService {

    /**
     * session保活
     *
     * @return
     */
    VideoSessionVO keepAliveSession();

    /**
     * 获取登录session
     *
     * @return
     */
    VideoSessionVO getLoginSession();

    /**
     * 获取登录session
     *
     * @param intranetAddress
     * @param username
     * @param password
     *
     * @return
     */
    VideoSessionVO getLoginSession(String intranetAddress, String username, String password);

    /**
     * 摄像机抓拍
     *
     * @return
     */
    VideoPhotographVO videoPhotograph(VideoPhotographModifyParam param);

    /**
     * 摄像机录像
     *
     * @return
     */
    VideotapeVO videotape(VideotapeModifyParam param);

    /**
     * 移除session
     */
    void removeSession();
}

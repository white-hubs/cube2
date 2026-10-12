package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.service;

import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.vo.NcdxEducationalVO;

public interface NcdxVisualizationService {
    NcdxEducationalVO getEducational();
    
    NcdxEducationalVO getEducationalTest(String baseUrl);
}

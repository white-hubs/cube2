package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolAppDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolVerCodeDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
public interface WholesalerProtocolService extends IService<WholesalerProtocol> {

    String PRO_TITLE ="待签订协议";
    String PRO_CONTENT ="您有新的待签订协议，请尽快在手机上进行签约";
    String PRO_NOTICE = "proNotice";
    String PRO_RELATIONSHIP = "protocol-relationship";
    Integer PRO_RANDOM_NUMBER_LENGTH = 6;
    String PRO_VERIFY_CODE_TYPE = "protocol-verify-code";

    /**
     * 根据协议ID催办下头没有签协议的档位
     * @param id 协议ID
     */
    void sendNotice(String projectId,String id);

    /**
     * 获取某档位下的协议列表
     * @param stallId  档位
     * @return 档位下的协议列表
     */
    List<WholesalerProtocolAppDTO> getProtocolsOnApp(String stallId);

    /**
     * 传入档位ID和协议ID，然后返回协议在该档位的具体执行情况
     * @param stallId 档位ID
     * @param protocolId 协议ID
     * @return 返回档位和对协议对应该档位的详情
     */
    WholesalerProtocolAppDTO getProtocolOnAppById(String stallId, String protocolId);
    /**
     * 根据ID查看详情，还有关联的一堆SPACEDTO
     * @param id id
     * @return 协议详情
     */
    WholesalerProtocolDTO getProtocolById(String projectId, String id);

    /**
     *
     * @param projectId 项目ID
     * @param user 用户
     * @param mobile 电话
     * @return
     */
    String sendVerifyCode(String projectId, String mobile, LoginUser user);
    /**
     * 新增协议
     * @param dto 协议Dto
     * @return 返回新增后的ID
     */
    String saveOrUpdate(WholesalerProtocolDTO dto);

    /**
     * 传入档位ID，协议ID，短信验证码，
     * 定为到唯一的一条relationship记录，进行协议签署
     * @param user 当前登录用户
     * @param stallId 档位ID
     * @param protocolId 协议ID
     * @param verifyCode 验证码
     */
    void signProtocol(WholesalerProtocolVerCodeDTO dto, LoginUser user);

    /**
     * PC上获取列表
     * @param queryDTO
     * @return
     */
    List<WholesalerProtocolDTO> getProtocolsOnPc(WholesalerProQueryDTO queryDTO);

    void  remove(String id);

    List<WholesalerProtocolDTO> getProtocolsByCompanyIdOrStallId(String projectId,String companyId, String stallId);
}

package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContract;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractSignDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerSecondPartDTO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-14
 */
public interface WholesalerContractService extends IService<WholesalerContract> {

    String CONTRACT_NOTICE = "contractNotice";
    String CONTRACT_TITLE = "合同待签约";
    String CONTRACT_TITLE_PROJECT = "合同签订完成";
    String CONTRACT_CONTENT = "您有新的待签订合同，请尽快在手机上进行签订";
    String CONTRACT_CONTENT_TO_PROJECT = "已完成了合同签订，请知晓！";
    Integer EFFECTED_STATUS = 3;
    Integer RANDOM_NUMBER_LENGTH = 6;
    String VERIFY_CODE_TYPE = "second-part-verify-code";
    String REDIS_TYPE = "second-part";
    String TYPE_CONTRACT = "wholesaler-contract";

    void sendNotice(String projectId, String id);

    /**
     * @param queryDTO
     * @return
     */
    List<WholesalerContractDTO> getContracts(WholesalerContractQueryDTO queryDTO);

    PageResponse<WholesalerContractDTO> getMyContractDTOS(WholesalerContractQueryDTO queryDTO, PageRequest pageRequest);

    /**
     * @return
     */
    WholesalerContractDTO getContractById(String id);

    /**
     * 新增合同草稿
     *
     * @param dto 合同草稿DTO
     * @return 返回ID
     */
    Long saveOrUpdate(WholesalerContractDTO dto);

    /**
     * 传入合同ID，还有乙方信息，进行签约
     *
     * @param dto 乙方信息
     * @return 返回乙方信息id
     */
    String signBySecondPart(WholesalerSecondPartDTO dto);

    /**
     * 项目ID和合同ID，还有电话作为redis中键组成的部分，
     * 电话还可以用来接收验证码撒
     *
     * @param projectId  项目ID
     * @param contractId 合同ID
     * @param mobile     电环
     * @return 返回验证码
     */
    String sendVerifyCode(String projectId, String contractId, String mobile, LoginUser user);

    void signConfirm(WholesalerContractSignDTO dto, LoginUser user);

    void signConfirmFirstPart(String signatoryName, String signatoryId, String contractId);

    List<WholesalerContract> getContractList(String projectId);

    void commit(String id);

}

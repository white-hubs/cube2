package com.guhecloud.cube.base.cloud.qr.code.sdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.cloud.qr.code.sdk.config.CloudQrCodeSdkConfig;
import com.guhecloud.cube.base.cloud.qr.code.sdk.http.HttpClient;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchCreateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchCreateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchDeleteByCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchQueryByCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchUpdateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchUpdateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.PageQueryRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.PageResult;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeDTO;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeTypeDTO;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeTypeRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 云二维码SDK客户端
 * <p>
 * 使用示例：
 * <pre>
 * CloudQrCodeSdkConfig config = new CloudQrCodeSdkConfig("your_app_id", "your_app_secret", "https://api.example.com");
 * CloudQrCodeClient client = new CloudQrCodeClient(config);
 *
 * // 创建二维码
 * CreateQrCodeRequest request = new CreateQrCodeRequest();
 * request.setName("测试二维码");
 * request.setQrTypeId(1L);
 * request.setQrContentType("TEXT");
 * request.setQrContent("Hello World");
 * request.setEnabled(true);
 * QrCodeDTO qrCode = client.createQrCode(request);
 * </pre>
 *
 * @author system
 */
public class CloudQrCodeClient {

    private final HttpClient httpClient;

    private static final String BASE_URL = "/cloudQrCode";

    /**
     * 构造函数
     *
     * @param config SDK配置
     */
    public CloudQrCodeClient(CloudQrCodeSdkConfig config) {
        this.httpClient = new HttpClient(config);
    }

    // ==================== 二维码管理 ====================

    /**
     * 创建二维码
     *
     * @param request 创建请求
     *
     * @return 二维码信息
     */
    public QrCodeDTO createQrCode(CreateQrCodeRequest request) {
        return httpClient.post(BASE_URL + "/cloud-qr-code", request, QrCodeDTO.class);
    }

    /**
     * 通过code更新二维码
     *
     * @param request 更新请求
     *
     * @return 二维码信息
     */
    public QrCodeDTO updateQrCodeByCode(UpdateQrCodeRequest request) {
        return httpClient.put(BASE_URL + "/cloud-qr-code/code/" + request.getCode(), request, QrCodeDTO.class);
    }

    /**
     * 通过code删除二维码
     *
     * @param code 二维码code
     */
    public void deleteQrCodeByCode(String code) {
        httpClient.delete(BASE_URL + "/cloud-qr-code/code/" + code);
    }

    /**
     * 通过code获取二维码
     *
     * @param code 二维码code
     *
     * @return 二维码信息
     */
    public QrCodeDTO getQrCodeByCode(String code) {
        return httpClient.get(BASE_URL + "/cloud-qr-code/code/" + code, QrCodeDTO.class);
    }

    /**
     * 通过codes获取二维码
     *
     * @param codes 二维码code列表
     *
     * @return 二维码信息
     */
    public List<QrCodeDTO> getQrCodeByCodes(Collection<String> codes) {
        return httpClient.post(BASE_URL + "/cloud-qr-code/query/by-codes", codes, new TypeReference<>() {});
    }

    /**
     * 分页查询二维码列表
     *
     * @param request 查询请求
     *
     * @return 分页结果
     */
    public PageResult<QrCodeDTO> queryQrCodes(PageQueryRequest request) {
        Map<String, String> params = new HashMap<>();
        if (request.getPageNum() != null) {
            params.put("pageNum", request.getPageNum().toString());
        }
        if (request.getPageSize() != null) {
            params.put("pageSize", request.getPageSize().toString());
        }
        if (request.getKeyword() != null) {
            params.put("keyword", request.getKeyword());
        }
        return httpClient.get(BASE_URL + "/cloud-qr-code", params, new TypeReference<>() {
        });
    }

    /**
     * 批量创建二维码
     *
     * @param request 批量创建请求
     *
     * @return 二维码信息列表
     */
    public List<QrCodeDTO> batchCreateQrCodes(BatchCreateQrCodeRequest request) {
        return httpClient.post(BASE_URL + "/cloud-qr-code/batch", request, new TypeReference<List<QrCodeDTO>>() {
        });
    }

    /**
     * 批量更新二维码
     *
     * @param request 批量更新请求
     *
     * @return 二维码信息列表
     */
    public List<QrCodeDTO> batchUpdateQrCodes(BatchUpdateQrCodeRequest request) {
        return httpClient.put(BASE_URL + "/cloud-qr-code/batch", request, new TypeReference<List<QrCodeDTO>>() {
        });
    }

    /**
     * 批量通过code删除二维码
     *
     * @param request 批量删除请求
     */
    public void batchDeleteQrCodesByCode(BatchDeleteByCodeRequest request) {
        httpClient.delete(BASE_URL + "/cloud-qr-code/batch", request);
    }

    // ==================== 二维码类型管理 ====================

    /**
     * 创建二维码类型
     *
     * @param request 创建请求
     *
     * @return 二维码类型信息
     */
    public QrCodeTypeDTO createQrCodeType(CreateQrCodeTypeRequest request) {
        return httpClient.post(BASE_URL + "/cloud-qr-code-type", request, QrCodeTypeDTO.class);
    }

    /**
     * 更新二维码类型
     *
     * @param request 更新请求
     *
     * @return 二维码类型信息
     */
    public QrCodeTypeDTO updateQrCodeTypeByCode(UpdateQrCodeTypeRequest request) {
        return httpClient.put(BASE_URL + "/cloud-qr-code-type/code/" + request.getCode(), request, QrCodeTypeDTO.class);
    }

    /**
     * 删除二维码类型
     *
     * @param code 类型编码
     */
    public void deleteQrCodeTypeByCode(String code) {
        httpClient.delete(BASE_URL + "/cloud-qr-code-type/code/" + code);
    }

    /**
     * 根据ID获取二维码类型
     *
     * @param id 类型ID
     *
     * @return 二维码类型信息
     */
    public QrCodeTypeDTO getQrCodeType(Long id) {
        return httpClient.get(BASE_URL + "/cloud-qr-code-type/" + id, QrCodeTypeDTO.class);
    }

    /**
     * 分页查询二维码类型列表
     *
     * @param request 查询请求
     *
     * @return 分页结果
     */
    public PageResult<QrCodeTypeDTO> queryQrCodeTypes(PageQueryRequest request) {
        Map<String, String> params = new HashMap<>();
        if (request.getPageNum() != null) {
            params.put("pageNum", request.getPageNum().toString());
        }
        if (request.getPageSize() != null) {
            params.put("pageSize", request.getPageSize().toString());
        }
        if (request.getKeyword() != null) {
            params.put("keyword", request.getKeyword());
        }
        return httpClient.get(BASE_URL + "/cloud-qr-code-type", params, new TypeReference<PageResult<QrCodeTypeDTO>>() {
        });
    }

    /**
     * 批量创建二维码类型
     *
     * @param request 批量创建请求
     *
     * @return 二维码类型信息列表
     */
    public List<QrCodeTypeDTO> batchCreateQrCodeTypes(BatchCreateQrCodeTypeRequest request) {
        return httpClient.post(BASE_URL + "/cloud-qr-code-type/batch", request, new TypeReference<List<QrCodeTypeDTO>>() {
        });
    }

    /**
     * 批量更新二维码类型
     *
     * @param request 批量更新请求
     *
     * @return 二维码类型信息列表
     */
    public List<QrCodeTypeDTO> batchUpdateQrCodeTypes(BatchUpdateQrCodeTypeRequest request) {
        return httpClient.put(BASE_URL + "/cloud-qr-code-type/batch", request, new TypeReference<List<QrCodeTypeDTO>>() {
        });
    }

    /**
     * 批量通过code删除二维码类型
     *
     * @param request 批量删除请求
     */
    public void batchDeleteQrCodeTypesByCode(BatchDeleteByCodeRequest request) {
        httpClient.delete(BASE_URL + "/cloud-qr-code-type/batch", request);
    }

    /**
     * 批量通过code查询二维码类型
     *
     * @param request 批量查询请求
     *
     * @return 二维码类型信息列表
     */
    public List<QrCodeTypeDTO> batchQueryQrCodeTypesByCode(BatchQueryByCodeRequest request) {
        return httpClient.post(BASE_URL + "/cloud-qr-code-type/batch/query", request, new TypeReference<>() {
        });
    }
}

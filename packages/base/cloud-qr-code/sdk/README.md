# 云二维码 SDK

云二维码服务的Java SDK，提供简单易用的API供外部系统集成。

## 功能特性

- ✅ OAuth2客户端认证（client_credentials模式，client信息在请求体中）
- ✅ 自动令牌管理和刷新
- ✅ 二维码CRUD操作
- ✅ 二维码类型管理
- ✅ 分页查询支持
- ✅ 完整的异常处理
- ✅ 零依赖（仅需Jackson）

## 快速开始

### 1. 添加依赖

Maven:
```xml
<dependency>
    <groupId>com.guhecloud</groupId>
    <artifactId>cloud-qr-code-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle:
```groovy
implementation 'com.guhecloud:cloud-qr-code-sdk:1.0.0'
```

### 2. 初始化客户端

```java
// 基础配置
CloudQrCodeSdkConfig config = new CloudQrCodeSdkConfig(
    "your_app_id",        // 应用ID (client_id)
    "your_app_secret",    // 应用密钥 (client_secret)
    "https://api.example.com"  // 服务地址
);

// 带租户ID的配置
CloudQrCodeSdkConfig config = new CloudQrCodeSdkConfig(
    "your_app_id",
    "your_app_secret",
    "https://api.example.com",
    "tenant_123"  // 可选的租户ID
);

// 创建客户端
CloudQrCodeClient client = new CloudQrCodeClient(config);
```

### 3. 使用示例

#### 创建二维码

```java
// 创建二维码请求
CreateQrCodeRequest request = new CreateQrCodeRequest();
request.setName("停车场入口");
request.setQrTypeId(1L);
request.setQrContentType("TEXT");
request.setQrContent("PARKING_ENTRANCE_001");
request.setEnabled(true);

// 设置路由信息（可选）
QrRouteDTO route = new QrRouteDTO();
route.setRouteType("H5");
route.setH5Url("https://example.com/parking");
request.setRoute(route);

// 执行创建
QrCodeDTO qrCode = client.createQrCode(request);
System.out.println("二维码ID: " + qrCode.getId());
System.out.println("二维码编码: " + qrCode.getCode());
```

#### 查询二维码

```java
// 根据ID查询
QrCodeDTO qrCode = client.getQrCode(1L);

// 分页查询
PageQueryRequest queryRequest = new PageQueryRequest();
queryRequest.setPageNum(1);
queryRequest.setPageSize(10);
queryRequest.setKeyword("停车");

PageResult<QrCodeDTO> result = client.queryQrCodes(queryRequest);
System.out.println("总记录数: " + result.getTotal());
for (QrCodeDTO item : result.getRecords()) {
    System.out.println("二维码: " + item.getName());
}
```

#### 更新二维码

```java
UpdateQrCodeRequest updateRequest = new UpdateQrCodeRequest();
updateRequest.setName("停车场入口-更新");
updateRequest.setQrContent("PARKING_ENTRANCE_001_V2");
updateRequest.setEnabled(true);

QrCodeDTO updated = client.updateQrCode(1L, updateRequest);
```

#### 删除二维码

```java
client.deleteQrCode(1L);
```

#### 管理二维码类型

```java
// 创建类型
CreateQrCodeTypeRequest typeRequest = new CreateQrCodeTypeRequest();
typeRequest.setCode("PARKING");
typeRequest.setName("停车场");
typeRequest.setRemark("停车场相关二维码");

QrCodeTypeDTO type = client.createQrCodeType(typeRequest);

// 查询类型
QrCodeTypeDTO typeInfo = client.getQrCodeType(type.getId());

// 分页查询类型
PageResult<QrCodeTypeDTO> types = client.queryQrCodeTypes(new PageQueryRequest());

// 删除类型
client.deleteQrCodeType(type.getId());
```

## API文档

### 二维码管理

| 方法 | 说明 | 参数 | 返回值 |
|------|------|------|--------|
| createQrCode | 创建二维码 | CreateQrCodeRequest | QrCodeDTO |
| updateQrCode | 更新二维码 | Long id, UpdateQrCodeRequest | QrCodeDTO |
| deleteQrCode | 删除二维码 | Long id | void |
| getQrCode | 获取二维码 | Long id | QrCodeDTO |
| queryQrCodes | 分页查询 | PageQueryRequest | PageResult\<QrCodeDTO\> |

### 二维码类型管理

| 方法 | 说明 | 参数 | 返回值 |
|------|------|------|--------|
| createQrCodeType | 创建类型 | CreateQrCodeTypeRequest | QrCodeTypeDTO |
| updateQrCodeType | 更新类型 | Long id, CreateQrCodeTypeRequest | QrCodeTypeDTO |
| deleteQrCodeType | 删除类型 | Long id | void |
| getQrCodeType | 获取类型 | Long id | QrCodeTypeDTO |
| queryQrCodeTypes | 分页查询 | PageQueryRequest | PageResult\<QrCodeTypeDTO\> |

## 数据模型

### QrCodeDTO
```java
public class QrCodeDTO {
    private Long id;                    // 二维码ID
    private String code;                // 二维码编码
    private String name;                // 二维码名称
    private Long qrTypeId;             // 二维码类型ID
    private String qrContentType;      // 内容类型
    private String qrContent;          // 内容
    private String grantClientCode;    // 客户端编码
    private Boolean enabled;           // 是否启用
    private QrRouteDTO route;          // 路由信息
}
```

### QrRouteDTO
```java
public class QrRouteDTO {
    private String routeType;          // 路由类型
    private String h5Url;              // H5页面地址
    private String miniAppId;          // 小程序AppId
    private String miniPagePath;       // 小程序页面路径
    private String appPagePath;        // APP页面路径
}
```

### QrCodeTypeDTO
```java
public class QrCodeTypeDTO {
    private Long id;                   // 类型ID
    private String code;               // 类型编码
    private String name;               // 类型名称
    private String grantClientCode;    // 客户端编码
    private String remark;             // 备注
}
```

## 异常处理

SDK抛出`CloudQrCodeSdkException`异常，包含错误码和消息：

```java
try {
    QrCodeDTO qrCode = client.getQrCode(1L);
} catch (CloudQrCodeSdkException e) {
    System.err.println("错误码: " + e.getCode());
    System.err.println("错误信息: " + e.getMessage());
}
```

常见错误码：
- `AUTH_FAILED`: 认证失败
- `AUTH_ERROR`: 认证过程错误
- `AUTH_EXPIRED`: 令牌过期
- `REQUEST_ERROR`: 请求失败
- `HTTP_xxx`: HTTP错误（xxx为状态码）

## 高级配置

### 自定义超时时间

```java
CloudQrCodeSdkConfig config = new CloudQrCodeSdkConfig();
config.setAppId("your_app_id");
config.setAppSecret("your_app_secret");
config.setServerUrl("https://api.example.com");
config.setConnectTimeout(10000);  // 连接超时10秒
config.setReadTimeout(60000);     // 读取超时60秒

CloudQrCodeClient client = new CloudQrCodeClient(config);
```

## 注意事项

1. **线程安全**: `CloudQrCodeClient`实例是线程安全的，建议在应用中创建单例使用
2. **令牌管理**: SDK自动管理访问令牌的获取和刷新，无需手动处理
3. **数据隔离**: 外部系统只能访问自己client_id下的数据，由服务端自动隔离
4. **租户隔离**: 如果配置了tenantId，所有请求会携带租户信息

## 许可证

Copyright © GuheCloud

package com.guhecloud.kaihe.scada.server.infrastructure.web.response;

import java.lang.annotation.*;

/**
 * 将带有 @RestController 或 @ResponseBody 注解的 Controller 类的方法返回值进行统一格式分封装，
 * <br/> 
 * 封装后的返回格式为 {success: xxx, data: {...}} 
 * <br/> 
 * 本注解可以配置在类或方法上，且必须配合 @UnifiedResponseConfigurer 进行配置才能生效
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UnifiedResponseBody {
}

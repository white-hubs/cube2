package com.guhecloud.platform.base.util;

import com.guhecloud.platform.base.standard.http.HttpClientFactory;
import org.apache.hc.core5.http.ContentType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class SpringFileUtils {

    private static RestTemplate REST_TEMPLATE = null;

    static {
        try {
            REST_TEMPLATE = HttpClientFactory.createDefaultRestTemplate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SpringFileUtils() {

    }

    /**
     * 下载远程网络文件
     *
     * @param picPath 文件地址
     *
     * @return DownLoadPictureResponse
     *
     * @throws IOException
     */
    public static DownLoadPictureResponse downloadFile(String picPath) throws IOException, URISyntaxException {
        var responseBody = REST_TEMPLATE.exchange(
                new URI(picPath),
                HttpMethod.GET,
                null,
                Resource.class);
        // 检查响应状态码
        if (responseBody.getStatusCode() == HttpStatus.OK) {
            // 获取资源并设置响应头
            var resource = responseBody.getBody();
            if (resource != null) {
                // 使用流将资源写入响应
                try (var inputStream = resource.getInputStream();
                     var outputStream = new ByteArrayOutputStream();) {
                    var buffer = new byte[1024]; // 使用合适的缓冲区大小
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    return new DownLoadPictureResponse(outputStream.toByteArray(), getFileExtension(picPath)
                            .or(() -> getFileType(responseBody))
                            .orElse(StringUtils.EMPTY_STRING));
                }
            } else {
                throw new RuntimeException("http resource is null");
            }
        } else {
            // 处理非200的响应状态码
            throw new RuntimeException("http error: " + responseBody.getStatusCode());
        }
    }

    /**
     * 获取远程网络文件的InputStream
     * 注意: 使用方使用完后必须手动关闭流
     *
     * @param url 远程资源URL
     *
     * @return 输入流（调用者需负责关闭）可能为空
     *
     * @throws IOException 文件异常
     */
    public static InputStream getRemoteInputStream(String url) throws IOException {
        ResponseEntity<Resource> responseEntity = REST_TEMPLATE.exchange(
                url,
                HttpMethod.GET,
                null,
                Resource.class);
        Resource resource = responseEntity.getBody();
        if (resource != null) {
            return resource.getInputStream();
        }
        return null;
    }

    /**
     * 判断是否为远程文件
     *
     * @param path 地址
     *
     * @return true or false
     */
    public static boolean isRemoteUrl(String path) throws URISyntaxException {
        var uri = new URI(path);
        var scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    /**
     * 文件字节数组转MultipartFile
     *
     * @param fileName 文件名称
     * @param bytes    文件字节数组
     *
     * @return MultipartFile
     */
    public static MultipartFile fileByteConvertMultipartFile(String fileName, byte[] bytes) throws IOException {
        return fileByteConvertMultipartFile(fileName, bytes, ContentType.MULTIPART_FORM_DATA.getMimeType());
    }

    /**
     * 文件字节数组转MultipartFile
     *
     * @param fileName    文件名称
     * @param bytes       文件字节数组
     * @param contentType 类型
     *
     * @return MultipartFile
     */
    public static MultipartFile fileByteConvertMultipartFile(String fileName, byte[] bytes, String contentType) throws IOException {
        MultipartFile multipartFile;
        try (var inputStream = new ByteArrayInputStream(bytes)) {
            multipartFile = new MockMultipartFile(fileName, fileName, contentType, inputStream);
        }
        return multipartFile;
    }

    /**
     * 文件转换为MultipartFile
     *
     * @param file 文件
     *
     * @return MultipartFile
     *
     * @throws IOException
     */
    public static MultipartFile fileConvertMultipartFile(File file) throws IOException {
        MultipartFile multipartFile;
        try (var inputStream = new FileInputStream(file)) {
            multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.MULTIPART_FORM_DATA.getMimeType(), inputStream);
        }
        return multipartFile;
    }

    /**
     * 获取文件字节
     *
     * @param file 本地文件
     *
     * @return 文件字节数组
     */
    public static byte[] getFileBytes(File file) throws IOException {
        try (FileInputStream is = new FileInputStream(file);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            return os.toByteArray();
        }
    }

    /**
     * 获取文件类型
     *
     * @param responseBody 网络文件响应信息
     *
     * @return 文件类型
     */
    public static Optional<String> getFileType(ResponseEntity<Resource> responseBody) {
        //尝试从请求头获取
        if (Objects.nonNull(responseBody.getHeaders().getContentType())) {
            var contentType = responseBody.getHeaders().getContentType().toString();
            contentType = contentType.split(";")[0];
            return Optional.of(contentType.substring(contentType.lastIndexOf("/") + 1));
        }
        if (responseBody.getBody() != null) {
            try {
                return getFileType(responseBody.getBody());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    /**
     * 从 URL 路径提取扩展名
     */
    public static Optional<String> getFileExtension(String path) {
        if (path == null || path.isEmpty()) {
            return Optional.empty();
        }
        var cleanPath = path.split("[?#]")[0];

        var lastDotIndex = cleanPath.lastIndexOf('.');
        var lastSeparatorIndex = Math.max(
                cleanPath.lastIndexOf('/'),
                cleanPath.lastIndexOf('\\')
                                         );
        if (lastDotIndex > lastSeparatorIndex && lastDotIndex < cleanPath.length() - 1) {
            return Optional.of(cleanPath.substring(lastDotIndex + 1).toLowerCase());
        }
        return Optional.empty();
    }

    /**
     * 获取文件类型
     *
     * @param resource 资源文件
     *
     * @return 文件类型
     *
     * @throws IOException
     */
    public static Optional<String> getFileType(Resource resource) throws IOException {
        var fileType = Files.probeContentType(Paths.get(resource.getFilename()));
        return Optional.of(fileType.substring(fileType.lastIndexOf("/") + 1));
    }

    protected static void delFile(Path path) throws IOException {
        try {
            Files.delete(path);
        } catch (AccessDeniedException e) {
            // 可能遇到只读文件，无法删除.使用 file 方法删除
            if (!path.toFile().delete()) {
                throw e;
            }
        }
    }

    public static boolean del(File file) {
        if (file == null || !file.exists()) {
            // 如果文件不存在或已被删除，此处返回true表示删除成功
            return true;
        }

        if (file.isDirectory()) {
            // 清空目录下所有文件和目录
            boolean isOk = clean(file);
            if (!isOk) {
                return false;
            }
        }

        // 删除文件或清空后的目录
        final Path path = file.toPath();
        try {
            delFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static boolean clean(File directory) throws RuntimeException {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return true;
        }

        final File[] files = directory.listFiles();
        if (null != files) {
            for (File childFile : files) {
                if (!del(childFile)) {
                    // 删除一个出错则本次删除任务失败
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param fileBytes 文件字节
     * @param fileType  文件类型
     */
    public record DownLoadPictureResponse(byte[] fileBytes, String fileType) {

    }
}

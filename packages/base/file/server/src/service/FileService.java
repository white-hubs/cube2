package com.guhecloud.cube.base.file.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.param.FileGenerateParam;
import com.guhecloud.cube.base.file.server.param.FileParam;
import com.guhecloud.cube.base.file.server.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService extends IService<File>, FileStore {

    FileVO upload(MultipartFile file);

    Boolean physicalDeleteByIds(FileParam ids);

    File upload2OSS(MultipartFile file, String businessKey, Integer tenant);

    File publicUpload2OSS(MultipartFile file, String businessKey, Integer tenant);

    void remove(Long fileId);

    @Override
    byte[] getFileBytes(File file);

    FileVO getById(Long id);

    List<File> getByBusinessKey(Long businessKey);

    List<FileVO> listByBusinessKeys(List<Long> businessKeys);

    List<File> listByIds(List<Long> fileIds);

    /**
     * 预生成文件信息
     */
    FileVO getUploadDetails(FileGenerateParam param);

    /**
     * 文件信息入库
     */
    File saveFile(File param);

}

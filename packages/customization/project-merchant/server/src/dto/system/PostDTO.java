package com.guhecloud.cube.deliver.project.merchant.dto.system;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Post;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostDTO extends ConvertionDTO<PostDTO, Post> {

    public PostDTO() {
    }

    public PostDTO(Post post) {
        super();
        map(post);
    }

    public PostDTO(List<Post> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String code;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String name;

    private String pinyin;

    private Integer sort;

}

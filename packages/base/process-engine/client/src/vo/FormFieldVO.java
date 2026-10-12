package vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldVO {
    private String id;
    private String name;
    private boolean required;
    private String fromType;
}

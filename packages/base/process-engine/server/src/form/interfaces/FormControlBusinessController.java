package com.guhecloud.cube.base.flow.form.interfaces;

import com.guhecloud.cube.base.flow.form.commandgateway.FormControlBusinessCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlBusinessCommand;
import com.guhecloud.cube.base.flow.form.resolver.FormResultDataResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormControlBusinessController {

    private final FormResultDataResolver formResultDataResolver;
    private final FormControlBusinessCommandService formControlBusinessCommandService;

    @GetMapping("/form-control-business/parse")
    public String parse(@RequestParam("formInstanceId") Long formInstanceId,
                        @RequestParam("formDataJson") String formDataJson
                       ) {
        return formResultDataResolver.parse(formInstanceId, formDataJson);
    }

    @PostMapping("/form-control-business")
    public Long saveOrUpdate(@Valid @RequestBody FormControlBusinessCommand formControlBusinessCommand) {
        return formControlBusinessCommandService.saveOrUpdate(formControlBusinessCommand);
    }

}

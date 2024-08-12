package com.sendgrid.oai.go;

import com.sendgrid.oai.common.ApiResource;
import com.sendgrid.oai.go.GoApiResourceBuilder;
import lombok.Setter;

@Setter
public class GoApiResource extends ApiResource {
    protected String packageName;

    public GoApiResource(GoApiResourceBuilder goApiResourceBuilder) {
        super(goApiResourceBuilder);
    }

}

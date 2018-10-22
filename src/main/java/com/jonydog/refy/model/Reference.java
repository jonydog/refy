package com.jonydog.refy.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Reference {

    @NotNull @Size(min=5, max=250)
    private String title;

    @Size(min=3,max=250)
    private String authorsNames;

    @Size(min=0, max=300)
    private String keywords;

    @Size(min=0,max=300)
    private String link;

    @Size(min=0,max=150)
    private String filePath;

    @Size(min=0,max=250)
    private String observations;
}

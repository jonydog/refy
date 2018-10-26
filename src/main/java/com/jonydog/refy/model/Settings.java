package com.jonydog.refy.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class Settings {

    @Size(min = 3, max=1000 )
    private String homeFolder;

    @Size( max=1000 )
    private String pdfReaderPath;

    @Size( max=1000 )
    private String browserPath;
}

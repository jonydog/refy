package com.jonydog.refy.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class Settings {

    @Size(min = 3, max=1000 )
    private String homeFolder;

}

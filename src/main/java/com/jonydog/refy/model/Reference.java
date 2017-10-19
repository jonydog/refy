package com.jonydog.refy.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "REFERENCE")
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull @Size(min=1, max=150)
    private String title;

    @NotNull @Size(min=1,max=250)
    private String authorsNames;

    @Size(min=1, max=250)
    private String keywords;

    @NotNull @Size(min=3,max=150)
    private String filePath;


}

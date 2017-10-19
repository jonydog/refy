package com.jonydog.refy.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "USER_NOTE")
public class UserNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    private Reference reference;

    @NotNull @Size(min=1,max=150)
    private String title;

    @NotNull @Size(min=1, max=2000)
    private String text;
}

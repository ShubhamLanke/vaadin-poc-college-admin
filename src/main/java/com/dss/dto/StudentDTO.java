package com.dss.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {

    private Long id;
    private String name;
    private String phoneNo;
    private String email;
    private String gender;
    private LocalDate dob;
    private String address;

}

package com.dss.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNo;
    private String email;
    private String gender;
    private LocalDate dob;
    private String address;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

}

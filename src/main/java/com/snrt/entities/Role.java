package com.snrt.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor @ToString
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String rolename;
//    @ManyToMany(mappedBy="roles",fetch= FetchType.LAZY)
//    private List<User> users = new ArrayList<>();
}


package com.snrt.entities;


import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Advertisement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    private byte[] logo;
    private String title;
    @Lob
    private byte[] image;
    private String description;
    private String website;
    private String facebook;
    private String twitter;
    private String instagram;
    private String youtube;
    private int count;
    private int max;
    private double percentage;
    private double price;

    @OneToMany(mappedBy="primaryKey.advertisement", fetch= FetchType.EAGER)
    private List<Advert_User> advertByUser = new ArrayList<>();
}

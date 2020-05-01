package com.snrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String albumName;
    @Lob
    private byte[] albumImageURL;
    private String gender;
    //@Temporal(TemporalType.DATE)
    private String releaseDate;
    private int trackCount;
    private String description;
    private long views;
    private long rating;
    //private long ratings;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idUser")
    private User userAlbum;
    @OneToMany(mappedBy="album")
    private List<Track> tracks = new ArrayList<>();

    public Album(String albumName, int trackCount, List<Track> tracks, User user) {
        this.albumName = albumName;
        this.tracks=tracks;
        this.trackCount=trackCount;
        this.userAlbum=user;
    }
}

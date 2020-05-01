package com.snrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Track implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String trackTitle;
    private long trackNumber;
    @Lob
    private byte[] trackURL;
    @Lob
    private byte[] trackImageURL;
    private String fileType;
    //@JsonView(View.FileInfo.class)
    private String fileName;
    private long views;
    private long rating;
    //@Temporal(TemporalType.DATE)
    private String releaseDate;
    private String time;
    private double price;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idUser")
    private User userTrack;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAlbum")
    private Album album;

    @OneToMany(mappedBy="key.track", fetch= FetchType.LAZY)
    private List<Views> viewsByUser = new ArrayList<>();
    @OneToMany(mappedBy="primaryKey.track", fetch= FetchType.LAZY)
    private List<Rating> ratingByUser = new ArrayList<>();
//    @OneToMany(mappedBy="key.track", fetch= FetchType.LAZY)
//    private List<Views> viewsByUser = new ArrayList<>();

    public Track(String trackTitle, long trackNumber, User userTrack, Album album) {
        this.trackTitle = trackTitle;
        this.trackNumber = trackNumber;
        this.userTrack = userTrack;
        this.album = album;
    }

    public Track(String trackTitle, User userTrack, long views) {
        this.trackTitle = trackTitle;
        this.userTrack = userTrack;
        this.views = views;
    }
}

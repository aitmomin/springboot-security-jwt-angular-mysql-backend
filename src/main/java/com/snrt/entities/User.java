package com.snrt.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String lastname;
    private String firstname;
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    private String gender;
    private String city;
    private String address;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Lob
    private byte[] imageURL;

    @Column(unique = true)
    private String nickname;
    @OneToMany(mappedBy="userAlbum", fetch= FetchType.LAZY)
    private List<Album> albums = new ArrayList<>();
    @OneToMany(mappedBy="userTrack", fetch= FetchType.LAZY)
    private List<Track> singles = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="users_roles",
            joinColumns= @JoinColumn(name="idUser"),
            inverseJoinColumns=@JoinColumn(name="idRole"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy="key.user", fetch= FetchType.LAZY)
    private List<Views> viewsByUser = new ArrayList<>();
    @OneToMany(mappedBy="primaryKey.user", fetch= FetchType.LAZY)
    private List<Rating> ratingByUser = new ArrayList<>();
    @OneToMany(mappedBy="primaryKey.user_advrt", fetch= FetchType.LAZY)
    private List<Advert_User> advertByUser = new ArrayList<>();

    /*@OneToMany(mappedBy="primaryKey.user", fetch= FetchType.LAZY)
    private List<Rating> ratings = new ArrayList<>();*/

    public User(String lastname, String firstname, Date birthdate, String gender, String city, String address, String username,
                String password, String email, byte[] imageURL, Set<Role> roles) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.city = city;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
        this.imageURL = imageURL;
        this.roles = roles;
    }

    public User(String lastname, String firstname, Date birthdate, String gender, String city, String address, String username,
                String password, String email, Set<Role> roles) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.city = city;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
}

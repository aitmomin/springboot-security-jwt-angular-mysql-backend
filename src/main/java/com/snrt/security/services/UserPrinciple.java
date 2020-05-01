package com.snrt.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snrt.entities.Album;
import com.snrt.entities.Track;
import com.snrt.entities.User;
import com.snrt.entities.Views;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;
    private String lastname;
    private String firstname;
    private Date birthdate;
    private String gender;
    private String city;
    private String address;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private byte[] imageURL;
    private String nickname;
    private List<Album> albums;
    private List<Track> singles;
    private List<Views> viewsByUser;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String lastname, String firstname, Date birthdate, String gender, String city, String address,
                         String username, String password, String email, byte[] imageURL, String nickname, List<Album> albums,
                         List<Track> singles, List<Views> viewsByUser, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
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
        this.nickname = nickname;
        this.albums = albums;
        this.singles = singles;
        this.viewsByUser = viewsByUser;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getRolename())
        ).collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getLastname(),
                user.getFirstname(),
                user.getBirthdate(),
                user.getGender(),
                user.getCity(),
                user.getAddress(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getImageURL(),
                user.getNickname(),
                user.getAlbums(),
                user.getSingles(),
                user.getViewsByUser(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getImageURL() {
        return imageURL;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Track> getSingles() {
        return singles;
    }

    public List<Views> getViewsByUser() { return viewsByUser; }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}
package com.snrt.controllers;

import com.snrt.entities.*;
import com.snrt.message.request.LoginForm;
import com.snrt.message.response.JwtResponse;
import com.snrt.message.response.ResponseMessage;
import com.snrt.repositories.*;
import com.snrt.security.jwt.JwtProvider;
//import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class API {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    ViewsRepository viewsRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    AdvertUserRepository advertUserRepository;

    @RequestMapping(value="/artists", method=RequestMethod.GET)
    public List<User> getArtists(){
        return userRepository.findAll();
    }

    @RequestMapping(value="/artist/albums/{id}", method=RequestMethod.GET)
    public List<Album> getAlbums(@PathVariable Long id){
        return albumRepository.getAlbumsByArtist(id);
    }

    @RequestMapping(value="/artist/myAlbums/{id}", method=RequestMethod.GET)
    public List<Album> getAlbumsByConnected(@PathVariable Long id){
        return albumRepository.getAlbumsByConnected(id);
    }

    @RequestMapping(value="/artist/myAlbums2/{id}", method=RequestMethod.GET)
    public List<Album> getMyAlbums(@PathVariable Long id){
        return albumRepository.getAlbumByUserAlbumId(id);
    }

    @RequestMapping(value="/album/tracks/{id}", method=RequestMethod.GET)
    public List<Track> getTracks(@PathVariable Long id){
        return trackRepository.getTracksByAlbumIdOrderByTrackNumber(id);
    }

    @RequestMapping(value="/artist/singles/{id}", method=RequestMethod.GET)
    public List<Track> getSingles(@PathVariable Long id){
        return trackRepository.getTracksByUserTrackIdOrderByTrackNumber(id);
    }

    @RequestMapping(value="/artist/{id}", method=RequestMethod.GET)
    public User getArtistById(@PathVariable Long id){
        return userRepository.findById(id).get();
    }

    @RequestMapping(value="/artist/username/{username}", method=RequestMethod.GET)
    public User getArtistByUsername(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

    @RequestMapping(value="/album/{id}", method=RequestMethod.GET)
    public Album getAlbumById(@PathVariable Long id){
        return albumRepository.findById(id).get();
    }

    @RequestMapping(value="/track/{id}", method=RequestMethod.GET)
    public Track getTrackById(@PathVariable Long id){
        return trackRepository.findById(id).get();
    }

    @GetMapping(value="/search/artists/{name}")
    public List<User> searchArtists(@PathVariable String name){
        return userRepository.searchByNickname("%"+name+"%");
    }

    @GetMapping(value="/artists/random")
    //@PreAuthorize("hasRole('USER') or hasRole('ARTIST')")
    public List<User> getUsersRandom(){
        return userRepository.getUsersRandom();
    }

    @PostMapping("/upload")
    public void uploadMultipartFile(@RequestParam("mp3") MultipartFile file) throws UnsupportedAudioFileException, IOException{
        try {
            User u = userRepository.findById(2l).get();
            Track t = new Track();
            t.setTrackTitle("single platin");
            t.setTrackURL(file.getBytes());
            t.setFileType(file.getContentType());
            t.setFileName(file.getOriginalFilename());
            t.setUserTrack(u);
            trackRepository.save(t);
            System.out.println("upload finish !!!");
        } catch (	Exception e) {
            //return "FAIL! error error error";
            System.out.println("error = "+e.getMessage());
        }
        System.out.println("finish ...");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginForm request) {

        String jwt = "";
        UserDetails userDetails = null;

        if (!userRepository.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Erreur -> le nom d'utilisateur est incorrect !"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user = userRepository.findByUsername(request.getUsername());
            if(encoder.matches(request.getPassword(), user.getPassword())){
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                jwt = jwtProvider.generateJwtToken(authentication);
                userDetails = (UserDetails) authentication.getPrincipal();
            }else{
                return new ResponseEntity<>(new ResponseMessage("Erreur -> le mot de passe est incorrect !"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerWithImage(@RequestParam("profile") MultipartFile file, @RequestParam("lastname") String lastname, @RequestParam("firstname") String firstname,
                     @RequestParam("birthdate") String birthdate, @RequestParam("gender") String gender, @RequestParam("email") String email,
                     @RequestParam("city") String city, @RequestParam("address") String address, @RequestParam("password") String password,
                     @RequestParam("username") String username) throws IOException, ParseException {

        if (userRepository.existsByUsername(username)) {
            return new ResponseEntity<>(new ResponseMessage("Erreur -> le nom d'utilisateur '"+username+"' utilisé est déjà existe."),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity<>(new ResponseMessage("Erreur -> l'email '"+email+"' utilisé est déjà existe."),
                    HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRolename("USER").get());
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
        Date date = dt.parse(birthdate);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User(lastname, firstname, date, gender, city, address, username, encoder.encode(password), email, file.getBytes(), roles);

        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    @GetMapping("/test/with/jwt")
    public String test1(){
        return "test avec spring security et jwt";
    }
    @GetMapping("/test/without/jwt")
    public String test2(){
        return "test sans spring security et jwt";
    }

    @PostMapping("/auth/register/noimg")
    public ResponseEntity<?> registerWithoutImage(@RequestParam("lastname") String lastname, @RequestParam("firstname") String firstname,
                                      @RequestParam("birthdate") String birthdate, @RequestParam("gender") String gender, @RequestParam("email") String email,
                                      @RequestParam("city") String city, @RequestParam("address") String address, @RequestParam("password") String password,
                                      @RequestParam("username") String username) throws IOException, ParseException {

        if (userRepository.existsByUsername(username)) {
            return new ResponseEntity<>(new ResponseMessage("Erreur -> le nom d'utilisateur '"+username+"' utilisé est déjà existe."),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity<>(new ResponseMessage("Erreur -> l'email '"+email+"' utilisé est déjà existe."),
                    HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRolename("USER").get());
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
        Date date = dt.parse(birthdate);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User(lastname, firstname, date, gender, city, address, username, encoder.encode(password), email, roles);

        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    @GetMapping("/count/albums/{id}")
    public long countAlbumsByUser(@PathVariable Long id) {
        return albumRepository.countByUserAlbumId(id);
    }

    @GetMapping("/count/singles/{id}")
    public long countSinglesByUser(@PathVariable Long id) {
        return trackRepository.countByUserTrackId(id);
    }

    @GetMapping("/sum/album/views/{id}")
    public long sumViewsByUserAlbum(@PathVariable Long id) {
        return albumRepository.sumViewsByUserAlbumId(id);
    }

    @GetMapping("/sum/album/rating/{id}")
    public long sumRatingByUserAlbum(@PathVariable Long id) {
        return albumRepository.sumRatingByUserAlbumId(id);
    }

    @GetMapping("/sum/track/views/{id}")
    public long sumViewsByUserTrack(@PathVariable Long id) {
        return trackRepository.sumViewsByUserTrackId(id);
    }

    @GetMapping("/sum/track/rating/{id}")
    public long sumRatingByUserTrack(@PathVariable Long id) {
        return trackRepository.sumRatingByUserTrackId(id);
    }

    @PostMapping("/create/new/album")
    public void createAlbum(@RequestParam("nom") String nom, @RequestParam("nombre") String nombre, @RequestParam("photo") MultipartFile photo,
                            @RequestParam("genre") String genre, @RequestParam("description") String description, @RequestParam("user") String user) throws IOException, ParseException {
        Album album=new Album();
        album.setAlbumName(nom);
        album.setTrackCount(Integer.parseInt(nombre));
        album.setGender(genre);
        if (description=="null")    album.setDescription(null);
        album.setAlbumImageURL(photo.getBytes());
        album.setReleaseDate(LocalDate.now()+"");
        album.setUserAlbum(userRepository.findById(Long.parseLong(user)).get());
        albumRepository.save(album);
    }

    @PostMapping("/create/new/track")
    public void createTrack(@RequestParam("titre") String titre, @RequestParam("album") String album, @RequestParam("track") MultipartFile chanson,
                            @RequestParam("time") String time) throws IOException, ParseException {
        Track track=new Track();
        track.setTrackTitle(titre);
        track.setFileName(chanson.getOriginalFilename());
        track.setFileType(chanson.getContentType());
        track.setTrackURL(chanson.getBytes());
        track.setTime(time);
        //track.setTrackNumber(0l);
        track.setReleaseDate(LocalDate.now()+"");
        track.setAlbum(albumRepository.findById(Long.parseLong(album)).get());
        trackRepository.save(track);
    }

    @PostMapping("/create/new/single")
    public void createSingle(@RequestParam("titre") String titre, @RequestParam("user") String user, @RequestParam("single") MultipartFile chanson,
                             @RequestParam("photo") MultipartFile photo, @RequestParam("time") String time) throws IOException, ParseException {
        Track single=new Track();
        single.setTrackTitle(titre);
        single.setFileName(chanson.getOriginalFilename());
        single.setFileType(chanson.getContentType());
        single.setTrackURL(chanson.getBytes());
        single.setTrackImageURL(photo.getBytes());
        single.setTime(time);
        single.setReleaseDate(LocalDate.now()+"");
        single.setUserTrack(userRepository.findById(Long.parseLong(user)).get());
        trackRepository.save(single);
    }

    @PutMapping("/update/views/{id}")
    public void updateViewsURL(@PathVariable Long id) {
        Track t = trackRepository.findById(id).get();
        t.setViews(t.getViews()+1);
        trackRepository.save(t);
    }

    @PostMapping("/update/views/{user}/{track}")
    public void updateViewsURL(@PathVariable Long track, @PathVariable Long user) {
        Track t = trackRepository.findById(track).get();
        User u = userRepository.findById(user).get();

        boolean test =viewsRepository.findById(new ViewsID(u, t)).isPresent();

        if (test == false){
            Views v = new Views(new ViewsID(u, t), 1);
            viewsRepository.save(v);
        }else {
            Views v = viewsRepository.findById(new ViewsID(u, t)).get();
            v.setViews(v.getViews()+1);
            viewsRepository.save(v);
        }
    }

    @PostMapping("/rating/like/{user}/{track}")
    public void ratingLike(@PathVariable Long track, @PathVariable Long user) {
        Track t = trackRepository.findById(track).get();
        User u = userRepository.findById(user).get();
        Rating r = new Rating(new RatingID(u, t), true);
        ratingRepository.save(r);
    }

    @DeleteMapping("/rating/dislike/{user}/{track}")
    public void ratingDislike(@PathVariable Long track, @PathVariable Long user) {
        Track t = trackRepository.findById(track).get();
        User u = userRepository.findById(user).get();
        Rating r = ratingRepository.findById(new RatingID(u, t)).get();
        ratingRepository.delete(r);
    }

    @GetMapping("/already/liked/{user}/{track}")
    public boolean isAlreadyLiked(@PathVariable Long track, @PathVariable Long user) {
        Track t = trackRepository.findById(track).get();
        User u = userRepository.findById(user).get();
        boolean exist = ratingRepository.findById(new RatingID(u, t)).isPresent();
        return exist;
    }

    @GetMapping("/dashboard/albums/{id}")
    public List<Album> dashboardAlbums(@PathVariable Long id) {
        return albumRepository.getAlbumNameAndViewsAndRatingByUserAlbumId(id);
    }

    @GetMapping("/dashboard/singles/{id}")
    public List<Track> dashboardSingles(@PathVariable Long id) {
        return trackRepository.getTrackTitleAndViewsAndRatingByUserTrackIdOrderByTrackNumber(id);
    }

    @GetMapping("/dashboard/tracks/{id}")
    public List<Track> dashboardTracks(@PathVariable Long id) {
        return trackRepository.getTrackTitleAndViewsAndRatingByAlbumIdOrderByTrackNumber(id);
    }

    @PutMapping("/update/user/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User u = userRepository.findById(id).get();
        u.setUsername(user.getUsername());
        u.setPassword(encoder.encode(user.getPassword()));
        u.setAddress(user.getAddress());
        u.setCity(user.getCity());
        u.setEmail(user.getEmail());
        u.setBirthdate(user.getBirthdate());
        u.setFirstname(user.getFirstname());
        u.setLastname(user.getLastname());
        u.setGender(user.getGender());
        userRepository.save(u);
    }

    @PutMapping("/update/user/image/{id}")
    public void updateUserImage(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) throws IOException {
        User u = userRepository.findById(id).get();
        u.setImageURL(photo.getBytes());
        userRepository.save(u);
    }

    @PutMapping("/upgrade/{id}")
    public void upgrade(@PathVariable Long id, @RequestBody String nickname) throws IOException {
        User u = userRepository.findById(id).get();
        u.setNickname(nickname);
        u.getRoles().add(roleRepository.findByRolename("ARTIST").get());
        userRepository.save(u);
    }

    @GetMapping("/random/advertisements")
    public Advertisement getAdvertisement() {
        return advertisementRepository.getAdvertisementRandom();
    }

    @GetMapping("/random/advertisements/{id}")
    public Advertisement getAdvertisement2(@PathVariable Long id) {
        return advertisementRepository.getAdvertisementRandom2(id);
    }

    @PutMapping("/update/advertisement/{id}")
    public void updateAdvertisement(@PathVariable Long id) {
        Advertisement advertisement = advertisementRepository.findById(id).get();
        advertisement.setCount(advertisement.getCount()+1);
        advertisementRepository.save(advertisement);
    }

    @PostMapping("/user/advertisement/{user}/{advrt}")
    public void addUserAdvertisement(@PathVariable Long user, @PathVariable Long advrt) {
        Advertisement a = advertisementRepository.findById(advrt).get();
        User u = userRepository.findById(user).get();
        Advert_User au = new Advert_User(new AdvertID(u, a), true);
        advertUserRepository.save(au);
    }

    @PutMapping("/update/price/{track}/{advrt}")
    public void updatePriceTrack(@PathVariable Long track, @PathVariable Long advrt) {
        Track t = trackRepository.findById(track).get();
        Advertisement a = advertisementRepository.findById(advrt).get();
        double price = t.getPrice() + (a.getPercentage() * a.getPrice());
        t.setPrice(price);
        trackRepository.save(t);
    }

    @GetMapping("/sum/track/price/{id}")
    public double sumSinglesPrice(@PathVariable Long id) {
        return trackRepository.sumPriceByUserTrackId(id);
    }

    @GetMapping("/sum/album/price/{id}")
    public double sumAlbumTracksPrice(@PathVariable Long id) {
        return trackRepository.sumPriceByAlbumId(id);
    }

    @GetMapping("/playlist/{id}")
    public List<Object> getPlyalist(@PathVariable Long id) {
        return trackRepository.getPlaylist(id);
    }

    @GetMapping("/advertisements/image/{id}")
    public ResponseEntity<byte[]> getAdvertisementImage(@PathVariable Long id) {
        Optional<Advertisement> fileAdv = Optional.of(advertisementRepository.findById(id).get());

        if(fileAdv.isPresent()) {
            Advertisement file = fileAdv.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "image_"+file.getId() + "\"")
                    .body(file.getImage());
        }

        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/advertisements/logo/{id}")
    public ResponseEntity<byte[]> getAdvertisementLogo(@PathVariable Long id) {
        Optional<Advertisement> fileAdvlogo = Optional.of(advertisementRepository.findById(id).get());

        if(fileAdvlogo.isPresent()) {
            Advertisement filelogo = fileAdvlogo.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "logo_"+filelogo.getId() + "\"")
                    .body(filelogo.getLogo());
        }

        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/artist/image/{id}")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable Long id) {
        Optional<User> fileOptional = Optional.of(userRepository.findById(id).get());

        if(fileOptional.isPresent()) {
            User file = fileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getNickname() + "\"")
                    .body(file.getImageURL());
        }

        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/album/image/{id}")
    public ResponseEntity<byte[]> getAlbumImage(@PathVariable Long id) {
        Optional<Album> fileOptional = Optional.of(albumRepository.findById(id).get());

        if(fileOptional.isPresent()) {
            Album file = fileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getAlbumName() + "\"")
                    .body(file.getAlbumImageURL());
        }

        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/track/image/{id}")
    public ResponseEntity<byte[]> getTrackImage(@PathVariable Long id) {
        Optional<Track> fileOptional = Optional.of(trackRepository.findById(id).get());

        if(fileOptional.isPresent()) {
            Track file = fileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getTrackTitle() + "\"")
                    .body(file.getTrackImageURL());
        }

        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/track/url/{id}")
    public ResponseEntity<byte[]> getTrackURL(@PathVariable Long id) {
        Optional<Track> fileOptional = Optional.of(trackRepository.findById(id).get());

        if(fileOptional.isPresent()) {
            Track file = fileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getTrackURL());
        }

        return ResponseEntity.status(404).body(null);
    }
}

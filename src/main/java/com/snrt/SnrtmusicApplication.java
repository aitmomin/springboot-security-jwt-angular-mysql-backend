package com.snrt;

import com.snrt.repositories.AdvertisementRepository;
import com.snrt.repositories.AlbumRepository;
import com.snrt.repositories.TrackRepository;
import com.snrt.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SnrtmusicApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SnrtmusicApplication.class, args);
	}

	@Bean
	CommandLineRunner start(AdvertisementRepository advertisementRepository, UserRepository userRepository, TrackRepository trackRepository, AlbumRepository albumRepository){
		return args -> {
//            System.out.println("montant : "+trackRepository.sumPriceByUserTrackId(1));
//            System.out.println("montant : "+trackRepository.sumPriceByAlbumId(1));
        };
	}
}

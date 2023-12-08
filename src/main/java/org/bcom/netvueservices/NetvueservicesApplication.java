package org.bcom.netvueservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan
@EnableScheduling
@SpringBootApplication
public class NetvueservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetvueservicesApplication.class, args);
	}

}

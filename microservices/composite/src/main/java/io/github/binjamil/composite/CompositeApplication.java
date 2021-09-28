package io.github.binjamil.composite;

import org.modelmapper.ModelMapper;
import org.modelmapper.jackson.JsonNodeValueReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("io.github.binjamil")
public class CompositeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompositeApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		modelMapper.getConfiguration().addValueReader(new JsonNodeValueReader());
		return modelMapper;
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

package com.rabelo.libook_info;

import com.rabelo.libook_info.view.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibookInfoApplication implements CommandLineRunner {
	@Autowired
	private Menu menu;

	public static void main(String[] args) {
		SpringApplication.run(LibookInfoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        menu.start();
	}
}

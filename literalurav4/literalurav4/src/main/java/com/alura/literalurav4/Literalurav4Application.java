package com.alura.literalurav4;

import com.alura.literalurav4.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class Literalurav4Application{

	@Autowired
	private Main main;

	public static void main(String[] args) {
		SpringApplication.run(Literalurav4Application.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean continueRunning = true;

			while (continueRunning) {
				main.showMenu();
				System.out.print("Seleccione una opción: ");
				int option = scanner.nextInt();
				scanner.nextLine(); // Consumir la nueva línea después de leer el entero

				continueRunning = main.handleOption(option, scanner);
			}

			scanner.close();
			System.out.println("¡Gracias por usar la aplicación Literalura!");
		};
	}

}
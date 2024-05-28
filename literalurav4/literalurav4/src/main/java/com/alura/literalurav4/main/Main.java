package com.alura.literalurav4.main;

import com.alura.literalurav4.models.Author;
import com.alura.literalurav4.models.Book;
import com.alura.literalurav4.repository.BookRepository;
import com.alura.literalurav4.repository.AuthorRepository;
import com.alura.literalurav4.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class Main {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public Main(LibraryService libraryService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.libraryService = libraryService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        var option = -1;
        Scanner scanner = new Scanner(System.in);
        while (option != 0) {
            var menu = """
                    ---------------------------------------------
                    ********** BIENVENIDO A LITERALURA **********
                    ---------------------------------------------
                    Elige alguna de las siguientes opciones:
                    ---------------------------------------------
                    1- Buscar libros por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un año determinado
                    5- Listar libros por idiomas
                    6- Buscar autores por nombre
                    
                    0- Salir de la Aplicación
                    ---------------------------------------------
                    """;

            System.out.println(menu);
            System.out.print("Seleccione una opción: ");

            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consume la nueva línea después de leer el entero
                handleOption(option, scanner);
            } catch (InputMismatchException e) {
                System.out.println("********************");
                System.out.println("¡¡Entrada inválida!!");
                System.out.println("********************");
                scanner.nextLine(); // Consume la entrada inválida
            }
        }
        scanner.close();

    }
    //Scanner scanner = new Scanner(System.in);
    public boolean handleOption(int option, Scanner scanner) {
        // Implementa la lógica para manejar cada opción del menú
        switch (option) {
            case 1:
                buscarLibroPorTitulo(scanner);
                break;
            case 2:
                listarLibrosRegistrados();
                break;
            case 3:
                listarAutoresRegistrados();
                break;
            case 4:
                listarAutoresVivosEnAnio(scanner);
                break;
            case 5:
                listarLibrosPorIdioma(scanner);
                break;
            case 6:
                mostrarEstadisticas();
                break;
            case 0:
                System.out.println("-------------------------");
                System.out.println("Cerrando la aplicacion...");
                System.out.println("-------------------------");
                System.exit(0);
            default:
                System.out.println("********************");
                System.out.println("¡¡Opcion Invalida!!");
                System.out.println("********************");
        }
        return true; // Indica que el menú debe seguir mostrándose
    }

    private void buscarLibroPorTitulo(Scanner scanner){

        System.out.println("-------------------------------------------");
        System.out.println("Ingresa el nombre del Libro que desas buscar: ");
        System.out.println("---------------------------------------------\n");

        String nombreLibro = scanner.nextLine();
        libraryService.searchAndSaveBookByTitle(nombreLibro);

    }

    private void listarLibrosRegistrados(){

        System.out.println("-------------------------------------------");
        System.out.println("Ingresa el nombre del Libro que desas buscar: ");
        System.out.println("---------------------------------------------\n");

        Scanner scanner = new Scanner(System.in);
        String titulo = scanner.nextLine();
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseWithAuthors(titulo);

        if (books.isEmpty()){

            System.out.println("------------------------------------------");
            System.out.println("No se encontró ningún libro con este nombre.");
            System.out.println("--------------------------------------------\n");

        } else {
            for (Book book : books){

                System.out.println("************ DATOS DEL LIBRO ************");
                System.out.println("Título: " + book.getTitle());
                System.out.println("Idioma: " + book.getLanguage());
                System.out.println("Cantidad de descargas: " + book.getDownloadCount());
                System.out.println("********************************************");
                //System.out.println("Autores:");
                for (Author author : book.getAuthors()) {
                    System.out.println("Autor: " + author.getName());
                }
            }
        }
    }


    private void listarAutoresRegistrados() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()){

            System.out.println("-------------------------------------");
            System.out.println("No se encontró ningún autor registrado");
            System.out.println("---------------------------------------\n");

        } else {

            authors.forEach(System.out::println);

        }
    }

    private void listarAutoresVivosEnAnio(Scanner scanner){

        System.out.println("---------------");
        System.out.println("Ingrese el año: ");
        System.out.println("-----------------\n");

        int anio = scanner.nextInt();
        List<Author> authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(anio, anio);
        if (authors.isEmpty()){

            System.out.println("------------------------------------------");
            System.out.println("No se encontraron autores vivos en ese año.");
            System.out.println("-------------------------------------------\n");

        } else {

            authors.forEach(System.out::println);

        }
    }

    private void listarLibrosPorIdioma(Scanner scanner){

        System.out.println("-------------------------------------");
        System.out.println("Ingrese el idioma: " +
                "es: español," +
                "en: Inglés," +
                "pt: Portugués.");
        System.out.println("---------------------------------------\n");

        String idioma = scanner.nextLine();
        List<Book> books = bookRepository.findByLanguage(idioma);
        if (books.isEmpty()){

            System.out.println("-------------------------------------");
            System.out.println("No se encontraron libros en ese idioma.");
            System.out.println("---------------------------------------\n");

        } else {

            books.forEach(System.out::println);

        }
    }

    private void mostrarEstadisticas() {
        libraryService.showStatistics();
    }
}
package br.com.literalura.literalura.principal;

import br.com.literalura.literalura.model.Autor;
import br.com.literalura.literalura.model.DadosLivro;
import br.com.literalura.literalura.model.Livro;
import br.com.literalura.literalura.repository.LivroRepository;
import br.com.literalura.literalura.service.ConsumoApi;
import br.com.literalura.literalura.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private LivroRepository livroRepository;

    private final String API_KEY = "https://gutendex.com/books/?search=";

    public Principal(LivroRepository repositorio) {
        this.livroRepository = repositorio;
    }

    public void exibirMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    1 - Buscar livros pelo título
                    2 - Listar livros registrados
                    3 - Listar Autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                                        
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    getDadosLivro();
                    break;
                case 2:
                    mostrarLivros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    mostrarAutoresVivos();
                    break;
                case 5:
                    pesquiarPorIdiomas();
                    break;

                case 0:
                    System.out.println("Sistema encerrado!");
                    break;
                default:
                    System.out.println("Menu inválido");
                    break;
            }
        }
    }


    private void salvarLivros(List<Livro> livros) {

        livros.forEach(livroRepository::save);
    }

    private void getDadosLivro() {
        System.out.println("Digite o nome do livro: ");
        var nomeLivro = leitura.nextLine();
        var enderecoApi = API_KEY + nomeLivro.replace(" ", "+");
        String json = consumo.obterDados(enderecoApi);
        System.out.println(json);

        JsonNode rootNode = null;
        try {
            rootNode = conversor.getObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode resultsNode = rootNode.path("results");
        String convertedToString = String.valueOf(resultsNode);
        // DadosLivro dados = conversor.obterDados(convertedToString.substring(1, convertedToString.length() -1), DadosLivro.class);

        try {
            List<DadosLivro> livros = conversor.getObjectMapper()
                    .readerForListOf(DadosLivro.class)
                    .readValue(resultsNode);

            if (livros.isEmpty()) {
                System.out.println("Nada encontrado");
            }

            List<Livro> novosLivros = livros.stream().map(Livro::new).collect(Collectors.toList());
            novosLivros.stream().forEach(livroRepository::save);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DataIntegrityViolationException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            ;
        }

    }

    private void mostrarLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.stream().distinct().forEach(System.out::println);
            //livros.forEach(System.out::println);
        }
    }

    private void mostrarAutores() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
        } else {
            livros.stream()
                    .map(Livro::getAutor)
                    .distinct()
                    .forEach(a -> System.out.println(a.getAutor()));
        }


    }

    private void mostrarAutoresVivos() {
        System.out.println("Digite ano: ");
        int ano = leitura.nextInt();
        Year year = Year.of(ano);

        List<Autor> autores = livroRepository.findAutoresVivos(year);
        if (autores.isEmpty()) {
            System.out.println("Não foi encontrado no ano informado!");
        } else {
            autores.forEach(a -> System.out.println("Nome: " + a.getAutor() +
                    "\nAno nascimento: " + a.getAnoNascimento() +
                    "\nAno falescimento: " + a.getAnoFalecimento() +
                    "\n\n-----------------------------------------------------"));
        }


    }

    private void mostrarIdiomas() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            System.out.println("Menu idioma: \n");
            livros.stream().map(Livro::getIdioma)
                    .distinct().forEach(System.out::println);
        }
    }

    private void pesquiarPorIdiomas() {
        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            mostrarIdiomas();
            System.out.println(" ");
            System.out.println("Digite o idioma: ");
            var idioma = leitura.nextLine();

            List<Livro> encontrado = livros.stream().filter(a -> a.getIdioma().equalsIgnoreCase(idioma)).collect(Collectors.toList());

            if (encontrado.isEmpty()) {
                System.out.println("Idioma não encontrado! ");
            } else {
                encontrado.forEach(a -> System.out.println(a.getTitulo()));
            }
        }
    }


}
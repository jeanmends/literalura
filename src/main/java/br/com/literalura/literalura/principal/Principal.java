package br.com.literalura.literalura.principal;

import br.com.literalura.literalura.model.DadosLivro;
import br.com.literalura.literalura.model.Livro;
import br.com.literalura.literalura.repository.LivroRepository;
import br.com.literalura.literalura.service.ConsumoApi;
import br.com.literalura.literalura.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
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
                    mostrarAtuores();
                    break;


            }
        }
    }



    private void salvarLivros(List<Livro> livros){
        livros.forEach(livroRepository::save);
    }
    private void getDadosLivro(){
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

            List<Livro> novosLivros = livros.stream().map(Livro::new).collect(Collectors.toList());
            //System.out.println(novosLivros);
            salvarLivros(novosLivros);

        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private void mostrarAtuores() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            livros.stream()
                    .map(Livro::getAutor)
                    .distinct()
                    .forEach(autor -> System.out.println(autor.getAutor()));
        }
    }

}
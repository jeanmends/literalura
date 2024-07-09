package br.com.literalura.literalura.repository;

import br.com.literalura.literalura.model.Autor;
import br.com.literalura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository  extends JpaRepository <Livro, Long> {

}

package br.com.literalura.literalura.repository;

import br.com.literalura.literalura.model.Autor;
import br.com.literalura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.Date;
import java.util.List;

public interface LivroRepository  extends JpaRepository <Livro, Long> {
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivos(@Param("ano") Year year);


}

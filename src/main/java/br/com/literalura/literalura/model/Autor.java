package br.com.literalura.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String autor;

    @Column(name = "ano_nascimento")
    private Year anoNascimento;

    @Column(name = "ano_falecimento")
    private Year anoFalecimento;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    public Autor(){}
    public Autor(Long id, String autor, Year anoNascimento, Year anoFalecimento, List<Livro> livros) {
        this.id = id;
        this.autor = autor;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
        this.livros = livros;
    }

    public Autor(DadosAutor autorDTO) {
        this.autor = autorDTO.autor();
        this.anoNascimento = autorDTO.anoNascimento() != null ? Year.of(autorDTO.anoNascimento()) : null;
        this.anoFalecimento = autorDTO.anoFalecimento() != null ? Year.of(autorDTO.anoFalecimento()) : null;
    }

    public Autor(String autor, Year anoNascimento, Year anoFalecimento) {
        this.autor = autor;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Year getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Year anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public Year getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(Year anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        return  autor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor autor1)) return false;
        return Objects.equals(getAutor(), autor1.getAutor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAutor());
    }
}

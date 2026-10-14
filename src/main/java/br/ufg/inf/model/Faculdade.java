package br.ufg.inf.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "faculdade")
public class Faculdade {

    @Id
    private String id;
    private String nome;
    private String sigla;
    private String cidade;

    public Faculdade() {}

    public Faculdade(String nome, String sigla, String cidade) {
        this.nome = nome;
        this.sigla = sigla;
        this.cidade = cidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}

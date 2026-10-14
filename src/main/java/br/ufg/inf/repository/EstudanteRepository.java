package br.ufg.inf.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.ufg.inf.model.Estudante;
import br.ufg.inf.model.Faculdade;

@Repository
public interface EstudanteRepository extends MongoRepository<Estudante, String> {

    List<Estudante> findByFaculdade(Faculdade faculdade);

    boolean existsByFaculdade(Faculdade faculdade);
}
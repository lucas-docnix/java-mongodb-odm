package br.ufg.inf.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.ufg.inf.model.Faculdade;

@Repository
public interface FaculdadeRepository extends MongoRepository<Faculdade, String> {
}

package br.ufg.inf.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import br.ufg.inf.model.Estudante;

@DataMongoTest
class EstudanteRepositoryTest {

    @Autowired
    private EstudanteRepository repo;

    @Test
    void testSalvarEConsultar() {
        // Cria e salva um estudante
        Estudante e = new Estudante("Maria Teste", "1990-01-01", 12345);
        Estudante salvo = repo.save(e);

        // Verifica se o ID foi gerado
        assertNotNull(salvo.getId());

        // Busca todos os estudantes
        List<Estudante> lista = repo.findAll();

        // Verifica se há pelo menos um
        assertTrue(lista.size() > 0);
    }
}
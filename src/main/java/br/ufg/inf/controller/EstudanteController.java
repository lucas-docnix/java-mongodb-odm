package br.ufg.inf.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufg.inf.model.Estudante;
import br.ufg.inf.model.Faculdade;
import br.ufg.inf.repository.EstudanteRepository;
import br.ufg.inf.repository.FaculdadeRepository;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

    private final EstudanteRepository repo;
    private final FaculdadeRepository faculdadeRepo;

    public EstudanteController(EstudanteRepository repo, FaculdadeRepository faculdadeRepo) {
        this.repo = repo;
        this.faculdadeRepo = faculdadeRepo;
    }

    @GetMapping
    public List<Estudante> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Estudante> salvar(@RequestBody Estudante e) {
        if (faculdadeInvalida(e.getFaculdade())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.save(e));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudante> buscarPorId(@PathVariable String id) {
        return ResponseEntity.of(repo.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudante> atualizar(@PathVariable String id, @RequestBody Estudante estudante) {
        if (faculdadeInvalida(estudante.getFaculdade())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(repo.findById(id).map(existente -> {
            existente.setNomeCompleto(estudante.getNomeCompleto());
            existente.setDataDeNascimento(estudante.getDataDeNascimento());
            existente.setMatricula(estudante.getMatricula());
            existente.setFaculdade(estudante.getFaculdade());
            return repo.save(existente);
        }));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // A faculdade é opcional, mas se vier no JSON precisa existir no banco
    private boolean faculdadeInvalida(Faculdade faculdade) {
        return faculdade != null
                && (faculdade.getId() == null || !faculdadeRepo.existsById(faculdade.getId()));
    }
}
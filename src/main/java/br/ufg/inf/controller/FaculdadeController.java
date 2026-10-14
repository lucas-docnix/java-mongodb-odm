package br.ufg.inf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufg.inf.model.Estudante;
import br.ufg.inf.model.Faculdade;
import br.ufg.inf.repository.EstudanteRepository;
import br.ufg.inf.repository.FaculdadeRepository;

@RestController
@RequestMapping("/faculdades")
public class FaculdadeController {

    private final FaculdadeRepository repo;
    private final EstudanteRepository estudanteRepo;

    public FaculdadeController(FaculdadeRepository repo, EstudanteRepository estudanteRepo) {
        this.repo = repo;
        this.estudanteRepo = estudanteRepo;
    }

    @GetMapping
    public List<Faculdade> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Faculdade salvar(@RequestBody Faculdade f) {
        return repo.save(f);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculdade> buscarPorId(@PathVariable String id) {
        return ResponseEntity.of(repo.findById(id));
    }

    @GetMapping("/{id}/estudantes")
    public ResponseEntity<List<Estudante>> listarEstudantes(@PathVariable String id) {
        return ResponseEntity.of(repo.findById(id).map(estudanteRepo::findByFaculdade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculdade> atualizar(@PathVariable String id, @RequestBody Faculdade faculdade) {
        return ResponseEntity.of(repo.findById(id).map(existente -> {
            existente.setNome(faculdade.getNome());
            existente.setSigla(faculdade.getSigla());
            existente.setCidade(faculdade.getCidade());
            return repo.save(existente);
        }));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        var faculdade = repo.findById(id);
        if (faculdade.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Não remove uma faculdade que ainda tem estudantes apontando para ela
        if (estudanteRepo.existsByFaculdade(faculdade.get())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

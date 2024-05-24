package samleticias.desafiocreaapi.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import samleticias.desafiocreaapi.domain.entities.Professional;
import samleticias.desafiocreaapi.exceptions.InvalidActionException;
import samleticias.desafiocreaapi.exceptions.ProfessionalNotFoundException;
import samleticias.desafiocreaapi.services.ProfessionalService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/professionals")
public class ProfessionalController {
    @Autowired
    private ProfessionalService professionalService;

    @GetMapping
    public ResponseEntity<List<Professional>> findAll(){
        List<Professional> list = professionalService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<Professional> findProfessionalById(@PathVariable Integer id) throws ProfessionalNotFoundException {
        Professional obj = professionalService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Professional> insertProfessional(@RequestBody Professional obj) {
        obj = professionalService.saveProfessional(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Integer id) throws ProfessionalNotFoundException {
        professionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Professional> updateProfessional(@RequestBody Professional obj) throws ProfessionalNotFoundException, InvalidActionException {
        obj = professionalService.update(obj);
        return ResponseEntity.ok().body(obj);
    }


}

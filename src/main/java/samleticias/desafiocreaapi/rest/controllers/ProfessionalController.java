package samleticias.desafiocreaapi.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import samleticias.desafiocreaapi.domain.entities.Professional;
import samleticias.desafiocreaapi.domain.entities.Title;
import samleticias.desafiocreaapi.exceptions.*;
import samleticias.desafiocreaapi.rest.dto.ProfessionalDTO;
import samleticias.desafiocreaapi.rest.dto.TitlePK;
import samleticias.desafiocreaapi.services.ProfessionalService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/professional")
public class ProfessionalController {
    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

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

    @GetMapping("/email/{email}")
    public ResponseEntity<Professional> findProfessionalByEmail(@PathVariable String email) throws ProfessionalNotFoundException {
        Professional obj = professionalService.findProfessionalByEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/{uniqueCode}")
    public ResponseEntity<Professional> findByUniqueCode(@PathVariable String uniqueCode) throws ProfessionalNotFoundException {
        Professional obj = professionalService.getByUniqueCode(uniqueCode);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Professional> insertProfessional(@RequestBody ProfessionalDTO dto) throws AlreadyExistException, InvalidActionException {
        Professional obj = professionalService.registerProfessional(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Integer id) throws ProfessionalNotFoundException {
        professionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Professional> updateProfessional(@RequestBody Professional professional) throws ProfessionalNotFoundException, InvalidActionException {
        Professional obj = professionalService.update(professional);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping("/{professionalId}/add-professional-title")
    public ResponseEntity<Professional> addProfessionalTitle(@PathVariable(name = "professionalId") Integer professionalId, @RequestBody TitlePK titlePK)
            throws TitleNotFoundException, ProfessionalNotFoundException, InvalidActionException, DuplicateResourceException
    {
        Professional obj = professionalService.insertTitleForProfessional(professionalId, titlePK);
        return ResponseEntity.ok().body(obj);
    }


}

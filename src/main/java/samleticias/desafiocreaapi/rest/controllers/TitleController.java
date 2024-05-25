package samleticias.desafiocreaapi.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import samleticias.desafiocreaapi.domain.entities.Title;
import samleticias.desafiocreaapi.exceptions.AlreadyExistException;
import samleticias.desafiocreaapi.exceptions.InvalidActionException;
import samleticias.desafiocreaapi.exceptions.TitleNotFoundException;
import samleticias.desafiocreaapi.rest.dto.TitleDTO;
import samleticias.desafiocreaapi.services.TitleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/titles")
public class TitleController {
    private final TitleService titleService;
    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping
    public ResponseEntity<List<Title>> findAll(){
        List<Title> list = titleService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<Title> findTitleById(@PathVariable Integer id) throws TitleNotFoundException {
        Title obj = titleService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Title> insertTitle(@RequestBody TitleDTO dto) throws AlreadyExistException, InvalidActionException {
        Title obj = titleService.registerTitle(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteTitle(@PathVariable Integer id) throws TitleNotFoundException {
        titleService.deleteTitle(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Title> updateProfessional(@RequestBody TitleDTO dto) throws TitleNotFoundException, InvalidActionException {
        Title obj = new Title(dto);
        obj = titleService.update(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping
    public ResponseEntity<Title> update(@RequestBody Title title){
        return new ResponseEntity<>(titleService.update(title), HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Title> updateTitle(@RequestBody TitleDTO dto) throws TitleNotFoundException, InvalidActionException {
        Title obj = new Title(dto);
        obj = titleService.update(obj);
        return ResponseEntity.ok().body(obj);
    }
}

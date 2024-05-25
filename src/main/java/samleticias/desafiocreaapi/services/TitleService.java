package samleticias.desafiocreaapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samleticias.desafiocreaapi.domain.entities.Title;
import samleticias.desafiocreaapi.domain.repositories.TitleRepository;
import samleticias.desafiocreaapi.exceptions.TitleNotFoundException;
import samleticias.desafiocreaapi.rest.dto.TitleDTO;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {
    private final TitleRepository titleRepository;
    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public Title registerTitle(TitleDTO titleDTO){
        Title title = new Title();
        title.setDescription(titleDTO.description());

        this.saveTitle(title);
        return title;
    }

    public List<Title> findAll(){
        return titleRepository.findAll();
    }

    public Title findById(Integer id) throws TitleNotFoundException {
        Optional<Title> obj = titleRepository.findById(id);
        if(obj.isEmpty()) throw new TitleNotFoundException("Título não encontrado.");
        return obj.get();
    }
    public void saveTitle(Title title){
        this.titleRepository.save(title);
    }

    public void deleteTitle(Integer id){
        titleRepository.deleteById(id);
    }

    public Title update(Title title){
        titleRepository.save(title);
        return title;
    }

}

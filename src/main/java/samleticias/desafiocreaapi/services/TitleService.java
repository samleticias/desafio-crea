package samleticias.desafiocreaapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samleticias.desafiocreaapi.domain.entities.Title;
import samleticias.desafiocreaapi.domain.repositories.TitleRepository;
import samleticias.desafiocreaapi.exceptions.TitleNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {
    @Autowired
    private TitleRepository titleRepository;

    public List<Title> findAll(){
        return titleRepository.findAll();
    }

    public Title findById(Integer id) throws TitleNotFoundException {
        Optional<Title> obj = titleRepository.findById(id);
        return obj.orElseThrow(() -> new TitleNotFoundException("Título não encontrado."));
    }

    public Title saveTitle(Title title){
        return titleRepository.save(title);
    }

    public void deleteTitle(Integer id) throws TitleNotFoundException {
        Optional<Title> title = titleRepository.findById(id);
        if(title.isEmpty()) throw new TitleNotFoundException("Título não encontrado para deletar.");

        titleRepository.delete(title.get());
    }
}

package samleticias.desafiocreaapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samleticias.desafiocreaapi.domain.entities.Professional;
import samleticias.desafiocreaapi.domain.entities.enums.ProfessionalType;
import samleticias.desafiocreaapi.domain.repositories.ProfessionalRepository;
import samleticias.desafiocreaapi.exceptions.InvalidActionException;
import samleticias.desafiocreaapi.exceptions.ProfessionalNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {
    @Autowired
    private ProfessionalRepository professionalRepository;

    public List<Professional> findAll(){
        return professionalRepository.findAll();
    }

    public Professional findById(Integer id) throws ProfessionalNotFoundException {
        Optional<Professional> obj = professionalRepository.findById(id);
        return obj.orElseThrow(() -> new ProfessionalNotFoundException("Profissional não encontrado."));
    }

    public Professional findProfessionalByEmail(String email) throws ProfessionalNotFoundException {
        Optional<Professional> obj = professionalRepository.findProfessionalByEmail(email);
        return obj.orElseThrow(() -> new ProfessionalNotFoundException("Profissional não encontrado."));
    }

    public Professional saveProfessional (Professional professional){
        return professionalRepository.save(professional);
    }

    public void delete(Integer id) throws ProfessionalNotFoundException {
        Optional<Professional> professional = professionalRepository.findById(id);
        if(professional.isEmpty()) throw new ProfessionalNotFoundException("Profissional não encontrado.");

        professionalRepository.delete(professional.get());
    }

    private void verifyProfessionalType(Professional professional) throws InvalidActionException {
        if(professional.getProfessionalType() == ProfessionalType.REGISTERED && professional.getVisaDate() != null){
            throw new InvalidActionException("Não é permitido adicionar data de visto para um profissional do tipo registrado");
        }
        if(professional.getProfessionalType() == ProfessionalType.ENDORSED && professional.getRegistrationDate() != null){
            throw new InvalidActionException("Não é permitido adicionar data de registro para um profissional do tipo visado");
        }
    }

    public Professional update(Professional professionalToEdit) throws ProfessionalNotFoundException, InvalidActionException {
        Professional professionalBefore = findById(professionalToEdit.getId());

        verifyProfessionalType(professionalToEdit);

        // verifica e-mail
        if(!professionalToEdit.getEmail().equals(professionalBefore.getEmail())){
            if(professionalRepository.findProfessionalByEmail(professionalToEdit.getEmail()).isPresent()){
                throw new InvalidActionException("O email fornecido já está sendo utilizado");
            }
        }

        // verifica código único
        if(!professionalToEdit.getUniqueCode().equals(professionalBefore.getUniqueCode())){
            if(professionalRepository.findProfessionalByUniqueCode(professionalToEdit.getUniqueCode()).isPresent()){
                throw new InvalidActionException("O código único fornecido já está sendo utilizado");
            }
        }

        this.saveProfessional(professionalToEdit);
        return professionalToEdit;
    }

}

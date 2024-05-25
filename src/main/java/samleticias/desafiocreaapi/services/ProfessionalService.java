package samleticias.desafiocreaapi.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samleticias.desafiocreaapi.domain.entities.Professional;
import samleticias.desafiocreaapi.domain.entities.Title;
import samleticias.desafiocreaapi.domain.entities.enums.ProfessionalType;
import samleticias.desafiocreaapi.domain.entities.enums.RegistrationStatus;
import samleticias.desafiocreaapi.domain.repositories.ProfessionalRepository;
import samleticias.desafiocreaapi.domain.repositories.TitleRepository;
import samleticias.desafiocreaapi.exceptions.*;
import samleticias.desafiocreaapi.rest.dto.ProfessionalDTO;
import samleticias.desafiocreaapi.rest.dto.TitleIdDTO;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {
    private final ProfessionalRepository professionalRepository;
    private final TitleRepository titleRepository;

    private final TitleService titleService;

    public ProfessionalService(ProfessionalRepository professionalRepository,
                               TitleRepository titleRepository,
                               TitleService titleService)
    {
        this.professionalRepository = professionalRepository;
        this.titleRepository = titleRepository;
        this.titleService = titleService;
    }

    public List<Professional> findAll(){
        return professionalRepository.findAll();
    }

    public Professional registerProfessional(ProfessionalDTO professionalDTO) throws AlreadyExistException, InvalidActionException {
        if(professionalRepository.findProfessionalByEmail(professionalDTO.email()).isPresent()){
            throw new AlreadyExistException("Já existe um profissional com o e-mail informado.");
        }
        Professional professional = new Professional(professionalDTO);
        verifyProfessionalType(professional);

        this.saveProfessional(professional);
        return professional;
    }

    public Professional findById(Integer id) throws ProfessionalNotFoundException {
        Optional<Professional> obj = professionalRepository.findById(id);
        if (obj.isEmpty()) throw new ProfessionalNotFoundException("Profissional não encontrado com o ID informado.");
        return obj.get();
    }

    public Professional findProfessionalByEmail(String email) throws ProfessionalNotFoundException {
        Optional<Professional> obj = professionalRepository.findProfessionalByEmail(email);
        if (obj.isEmpty()) throw new ProfessionalNotFoundException("Profissional não encontrado com id com esse email.");
        return obj.get();
    }

    public Professional saveProfessional (Professional professional){
        return professionalRepository.save(professional);
    }

    public Professional getByUniqueCode(String uniqueCode) throws ProfessionalNotFoundException {
        Optional<Professional> obj = professionalRepository.findProfessionalByUniqueCode(uniqueCode);
        if(obj.isEmpty()) throw new ProfessionalNotFoundException("Profissional não encontrado com o código único informado.");
        return obj.get();
    }

    public void delete(Integer id) throws ProfessionalNotFoundException {
        Optional<Professional> professional = professionalRepository.findById(id);
        if(professional.isEmpty()) throw new ProfessionalNotFoundException("Profissional não encontrado para deletar.");

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

    public Professional insertTitleForProfessional(Integer professionalId, TitleIdDTO dto)
            throws ProfessionalNotFoundException,
            DuplicateResourceException,
            InvalidActionException,
            TitleNotFoundException
    {
        Optional<Professional> professionalAlternative = professionalRepository.findById(professionalId);

        Optional<Title> titleAlternative = titleRepository.findById(dto.titleId());

        if(professionalAlternative.isEmpty()) throw new ProfessionalNotFoundException("Profissional informado não foi encontrado na base de dados.");
        if(titleAlternative.isEmpty()) throw new TitleNotFoundException("Título informado não foi encontrado na base de dados.");

        Professional professional = professionalAlternative.get();
        Title title = titleAlternative.get();


        if(isProfessionalCancelled(professional))
        {
            throw new InvalidActionException("Não é possível atribuir um título a um profissional com registro cancelado.");
        } else {
            if(professional.getRegistrationStatus() == null)
            {
                activateProfessionalStatus(professional);
                professional.setUniqueCode();
            }
            if(checkProfessionalTitle(professional, title))
            {
                throw new DuplicateResourceException("O título informado já está associado ao profissional.");
            }
            professional.assignProfessionalTitle(title);
        }

        this.saveProfessional(professional);

        return professional;
    }

    public Professional activeProfessional(Integer id) throws ProfessionalNotFoundException, ExistingItemException {
        Professional professional = findById(id);

        if(checkProfessionalActivity(professional)) throw new ExistingItemException("O profissional já é ativo.");

        activateProfessionalStatus(professional);
        saveProfessional(professional);
        return professional;
    }

    private boolean checkProfessionalExistence(Professional professional){
        return professionalRepository.findById(professional.getId()).isPresent();
    }

    private boolean checkProfessionalActivity(Professional professional){
        return professional.getRegistrationStatus() == RegistrationStatus.ACTIVE;
    }

    private boolean isProfessionalCancelled(Professional professional){
        return professional.getRegistrationStatus() == RegistrationStatus.CANCELED;
    }

    private void activateProfessionalStatus(Professional professional) {
        professional.setRegistrationStatus(RegistrationStatus.ACTIVE);
    }

    private boolean checkProfessionalTitle(Professional professional, Title titleForValidation){
        for(Title title : professional.getTitles()){
            if(title.equals(titleForValidation)) return true;
        }
        return false;
    }






}

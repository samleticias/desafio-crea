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
        checkProfessionalType(professional);

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

    // verifica tipo do profissional, se é visado ou registrado
    private void checkProfessionalType(Professional professional) throws InvalidActionException {
        if(professional.getProfessionalType() == ProfessionalType.REGISTERED && professional.getVisaDate() != null){
            throw new InvalidActionException("Não é possível atribuir uma data de visto a um profissional com tipo registrado.");
        }
        if(professional.getProfessionalType() == ProfessionalType.ENDORSED && professional.getRegistrationDate() != null){
            throw new InvalidActionException("Não é atribuir data de registro para um profissional do tipo visado");
        }
    }

    public Professional update(Professional professionalToEdit) throws ProfessionalNotFoundException, InvalidActionException {
        Professional professionalBefore = findById(professionalToEdit.getId());

        checkProfessionalType(professionalToEdit);

        // verifica email
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

        // verifica se profissional possui registro cancelado
        if(isProfessionalCancelled(professional))
        {
            throw new InvalidActionException("Não é possível atribuir um título a um profissional com registro cancelado.");
        } else {
            if(professional.getRegistrationStatus() == null)
            {
                // realiza ativação do registro

                activateProfessionalStatus(professional);
                professional.setUniqueCode();
            }
            if(checkProfessionalTitle(professional, title))
            {
                // verifica se o profissional já possui o título a ser verificado
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

    public Professional disableProfessional(Integer id) throws ProfessionalNotFoundException, ExistingItemException {
        Professional professional = findById(id);

        // verifica se o profissional já possui registro inativo
        if(checkProfessionalInactivity(professional)) throw new ExistingItemException("O profissional já é inativo.");

        professional.setRegistrationStatus(RegistrationStatus.INACTIVE);
        saveProfessional(professional);
        return professional;
    }

    public Professional cancelProfessional(Integer id) throws ProfessionalNotFoundException, ExistingItemException {
        Professional professional = findById(id);

        // verifica se profissional já está cancelado
        if(isProfessionalCancelled(professional)) throw new ExistingItemException("O profissional já está cancelado.");

        professional.setRegistrationStatus(RegistrationStatus.CANCELED);
        saveProfessional(professional);
        return professional;
    }

    // verifica se o profissional está presente na base de dados
    private boolean checkProfessionalExistence(Professional professional){
        return professionalRepository.findById(professional.getId()).isPresent();
    }

    // verifica se o profissional já possui registro ativo
    private boolean checkProfessionalActivity(Professional professional){
        return professional.getRegistrationStatus() == RegistrationStatus.ACTIVE;
    }

    // verifica se profissional possui registro inativo
    private boolean checkProfessionalInactivity(Professional professional){
        return professional.getRegistrationStatus() == RegistrationStatus.INACTIVE;
    }

    // verifica se o profissional possui registro cancelado
    private boolean isProfessionalCancelled(Professional professional){
        return professional.getRegistrationStatus() == RegistrationStatus.CANCELED;
    }

    // ativação do registro do profissional
    private void activateProfessionalStatus(Professional professional) {
        professional.setRegistrationStatus(RegistrationStatus.ACTIVE);
    }

    // faz comparação para verificar se o título desejado já está na lista de tíyulos do profissional
    private boolean checkProfessionalTitle(Professional professional, Title titleForValidation){
        for(Title title : professional.getTitles()){
            if(title.equals(titleForValidation)) return true;
        }
        return false;
    }






}

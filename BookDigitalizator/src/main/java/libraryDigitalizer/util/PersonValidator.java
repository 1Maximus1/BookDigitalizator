package libraryDigitalizer.util;

import libraryDigitalizer.model.Person;
import libraryDigitalizer.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService, PeopleService peopleService1) {
        this.peopleService = peopleService1;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if(peopleService.getPersonByFullName(person.getName()).isPresent()){
            errors.rejectValue("name", "", "This name has already taken!");
        }
    }
}

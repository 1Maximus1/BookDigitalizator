package libraryDigitalizer.services;

import libraryDigitalizer.model.Book;
import libraryDigitalizer.model.Person;
import libraryDigitalizer.repositories.PeopleRepository;
import libraryDigitalizer.util.UserNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int personId){
        Optional<Person> person = peopleRepository.findById(personId);
        return person.orElseThrow(()->new UserNotFoundException("User with this id not found"));
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }


    @Transactional
    public void update(int person_id, Person updatedPerson){
        updatedPerson.setPersonId(person_id);
        peopleRepository.save(updatedPerson);
    }


    @Transactional
    public void delete(int person_id){
        peopleRepository.deleteById(person_id);
    }

    public Optional<Person> getPersonByFullName(String name){
        return peopleRepository.findByName(name);
    }

    public List<Book> getBooksByPersonId(int personId){
        Optional<Person> person = peopleRepository.findById(personId);

        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if(diffInMillis > 864000000){
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}

package libraryDigitalizer.services;

import libraryDigitalizer.model.Book;
import libraryDigitalizer.model.Person;
import libraryDigitalizer.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear){
        if (sortByYear){
            return booksRepository.findAll(Sort.by("yearOfProduction"));
        }else {
            return booksRepository.findAll();
        }
    }

    public List<Book> findWithPagination(Integer pageN, Integer booksPerPage, boolean sortByYear){
        if (sortByYear){
            return booksRepository.findAll(PageRequest.of(pageN, booksPerPage, Sort.by("yearOfProduction"))).getContent();
        }else {
            return booksRepository.findAll(PageRequest.of(pageN, booksPerPage)).getContent();
        }
    }

    public Book findOne(int bookId){
        return booksRepository.findById(bookId).orElse(null);
    }

    public List<Book> searchByTitle(String query){
        return booksRepository.findByTitleStartingWith(query);
    }

    @Transactional
    public void save(Book newBook){
        booksRepository.save(newBook);
    }

    @Transactional
    public void update(int bookId, Book updatedBook) {
        /*
         * 'updatedBook' is transient and not known by Hibernate.
         * Therefore, we retrieve the existing book from the database using bookId.
         * We then set the bookId of updatedBook to ensure Hibernate identifies it as an existing record.
         * We also set the owner from the retrieved book to maintain the relationship, as the owner in updatedBook may be null from the form submission.
         *
         * Hibernate will detect that the 'bookId' matches an existing record and update the corresponding entry in the table.
         */
        Book book = booksRepository.findById(bookId).orElse(null);

        updatedBook.setBookId(bookId);
        updatedBook.setOwner(book.getOwner()); // Maintain the relationship by setting the owner from the existing book

        booksRepository.save(updatedBook);
    }


    @Transactional
    public void delete(int bookId){
        booksRepository.deleteById(bookId);
    }

    public Person getBookOwner(int bookId){
        return booksRepository.findById(bookId).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void appointing(int bookId, Person appointPerson) {
        /*
         * Why is it not necessary to call booksRepository.save(book)?
         * Because we are using setters on an entity that is already in the persistence context.
         * We retrieved this book using booksRepository.findById(bookId) — it is being tracked by Hibernate.
         * Therefore, we can simply call the setters, and Hibernate will automatically update the database table.
         */

        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(appointPerson);
                    book.setTakenAt(new Date());
                }
        );
    }


    @Transactional
    public void release(int bookId) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                }
        );
    }



}

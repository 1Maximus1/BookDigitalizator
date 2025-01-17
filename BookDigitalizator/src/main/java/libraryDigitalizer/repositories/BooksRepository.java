package libraryDigitalizer.repositories;


import libraryDigitalizer.model.Book;
import libraryDigitalizer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
          List<Book> findByTitleStartingWith(String title);

    //    List<Book> findBooksByTitleContaining(String s);
}

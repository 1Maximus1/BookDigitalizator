package libraryDigitalizer.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class BookDAO {

//    private final EntityManager entityManager;
//
//    @Autowired
//    public BookDAO(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//
//    public Person searchOfOwner(int bookId) {
//        Session session = entityManager.unwrap(Session.class);
//
//        Query<Person> person = session.createQuery("select b.owner from Book b join fetch b.owner where b.id = :bookId",Person.class);
//        person.setParameter("bookId", bookId);
//        return person.stream().findAny().orElse(null);
//
//    }
//
//    public Person findOwnerByBookId(int bookId){
//        Session session = entityManager.unwrap(Session.class);
//        return session.createQuery("SELECT b.owner from Book b where b.bookId=:bookId", Person.class).setParameter("bookId", bookId).stream().findAny().orElse(null);
//    }
//
//    @Transactional
//    public void appointing(int bookId, Person appointPerson){
//        Session session = entityManager.unwrap(Session.class);
//        session.createQuery("UPDATE Book b set b.owner=:appointPerson where b.id=:bookId").setParameter("appointPerson", appointPerson).setParameter("bookId", bookId).executeUpdate();
//    }
//
//    @Transactional
//    public void release(int bookId) {
//        Session session = entityManager.unwrap(Session.class);
//        session.createQuery("UPDATE Book b set b.owner=:appointPerson where b.id=:bookId").setParameter("appointPerson", null).setParameter("bookId", bookId).executeUpdate();
//    }

}

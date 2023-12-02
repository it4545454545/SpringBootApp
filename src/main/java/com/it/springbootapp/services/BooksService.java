package com.it.springbootapp.services;

import com.springjpa.app.models.Book;
import com.springjpa.app.models.Person;
import com.springjpa.app.repositories.BooksRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ivan L
 */
@Service
// makes all public method transactional(readOnly=true) but @Transactional without readnonly will allow the DB write actions
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    PeopleService peopleService;
    EntityManager entityManager;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    //pagination
    public List<Integer> getPageNumbers(int booksPerPage) {
        if( booksPerPage == 0) booksPerPage = 1;
        int numberOfPages = findAll().size() / booksPerPage;
        List<Integer> listOfPages = IntStream.rangeClosed(1, numberOfPages)
                .boxed()
                .collect(Collectors.toUnmodifiableList());

        return listOfPages;
    }

    public List<Book> findAll(int pageNumber, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage)).getContent();
    }

    //sorting
    public List<Book> findAll(boolean sortByYear) {
        return booksRepository.findAll(Sort.by("issueDate"));
    }

    //pagination + sorting
    public List<Book> findAll(int pageNumber, int booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage, Sort.by("issueDate"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage)).getContent();
        }
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book Book) {
        booksRepository.save(Book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        //save sees the id and updates instead of creating a new one
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findByPersonOfBook(Person person) {
        return booksRepository.findByPersonOfBook(person);
    }

    @Transactional
    public void setPersonToBook(int bookId, Person newPerson) {
        Optional<Book> book = getBookProxy(bookId);
        book.ifPresent(value -> value.setPersonOfBook(newPerson));
    }

    @Transactional
    public Optional<Book> getBookProxy(int bookId) {
        try (Session session = entityManager.unwrap(Session.class);
        ) {
            return Optional.of(session.load(Book.class, bookId));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void test() {
        System.out.println("f");
    }
}

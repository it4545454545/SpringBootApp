package com.it.springbootapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.ColumnTransformer;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    @NotEmpty
    private String title;
    @Column(name = "author")
    @NotEmpty
    private String author;

    @Column(name = "date")
    @ColumnTransformer(read = "to_char(date, 'YYYY-MM-DD')", write = "to_date(?,'YYYY-MM-DD')")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "The format must be 2022-09-29")
    private String issueDate;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person personOfBook;

    @Column(name = "assign_timestamp")
    private Timestamp timestamp;
    @Transient
    private boolean isOverdue;
    public Book() {
    }
    public Book(int id, String title, String author, String issueDate, Person person) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issueDate = issueDate;
        this.personOfBook = person;
    }

    public Person getPersonOfBook() {
        return personOfBook;
    }

    public void setPersonOfBook(Person owner) {
        this.personOfBook = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (!Objects.equals(title, book.title)) return false;
        if (!Objects.equals(author, book.author)) return false;
        return Objects.equals(issueDate, book.issueDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (issueDate != null ? issueDate.hashCode() : 0);
        return result;
    }
}

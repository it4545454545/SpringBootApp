package com.it.springbootapp.repositories;


import com.it.springbootapp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ivan L
 */
@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {

}

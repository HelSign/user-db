package ua.com.helsign.userdb.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ua.com.helsign.userdb.model.entity.Person;

import java.util.List;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long>{
    List<Person> findByLastName(@Param("lastName") String name);
}

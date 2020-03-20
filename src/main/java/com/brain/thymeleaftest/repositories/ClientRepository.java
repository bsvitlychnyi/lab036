package com.brain.thymeleaftest.repositories;

import com.brain.thymeleaftest.entities.Client;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findByName(String name);

    @Query("select obj from Client obj order by name ASC ")
    List<Client> findAllAsc();

    @Query("select obj from Client obj order by name desc ")
    List<Client> findAllDesc();

    @Query("select obj from Client obj where obj.phone LIKE %:phone% order by name ASC ")
    List<Client> findByPhoneAsc(@Param("phone") String phone);

    @Query("select obj from Client obj where obj.phone LIKE %:phone% order by name desc ")
    List<Client> findByPhoneDesc(@Param("phone") String phone);
}

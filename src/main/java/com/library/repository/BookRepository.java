package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAll();

    List<Book> findBookByTitleContaining(String title);

    @Modifying
    @Query("""
            UPDATE Book b
            SET b.isAvailable = :available
            WHERE b.id = :id
            """)
    void updateAvailabilityById(@Param("id") int id, @Param("available") boolean available);

}

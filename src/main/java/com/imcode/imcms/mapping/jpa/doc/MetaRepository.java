package com.imcode.imcms.mapping.jpa.doc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Integer> {

    @Query("SELECT m.id FROM DocMeta m")
    List<Integer> findAllIds();

    @Query("SELECT m.id FROM DocMeta m WHERE m.id BETWEEN ?1 and ?2")
    List<Integer> findIdsBetween(int from, int to);

    @Query("SELECT min(m.id) FROM DocMeta m")
    Integer findMinId();

    @Query("SELECT max(m.id) FROM DocMeta m")
    Integer findMaxId();

    @Query("SELECT min(m.id), max(m.id) FROM DocMeta m")
    Integer[] findMinAndMaxId();
}

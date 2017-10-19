package com.jonydog.refy.daos;

import com.jonydog.refy.model.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceDAO extends JpaRepository<Reference,Long> {

    @Query(" select r from Reference r where r.title LIKE %:term%  or r.authorsNames LIKE %:term% ")
    List<Reference> searchReferences(@Param("term") String term);

}

package com.jonydog.refy.daos;

import com.jonydog.refy.model.UserNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteDAO extends JpaRepository<UserNote,Long> {



}

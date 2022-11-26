package com.example.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.cloudservice.model.File;
import com.example.cloudservice.model.UserDao;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, String> {


    void deleteByUserAndFilename(UserDao userDao, String filename);

    File findByUserAndFilename(UserDao userDao, String filename);

    @Modifying
    @Query("update File f set f.filename = :newName where f.filename = :filename and f.user = :user")
    void editFileNameByUser(@Param("user") UserDao userDao, @Param("filename") String filename, @Param("newName") String newName);

    List<File> findAllByUser(UserDao userDao);

}

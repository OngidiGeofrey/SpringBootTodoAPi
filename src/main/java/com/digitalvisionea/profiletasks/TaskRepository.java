package com.digitalvisionea.profiletasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // custom query to search to blog post by title or content
    List<Task> findByTitleContainingOrContentContaining(String text, String textAgain);
    List<Task> findByUserId(int userId);

}
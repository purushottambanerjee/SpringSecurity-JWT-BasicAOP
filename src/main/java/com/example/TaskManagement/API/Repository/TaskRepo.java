package com.example.TaskManagement.API.Repository;

import com.example.TaskManagement.API.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {

    public List<Task> findByUser_UserId(Long userId);

}

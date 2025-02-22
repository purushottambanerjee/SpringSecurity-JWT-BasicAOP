package com.example.TaskManagement.API.Repository;

import com.example.TaskManagement.API.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Task,Long> {
}

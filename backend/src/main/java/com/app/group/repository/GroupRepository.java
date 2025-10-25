package com.app.group.repository;

import com.app.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String name);

    List<Group> findByUser_Id(int userId);
}

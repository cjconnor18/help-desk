package com.cSquared.helpDesk.data;

import com.cSquared.helpDesk.models.AccessLevel;
import com.cSquared.helpDesk.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByAccessLevel(AccessLevel accessLevel);
    List<User> findAllByAccessLevel(AccessLevel accessLevel);
}

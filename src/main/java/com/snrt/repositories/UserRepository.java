package com.snrt.repositories;

import com.snrt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value="SELECT * " +
                "FROM user u, role r, users_roles ur " +
                "where u.id=ur.id_user and r.id=ur.id_role and r.rolename like 'ARTIST' " +
                "order by rand() limit 7", nativeQuery = true)
    public List<User> getUsersRandom();
    @Query(value="SELECT * " +
            "FROM user u, role r, users_roles ur " +
            "where u.nickname like ?1 and u.id=ur.id_user and r.id=ur.id_role and r.rolename like 'ARTIST' " +
            "and u.nickname is not null ", nativeQuery = true)
    public List<User> searchByNickname(String nickname);
    public User findUserByUsernameAndPassword(String username, String password);
    public User findByUsername(String username);
    Optional<User> getUserByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

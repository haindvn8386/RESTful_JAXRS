package restful.jaxrs.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import restful.jaxrs.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findByUserNameContainingIgnoreCase(String username, Pageable pageable);

}

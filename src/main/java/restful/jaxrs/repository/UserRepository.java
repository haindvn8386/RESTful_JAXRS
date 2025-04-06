package restful.jaxrs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import restful.jaxrs.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

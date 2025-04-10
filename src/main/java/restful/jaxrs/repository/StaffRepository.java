package restful.jaxrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restful.jaxrs.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}

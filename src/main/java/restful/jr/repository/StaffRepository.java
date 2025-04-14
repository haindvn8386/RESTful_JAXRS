package restful.jr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restful.jr.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}

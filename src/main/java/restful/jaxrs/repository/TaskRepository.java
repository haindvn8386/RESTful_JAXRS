package restful.jaxrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restful.jaxrs.entity.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {
}

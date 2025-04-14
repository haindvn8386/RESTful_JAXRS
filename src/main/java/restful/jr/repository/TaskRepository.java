package restful.jr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restful.jr.entity.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitle(String title);
}

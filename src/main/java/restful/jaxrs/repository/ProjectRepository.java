package restful.jaxrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restful.jaxrs.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

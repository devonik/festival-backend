package nik.dev.repository;

import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.repository.CrudRepository;
import nik.dev.model.Festival;

@RepositoryRestResource(collectionResourceRel = "festival", path = "festival")
public interface IFestivalRepository extends CrudRepository<Festival, Long> {
	List<Festival> findBySyncStatus(String syncStatus);
	Festival findByFestivalDetailId(Long id);
}

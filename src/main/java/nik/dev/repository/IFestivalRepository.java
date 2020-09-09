package nik.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import nik.dev.model.Festival;

public interface IFestivalRepository extends CrudRepository<Festival, Long> {
	Optional<Festival> findByGoabaseId(Long goabaseId);
	List<Festival> findBySyncStatus(String syncStatus);
	Festival findByFestivalDetailId(Long id);
}

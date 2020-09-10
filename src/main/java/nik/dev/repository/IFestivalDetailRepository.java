package nik.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.FestivalDetail;

public interface IFestivalDetailRepository extends CrudRepository<FestivalDetail, Long> {
	Optional<FestivalDetail> findByFestivalId(Long festivalId);
	List<FestivalDetail> findBySyncStatus(String syncStatus);
}

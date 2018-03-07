package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.FestivalDetail;

public interface IFestivalDetailRepository extends CrudRepository<FestivalDetail, Long> {
	List<FestivalDetail> findBySyncStatus(String syncStatus);
}

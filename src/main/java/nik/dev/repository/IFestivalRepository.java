package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.Festival;

public interface IFestivalRepository extends CrudRepository<Festival, Long> {
	
	List<Festival> findBySyncStatus(Boolean syncStatus);

}

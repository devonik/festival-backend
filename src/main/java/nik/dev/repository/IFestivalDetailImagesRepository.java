package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.FestivalDetailImages;

public interface IFestivalDetailImagesRepository extends CrudRepository<FestivalDetailImages, Long> {
	List<FestivalDetailImages> findByFestivalDetailId(Long id);
}

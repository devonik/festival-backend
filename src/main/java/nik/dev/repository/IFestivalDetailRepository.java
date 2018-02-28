package nik.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nik.dev.model.FestivalDetail;

public interface IFestivalDetailRepository extends JpaRepository<FestivalDetail, Long> {

}

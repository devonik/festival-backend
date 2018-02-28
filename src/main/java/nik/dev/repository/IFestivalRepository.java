package nik.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nik.dev.model.Festival;

public interface IFestivalRepository extends JpaRepository<Festival, Long> {
	
	

}

package nik.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nik.dev.model.Shipwreck;

public interface ShipwreckRepository extends JpaRepository<Shipwreck, Long> {
	
	

}

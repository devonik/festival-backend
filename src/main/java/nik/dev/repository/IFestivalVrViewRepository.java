package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.FestivalVrView;
import nik.dev.model.MusicGenre;

public interface IFestivalVrViewRepository extends CrudRepository<FestivalVrView, Long> {
	List<FestivalVrView> findByFestivalDetailId(Long id);
}

package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.MusicGenre;

public interface IFestivalTicketPhaseRepository extends CrudRepository<FestivalTicketPhase, Long> {
	List<FestivalTicketPhase> findByFestival(Festival festival);
	FestivalTicketPhase findByFestivalAndSoldAndStarted(Festival festival, String sold, String started);
}

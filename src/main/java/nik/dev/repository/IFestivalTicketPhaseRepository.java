package nik.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.MusicGenre;

public interface IFestivalTicketPhaseRepository extends CrudRepository<FestivalTicketPhase, Long> {
	List<FestivalTicketPhase> findByFestival(Festival festival);
	Optional<FestivalTicketPhase> findByFestivalAndSoldAndStarted(Festival festival, String sold, String started);
}

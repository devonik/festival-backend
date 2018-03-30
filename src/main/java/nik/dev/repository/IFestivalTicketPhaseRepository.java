package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.MusicGenre;

@RepositoryRestResource(collectionResourceRel = "ticketPhase", path = "ticketPhase")
public interface IFestivalTicketPhaseRepository extends CrudRepository<FestivalTicketPhase, Long> {
	List<FestivalTicketPhase> findByFestival(Festival festival);
}

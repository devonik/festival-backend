package nik.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.FestivalVrView;
import nik.dev.model.MusicGenre;

@RepositoryRestResource(collectionResourceRel = "vrView", path = "vrView")
public interface IFestivalVrViewRepository extends CrudRepository<FestivalVrView, Long> {
	List<FestivalVrView> findByFestivalId(Long id);
}

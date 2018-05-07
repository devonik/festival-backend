package nik.dev.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.FestivalVrView;
import nik.dev.model.MusicGenre;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import nik.dev.repository.IFestivalVrViewRepository;
import nik.dev.repository.IMusicGenreRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalVrViewController {
	@Autowired
	private IFestivalVrViewRepository festivalVrViewRepository;
	
	@RequestMapping(value="vrView", method= RequestMethod.GET)
	public Iterable<FestivalVrView> list(){
		return festivalVrViewRepository.findAll();
	}
	@RequestMapping(value="vrViewByFestivalId/{id}", method= RequestMethod.GET)
	public Iterable<FestivalVrView> findByFestivalId(@PathVariable Long id){
		return festivalVrViewRepository.findByFestivalId(id);
	}
	@RequestMapping(value="vrView", method = RequestMethod.POST)
	public FestivalVrView create(@RequestBody FestivalVrView festivalTicketPhase) {
		return festivalVrViewRepository.save(festivalTicketPhase);
	}
	
	@RequestMapping(value="vrView/{id}", method = RequestMethod.PUT)
	public FestivalVrView update(@PathVariable Long id, @RequestBody FestivalVrView festivalTicketPhase) {
		return festivalVrViewRepository.save(festivalTicketPhase);
	}
	
	@RequestMapping(value="vrView/{id}", method = RequestMethod.DELETE)
	public Optional<FestivalVrView> delete(@PathVariable Long id) {
		Optional<FestivalVrView> existingVrView = festivalVrViewRepository.findById(id);
		festivalVrViewRepository.deleteById(existingVrView.get().getId());
		return existingVrView;
	}
}

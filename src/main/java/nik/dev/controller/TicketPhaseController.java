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
import nik.dev.model.MusicGenre;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import nik.dev.repository.IMusicGenreRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class TicketPhaseController {
	@Autowired
	private IFestivalTicketPhaseRepository festivalTicketPhaseRepository;
	
	@RequestMapping(value="ticketPhase", method= RequestMethod.GET)
	public Iterable<FestivalTicketPhase> list(){
		Iterable<FestivalTicketPhase> list = festivalTicketPhaseRepository.findAll();
		for(FestivalTicketPhase item:list) {
			item.setFestival_id(item.getFestival().getFestival_id());
		}
		return list;
	}
	@RequestMapping(value="ticketPhase", method = RequestMethod.POST)
	public FestivalTicketPhase create(@RequestBody FestivalTicketPhase festivalTicketPhase) {
		return festivalTicketPhaseRepository.save(festivalTicketPhase);
	}
	
	@RequestMapping(value="ticketPhase/{id}", method = RequestMethod.PUT)
	public FestivalTicketPhase update(@PathVariable Long id, @RequestBody FestivalTicketPhase festivalTicketPhase) {
		return festivalTicketPhaseRepository.save(festivalTicketPhase);
	}
	
	@RequestMapping(value="ticketPhase/{id}", method = RequestMethod.DELETE)
	public Optional<FestivalTicketPhase> delete(@PathVariable Long id) {
		Optional<FestivalTicketPhase> existingTicketPhase = festivalTicketPhaseRepository.findById(id);
		festivalTicketPhaseRepository.deleteById(existingTicketPhase.get().getFestival_ticket_phase_id());
		return existingTicketPhase;
	}
}

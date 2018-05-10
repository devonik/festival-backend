package nik.dev.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.FestivalVrView;
import nik.dev.model.MusicGenre;
import nik.dev.model.WhatsNew;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import nik.dev.repository.IFestivalVrViewRepository;
import nik.dev.repository.IMusicGenreRepository;
import nik.dev.repository.IWhatsNewRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalVrViewController {
	@Autowired
	private IFestivalVrViewRepository festivalVrViewRepository;
	
	@Autowired
	private IWhatsNewRepository whatsNewRepository;
	
	@Autowired
	private IFestivalRepository festivalRepository;
	
	@RequestMapping(value="vrView", method= RequestMethod.GET)
	public Iterable<FestivalVrView> list(){
		return festivalVrViewRepository.findAll();
	}
	@RequestMapping(value="vrViewByFestivalDetailId/{id}", method= RequestMethod.GET)
	public Iterable<FestivalVrView> findByFestivalDetailId(@PathVariable Long id){
		return festivalVrViewRepository.findByFestivalDetailId(id);
	}
	@RequestMapping(value="vrView", method = RequestMethod.POST)
	public FestivalVrView create(@RequestBody FestivalVrView vrView) {
		//Add Whats new Entry
		WhatsNew whatsNew = new WhatsNew();
		Festival festival = festivalRepository.findByFestivalDetailId(vrView.getFestivalDetailId());
		if(vrView.getType().equals("photo")) {
			whatsNew.setContent("Es gibt ein neues VR Foto des Festivals: "+festival.getName());
			whatsNewRepository.save(whatsNew);
		}
		else if(vrView.getType().equals("video")) {
			whatsNew.setContent("Es gibt ein neues VR Video des Festivals: "+festival.getName());
			whatsNewRepository.save(whatsNew);
		}
		
		return festivalVrViewRepository.save(vrView);
	}
	
	@RequestMapping(value="vrView/{id}", method = RequestMethod.PUT)
	public FestivalVrView update(@PathVariable Long id, @RequestBody FestivalVrView vrView) {
		return festivalVrViewRepository.save(vrView);
	}
	
	@RequestMapping(value="vrView/{id}", method = RequestMethod.DELETE)
	public Optional<FestivalVrView> delete(@PathVariable Long id) {
		Optional<FestivalVrView> existingVrView = festivalVrViewRepository.findById(id);
		festivalVrViewRepository.deleteById(existingVrView.get().getId());
		return existingVrView;
	}
}

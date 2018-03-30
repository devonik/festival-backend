package nik.dev.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.helper.CompareSetList;
import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.MusicGenre;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import nik.dev.repository.IMusicGenreRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalController {
	@Autowired
	private IFestivalRepository festivalRepository;
	@Autowired
	private IMusicGenreRepository musicGenreRepository;
	@Autowired
	private IFestivalTicketPhaseRepository ticketRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(value="festivals", method= RequestMethod.GET)
	public Iterable<Festival> list(){
		return festivalRepository.findAll();
	}
	
	@RequestMapping(value="festivals", method = RequestMethod.POST)
	public Festival create(@RequestBody Festival festival) {
		Set<MusicGenre> list = new HashSet<MusicGenre>();
		for(Long itemId:festival.getMusicGenreIds()) {
			MusicGenre genre = musicGenreRepository.findOne(itemId);
			if(genre != null) {
				list.add(genre);
			}
		}
		festival.setMusicGenres(list);
		Festival festivalReturn = festivalRepository.save(festival);
		
		//Connect to new Detail Page
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("connect_new_festival_detail");
		storedProcedure.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);
		storedProcedure.setParameter(1,festivalReturn.getFestival_id());
		storedProcedure.execute();
		//New generated festival_detail_id
		festivalReturn.setFestival_detail_id((Long) storedProcedure.getOutputParameterValue(2));
		//pushController.send("Es kam eines neues Festival hinzu!", festival.getName()+": "+new SimpleDateFormat("dd.MM.").format(festival.getDatum_start()));
		
		return festivalReturn;
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.GET)
	public Festival get(@PathVariable Long id) {
		return festivalRepository.findOne(id);
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.PUT)
	public Festival update(@PathVariable Long id, @RequestBody Festival festival) {
		Set<MusicGenre> list = new HashSet<MusicGenre>();
		for(Long itemId:festival.getMusicGenreIds()) {
			MusicGenre genre = musicGenreRepository.findOne(itemId);
			if(genre != null) {
				list.add(genre);
			}
		}
		festival.setMusicGenres(list);
		// Create a sorted set of some names
		/*Festival currentFestival = festivalRepository.findOne(festival.getFestival_id());
		for(FestivalTicketPhase item: currentFestival.getTicketPhases()) {
			for(FestivalTicketPhase incomingItem: festival.getTicketPhases()) {
				if(!item.equals(incomingItem)) {
					System.out.println("new Ticket Phase detected!");
				}
			}
		}*/
		
		return festivalRepository.save(festival);
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.DELETE)
	public Festival delete(@PathVariable Long id) {
		Festival existingFestival = festivalRepository.findOne(id);
		festivalRepository.delete(existingFestival);
		return existingFestival;
	}
	
	//@TODO kann weg
	@RequestMapping(value="festivalsByUnSync", method= RequestMethod.GET)
	public List<Festival> listUnsync(){
		return festivalRepository.findBySyncStatus("no");                                                                                      
	}
	@RequestMapping(value="festivals/setAllSynced", method= RequestMethod.GET)
	public Integer setAllSynced(){
		List<Festival> unsyncedFestivals = festivalRepository.findBySyncStatus("no");
		Integer unsynchedCount = 0;
		for(Festival item: unsyncedFestivals) {
			item.setSyncStatus("yes");
			update(item.getFestival_id(), item);
			unsynchedCount++;
		}
		return unsynchedCount;
	}
}                            

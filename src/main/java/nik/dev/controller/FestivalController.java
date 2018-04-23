package nik.dev.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import nik.dev.helper.CompareSetList;
import nik.dev.model.Festival;
import nik.dev.model.FestivalTicketPhase;
import nik.dev.model.MusicGenre;
import nik.dev.model.WhatsNew;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IFestivalTicketPhaseRepository;
import nik.dev.repository.IMusicGenreRepository;
import nik.dev.repository.IWhatsNewRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalController {
	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	@Autowired
	private IFestivalRepository festivalRepository;
	@Autowired
	private IMusicGenreRepository musicGenreRepository;
	@Autowired
	private IFestivalTicketPhaseRepository ticketRepository;
	@Autowired
	private PushController pushController;
	@Autowired
	private IWhatsNewRepository whatsNewRepository;
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
			Optional<MusicGenre> genre = musicGenreRepository.findById(itemId);
			if(genre != null) {
				list.add(genre.get());
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
		
		//Add Whats new Entry
		WhatsNew whatsNew = new WhatsNew();
		whatsNew.setContent("Neues Festival: "+festivalReturn.getName());
		whatsNewRepository.save(whatsNew);
		
		return festivalReturn;
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.GET)
	public Optional<Festival> get(@PathVariable Long id) {
		return festivalRepository.findById(id);
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.PUT)
	public Festival update(@PathVariable Long id, @RequestBody Festival festival) {
		Optional<Festival> existingFestival = festivalRepository.findById(festival.getFestival_id());
		Set<MusicGenre> list = new HashSet<MusicGenre>();
		for(Long itemId:festival.getMusicGenreIds()) {
			Optional<MusicGenre> genre = musicGenreRepository.findById(itemId);
			if(genre != null) {
				list.add(genre.get());
			}
		}
		festival.setMusicGenres(list);
		// Create a couple ArrayList objects and populate them
		// with some delicious fruits.
		Set<FestivalTicketPhase> incomingTicketPhases = festival.getTicketPhases();
		
		for(FestivalTicketPhase incomingTicketPhase:incomingTicketPhases) {
			if(incomingTicketPhase.getFestival_ticket_phase_id() == null){
				//Item new in database
				System.out.println("new ticket id detected!");
				if(incomingTicketPhase.getSold().equals("no")&&incomingTicketPhase.getStarted().equals("yes")) {
					System.out.println("new ticket is started and not sold!");
					try {
						pushController.send(festival.getName()+" naechste Ticketphase", 
								"Der neue Preis eines Tickets betraegt: "+incomingTicketPhase.getPrice()+" €",
								"newTicketPhase");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//Add Whats new Entry
					WhatsNew whatsNew = new WhatsNew();
					whatsNew.setContent("Neuer Preis ("+incomingTicketPhase.getPrice()+"€) eines Tickets für: "+festival.getName());
					whatsNewRepository.save(whatsNew);
				}
			}
			else if(incomingTicketPhase.getFestival_ticket_phase_id() != null) {
				//Item already exist in database
				Optional<FestivalTicketPhase> existingTicketPhase = ticketRepository.findById(incomingTicketPhase.getFestival_ticket_phase_id());
				if((!existingTicketPhase.get().getSold().equals(incomingTicketPhase.getSold()) && incomingTicketPhase.getSold().equals("no"))
					||
					(!existingTicketPhase.get().getStarted().equals(incomingTicketPhase.getStarted()) && incomingTicketPhase.getStarted().equals("yes"))) {
					//Existing ticketPhase gone to started
					System.out.println("Existing ticket is now started and not sold!");
					try {
						pushController.send("Das Festival: "+festival.getName()+" hat ihre naechste Ticket Phase begonnen", 
								"Der neue Preis eines Tickets betraegt: "+incomingTicketPhase.getPrice()+" €",
								"newTicketPhase");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//Add Whats new Entry
					WhatsNew whatsNew = new WhatsNew();
					whatsNew.setContent("Neuer Preis ("+incomingTicketPhase.getPrice()+"€) eines Tickets für: "+festival.getName());
					whatsNewRepository.save(whatsNew);
				}
			}
			
		}
		DiffNode diff = ObjectDifferBuilder.buildDefault().compare(festival, existingFestival);
		diff.visit(new DiffNode.Visitor()
		{
		    public void node(DiffNode node, Visit visit)
		    {
		        final Object baseValue = node.canonicalGet(existingFestival);
		        final Object workingValue = node.canonicalGet(festival);
		        
		        if(node.getPropertyName()!= null && (node.getPropertyName().equals("datum_end") || node.getPropertyName().equals("datum_start"))) {
		        	
		        	String startDateBase = dateFormat.format((Date)baseValue);
		        	String startDateWorking = dateFormat.format((Date)workingValue);
		        	
		        	String endDateBase = dateFormat.format((Date)baseValue);
		        	String endDateWorking = dateFormat.format((Date)workingValue);
		        	
		        	if(!startDateBase.equals(startDateWorking) || !endDateBase.equals(endDateWorking)) {
		        		//Add Whats new Entry
						WhatsNew whatsNew = new WhatsNew();
						whatsNew.setContent("Das Datum des Festivals: "+festival.getName()+
								" hat sich geändert zu "+dateFormat.format(festival.getDatum_start())+
								" - "+dateFormat.format(festival.getDatum_end()));
						whatsNewRepository.save(whatsNew);
		        	};
		        	
		        }
		    }
		});
		return festivalRepository.save(festival);
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.DELETE)
	public Optional<Festival> delete(@PathVariable Long id) {
		Optional<Festival> existingFestival = festivalRepository.findById(id);
		festivalRepository.deleteById(existingFestival.get().getFestival_id());
		//Add Whats new Entry
		WhatsNew whatsNew = new WhatsNew();
		whatsNew.setContent("Festival wurde gelöscht: "+existingFestival.get().getName());
		whatsNewRepository.save(whatsNew);
		
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

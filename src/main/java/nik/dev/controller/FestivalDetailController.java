package nik.dev.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import nik.dev.model.Festival;
import nik.dev.model.FestivalDetail;
import nik.dev.model.WhatsNew;
import nik.dev.repository.IFestivalDetailRepository;
import nik.dev.repository.IFestivalRepository;
import nik.dev.repository.IWhatsNewRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalDetailController {
	@Autowired
	private IFestivalDetailRepository festivalDetailRepository;
	@Autowired
	private IFestivalRepository festivalRepository;
	@Autowired
	private IWhatsNewRepository whatsNewRepository;
	
	@RequestMapping(value="festivalDetails", method = RequestMethod.POST)
	public FestivalDetail create(@RequestBody FestivalDetail festival) {
		return festivalDetailRepository.save(festival);
	}
	@RequestMapping(value="festivalDetails", method = RequestMethod.GET)
	public Iterable<FestivalDetail> list() {
		return festivalDetailRepository.findAll();
	}
	@RequestMapping(value="festivalDetails/{id}", method = RequestMethod.GET)
	public Optional<FestivalDetail> get(@PathVariable Long id) {
		return festivalDetailRepository.findById(id);
	}
	
	@RequestMapping(value="festivalDetails/{id}", method = RequestMethod.PUT)
	public FestivalDetail update(@RequestBody FestivalDetail festivalDetail) {
		Optional<FestivalDetail> existingFestivalDetail = festivalDetailRepository.findById(festivalDetail.getFestival_detail_id());
		Optional<Festival> existingFestival = festivalRepository.findById(festivalDetail.getFestival_id());
		List<String> changedFields = new ArrayList<String>();

		if(festivalDetail.getDescription() != null &&
				(!festivalDetail.getDescription().equals(existingFestivalDetail.get().getDescription()))) {
			
        	changedFields.add("Beschreibung");
        	
        }
		
        if(festivalDetail.getHomepage_url() != null &&
        		(!festivalDetail.getHomepage_url().equals(existingFestivalDetail.get().getHomepage_url()))) {
        	
        	changedFields.add("Homepage");
        	
        }
        if(festivalDetail.getTicket_url() != null &&
        		(!festivalDetail.getTicket_url().equals(existingFestivalDetail.get().getTicket_url()))) {
        	
        	changedFields.add("Ticket Seite");
        	
        }
        if((festivalDetail.getGeoLatitude() != null &&
        		(!festivalDetail.getGeoLatitude().equals(existingFestivalDetail.get().getGeoLatitude()))
        	) || 
        		(festivalDetail.getGeoLongitude() != null && 
        		(!festivalDetail.getGeoLongitude().equals(existingFestivalDetail.get().getGeoLongitude()))
        		)
          ) {
        	
        	changedFields.add("Ort");
        	
        }
	        
		if(changedFields.size() > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for(String field:changedFields) {
				stringBuilder.append("\n- "+field);
			}
			//Add Whats new Entry
			WhatsNew whatsNew = new WhatsNew();
			whatsNew.setContent("Änderungen des Festivals: "+existingFestival.get().getName()+
								stringBuilder);
			whatsNewRepository.save(whatsNew);
		}
		return festivalDetailRepository.save(festivalDetail);
	}
	
	@RequestMapping(value="festivalDetails/{id}", method = RequestMethod.DELETE)
	public Optional<FestivalDetail> delete(@PathVariable Long id) {
		Optional<FestivalDetail> existingFestivalDetail = festivalDetailRepository.findById(id);
		festivalDetailRepository.deleteById(existingFestivalDetail.get().getFestival_detail_id());
		return existingFestivalDetail;
	}
	@RequestMapping(value="festivalDetailsByUnSync", method= RequestMethod.GET)
	public List<FestivalDetail> listUnsync(){
		return festivalDetailRepository.findBySyncStatus("no");                                                                                      
	}
	
}

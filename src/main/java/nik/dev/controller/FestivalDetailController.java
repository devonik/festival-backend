package nik.dev.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public FestivalDetail get(@PathVariable Long id) {
		return festivalDetailRepository.findOne(id);
	}
	
	@RequestMapping(value="festivalDetails/{id}", method = RequestMethod.PUT)
	public FestivalDetail update(@RequestBody FestivalDetail festivalDetail) {
		FestivalDetail existingFestivalDetail = festivalDetailRepository.findOne(festivalDetail.getFestival_detail_id());
		Festival existingFestival = festivalRepository.findOne(festivalDetail.getFestival_id());
		List<String> changedFields = new ArrayList<String>();
		/*DiffNode diff = ObjectDifferBuilder.buildDefault().compare(festivalDetail, existingFestivalDetail);
		diff.visit(new DiffNode.Visitor()
		{
		    public void node(DiffNode node, Visit visit)
		    {
		        //final Object baseValue = node.canonicalGet(existingFestivalDetail);
		        //final Object workingValue = node.canonicalGet(festivalDetail);
		        if(node.getPropertyName().equals("description")) {
		        	changedFields.add("Beschreibung");
		        }
		        if(node.getPropertyName().equals("homepage_url")) {
		        	changedFields.add("Homepage");
		        }
		        if(node.getPropertyName().equals("ticket_url")) {
		        	changedFields.add("Ticket Seite");
		        }
		        if(node.getPropertyName().equals("geoLatitude") || node.getPropertyName().equals("geoLongitude")) {
		        	changedFields.add("Ort");
		        }
		    }
		});*/
		if(festivalDetail.getDescription() != null &&
				(!festivalDetail.getDescription().equals(existingFestivalDetail.getDescription()))) {
			
        	changedFields.add("Beschreibung");
        	
        }
		
        if(festivalDetail.getHomepage_url() != null &&
        		(!festivalDetail.getHomepage_url().equals(existingFestivalDetail.getHomepage_url()))) {
        	
        	changedFields.add("Homepage");
        	
        }
        if(festivalDetail.getTicket_url() != null &&
        		(!festivalDetail.getTicket_url().equals(existingFestivalDetail.getTicket_url()))) {
        	
        	changedFields.add("Ticket Seite");
        	
        }
        if((festivalDetail.getGeoLatitude() != null &&
        		(!festivalDetail.getGeoLatitude().equals(existingFestivalDetail.getGeoLatitude()))
        	) || 
        		(festivalDetail.getGeoLongitude() != null && 
        		(!festivalDetail.getGeoLongitude().equals(existingFestivalDetail.getGeoLongitude()))
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
			whatsNew.setContent("Änderungen des Festivals: "+existingFestival.getName()+
								stringBuilder);
			whatsNewRepository.save(whatsNew);
		}
		return festivalDetailRepository.save(festivalDetail);
	}
	
	@RequestMapping(value="festivalDetails/{id}", method = RequestMethod.DELETE)
	public FestivalDetail delete(@PathVariable Long id) {
		FestivalDetail existingFestivalDetail = festivalDetailRepository.findOne(id);
		festivalDetailRepository.delete(existingFestivalDetail);
		return existingFestivalDetail;
	}
	@RequestMapping(value="festivalDetailsByUnSync", method= RequestMethod.GET)
	public List<FestivalDetail> listUnsync(){
		return festivalDetailRepository.findBySyncStatus("no");                                                                                      
	}
	
}

package nik.dev.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.Festival;
import nik.dev.repository.IFestivalRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalController {
	@Autowired
	private IFestivalRepository festivalRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(value="festivals", method= RequestMethod.GET)
	public Iterable<Festival> list(){
		return festivalRepository.findAll();
	}
	
	@RequestMapping(value="festivals", method = RequestMethod.POST)
	public Festival create(@RequestBody Festival festival) {
		Festival festivalReturn = festivalRepository.save(festival);
		//Connect to new Detail Page
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("connect_new_festival_detail");
		storedProcedure.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter(2, Long.class, ParameterMode.OUT);
		storedProcedure.setParameter(1,festivalReturn.getFestival_id());
		storedProcedure.execute();
		//New generated festival_detail_id
		festivalReturn.setFestival_detail_id((Long) storedProcedure.getOutputParameterValue(2));
		
		return festivalReturn;
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.GET)
	public Festival get(@PathVariable Long id) {
		return festivalRepository.findOne(id);
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.PUT)
	public Festival update(@PathVariable Long id, @RequestBody Festival festival) {
		return festivalRepository.save(festival);
	}
	
	@RequestMapping(value="festivals/{id}", method = RequestMethod.DELETE)
	public Festival delete(@PathVariable Long id) {
		Festival existingFestival = festivalRepository.findOne(id);
		festivalRepository.delete(existingFestival);
		return existingFestival;
	}
	
	@RequestMapping(value="festivalsByUnSync", method= RequestMethod.GET)
	public List<Festival> listUnsync(){
		return festivalRepository.findBySyncStatus("no");                                                                                      
	}
}                            

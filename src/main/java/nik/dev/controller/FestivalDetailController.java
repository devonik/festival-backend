package nik.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.FestivalDetail;
import nik.dev.repository.IFestivalDetailRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalDetailController {
	@Autowired
	private IFestivalDetailRepository festivalDetailRepository;
	
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

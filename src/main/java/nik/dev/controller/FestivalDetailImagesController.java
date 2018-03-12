package nik.dev.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.Festival;
import nik.dev.model.FestivalDetail;
import nik.dev.model.FestivalDetailImages;
import nik.dev.repository.IFestivalDetailImagesRepository;
import nik.dev.repository.IFestivalRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalDetailImagesController {
	@Autowired
	private IFestivalDetailImagesRepository festivalDetailImagesRepository;
	@Autowired
	private PushController pushController;
	@Autowired
	private IFestivalRepository festivalRepository;
	
	@RequestMapping(value="festivalDetailImages/", method = RequestMethod.POST)
	public FestivalDetailImages save (@RequestBody FestivalDetailImages images) {
		Festival festival = festivalRepository.findByFestivalDetailId(images.getFestivalDetailId());
		pushController.send("Aenderungen des Festivals: "+festival.getName(), "Es kam ein neues Bild hinzu/wurde geändert");
		return festivalDetailImagesRepository.save(images);
	}
	@RequestMapping(value="festivalDetailImages", method = RequestMethod.GET)
	public Iterable<FestivalDetailImages> list() {
		return festivalDetailImagesRepository.findAll();
	}
	@RequestMapping(value="festivalDetailImages/{id}", method = RequestMethod.GET)
	public List<FestivalDetailImages> get(@PathVariable Long id) {
		return festivalDetailImagesRepository.findByFestivalDetailId(id);
	}
	
	@RequestMapping(value="festivalDetailImages/{id}", method = RequestMethod.DELETE)
	public FestivalDetailImages delete(@PathVariable Long id) {
		FestivalDetailImages existingFestivalDetail = festivalDetailImagesRepository.findOne(id);
		festivalDetailImagesRepository.delete(existingFestivalDetail);
		return existingFestivalDetail;
	}
	@RequestMapping(value="festivalDetailImagesByUnSync", method= RequestMethod.GET)
	public List<FestivalDetailImages> listUnsync(){
		return festivalDetailImagesRepository.findBySyncStatus("no");                                                                                      
	}
	
}

package nik.dev.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.FestivalDetailImages;
import nik.dev.repository.IFestivalDetailImagesRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class FestivalDetailImagesController {
	@Autowired
	private IFestivalDetailImagesRepository festivalDetailImagesRepository;
	
	@RequestMapping(value="festivalDetailImages/", method = RequestMethod.POST)
	public FestivalDetailImages save (@RequestBody FestivalDetailImages images) {
		//Festival festival = festivalRepository.findByFestivalDetailId(images.getFestivalDetailId());
		//pushController.send("Aenderungen des Festivals: "+festival.getName(), "Es kam ein neues Bild hinzu/wurde geändert");
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
	public Optional<FestivalDetailImages> delete(@PathVariable Long id) {
		Optional<FestivalDetailImages> existingFestivalDetail = festivalDetailImagesRepository.findById(id);
		festivalDetailImagesRepository.deleteById(existingFestivalDetail.get().getFestival_detail_images_id());
		return existingFestivalDetail;
	}
	@RequestMapping(value="festivalDetailImagesByUnSync", method= RequestMethod.GET)
	public List<FestivalDetailImages> listUnsync(){
		return festivalDetailImagesRepository.findBySyncStatus("no");                                                                                      
	}
	
}

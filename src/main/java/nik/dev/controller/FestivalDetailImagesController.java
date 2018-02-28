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

import nik.dev.model.FestivalDetailImages;
import nik.dev.repository.IFestivalDetailImagesRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class FestivalDetailImagesController {
	@Autowired
	private IFestivalDetailImagesRepository festivalDetailImagesRepository;
	
	@RequestMapping(value="festivalDetailImages/", method = RequestMethod.POST)
	public FestivalDetailImages save (@RequestBody FestivalDetailImages images) {
			return festivalDetailImagesRepository.save(images);
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
	
}

package nik.dev.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.WhatsNew;
import nik.dev.repository.IWhatsNewRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class WhatsNewController {
	@Autowired
	private IWhatsNewRepository whatsNewRepository;
	@RequestMapping(value="whatsNew", method= RequestMethod.GET)
	public Iterable<WhatsNew> getAll(){
		return whatsNewRepository.findAll();
	}
	@RequestMapping(value="whatsNewBetweenDates", method= RequestMethod.GET)
	public List<WhatsNew> get(Date start, Date end){
		return whatsNewRepository.findByCreatedDateBetween(start, end);
	}
	@RequestMapping(value="whatsNew", method = RequestMethod.POST)
	public WhatsNew create(@RequestBody WhatsNew whatsNew) {
		return whatsNewRepository.save(whatsNew);
	}
	
}

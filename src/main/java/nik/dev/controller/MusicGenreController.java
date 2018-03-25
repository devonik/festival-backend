package nik.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.MusicGenre;
import nik.dev.repository.IMusicGenreRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class MusicGenreController {
	@Autowired
	private IMusicGenreRepository musicGenreRepository;
	
	@RequestMapping(value="musicGenre", method= RequestMethod.GET)
	public Iterable<MusicGenre> list(){
		return musicGenreRepository.findAll();
	}
	@RequestMapping(value="musicGenre", method = RequestMethod.POST)
	public MusicGenre create(@RequestBody MusicGenre musicGenre) {
		return musicGenreRepository.save(musicGenre);
	}
	
	@RequestMapping(value="musicGenre/{id}", method = RequestMethod.PUT)
	public MusicGenre update(@PathVariable Long id, @RequestBody MusicGenre musicGenre) {
		return musicGenreRepository.save(musicGenre);
	}
	
	@RequestMapping(value="musicGenre/{id}", method = RequestMethod.DELETE)
	public MusicGenre delete(@PathVariable Long id) {
		MusicGenre existingMusicGenre = musicGenreRepository.findOne(id);
		musicGenreRepository.delete(existingMusicGenre);
		return existingMusicGenre;
	}
}

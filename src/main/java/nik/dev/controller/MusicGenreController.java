package nik.dev.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.MusicGenre;
import nik.dev.model.MusicGenreFestivals;
import nik.dev.repository.IMusicGenreFestivalsRepository;
import nik.dev.repository.IMusicGenreRepository;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class MusicGenreController {
	@Autowired
	private IMusicGenreRepository musicGenreRepository;
	@Autowired
	private IMusicGenreFestivalsRepository musicGenreFestivalsRepository;
	
	@RequestMapping(value="musicGenre", method= RequestMethod.GET)
	public Iterable<MusicGenre> list(){
		return musicGenreRepository.findAll();
	}
	@RequestMapping(value="musicGenreFestivals", method= RequestMethod.GET)
	public Iterable<MusicGenreFestivals> listGenreFestivals(){
		return musicGenreFestivalsRepository.findAll();
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
	public Optional<MusicGenre> delete(@PathVariable Long id) {
		Optional<MusicGenre> existingMusicGenre = musicGenreRepository.findById(id);
		musicGenreRepository.deleteById(existingMusicGenre.get().getId());
		return existingMusicGenre;
	}
}

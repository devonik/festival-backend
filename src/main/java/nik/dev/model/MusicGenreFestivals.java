package nik.dev.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="music_genre_festivals")
public class MusicGenreFestivals {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long festival_id;
	private Long music_genre_id;
	public Long getFestival_id() {
		return festival_id;
	}
	public void setFestival_id(Long festival_id) {
		this.festival_id = festival_id;
	}
	public Long getMusic_genre_id() {
		return music_genre_id;
	}
	public void setMusic_genre_id(Long music_genre_id) {
		this.music_genre_id = music_genre_id;
	}
	
	
}

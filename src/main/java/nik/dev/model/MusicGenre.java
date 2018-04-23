package nik.dev.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;

@Entity
@Table(name="music_genre")
public class MusicGenre{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="music_genre_id")
	private Long id;
	private String name;
	@ManyToMany(mappedBy = "musicGenres")
	@JsonBackReference
	@Transient
    private List<Festival> festivals;
	
	public MusicGenre() {}
	public MusicGenre(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public List<Festival> getFestivals() {
		return festivals;
	}
	
}

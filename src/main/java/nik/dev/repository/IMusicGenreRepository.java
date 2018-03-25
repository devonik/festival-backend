package nik.dev.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import nik.dev.model.MusicGenre;

@RepositoryRestResource(collectionResourceRel = "musicGenre", path = "musicGenre")
public interface IMusicGenreRepository extends CrudRepository<MusicGenre, Long> {
}

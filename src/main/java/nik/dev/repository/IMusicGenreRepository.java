package nik.dev.repository;

import org.springframework.data.repository.CrudRepository;

import nik.dev.model.MusicGenre;

public interface IMusicGenreRepository extends CrudRepository<MusicGenre, Long> {
}

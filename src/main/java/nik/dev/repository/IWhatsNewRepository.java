package nik.dev.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import nik.dev.model.WhatsNew;

public interface IWhatsNewRepository extends CrudRepository<WhatsNew, Long> {
	List<WhatsNew> findByCreatedDateBetween(Date start, Date end);
}

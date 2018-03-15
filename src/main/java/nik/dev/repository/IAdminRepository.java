package nik.dev.repository;
import org.springframework.data.repository.CrudRepository;

import nik.dev.model.Authentication.Admin;

public interface IAdminRepository extends CrudRepository<Admin, Long>{
	Admin findByUsername(String username);
}

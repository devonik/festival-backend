package nik.dev.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Shipwreck {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	String name;
	String description;
	String conditional;
	Integer depth;
	Double latitude;
	Double longitude;
	Integer year_discovered;

	public Shipwreck() { }

	public Shipwreck(Long id, String name, String description, String conditional, Integer depth, Double latitude, Double longitude, Integer year_discovered) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.conditional = conditional;
		this.depth = depth;
		this.latitude = latitude;
		this.longitude = longitude;
		this.year_discovered = year_discovered;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCondition() {
		return conditional;
	}

	public void setCondition(String conditional) {
		this.conditional = conditional;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getYearDiscovered() {
		return year_discovered;
	}

	public void setYearDiscovered(Integer year_discovered) {
		this.year_discovered = year_discovered;
	}
}

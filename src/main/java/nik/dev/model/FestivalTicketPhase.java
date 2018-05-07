package nik.dev.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "festival_ticket_phase")
public class FestivalTicketPhase {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		Long festival_ticket_phase_id;
		@Transient
		Long festival_id;
		@ManyToOne
	    @JoinColumn(name = "festival_id", nullable = false)
		@JsonBackReference
		Festival festival;
		String title;
		Double price;
		String syncStatus;
		String sold;
		String started;
		public FestivalTicketPhase() { }

		public FestivalTicketPhase(Long festival_ticket_phase_id, Festival festival,String title, Double price, String sold, String started) {
			this.festival_ticket_phase_id = festival_ticket_phase_id;
			this.title = title;
			this.price = price;
			this.sold = sold;
			this.festival = festival;
			this.started = started;
		}

		public Long getFestival_id() {
			return festival_id;
		}

		public void setFestival_id(Long festival_id) {
			this.festival_id = festival_id;
		}

		
		public Festival getFestival() {
			return festival;
		}

		public void setFestival(Festival festival) {
			this.festival = festival;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public String getSold() {
			return sold;
		}

		public void setSold(String sold) {
			this.sold = sold;
		}

		public String getStarted() {
			return started;
		}

		public void setStarted(String started) {
			this.started = started;
		}

		public String getSyncStatus() {
			return syncStatus;
		}

		public void setSyncStatus(String syncStatus) {
			this.syncStatus = syncStatus;
		}

		public Long getFestival_ticket_phase_id() {
			return festival_ticket_phase_id;
		}

		
}

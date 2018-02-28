package nik.dev.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FestivalTicketPhase {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		Long festival_ticket_phase_id;
		Long festival_id;
		String title;
		Double price;
		Integer sold;
		Integer started;

		public FestivalTicketPhase() { }

		public FestivalTicketPhase(Long festival_ticket_phase_id, Long festival_id, String title, Double price, Integer sold, Integer started) {
			this.festival_ticket_phase_id = festival_ticket_phase_id;
			this.festival_id = festival_id;
			this.title = title;
			this.price = price;
			this.sold = sold;
			this.started = started;
		}

		public Long getFestival_id() {
			return festival_id;
		}

		public void setFestival_id(Long festival_id) {
			this.festival_id = festival_id;
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

		public Integer getSold() {
			return sold;
		}

		public void setSold(Integer sold) {
			this.sold = sold;
		}

		public Integer getStarted() {
			return started;
		}

		public void setStarted(Integer started) {
			this.started = started;
		}

		public Long getFestival_ticket_phase_id() {
			return festival_ticket_phase_id;
		}

		
}

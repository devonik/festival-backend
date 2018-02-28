package nik.dev.model;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FestivalArchive {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		Long festival_id;
		Long festival_detail_id;
		String name;
		String thumbnail_image_url;
		Date datum;
		String homepage_url;
		String ticket_url;

		public FestivalArchive() { }

		public FestivalArchive(Long festival_id, Long festival_detail_id, String name, String thumbnail_image_url, Date datum, String homepage_url, String ticket_url) {
			this.festival_id = festival_id;
			this.festival_detail_id = festival_detail_id;
			this.name = name;
			this.thumbnail_image_url = thumbnail_image_url;
			this.datum = datum;
			this.homepage_url = homepage_url;
			this.ticket_url = ticket_url;
		}

		public Long getFestival_id() {
			return festival_id;
		}

		public Long getFestival_detail_id() {
			return festival_detail_id;
		}

		public void setFestival_detail_id(Long festival_detail_id) {
			this.festival_detail_id = festival_detail_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getThumbnail_image_url() {
			return thumbnail_image_url;
		}

		public void setThumbnail_image_url(String thumbnail_image_url) {
			this.thumbnail_image_url = thumbnail_image_url;
		}

		public Date getDatum() {
			return datum;
		}

		public void setDatum(Date datum) {
			this.datum = datum;
		}

		public String getHomepage_url() {
			return homepage_url;
		}

		public void setHomepage_url(String homepage_url) {
			this.homepage_url = homepage_url;
		}

		public String getTicket_url() {
			return ticket_url;
		}

		public void setTicket_url(String ticket_url) {
			this.ticket_url = ticket_url;
		}
		
		
}

package nik.dev.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FestivalDetail {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		Long festival_detail_id;
		Long festival_detail_images_id;
		Long festival_id;
		String description;
		String homepage_url;
		String ticket_url;
		Double geoLatitude;
		Double geoLongitude;
		String syncStatus;
		
		public FestivalDetail() { }

		public FestivalDetail(Long festival_detail_id, 
							  Long festival_detail_images_id, 
							  Long festival_id, 
							  String description,
							  String homepage_url, 
							  String ticket_url,
							  Double geoLatitude,
							  Double geoLongitude,
							  String syncStatus) {
			this.festival_detail_id = festival_detail_id;
			this.festival_detail_images_id = festival_detail_images_id;
			this.festival_id = festival_id;
			this.description = description;
			this.homepage_url = homepage_url;
			this.ticket_url = ticket_url;
			this.geoLatitude = geoLatitude;
			this.geoLongitude = geoLongitude;
			this.syncStatus = syncStatus;
		}


		public Long getFestival_detail_id() {
			return festival_detail_id;
		}

		public Long getFestival_detail_images_id() {
			return festival_detail_images_id;
		}

		public void setFestival_detail_images_id(Long festival_detail_images_id) {
			this.festival_detail_images_id = festival_detail_images_id;
		}

		public Long getFestival_id() {
			return festival_id;
		}

		public void setFestival_id(Long festival_id) {
			this.festival_id = festival_id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public Double getGeoLatitude() {
			return geoLatitude;
		}

		public void setGeoLatitude(Double geoLatitude) {
			this.geoLatitude = geoLatitude;
		}

		public Double getGeoLongitude() {
			return geoLongitude;
		}

		public void setGeoLongitude(Double geoLongitude) {
			this.geoLongitude = geoLongitude;
		}

		public String getSyncStatus() {
			return syncStatus;
		}

		public void setSyncStatus(String syncStatus) {
			this.syncStatus = syncStatus;
		}

		
}

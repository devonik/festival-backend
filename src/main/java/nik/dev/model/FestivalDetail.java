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

		public FestivalDetail() { }

		public FestivalDetail(Long festival_detail_id, Long festival_detail_images_id, Long festival_id, String description) {
			this.festival_detail_id = festival_detail_id;
			this.festival_detail_images_id = festival_detail_images_id;
			this.festival_id = festival_id;
			this.description = description;
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

		
}

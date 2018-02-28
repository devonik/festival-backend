package nik.dev.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="festival_detail_images")
public class FestivalDetailImages {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		Long festival_detail_images_id;
		@Column(name ="festival_detail_id", nullable = false)
		Long festivalDetailId;
		String title;
		String url;
		String description;

		protected FestivalDetailImages() { }

		public FestivalDetailImages(Long festival_detail_images_id, Long festivalDetailId, String title, String url, String description) {
			this.festival_detail_images_id = festival_detail_images_id;
			this.festivalDetailId = festivalDetailId;
			this.title = title;
			this.url = url;
			this.description = description;
		}
		public Long getFestival_detail_images_id() {
			return festival_detail_images_id;
		}

		public Long getFestivalDetailId() {
			return festivalDetailId;
		}

		public void setFestivalDetailId(Long festivalDetailId) {
			this.festivalDetailId = festivalDetailId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		

		
		
}

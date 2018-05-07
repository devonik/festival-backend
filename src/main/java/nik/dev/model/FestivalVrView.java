package nik.dev.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "festival_vr_view")
public class FestivalVrView {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		Long id;
		Long festivalId;
		String url;
		String description;
		/**
		 * @return the festivalId
		 */
		public Long getFestivalId() {
			return festivalId;
		}
		/**
		 * @param festival_id the festivalId to set
		 */
		public void setFestivalId(Long festivalId) {
			this.festivalId = festivalId;
		}
		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}
		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
		
		
		
}

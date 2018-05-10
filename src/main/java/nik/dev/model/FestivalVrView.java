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
		Long festivalDetailId;
		String url;
		String description;
		String type;
		
		/**
		 * @return the festivalDetailId
		 */
		public Long getFestivalDetailId() {
			return festivalDetailId;
		}
		/**
		 * @param festivalDetailId the festivalDetailId to set
		 */
		public void setFestivalDetailId(Long festivalDetailId) {
			this.festivalDetailId = festivalDetailId;
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
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
		
		
		
}

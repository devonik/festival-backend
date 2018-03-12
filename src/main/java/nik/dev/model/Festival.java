package nik.dev.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="festival")
public class Festival {
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		Long festival_id;
		@Column(name="festival_detail_id")
		Long festivalDetailId;
		String name;
		String thumbnail_image_url;
		@Temporal(TemporalType.TIMESTAMP)
		Date datum_start;
		@Temporal(TemporalType.TIMESTAMP)
		Date datum_end;
		String syncStatus;

		public Festival() { }

		public Festival(Long festival_id, 
						Long festivalDetailId, 
						String name, 
						String thumbnail_image_url, 
						Date datum_start, 
						Date datum_end,
						String syncStatus) {
			
			this.festival_id = festival_id;
			this.festivalDetailId = festivalDetailId;
			this.name = name;
			this.thumbnail_image_url = thumbnail_image_url;
			this.datum_start = datum_start;
			this.datum_end = datum_end;
			this.syncStatus = syncStatus;
		}

		

		public Long getFestival_id() {
			return festival_id;
		}

		public Long getFestival_detail_id() {
			return festivalDetailId;
		}

		public void setFestival_detail_id(Long festival_detail_id) {
			this.festivalDetailId = festival_detail_id;
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

		public Date getDatum_start() {
			return datum_start;
		}

		public void setDatum_start(Date datum) {
			this.datum_start = datum;
		}

		public Date getDatum_end() {
			return datum_end;
		}

		public void setDatum_end(Date datum_end) {
			this.datum_end = datum_end;
		}

		public String getSyncStatus() {
			return syncStatus;
		}

		public void setSyncStatus(String syncStatus) {
			this.syncStatus = syncStatus;
		}
		
		
}

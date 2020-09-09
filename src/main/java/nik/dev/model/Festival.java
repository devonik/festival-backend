package nik.dev.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="festival")

public class Festival{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long festival_id;
		@Column(name="festival_detail_id")
		private Long festivalDetailId;
		private String name;
		private String thumbnail_image_url;
		@Temporal(TemporalType.TIMESTAMP)
		private Date datum_start;
		@Temporal(TemporalType.TIMESTAMP)
		private Date datum_end;
		private String syncStatus;
		private Long goabaseId;
		
		//EAGER = MAGIC -- Dont delete the Child one (music Genre)
		@ManyToMany(fetch = FetchType.EAGER)
	    @JoinTable(name = "music_genre_festivals",
	            joinColumns = { @JoinColumn(name = "festival_id") },
	            inverseJoinColumns = { @JoinColumn(name = "music_genre_id") })
		
	    private Set<MusicGenre> musicGenres;
		
		@OneToMany(orphanRemoval = true,
				fetch = FetchType.EAGER,
				cascade = CascadeType.ALL,
	            mappedBy = "festival")
		@JsonManagedReference
		@OrderBy("price")
	    private Set<FestivalTicketPhase> ticketPhases;
				
		@Transient
		private List<Long> musicGenreIds;
		
		public Festival() { }

		public Festival(Long festivalDetailId, 
						String name, 
						String thumbnail_image_url, 
						Date datum_start, 
						Date datum_end,
						String syncStatus,
						List<Long> musicGenreIds) {
			this.festivalDetailId = festivalDetailId;
			this.name = name;
			this.thumbnail_image_url = thumbnail_image_url;
			this.datum_start = datum_start;
			this.datum_end = datum_end;
			this.syncStatus = syncStatus;
			this.musicGenreIds = musicGenreIds;
		}

		
		
		public Set<FestivalTicketPhase> getTicketPhases() {
			return ticketPhases;
		}

		public void setTicketPhases(Set<FestivalTicketPhase> ticketPhases) {
			this.ticketPhases = ticketPhases;
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

		public List<Long> getMusicGenreIds() {
			return musicGenreIds;
		}

		public void setMusicGenreIds(List<Long> musicGenreIds) {
			this.musicGenreIds = musicGenreIds;
		}

		public Set<MusicGenre> getMusicGenres() {
			return musicGenres;
		}

		public void setMusicGenres(Set<MusicGenre> musicGenres) {
			this.musicGenres = musicGenres;
		}

		@Override
		public String toString() {
			return "Festival [festival_id=" + festival_id + ", festivalDetailId=" + festivalDetailId + ", name=" + name
					+ ", thumbnail_image_url=" + thumbnail_image_url + ", datum_start=" + datum_start + ", datum_end="
					+ datum_end + ", syncStatus=" + syncStatus + ", musicGenres=" + musicGenres + ", ticketPhases="
					+ ticketPhases + ", musicGenreIds=" + musicGenreIds + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((datum_end == null) ? 0 : datum_end.hashCode());
			result = prime * result + ((datum_start == null) ? 0 : datum_start.hashCode());
			result = prime * result + ((festivalDetailId == null) ? 0 : festivalDetailId.hashCode());
			result = prime * result + ((festival_id == null) ? 0 : festival_id.hashCode());
			result = prime * result + ((musicGenreIds == null) ? 0 : musicGenreIds.hashCode());
			result = prime * result + ((musicGenres == null) ? 0 : musicGenres.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((syncStatus == null) ? 0 : syncStatus.hashCode());
			result = prime * result + ((thumbnail_image_url == null) ? 0 : thumbnail_image_url.hashCode());
			result = prime * result + ((ticketPhases == null) ? 0 : ticketPhases.hashCode());
			
			System.out.println("HASHCODE: "+result);
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Festival other = (Festival) obj;
			if (datum_end == null) {
				if (other.datum_end != null) {
					return false;
				}
			} else if (!datum_end.equals(other.datum_end)) {
				return false;
			}
			if (datum_start == null) {
				if (other.datum_start != null) {
					return false;
				}
			} else if (!datum_start.equals(other.datum_start)) {
				return false;
			}
			if (festivalDetailId == null) {
				if (other.festivalDetailId != null) {
					return false;
				}
			} else if (!festivalDetailId.equals(other.festivalDetailId)) {
				return false;
			}
			if (festival_id == null) {
				if (other.festival_id != null) {
					return false;
				}
			} else if (!festival_id.equals(other.festival_id)) {
				return false;
			}
			if (musicGenreIds == null) {
				if (other.musicGenreIds != null) {
					return false;
				}
			} else if (!musicGenreIds.equals(other.musicGenreIds)) {
				return false;
			}
			if (musicGenres == null) {
				if (other.musicGenres != null) {
					return false;
				}
			} else if (!musicGenres.equals(other.musicGenres)) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (syncStatus == null) {
				if (other.syncStatus != null) {
					return false;
				}
			} else if (!syncStatus.equals(other.syncStatus)) {
				return false;
			}
			if (thumbnail_image_url == null) {
				if (other.thumbnail_image_url != null) {
					return false;
				}
			} else if (!thumbnail_image_url.equals(other.thumbnail_image_url)) {
				return false;
			}
			if (ticketPhases == null) {
				if (other.ticketPhases != null) {
					return false;
				}
			} else if (!ticketPhases.equals(other.ticketPhases)) {
				return false;
			}
			return true;
		}


	public Long getGoabaseId() {
		return goabaseId;
	}

	public void setGoabaseId(Long goabaseId) {
		this.goabaseId = goabaseId;
	}
}

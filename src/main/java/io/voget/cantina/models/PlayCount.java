package io.voget.cantina.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;

public class PlayCount {

	@Id
	private String id;
	private String songId;
	private Long playCounter;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSongId() {
		return songId;
	}
	
	public void setSongId(String songId) {
		this.songId = songId;
	}
	
	public Long getPlayCounter() {
		if (playCounter == null) {
			playCounter = 0L;
		}
		
		return playCounter;
	}
	
	public void setPlayCounter(Long playCounter) {
		this.playCounter = playCounter;
	}
	
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
}

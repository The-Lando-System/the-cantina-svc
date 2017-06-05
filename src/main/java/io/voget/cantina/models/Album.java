package io.voget.cantina.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Album {
	
	@Id
	private String id;
	private String name;
	private String description;
	private String artUrl;
	private List<String> songIds;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<String> getSongIds() {
		if (this.songIds == null){
			this.songIds = new ArrayList<String>();
		}
		return songIds;
	}
	
	public void setSongIds(List<String> songIds) {
		this.songIds = songIds;
	}

	public String getArtUrl() {
		return artUrl;
	}

	public void setArtUrl(String artUrl) {
		this.artUrl = artUrl;
	}
	
}

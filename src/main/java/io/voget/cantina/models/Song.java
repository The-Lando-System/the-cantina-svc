package io.voget.cantina.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;

public class Song {
	
	@Id
	private String id;
	private String name;
	private String url;
	private String filename;
	private String artUrl;
	private String albumId;
	
	public Song() {
		this.id = UUID.randomUUID().toString();
	}
	
	public Song(String name, String filename, String albumId) {
		this();
		this.name = name;
		this.filename = filename;
		this.albumId = albumId;
	}
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getArtUrl() {
		return artUrl;
	}

	public void setArtUrl(String artUrl) {
		this.artUrl = artUrl;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	
}

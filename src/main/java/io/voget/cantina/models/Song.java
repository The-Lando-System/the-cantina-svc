package io.voget.cantina.models;

import org.springframework.data.annotation.Id;

public class Song {
	
	@Id
	private String id;
	private String name;
	private byte[] data;
	
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
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}

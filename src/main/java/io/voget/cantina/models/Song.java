package io.voget.cantina.models;

import java.io.InputStream;

import org.springframework.data.annotation.Id;

public class Song {
	
	@Id
	private String id;
	private byte[] data;
	private String name;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}

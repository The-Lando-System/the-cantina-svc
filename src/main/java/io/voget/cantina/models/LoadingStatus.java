package io.voget.cantina.models;

public class LoadingStatus {

	private String songId;
	private boolean isLoading;
	private String status;
	
	public LoadingStatus(String songId, boolean isLoading, String status) {
		this.songId = songId;
		this.isLoading = isLoading;
		this.status = status;
	}
	
	public String getSongId() {
		return songId;
	}
	
	public void setSongId(String songId) {
		this.songId = songId;
	}
	
	public boolean isLoading() {
		return isLoading;
	}
	
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

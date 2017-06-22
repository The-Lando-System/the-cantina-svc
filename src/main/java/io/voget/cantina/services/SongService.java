package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.voget.cantina.models.Album;
import io.voget.cantina.models.Song;
import io.voget.cantina.models.SongOrder;
import io.voget.cantina.repos.AlbumRepo;
import io.voget.cantina.repos.SongOrderRepo;
import io.voget.cantina.repos.SongRepo;

@Component
public class SongService {

	private Logger log = LoggerFactory.getLogger(SongService.class);
	
	@Autowired SongRepo songRepo;
	@Autowired AlbumRepo albumRepo;
	@Autowired S3Wrapper s3Wrapper;
	@Autowired AlbumService albumSvc;
	@Autowired SongOrderRepo songOrderRepo;
	
	// Public Methods ==========================================================
	
	public List<Song> getSongs() {
		
		if (log.isDebugEnabled()){
			log.debug("Retrieving all songs");
		}
		
		return songRepo.findAll();
	}
	
		
	@Transactional
	public Song createNewSong(String songName, String filename, String albumId, byte[] songData) throws IOException {
		if (log.isDebugEnabled()){
			log.debug(String.format("Creating new song with name [%s]",songName));
		}
		
		Song newSong = new Song(songName,filename,albumId);
		String songPath = String.format("songs/%s/%s",newSong.getId(),filename);
		
		s3Wrapper.upload(
    		new ByteArrayInputStream(songData),
    		songPath
    	);
		
		newSong.setUrl(s3Wrapper.getObjectUrls(songPath).get(songPath));
		
		Song savedSong = songRepo.save(newSong);
    	
		if (log.isDebugEnabled()){
			log.debug("Song successfully saved!");
		}
		
		albumSvc.addSongToAlbum(savedSong.getId(), savedSong.getAlbumId());
    	
		
		List<String> songIds = new ArrayList<String>();
		for (Song song : getSongsByAlbumId(savedSong.getAlbumId())) {
			songIds.add(song.getId());
		}
		
		createOrUpdateSongOrder(savedSong.getAlbumId(), songIds);
		
		return savedSong;
	}
	
	public Song getSongById(String songId) {
		return songRepo.findOne(songId);
	}
	
	@Transactional
	public void deleteSong(String songId) {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Deleting song with ID [%s]",songId));
		}
		
		Song songToDelete = songRepo.findOne(songId);
		
		String songKey = String.format("songs/%s/%s", songId, songToDelete.getFilename());
		s3Wrapper.delete(songKey);
		
		songRepo.delete(songId);
		
		albumSvc.removeSongFromAlbum(songId, songToDelete.getAlbumId());
		
		if (log.isDebugEnabled()){
			log.debug("Song successfully deleted!");
		}
	}
	
	@Transactional
	public Song updateSong(Song songToUpdate) {
		if (log.isDebugEnabled()){
			log.debug(String.format("Updating song with ID [%s]", songToUpdate.getId()));
		}
		
		Song song = songRepo.findOne(songToUpdate.getId());
		if (song == null) {
			throw new IllegalArgumentException("Could not update song. Failed to find it with the given ID!");
		}
		
		if (StringUtils.isBlank(songToUpdate.getName()))
			songToUpdate.setName(song.getName());
		
		if (StringUtils.isBlank(songToUpdate.getUrl()))
			songToUpdate.setUrl(song.getUrl());
		
		if (StringUtils.isBlank(songToUpdate.getFilename()))
			songToUpdate.setFilename(song.getFilename());
		
		if (StringUtils.isBlank(songToUpdate.getArtUrl()))
			songToUpdate.setArtUrl(song.getArtUrl());
		
		// Don't allow song's album to be modified here
		songToUpdate.setAlbumId(song.getAlbumId());

		return songRepo.save(songToUpdate);
	}
	
	public List<Song> getSongsByAlbumId(String albumId) {
		if (log.isDebugEnabled()){
			log.debug(String.format("Retrieving songs belonging to the album with ID [%s]",albumId));
		}
		
		return getOrderedSongs(albumId);
	}
	
	public List<Song> getOrderedSongs(String albumId) {
		
		SongOrder songOrder = songOrderRepo.findByAlbumId(albumId);
		final List<Song> songs = new ArrayList<Song>();
	
		// Set a default order
		if (songOrder == null) {
			songOrder = createOrUpdateSongOrder(albumId, null);
		}
		
		songOrder.getSongOrder().entrySet().stream()
	    .sorted(Map.Entry.comparingByValue())
	    .forEach(entry -> {
	    	songs.add(songRepo.findOne(entry.getKey()));
	    });

		return songs;
	}
	
	@Transactional
	public SongOrder createOrUpdateSongOrder(String albumId, List<String> songIds) {
		
		SongOrder songOrder = songOrderRepo.findByAlbumId(albumId);
		
		if (songOrder == null) {
			songOrder = new SongOrder();
		}
		
		songOrder.setAlbumId(albumId);
		songOrder.setSongOrder(new LinkedHashMap<String,Integer>());

		// Set the default order
		if (songIds == null) {
			songIds = new ArrayList<String>();
			for (Song song : retrieveSongsByAlbumId(albumId)) {
				songIds.add(song.getId());
			}
		}
		
		int orderCount = 0;
		for (String songId : songIds) {
			songOrder.getSongOrder().put(songId, orderCount);
			orderCount++;
		}
		
		return songOrderRepo.save(songOrder);
	}
	
	
	private List<Song> retrieveSongsByAlbumId(String albumId) {
		Album album = albumRepo.findOne(albumId);
		
		if (album == null) {
			throw new IllegalArgumentException("Invalid album ID provided!");
		}
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Found album with name [%s]; Getting the songs",album.getName()));
		}
		
		List<Song> songs = new ArrayList<Song>();
		
		for (String songId : album.getSongIds()) {
			songs.add(songRepo.findOne(songId));
		}
		
		return songs;
	}
}

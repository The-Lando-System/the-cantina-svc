package io.voget.cantina.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.voget.cantina.models.Album;
import io.voget.cantina.models.Song;
import io.voget.cantina.repos.AlbumRepo;
import io.voget.cantina.repos.SongRepo;

@Component
public class AlbumService {

	private Logger log = LoggerFactory.getLogger(AlbumService.class);
	
	@Autowired AlbumRepo albumRepo;
	@Autowired SongRepo songRepo;
	
	// Public Methods ==========================================================
	
	public List<Album> getAlbums() {
		
		if (log.isDebugEnabled()){
			log.debug("Retrieving all albums");
		}
		
		return albumRepo.findAll();
	}
	
	public Album getAlbumById(String albumId) {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Retrieving album with ID [%s]",albumId));
		}
		
		return albumRepo.findOne(albumId);
	}
	
	@Transactional
	public Album createNewAlbum(Album newAlbum) {
		
		if (StringUtils.isBlank(newAlbum.getName())){
			throw new IllegalArgumentException("Album name cannot be blank!");
		}
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Creating new album with name [%s]",newAlbum.getName()));
		}
		
		Album savedAlbum = albumRepo.save(newAlbum);
    	
		if (log.isDebugEnabled()){
			log.debug("Album successfully saved!");
		}
    	
		return savedAlbum;
	}

	@Transactional
	public void deleteAlbum(String albumId) {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Deleting album with id [%s]",albumId));
		}
						
		albumRepo.delete(albumId);
		
		if (log.isDebugEnabled()){
			log.debug("Album successfully deleted!");
		}
	}

	@Transactional
	public Album updateAlbum(Album albumToUpdate) {
		if (log.isDebugEnabled()){
			log.debug(String.format("Updating album with ID [%s]", albumToUpdate.getId()));
		}
		
		Album album = albumRepo.findOne(albumToUpdate.getId());
		if (album == null) {
			throw new IllegalArgumentException("Could not update album. Failed to find it with the given ID!");
		}
		
		if (StringUtils.isBlank(albumToUpdate.getName()))
			albumToUpdate.setName(album.getName());
		
		if (StringUtils.isBlank(albumToUpdate.getArtUrl()))
			albumToUpdate.setArtUrl(album.getArtUrl());
		
		if (StringUtils.isBlank(albumToUpdate.getDescription()))
			albumToUpdate.setDescription(album.getDescription());
		
		// Don't allow for changes to album's songs to be modified here
		albumToUpdate.setSongIds(album.getSongIds());
		
		return albumRepo.save(albumToUpdate);
	}
	
	@Transactional
	public void addSongToAlbum(String songId, String albumId) {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Adding song ID [%s] to album ID [%s]", songId, albumId));
		}
		
		Album album = albumRepo.findOne(albumId);
		if (album == null) {
			throw new IllegalArgumentException(String.format("Failed to find an album with ID [%s]",albumId));
		}

		album.getSongIds().add(songId);
	
		Song song = songRepo.findOne(songId);
		String oldAlbumId = song.getAlbumId();
		song.setAlbumId(albumId);
		songRepo.save(song);
		albumRepo.save(album);
		
		if (!StringUtils.equals(oldAlbumId, albumId)){
			removeSongFromAlbum(songId, oldAlbumId);
		}
	}
	
	@Transactional
	public void removeSongFromAlbum(String songId, String albumId) {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Removing song ID [%s] from album ID [%s]", songId, albumId));
		}
		
		Album album = albumRepo.findOne(albumId);
		if (album == null) {
			throw new IllegalArgumentException(String.format("Failed to find an album with ID [%s]",albumId));
		}
		
		int songIndex = -1;
		for (int i=0; i<album.getSongIds().size(); i++){
			if (StringUtils.equals(album.getSongIds().get(i), songId)){
				songIndex = i;
				break;
			}
		}
		
		if (songIndex == -1) {
			throw new IllegalArgumentException(String.format("Album does not contain song with ID [%s]",songId));
		}
		
		album.getSongIds().remove(songIndex);
		albumRepo.save(album);
	}
}

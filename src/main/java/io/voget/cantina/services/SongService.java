package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.voget.cantina.models.Song;
import io.voget.cantina.repos.SongRepo;

@Component
public class SongService {

	private Logger log = LoggerFactory.getLogger(SongService.class);
	
	@Autowired SongRepo songRepo;
	@Autowired S3Wrapper s3Wrapper;

	// Public Methods ==========================================================
	
	public List<Song> getSongs() {
		
		if (log.isDebugEnabled()){
			log.debug("Retrieving all songs");
		}
		
		return songRepo.findAll();
	}
	
		
	@Transactional
	public Song createNewSong(String songName, String filename, byte[] songData) throws IOException{
		if (log.isDebugEnabled()){
			log.debug(String.format("Creating new song with name [%s]",songName));
		}
		
		Song newSong = new Song(songName,filename);
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
		
		if (log.isDebugEnabled()){
			log.debug("Song successfully deleted!");
		}
	}
	
}

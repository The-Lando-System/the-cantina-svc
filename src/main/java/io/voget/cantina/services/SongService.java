package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.gridfs.GridFSDBFile;

import io.voget.cantina.models.Song;
import io.voget.cantina.repos.SongRepo;

@Component
public class SongService {

	private Logger log = LoggerFactory.getLogger(SongService.class);
	
	@Autowired SongRepo songRepo;
	@Autowired CompressionService compSvc;
	@Autowired GridFsTemplate gridFsTemplate;

	// TODO - Holding all songs in memory is expensive...
	//private Map<String,byte[]> songs = new HashMap<String,byte[]>();

	
	// Public Methods ==========================================================
	
	public List<Song> getSongs() {
		
		if (log.isDebugEnabled()){
			log.debug("Retrieving all songs");
		}
		
		return songRepo.findAll();
	}
		
	@Transactional
	public Song createNewSong(String songName, byte[] songData) throws CompressorException, IOException{
		if (log.isDebugEnabled()){
			log.debug(String.format("Creating new song with name [%s]",songName));
		}
		
		Song savedSong = songRepo.save(new Song(songName));
		
    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(songData, CompressionType.LZMA)),
    		savedSong.getId()
    	);
		    	
		if (log.isDebugEnabled()){
			log.debug("Song successfully saved!");
		}
    	
		return savedSong;
	}
		
	public byte[] getSongDataById(String songId) throws IOException, CompressorException {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Getting song data for song with ID [%s]",songId));
		}
		
		return getSongDataFromDb(songId);
	}
	
	@Transactional
	public void deleteSong(String songId) {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Deleting song with ID [%s]",songId));
		}

		songRepo.delete(songId);
		gridFsTemplate.delete(new Query(Criteria.where("filename").is(songId)));
		
		if (log.isDebugEnabled()){
			log.debug("Song successfully deleted!");
		}
	}
	
	// Private Helper Methods ==========================================================
	
//	@PostConstruct
//	private void initSongs() throws IOException, CompressorException {
//		for (Song song : getSongs()) {
//			songs.put(song.getId(), getSongDataFromDb(song.getId()));
//		}
//		
//		if (log.isDebugEnabled()){
//			log.debug("Successfully loaded all songs!");
//		}
//	}

	private byte[] getSongDataFromDb(String songId) throws IOException, CompressorException{
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Retrieving song from DB with ID [%s]",songId));
		}
		
		GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(songId)));		
		return compSvc.inflate(IOUtils.toByteArray(gridFsdbFile.getInputStream()), CompressionType.LZMA);
	}

	
}

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
	
	public List<Song> getSongs() {
		
		if (log.isDebugEnabled()){
			log.debug("Retrieving all songs");
		}
		
		return songRepo.findAll();
	}
	
	public byte[] getSongDataById(String songId) throws IOException, CompressorException{
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Finding song by ID [%s]",songId));
		}
		
		GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(songId)));		
		return compSvc.inflate(IOUtils.toByteArray(gridFsdbFile.getInputStream()), CompressionType.LZMA);
	}
	
	@Transactional
	public Song createNewSong(Song newSong, byte[] songData) throws CompressorException, IOException{
		if (log.isDebugEnabled()){
			log.debug(String.format("Creating new song with name [%s]",newSong.getName()));
		}
		
		Song savedSong = songRepo.save(newSong);

    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(songData, CompressionType.LZMA)),
    		savedSong.getId()
    	);
		
		return savedSong;
	}
	
	@Transactional
	public void loadSongs() throws CompressorException, IOException{
    	
		if (log.isDebugEnabled()){
			log.debug("Bulk loading songs...");
		}
		
    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("1. Garbanso.wav")), CompressionType.LZMA)),
    		songRepo.save(new Song("Garbanso")).getId()
    	);
    	
    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("2. Smittywerbenjagermanjensen.wav")), CompressionType.LZMA)),
    		songRepo.save(new Song("Smittywerbenjagermanjensen")).getId()
    	);
    	
    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("3. One Quarter Portion.wav")), CompressionType.LZMA)),
    		songRepo.save(new Song("One Quarter Portion")).getId()
    	);

    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("4. Why Just This Last Week.wav")), CompressionType.LZMA)),
    		songRepo.save(new Song("Why Just This Last Week")).getId()
    	);

    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("5. It's High Noon.wav")), CompressionType.LZMA)),
    		songRepo.save(new Song("It's High Noon")).getId()
    	);

    	gridFsTemplate.store(
    		new ByteArrayInputStream(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("6. Cal Ripken Sr..wav")), CompressionType.LZMA)),
    		songRepo.save(new Song("Cal Ripken Sr.")).getId()
    	);

		if (log.isDebugEnabled()){
			log.debug("Bulk loading songs - Complete!");
		}
    	
	}
	
}

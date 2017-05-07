package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mongodb.gridfs.GridFSDBFile;

import io.voget.cantina.models.Song;
import io.voget.cantina.repos.SongRepo;

@Component
public class SongService {

	private Logger log = LoggerFactory.getLogger(SongService.class);
	
	@Autowired SongRepo songRepo;
	@Autowired CompressionService compSvc;
	@Autowired GridFsTemplate gridFsTemplate;
	
    private LoadingCache<String,byte[]> songCache = CacheBuilder.newBuilder()
	    .maximumSize(50)
	    .expireAfterAccess(30, TimeUnit.MINUTES)
	    .build(
            new CacheLoader<String, byte[]>() {
                public byte[] load(String songId) throws Exception {
                	return getSongDataFromDb(songId);
                }
            }
	    );

	@PostConstruct
	public void initSongs() {
		for (Song song : getSongs()) {
			try {
				songCache.get(song.getId());
			} catch (ExecutionException e) {
				log.error(String.format("Failed to get song with ID [%s]",song.getId()),e);
			}
		}
	}
	
	public List<Song> getSongs() {
		
		if (log.isDebugEnabled()){
			log.debug("Retrieving all songs");
		}
		
		return songRepo.findAll();
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
	
	public byte[] getSongDataById(String songId) throws ExecutionException {
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Getting song data for song with ID [%s]",songId));
		}
		
		return songCache.get(songId);
	}
	
	private byte[] getSongDataFromDb(String songId) throws IOException, CompressorException{
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Retrieving song from DB with ID [%s]",songId));
		}
		
		GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(songId)));		
		return compSvc.inflate(IOUtils.toByteArray(gridFsdbFile.getInputStream()), CompressionType.LZMA);
	}

	
}

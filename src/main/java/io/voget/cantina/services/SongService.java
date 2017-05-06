package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.voget.cantina.models.Song;
import io.voget.cantina.repos.SongRepo;

@Component
public class SongService {

	private Logger log = LoggerFactory.getLogger(SongService.class);
	
	@Autowired SongRepo songRepo;
	@Autowired CompressionService compSvc;
	@Autowired GridFsTemplate gridFsTemplate;
	
	public byte[] getSongDataById(String songId){
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Finding song by ID [%s]",songId));
		}
		
		return songRepo.findOne(songId).getData();
	}
	
	public Song createNewSong(Song newSong){
		if (log.isDebugEnabled()){
			log.debug(String.format("Creating new song with name [%s]",newSong.getName()));
		}

		return songRepo.save(newSong);
	}
	
	@Transactional
	public void loadSongs() throws CompressorException, IOException{
    	List<Song> songs = new ArrayList<Song>();
    	
		Song s1 = new Song();
    	s1.setName("Garbanso");
    	s1.setData(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("1. Garbanso.wav")), CompressionType.LZMA));
    	songs.add(s1);

//		Song s2 = new Song();
//    	s2.setName("Smittywerbenjagermanjensen");
//    	s2.setData(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("2. Smittywerbenjagermanjensen.wav")), CompressionType.LZMA));
//    	songs.add(s2);
//
//		Song s3 = new Song();
//    	s3.setName("One Quarter Portion");
//    	s3.setData(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("3. One Quarter Portion.wav")), CompressionType.LZMA));
//    	songs.add(s3);
//
//		Song s4 = new Song();
//    	s4.setName("Why Just This Last Week");
//    	s4.setData(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("4. Why Just This Last Week.wav")), CompressionType.LZMA));
//    	songs.add(s4);
//
//		Song s5 = new Song();
//    	s5.setName("It's High Noon");
//    	s5.setData(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("5. It's High Noon.wav")), CompressionType.LZMA));
//    	songs.add(s5);
//
//		Song s6 = new Song();
//    	s6.setName("Cal Ripken Sr.");
//    	s6.setData(compSvc.compress(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("6. Cal Ripken Sr..wav")), CompressionType.LZMA));
//    	songs.add(s6);
    	
    	gridFsTemplate.store(new ByteArrayInputStream(s1.getData()), "Garbanso");
    	
    	//songRepo.save(songs);

	}
	
}

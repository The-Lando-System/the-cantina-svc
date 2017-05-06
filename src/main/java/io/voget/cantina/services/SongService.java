package io.voget.cantina.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.voget.cantina.repos.SongRepo;

@Component
public class SongService {

	private Logger log = LoggerFactory.getLogger(SongService.class);
	
	@Autowired SongRepo songRepo;
	
	public byte[] getSongDataById(String songId){
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Finding song by ID [%s]",songId));
		}
		
		return songRepo.findOne(songId).getData();
	}
	
}

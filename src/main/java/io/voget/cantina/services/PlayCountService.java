package io.voget.cantina.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.voget.cantina.models.PlayCount;
import io.voget.cantina.repos.PlayCountRepo;
import io.voget.cantina.repos.SongRepo;

@Component
public class PlayCountService {

	private Logger log = LoggerFactory.getLogger(PlayCountService.class);
	
	@Autowired private PlayCountRepo playCountRepo;
	@Autowired private SongRepo songRepo;
	
	@Transactional
	public void increasePlayCountOnSong(String songId) {
		
		if (log.isDebugEnabled()) {
			log.debug(String.format("Incrementing the play count on song with ID [%s]", songId));
		}
		
		PlayCount playCount = playCountRepo.findBySongId(songId);
		playCount.setPlayCounter(playCount.getPlayCounter() + 1);
		
		playCountRepo.save(playCount);
	}
	
	@Transactional
	public PlayCount getPlayCountForSong(String songId) {
		
		if (log.isDebugEnabled()) {
			log.debug(String.format("Returning the play count for song with ID [%s]", songId));
		}
		
		PlayCount playCount = playCountRepo.findBySongId(songId);
		
		// Create a new entry if its null
		if (playCount == null) {
			if (songRepo.findOne(songId) == null) {
				throw new IllegalArgumentException(String.format("Song with ID [%s] does not exist!", songId));
			}
			
			if (log.isDebugEnabled()) {
				log.debug(String.format("Creating a new play count for song with ID [%s]", songId));
			}
			
			PlayCount newPlayCount = new PlayCount();
			newPlayCount.setSongId(songId);
			playCount = playCountRepo.save(newPlayCount);
		}
		
		return playCount;
	}
	
}

package io.voget.cantina.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.voget.cantina.models.LoadingStatus;
import io.voget.cantina.models.Song;
import io.voget.cantina.services.SongService;

@Controller
@RequestMapping(value="songs", produces= MediaType.APPLICATION_JSON_VALUE)
public class SongController {
	
	private static final String STATUS_TOPIC = "/topic/song-status";
	
	@Autowired SongService songSvc;
	@Autowired SimpMessagingTemplate websocketMsg;
	
    @RequestMapping(value="/", method= RequestMethod.GET)
    @ResponseBody
    public List<Song> getSongs() {  
    	return songSvc.getSongs();
    }
    
    @RequestMapping(value="/{songId}", method= RequestMethod.GET)
    @ResponseBody
    public Song getSongById(@PathVariable String songId) {  
    	return songSvc.getSongById(songId);
    }
	
    @RequestMapping(value="/{songId}/song-data", method= RequestMethod.GET)
    @ResponseBody
    public byte[] getSongDataById(@PathVariable String songId) throws IOException, CompressorException {  
    	websocketMsg.convertAndSend(STATUS_TOPIC,new LoadingStatus(songId, true,"Loading Song..."));
    	byte[] songData = songSvc.getSongDataById(songId);
    	websocketMsg.convertAndSend(STATUS_TOPIC,new LoadingStatus(songId, false,""));
    	return songData;
    }
    
    @RequestMapping(value="/{songId}", method= RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public void deleteSong(@PathVariable String songId) {  
    	songSvc.deleteSong(songId);
    }

	@RequestMapping(value="/{songName}", method=RequestMethod.POST, consumes = "multipart/form-data")
	@ResponseBody
	public Song createNewSong(@PathVariable String songName, @RequestParam("song") MultipartFile songData) throws CompressorException, IOException {
		Song newSong = songSvc.createNewSong(songName, songData.getBytes());
		websocketMsg.convertAndSend(STATUS_TOPIC,new LoadingStatus(newSong.getId(), false,""));
		return newSong;
	}

    
}

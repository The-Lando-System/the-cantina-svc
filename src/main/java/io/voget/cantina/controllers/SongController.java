package io.voget.cantina.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.voget.cantina.models.Song;
import io.voget.cantina.services.SongService;

@Controller
@RequestMapping(value="songs", produces= MediaType.APPLICATION_JSON_VALUE)
public class SongController {
		
	@Autowired SongService songSvc;
	
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
    
    @RequestMapping(value="/{songId}", method= RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public void deleteSong(@PathVariable String songId) {  
    	songSvc.deleteSong(songId);
    }

	@RequestMapping(value="/", method=RequestMethod.POST, consumes = "multipart/form-data")
	@ResponseBody
	public Song createNewSong(
			@RequestParam("name") String songName,
			@RequestParam("filename") String songFilename,
			@RequestParam("song") MultipartFile songData) throws IOException {
		return songSvc.createNewSong(songName, songFilename, songData.getBytes());
	}
	
}

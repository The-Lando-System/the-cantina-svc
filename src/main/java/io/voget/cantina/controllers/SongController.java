package io.voget.cantina.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    public byte[] getSongDataById(@PathVariable String songId) throws IOException, CompressorException {  
    	return songSvc.getSongDataById(songId);
    }

	@RequestMapping(value="/", method=RequestMethod.POST)
	@ResponseBody
	public Song createNewSong(@RequestBody Song newSong, byte[] songData) throws CompressorException, IOException {
		return songSvc.createNewSong(newSong, songData);
	}

    @RequestMapping(value="/load-songs", method= RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public void loadSongs() throws IOException, CompressorException {
    	songSvc.loadSongs();
       	return;
    }

    
}

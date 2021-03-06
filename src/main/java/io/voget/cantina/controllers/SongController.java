package io.voget.cantina.controllers;

import static com.mattvoget.sarlacc.client.SarlaccUserService.TOKEN_NAME;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import io.voget.cantina.models.PlayCount;
import io.voget.cantina.models.Song;
import io.voget.cantina.services.PlayCountService;
import io.voget.cantina.services.SecurityHelper;
import io.voget.cantina.services.SongService;

@Controller
@RequestMapping(value="songs", produces= MediaType.APPLICATION_JSON_VALUE)
public class SongController {
	
	@Autowired private SecurityHelper securityHelper;
	@Autowired private SongService songSvc;
	@Autowired private PlayCountService playCountSvc;
	
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
    
    @RequestMapping(value="/{songId}", method= RequestMethod.PUT)
    @ResponseBody
    public Song editSongById(@RequestHeader(value=TOKEN_NAME) String accessToken, @RequestBody Song songToUpdate, @PathVariable String songId) {  
    	
    	securityHelper.checkAdmin(accessToken);
    	
    	return songSvc.updateSong(songToUpdate);
    }
    
    @RequestMapping(value="/{songId}", method= RequestMethod.DELETE)
    @ResponseStatus(code=HttpStatus.OK)
    public void deleteSong(@RequestHeader(value=TOKEN_NAME) String accessToken, @PathVariable String songId) {  
    	
    	securityHelper.checkAdmin(accessToken);
    	
    	songSvc.deleteSong(songId);
    }

	@RequestMapping(value="/", method=RequestMethod.POST, consumes = "multipart/form-data")
	@ResponseBody
	public Song createNewSong(
			@RequestHeader(value=TOKEN_NAME) String accessToken,
			@RequestParam("name") String songName,
			@RequestParam("filename") String songFilename,
			@RequestParam("albumId") String albumId,
			@RequestParam("song") MultipartFile songData) throws IOException {
		
		securityHelper.checkAdmin(accessToken);
		
		return songSvc.createNewSong(songName, songFilename, albumId, songData.getBytes());
	}
	
	@RequestMapping(value="/album/{albumId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Song> getSongsByAlbumId(@PathVariable String albumId) {
		return songSvc.getOrderedSongs(albumId);
	}
	
    @RequestMapping(value="/order/{albumId}", method= RequestMethod.POST)
    @ResponseStatus(code=HttpStatus.OK)
    public void setSongOrder(@RequestHeader(value=TOKEN_NAME) String accessToken, @PathVariable String albumId, @RequestBody List<String> songIds) {  
    	securityHelper.checkAdmin(accessToken);
    	songSvc.createOrUpdateSongOrder(albumId, songIds);
    }

    @RequestMapping(value="/count/{songId}", method= RequestMethod.POST)
    @ResponseStatus(code=HttpStatus.OK)
    public void incrementPlayCount(@PathVariable String songId) {  
    	playCountSvc.increasePlayCountOnSong(songId);
    }
    
    @RequestMapping(value="/count/{songId}", method= RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public PlayCount getPlayCount(@PathVariable String songId) {  
    	return playCountSvc.getPlayCountForSong(songId);
    }
	
}

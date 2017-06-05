package io.voget.cantina.controllers;

import static com.mattvoget.sarlacc.client.SarlaccUserService.TOKEN_NAME;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.voget.cantina.models.Album;
import io.voget.cantina.services.AlbumService;
import io.voget.cantina.services.SecurityHelper;

@Controller
@RequestMapping(value="albums", produces= MediaType.APPLICATION_JSON_VALUE)
public class AlbumController {

	@Autowired SecurityHelper securityHelper;
	
	@Autowired AlbumService albumSvc;

    @RequestMapping(value="/", method= RequestMethod.GET)
    @ResponseBody
    public List<Album> getAlbums() {  
    	return albumSvc.getAlbums();
    }

    @RequestMapping(value="/{albumId}", method= RequestMethod.GET)
    @ResponseBody
    public Album getAlbumById(@PathVariable String albumId) {  
    	return albumSvc.getAlbumById(albumId);
    }
    
    @RequestMapping(value="/{albumId}", method= RequestMethod.PUT)
    @ResponseBody
    public Album editAlbumById(@RequestHeader(value=TOKEN_NAME) String accessToken, @RequestBody Album albumToUpdate, @PathVariable String albumId) {  
    	securityHelper.checkAdmin(accessToken);
    	return albumSvc.updateAlbum(albumToUpdate);
    }

    @RequestMapping(value="/{albumId}", method= RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public void deleteAlbum(@RequestHeader(value=TOKEN_NAME) String accessToken, @PathVariable String albumId) {  
    	securityHelper.checkAdmin(accessToken);
    	albumSvc.deleteAlbum(albumId);
    }
    
    @RequestMapping(value="/", method= RequestMethod.POST)
    @ResponseBody
    public Album createAlbum(@RequestHeader(value=TOKEN_NAME) String accessToken, @RequestBody Album newAlbum) {
    	securityHelper.checkAdmin(accessToken);
    	return albumSvc.createNewAlbum(newAlbum);
    }
    
    @RequestMapping(value="/{albumId}/{songId}", method= RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public void addSongToAlbum(@RequestHeader(value=TOKEN_NAME) String accessToken, @PathVariable String albumId, @PathVariable String songId) {
    	securityHelper.checkAdmin(accessToken);
    	albumSvc.addSongToAlbum(songId, albumId);
    }
    
    @RequestMapping(value="/{albumId}/{songId}", method= RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(code=HttpStatus.OK)
    public void removeSongFromAlbum(@RequestHeader(value=TOKEN_NAME) String accessToken, @PathVariable String albumId, @PathVariable String songId) {
    	securityHelper.checkAdmin(accessToken);
    	albumSvc.removeSongFromAlbum(songId, albumId);
    }


}

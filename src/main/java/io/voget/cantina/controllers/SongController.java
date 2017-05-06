package io.voget.cantina.controllers;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.voget.cantina.services.SongService;

@Controller
@RequestMapping(value="songs", produces= MediaType.APPLICATION_JSON_VALUE)
public class SongController {
	
	@Autowired SongService songSvc;
	
    @RequestMapping(value="/{songId}", method= RequestMethod.GET)
    @ResponseBody
    public byte[] getSongDataById(@PathVariable String songId) {  
    	return songSvc.getSongDataById(songId);
    }

	

    @RequestMapping(value="/test", method= RequestMethod.GET)
    @ResponseBody
    public byte[] getSong() throws IOException {    	
    	return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("bluten_tea.wav"));
    }

    
}

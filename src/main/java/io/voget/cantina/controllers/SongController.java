package io.voget.cantina.controllers;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="songs", produces= MediaType.APPLICATION_JSON_VALUE)
public class SongController {
	

    @RequestMapping(value="/test", method= RequestMethod.GET)
    @ResponseBody
    public byte[] getSong() throws IOException {    	
    	return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("bluten_tea.wav"));
    }

    
}

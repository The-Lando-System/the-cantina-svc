package io.voget.cantina.controllers;

import static com.mattvoget.sarlacc.client.SarlaccUserService.TOKEN_NAME;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mattvoget.sarlacc.client.SarlaccUserService;
import com.mattvoget.sarlacc.models.User;

import io.voget.cantina.models.Song;

@Controller
@RequestMapping(value="songs", produces= MediaType.APPLICATION_JSON_VALUE)
public class SongController {
	
	//@Autowired private SarlaccUserService userSvc;
	
    @RequestMapping(value="/", method= RequestMethod.GET)
    @ResponseBody
    public Song getSongs(@RequestHeader(value=TOKEN_NAME) String accessToken) throws IOException {
    	
    	//User user = userSvc.getUser(accessToken);
    	
    	Song s = new Song();
    	s.setId(UUID.randomUUID().toString());
    	s.setName("Bluten Tea");
    	
    	InputStream is = getClass().getClassLoader().getResourceAsStream("bluten_tea.wav");
    	
    	s.setData(IOUtils.toByteArray(is));
        return s;
    }


    @RequestMapping(value="/song", method= RequestMethod.GET)
    @ResponseBody
    public byte[] getSong() throws IOException {
    	return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("bluten_tea.wav"));
    }

    
}

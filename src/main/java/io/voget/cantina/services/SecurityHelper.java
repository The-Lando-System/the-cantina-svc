package io.voget.cantina.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mattvoget.sarlacc.client.SarlaccUserException;
import com.mattvoget.sarlacc.client.SarlaccUserService;
import com.mattvoget.sarlacc.models.App;

@Component
public class SecurityHelper {

	@Autowired App app;
	@Autowired SarlaccUserService userService;
	
	public void checkAdmin(String accessToken) {
		if (!userService.isUserAdminForApp(accessToken, app)) {
			throw new SarlaccUserException("User does not have permissions to access this API!");
		}
	}	
}

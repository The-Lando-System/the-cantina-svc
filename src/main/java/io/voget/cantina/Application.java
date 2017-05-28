package io.voget.cantina;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mattvoget.sarlacc.client.SarlaccUserService;
import com.mattvoget.sarlacc.client.SarlaccUserServiceImpl;
import com.mattvoget.sarlacc.models.App;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {		
        SpringApplication.run(Application.class, args);
    }
		
    // Sarlacc Client Configuration ===================================
    @Value("${sarlacc.url}")
    private String sarlaccUrl;

    @Value("${sarlacc.client.id}")
    private String sarlaccClientId;

    @Value("${sarlacc.client.password}")
    private String sarlaccClientPassword;

    @Bean
    public SarlaccUserService sarlaccUserService(){
        return new SarlaccUserServiceImpl(sarlaccUrl,sarlaccClientId,sarlaccClientPassword);
    }
    
    @Bean
    public App app() {
    	App app = new App();
    	app.setName("the-cantina");
    	return app;
    }

}

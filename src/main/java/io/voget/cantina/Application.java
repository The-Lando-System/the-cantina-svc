package io.voget.cantina;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mattvoget.sarlacc.client.SarlaccUserService;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {		
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
	@Autowired
	public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) throws Exception {
		return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
	}
	
    // Sarlacc Client Configuration ===================================
    @Value("${auth.url.token}")
    private String authUrlToken;

    @Value("${auth.url.user}")
    private String authUrlUser;

    @Value("${auth.client.id}")
    private String authClientId;

    @Value("${auth.client.password}")
    private String authClientPassword;

    @Bean
    public SarlaccUserService sarlaccUserService(){
        return new SarlaccUserService(authUrlToken,authUrlUser,authClientId,authClientPassword);
    }

}

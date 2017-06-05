package io.voget.cantina.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import io.voget.cantina.models.Album;

@Component
public interface AlbumRepo extends MongoRepository<Album, String>{

}

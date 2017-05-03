package io.voget.cantina.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import io.voget.cantina.models.Song;

@Component
public interface SongRepo extends MongoRepository<Song, String>{

}

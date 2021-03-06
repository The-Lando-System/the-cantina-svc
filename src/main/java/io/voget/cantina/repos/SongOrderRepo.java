package io.voget.cantina.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import io.voget.cantina.models.SongOrder;

@Component
public interface SongOrderRepo extends MongoRepository<SongOrder, String> {

	public SongOrder findByAlbumId(String albumId);
	
}
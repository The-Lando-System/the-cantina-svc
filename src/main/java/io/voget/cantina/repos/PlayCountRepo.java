package io.voget.cantina.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import io.voget.cantina.models.PlayCount;

@Component
public interface PlayCountRepo extends MongoRepository<PlayCount, String> {

	public PlayCount findBySongId(String songId);
}

package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;

public class CompressionService {
	
	private static final String DEFAULT_ALG = CompressorStreamFactory.GZIP;

	public static byte[] compress(byte[] uncompressedData, String algorithm) throws CompressorException, IOException {
	    
		if (StringUtils.isBlank(algorithm)){
			algorithm = DEFAULT_ALG;
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		CompressorOutputStream compressedOut = new CompressorStreamFactory()
			    .createCompressorOutputStream(algorithm, os);
	    
		compressedOut.write(uncompressedData);
		
		//compressedOut.flush();
		compressedOut.close();
		
		return os.toByteArray();
	}	
	
	public static byte[] inflate(byte[] compressedData, String algorithm) throws CompressorException, IOException {
		
		if (StringUtils.isBlank(algorithm)){
			algorithm = DEFAULT_ALG;
		}
		
		ByteArrayInputStream is = new ByteArrayInputStream(compressedData);
		
		CompressorInputStream compressedIn = new CompressorStreamFactory()
			.createCompressorInputStream(algorithm, is);
		
		
		return IOUtils.toByteArray(compressedIn);
	}
	
	public static byte[] compress(byte[] uncompressedData) throws CompressorException, IOException {		
		return compress(uncompressedData, DEFAULT_ALG);
	}	
	
	public static byte[] inflate(byte[] compressedData) throws CompressorException, IOException {
		return inflate(compressedData, DEFAULT_ALG);
	}
	
}

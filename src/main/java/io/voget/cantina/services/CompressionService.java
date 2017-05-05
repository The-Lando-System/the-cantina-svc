package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class CompressionService {
	
	private static final String COMPRESSION_ALG = CompressorStreamFactory.BZIP2;

	public static byte[] compress(byte[] uncompressedData) throws CompressorException, IOException {
	    
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		CompressorOutputStream compressedOut = new CompressorStreamFactory()
			    .createCompressorOutputStream(COMPRESSION_ALG, os);
	    
		compressedOut.write(uncompressedData);
		
		return os.toByteArray();
	}	
	
	public static byte[] inflate(byte[] compressedData) throws CompressorException, IOException {
		
		ByteArrayInputStream is = new ByteArrayInputStream(compressedData);
		
		CompressorInputStream compressedIn = new CompressorStreamFactory()
			.createCompressorInputStream(COMPRESSION_ALG, is);
		
		return IOUtils.toByteArray(compressedIn);
	}
	
}

package io.voget.cantina.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompressionService {
	
	private Logger log = LoggerFactory.getLogger(CompressionService.class);
	
	public byte[] compress(byte[] uncompressedData, CompressionType compressionType) throws CompressorException, IOException {
	    
		String algorithm = compressionType.getAlgorithm();
		
		if (log.isDebugEnabled()){
			log.debug(String.format("Using compression algorithm: %s", algorithm));
			log.debug(String.format("Size before compression: %d bytes", uncompressedData.length));
			log.debug("Compressing...");
		}
		
		// Record Start Time
		long start = (new Date()).getTime();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		CompressorOutputStream compressedOut = new CompressorStreamFactory()
			    .createCompressorOutputStream(algorithm, os);
	    
		compressedOut.write(uncompressedData);
		compressedOut.close();
		
		byte[] compressedData = os.toByteArray();
		
		if (log.isDebugEnabled()){
			
			String compPct = (new Integer((int) (100*(((double)uncompressedData.length - (double)compressedData.length) / (double)uncompressedData.length)))).toString() + "%";
			int duration = (int)(((new Date()).getTime() - start)/1000);
			int rate = (int) (((double) compressedData.length / (double) duration)/1000);
			
			log.debug(String.format("Percent Compressed: %s",compPct));
			log.debug(String.format("Compression Rate: %d Kb/second",rate));
			log.debug(String.format("Size after compression: %d bytes",compressedData.length));
		}
		
		return compressedData;
	}	
	
	public byte[] inflate(byte[] compressedData, CompressionType compressionType) throws CompressorException, IOException {
		
		String algorithm = compressionType.getAlgorithm();

		if (log.isDebugEnabled()){
			log.debug(String.format("Using inflation algorithm: %s", algorithm));
			log.debug(String.format("Size before inflation: %d bytes", compressedData.length));
			log.debug("Inflating...");
		}
		
		// Record Start Time
		long start = (new Date()).getTime();
		
		ByteArrayInputStream is = new ByteArrayInputStream(compressedData);
		
		CompressorInputStream compressedIn = new CompressorStreamFactory()
			.createCompressorInputStream(algorithm, is);
		
		byte[] inflatedData = IOUtils.toByteArray(compressedIn);
		
		if (log.isDebugEnabled()){
			int duration = (int)(((new Date()).getTime() - start)/1000);
			
			int rate = (int) ((((double) inflatedData.length - (double) compressedData.length) / (double) duration)/1000);
			log.debug(String.format("Inflation Rate: %d Kb/second",rate));
			log.debug(String.format("Size after inflation: %d bytes",inflatedData.length));
		}

		return inflatedData;
	}
	
	public byte[] compress(byte[] uncompressedData) throws CompressorException, IOException {		
		return compress(uncompressedData,CompressionType.GZIP);
	}	
	
	public byte[] inflate(byte[] compressedData) throws CompressorException, IOException {
		return inflate(compressedData,CompressionType.GZIP);
	}
	
}

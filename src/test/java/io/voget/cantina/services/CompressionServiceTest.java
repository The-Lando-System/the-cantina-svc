package io.voget.cantina.services;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CompressionServiceTest {

	@Test
	public void testCompressSimpleData() throws IOException, CompressorException {
		
		
		
		byte[] inputData = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("bluten_tea.wav"));
		
		List<String> algorithms = Arrays.asList(
			CompressorStreamFactory.GZIP,
			CompressorStreamFactory.BZIP2,
			CompressorStreamFactory.XZ,
			CompressorStreamFactory.DEFLATE,
			CompressorStreamFactory.LZMA
		);

		for (String alg : algorithms) {
			testCompressor(alg, inputData);
			System.out.println("===============================");
		}
		
		assertTrue(true);
	}
	
	private void testCompressor(String algorithm, byte[] inputData) throws CompressorException, IOException {
		
		System.out.println(String.format("Compression Algorithm: %s",algorithm));
		
		int inputSize = inputData.length;
		
		System.out.println(String.format("Size before compression: %d",inputSize));
		
		byte[] compressedData = null;
		
		long start = (new Date()).getTime();
		compressedData = CompressionService.compress(inputData,algorithm);
		int duration = (int)(((new Date()).getTime() - start)/1000);
		
		int outputSize = compressedData.length;
		
		int rate = (int) (((double) outputSize / (double) duration)/1000);
		System.out.println(String.format("Compression Rate: %d Kb/second",rate));
		
		System.out.println(String.format("Size after compression: %d",outputSize));
		
		String compressionRate = (new Integer((int) (100*(((double)inputSize - (double)outputSize) / (double)inputSize)))).toString() + "%";
		
		System.out.println("Percent of Data Compressed: " + compressionRate);
		
		byte[] inflatedData = CompressionService.inflate(compressedData,algorithm);
		int inflatedSize = inflatedData.length;
		
		System.out.println(String.format("Size after inflation: %d",inflatedSize));

		
	}
	
	
}

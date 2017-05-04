package io.voget.cantina.services;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CompressionServiceTest {

	@Test
	public void testCompressSimpleData() throws IOException {
		
		byte[] compressedData = null;
		
		byte[] inputData = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("bluten_tea.wav"));
		int inputSize = inputData.length;
		
		System.out.println(String.format("Size before compression: %d",inputSize));
		
		compressedData = CompressionService.compress(inputData);
		int outputSize = compressedData.length;
		
		System.out.println(String.format("Size after compression: %d",outputSize));
		
		String compressionRate = (new Integer((int) (100*(((double)inputSize - (double)outputSize) / (double)inputSize)))).toString() + "%";
		
		System.out.println("Compression Rate: " + compressionRate);
		
		assertTrue(true);
	}
	
	
}

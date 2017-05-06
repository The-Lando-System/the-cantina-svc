package io.voget.cantina.services;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CompressionServiceTest {

	CompressionService compSvc = new CompressionService();
	
	@Test
	public void testCompressSimpleData() throws IOException, CompressorException {
		
		byte[] inputData = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("bluten_tea.wav"));
		
		List<CompressionType> compTypes = Arrays.asList(
			CompressionType.GZIP,
			CompressionType.BZIP2,
			CompressionType.XZ,
			CompressionType.DEFLATE,
			CompressionType.LZMA
		);

		for (CompressionType compType : compTypes) {
			assertTrue(testCompressionAlgorithm(compType, inputData));
		}
		
		assertTrue(true);
	}
	
	private boolean testCompressionAlgorithm(CompressionType compType, byte[] inputData) throws CompressorException, IOException {		
		return Arrays.equals(inputData, compSvc.inflate(compSvc.compress(inputData,compType),compType));
	}
	
	
}

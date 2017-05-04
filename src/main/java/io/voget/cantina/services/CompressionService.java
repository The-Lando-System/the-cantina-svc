package io.voget.cantina.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class CompressionService {

	public static byte[] compress(byte[] uncompressedData) {
	    
		
		//ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
	    
		return uncompressedData;
	}

	private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
	    for (File file : new File(sourceDir).listFiles()) {
	        if (file.isDirectory()) {
	            compressDirectoryToZipfile(rootDir, sourceDir + File.separator + file.getName(), out);
	        } else {
	            ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
	            out.putNextEntry(entry);

	            FileInputStream in = new FileInputStream(sourceDir + file.getName());
	            IOUtils.copy(in, out);
	            IOUtils.closeQuietly(in);
	        }
	    }
	}
	
	
}

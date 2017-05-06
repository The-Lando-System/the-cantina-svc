package io.voget.cantina.services;

import org.apache.commons.compress.compressors.CompressorStreamFactory;

public enum CompressionType {

	GZIP(CompressorStreamFactory.GZIP),
	BZIP2(CompressorStreamFactory.BZIP2),
	XZ(CompressorStreamFactory.XZ),
	DEFLATE(CompressorStreamFactory.DEFLATE),
	LZMA(CompressorStreamFactory.LZMA);

    private String algorithm;

    CompressionType(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

	
}

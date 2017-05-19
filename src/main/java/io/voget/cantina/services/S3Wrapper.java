package io.voget.cantina.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3Wrapper {

	@Value("${cloud.aws.s3.bucket}") private String bucket;
    @Autowired private AmazonS3Client amazonS3Client;

    public PutObjectResult upload(InputStream inputStream, String uploadKey) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, new ObjectMetadata());

        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);

        IOUtils.closeQuietly(inputStream);

        return putObjectResult;
    }
    
    public List<S3ObjectSummary> list(String prefix) {
        ObjectListing objectListing =
        	amazonS3Client.listObjects(
        		new ListObjectsRequest()
        			.withBucketName(bucket)
        			.withPrefix(prefix)
        			.withDelimiter("/")
        	);

        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();
        
        List<S3ObjectSummary> s3Objs = s3ObjectSummaries.stream()
        	    .filter(obj -> !StringUtils.endsWithIgnoreCase(obj.getKey(), "/"))
        	    .collect(Collectors.toList());
        
        return s3Objs;
    }
    
    public Map<String,String> getObjectUrls(String prefix) {
    	Map<String,String> urls = new HashMap<String,String>();
    	
    	for (S3ObjectSummary summary : list(prefix)){
    		urls.put(summary.getKey(), amazonS3Client.getUrl(bucket, summary.getKey()).toExternalForm());
    	}
    	
    	return urls;
    }
    
    public void delete(String objectKey) {
    	amazonS3Client.deleteObject(
    		new DeleteObjectRequest(bucket, objectKey)
    	);
    }
}

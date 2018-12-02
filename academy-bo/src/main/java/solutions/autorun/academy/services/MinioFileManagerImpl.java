package solutions.autorun.academy.services;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import org.springframework.beans.factory.annotation.Value;
import solutions.autorun.academy.exceptions.FileManagerException;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioFileManagerImpl implements FileManager {

    private final Logger log = LoggerFactory.getLogger(MinioFileManagerImpl.class);

    @Value("${solutions.autorun.academy.minio.endpoint}")
    String minioEndpoint;
    @Value("${solutions.autorun.academy.minio.accessKey}")
    String minioAccessKey;
    @Value("${solutions.autorun.academy.minio.secretKey}")
    String minioSecretKey;
    @Value("${solutions.autorun.academy.minio.bucket}")
    String minioBucket;

    @Override
    public String addFile(MultipartFile file, String fileName) {
        try {
            MinioClient minioClient = new MinioClient(minioEndpoint, minioAccessKey,
                    minioSecretKey);
            boolean found = minioClient.bucketExists(minioBucket);
            if (found) {
                log.trace(minioBucket + " already exists");
            } else {
                minioClient.makeBucket(minioBucket);
                log.trace(minioBucket + " is created successfully");
            }

            byte [] byteArr=file.getBytes();
            InputStream bais = new ByteArrayInputStream(byteArr);
            minioClient.putObject(minioBucket, fileName, bais, bais.available(), "application/octet-stream");
            bais.close();
           log.trace(fileName + " is uploaded successfully");
            return fileName + " is uploaded successfully";
        } catch (
                MinioException e) {
            throw new FileManagerException(e.getMessage());
        }

        catch (
                IOException e) {
            throw new FileManagerException(e.getMessage());
        }

        catch (
                NoSuchAlgorithmException e) {
            throw new FileManagerException(e.getMessage());
        }
        catch (
                InvalidKeyException e) {
            throw new FileManagerException(e.getMessage());
        }

        catch (
                XmlPullParserException e) {
            throw new FileManagerException(e.getMessage());
        }
    }

    @Override
    public InputStream getFile(String fileName){
        try {
            MinioClient minioClient = new MinioClient(minioEndpoint, minioAccessKey,
                    minioSecretKey);

            minioClient.statObject(minioBucket, fileName);


           return minioClient.getObject(minioBucket, fileName);



        } catch (MinioException e) {
            throw new FileManagerException(e.getMessage());
        }

        catch (
                IOException e) {
            throw new FileManagerException(e.getMessage());
        }

        catch (
                NoSuchAlgorithmException e) {
            throw new FileManagerException(e.getMessage());
        }
        catch (
                InvalidKeyException e) {
            throw new FileManagerException(e.getMessage());
        }

        catch (
                XmlPullParserException e) {
            throw new FileManagerException(e.getMessage());
        }
    }
}

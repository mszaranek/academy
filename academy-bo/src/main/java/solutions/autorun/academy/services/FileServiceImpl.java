package solutions.autorun.academy.services;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public ResponseEntity<String> addFile(MultipartFile file, String fileName) {
        try {
            MinioClient minioClient = new MinioClient("https://play.minio.io:9000", "Q3AM3UQ867SPQQA43P2F",
                    "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
            boolean found = minioClient.bucketExists("invoicesbucket");
            if (found) {
                System.out.println("invoicesbucket already exists");
            } else {
                minioClient.makeBucket("invoicesbucket");
                System.out.println("invoicesbucket is created successfully");
            }

            byte [] byteArr=file.getBytes();
            InputStream bais = new ByteArrayInputStream(byteArr);
            minioClient.putObject("invoicesbucket", fileName, bais, bais.available(), "application/octet-stream");
            bais.close();
            System.out.println(fileName + " is uploaded successfully");
            return new ResponseEntity<>(fileName + " is uploaded successfully",HttpStatus.OK);
        } catch (
                MinioException e) {
            System.out.println("Error occurred: " + e);
            return new ResponseEntity<>("Error occurred: " + e,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        catch (
                IOException e) {
            System.out.println("Error occurred: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        catch (
                NoSuchAlgorithmException e) {
            System.out.println("Error occurred: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (
                InvalidKeyException e) {
            System.out.println("Error occurred: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        catch (
                XmlPullParserException e) {
            System.out.println("Error occurred: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

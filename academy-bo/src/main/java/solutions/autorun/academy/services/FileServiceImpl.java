//package solutions.autorun.academy.services;
//
//import io.minio.MinioClient;
//import io.minio.errors.MinioException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.xmlpull.v1.XmlPullParserException;
//import org.springframework.beans.factory.annotation.Value;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//@Service
//public class FileServiceImpl implements FileService {
//
//    @Value("${solutions.autorun.academy.minio.endpoint}")
//    String minioEndpoint;
//    @Value("${solutions.autorun.academy.minio.accessKey}")
//    String minioAccessKey;
//    @Value("${solutions.autorun.academy.minio.secretKey}")
//    String minioSecretKey;
//    @Value("${solutions.autorun.academy.minio.bucket}")
//    String minioBucket;
//
//    @Override
//    public ResponseEntity<String> addFile(MultipartFile file, String fileName) {
////        try {
////            MinioClient minioClient = new MinioClient(minioEndpoint, minioAccessKey,
////                    minioSecretKey);
////            boolean found = minioClient.bucketExists(minioBucket);
////            if (found) {
////                System.out.println(minioBucket + " already exists");
////            } else {
////                minioClient.makeBucket(minioBucket);
////                System.out.println(minioBucket + " is created successfully");
////            }
////
////            byte [] byteArr=file.getBytes();
////            InputStream bais = new ByteArrayInputStream(byteArr);
////            minioClient.putObject(minioBucket, fileName, bais, bais.available(), "application/octet-stream");
////            bais.close();
////            System.out.println(fileName + " is uploaded successfully");
////            return new ResponseEntity<>(fileName + " is uploaded successfully",HttpStatus.OK);
////        } catch (
////                MinioException e) {
////            System.out.println("Error occurred: " + e);
////            return new ResponseEntity<>("Error occurred: " + e,HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////
////        catch (
////                IOException e) {
////            System.out.println("Error occurred: " + e);
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////
////        catch (
////                NoSuchAlgorithmException e) {
////            System.out.println("Error occurred: " + e);
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////        catch (
////                InvalidKeyException e) {
////            System.out.println("Error occurred: " + e);
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////
////        catch (
////                XmlPullParserException e) {
////            System.out.println("Error occurred: " + e);
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }
//
//    @Override
//    public InputStream getFile(String fileName){
//        try {
//            MinioClient minioClient = new MinioClient(minioEndpoint, minioAccessKey,
//                    minioSecretKey);
//
//            minioClient.statObject(minioBucket, fileName);
//
//
//           return minioClient.getObject(minioBucket, fileName);
//
//
//
//        } catch (MinioException e) {
//            System.out.println("Error occurred: " + e);
//            return null;
//        }
//
//        catch (
//                IOException e) {
//            System.out.println("Error occurred: " + e);
//            return null;
//        }
//
//        catch (
//                NoSuchAlgorithmException e) {
//            System.out.println("Error occurred: " + e);
//            return null;
//        }
//        catch (
//                InvalidKeyException e) {
//            System.out.println("Error occurred: " + e);
//            return null;
//        }
//
//        catch (
//                XmlPullParserException e) {
//            System.out.println("Error occurred: " + e);
//            return null;
//        }
//    }
//}

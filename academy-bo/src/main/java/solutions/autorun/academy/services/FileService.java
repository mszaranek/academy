package solutions.autorun.academy.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileService {
    ResponseEntity<String> addFile(MultipartFile file, String fileName);


}

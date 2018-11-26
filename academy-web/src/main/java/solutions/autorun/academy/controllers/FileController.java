package solutions.autorun.academy.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import solutions.autorun.academy.services.FileService;

import java.io.ByteArrayInputStream;

@AllArgsConstructor
@RestController
@RequestMapping
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/files/add/{fileName}")
    public ResponseEntity<String> addFile(@RequestParam MultipartFile file, @PathVariable String fileName){

        return fileService.addFile(file,fileName);
    }
}

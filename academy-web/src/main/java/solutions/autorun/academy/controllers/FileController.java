//package solutions.autorun.academy.controllers;
//
//
//import lombok.AllArgsConstructor;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import solutions.autorun.academy.services.FileService;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@AllArgsConstructor
//@RestController
//@RequestMapping
//public class FileController {
//
//    private final FileService fileService;
//
//    @PostMapping(value = "/files/add/{fileName}")
//    public ResponseEntity<String> addFile(@RequestParam(value = "file") MultipartFile file, @PathVariable String fileName){
//
//        return fileService.addFile(file,fileName);
//    }
//
//    @GetMapping(value="/files/get/{fileName}", produces = MediaType.APPLICATION_PDF_VALUE)
//    public ResponseEntity<Void> getFile(@PathVariable String fileName, HttpServletResponse response){
//
//        try {
//            IOUtils.copy(fileService.getFile(fileName), response.getOutputStream());
//            response.setContentType("application/pdf");
//            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//            response.flushBuffer();
//            return new ResponseEntity<>( HttpStatus.OK);
//        }
//        catch(IOException ex){
//            throw new RuntimeException("IOError writing file to output stream");
//            //return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//}

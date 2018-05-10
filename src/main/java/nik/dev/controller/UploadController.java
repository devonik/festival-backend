package nik.dev.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nik.dev.model.WhatsNew;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class UploadController {
	
	@RequestMapping(value="upload", method= RequestMethod.POST)
	public boolean singleFileUpload(@RequestParam("file") MultipartFile file) {
		
		return false;
	}
	
}

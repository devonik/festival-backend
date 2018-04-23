package nik.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.model.Authentication.Admin;
import nik.dev.model.Authentication.LoginDto;
import nik.dev.model.Authentication.UserJWTGenerator;
import nik.dev.repository.IAdminRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {
	@Autowired
    private IAdminRepository adminRepository;

    private UserJWTGenerator jwtGenerator = new UserJWTGenerator();

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginDto loginDto) {

        Admin correspondingAdmin = adminRepository.findByUsername(loginDto.getUsername());
        if (correspondingAdmin == null) return null;

        if (BCrypt.checkpw(loginDto.getPassword(), correspondingAdmin.getPassword())) {
            String token = jwtGenerator.generate(loginDto);
            JSONObject resp = new JSONObject();
            try {
				resp.put("token", token);
				resp.put("expiredIn", jwtGenerator.getExpiresMinute());
	            resp.put("id", correspondingAdmin.getId());
	            resp.put("email", correspondingAdmin.getEmail());
	            resp.put("username", correspondingAdmin.getUsername());
	            resp.put("company", correspondingAdmin.getCompany());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return resp.toString();
        } else {
            return null;
        }
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Admin register(@RequestBody Admin admin) {

        Admin existingUser = adminRepository.findByUsername(admin.getUsername());

        //requestJSON.getString("password") != "" ... verhinder das das password gefüllt sein muss
        if (existingUser == null && admin.getPassword() != "") {
            String pwHash = createPassword(admin.getPassword());
            admin.setPassword(pwHash);
            adminRepository.save(admin);

            LOGGER.info("Creating new admin:", admin);
            return admin;

        } else {
            return null;
        }


    }

    private String createPassword(String plainPW) {
        return BCrypt.hashpw(plainPW, BCrypt.gensalt(13));
    }

}

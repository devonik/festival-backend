package nik.dev.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nik.dev.service.AndroidPushNotificationsService;

@RestController
@RequestMapping("api/v1/push/")
@CrossOrigin(origins = "*")
public class PushController {
	private final String TOPIC = "news";
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;
 
	@RequestMapping(value = "send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send(String titleText, String bodyText, String pushMethod) throws JSONException {
 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + TOPIC);
		body.put("priority", "high");
 
		JSONObject notification = new JSONObject();
		notification.put("title", titleText);
		notification.put("body", bodyText);
		notification.put("sound", "default");
		
		JSONObject data = new JSONObject();
		if(pushMethod == null || pushMethod == "") {
			//Sync as default
			pushMethod = "sync";
		}
		data.put("devnik.trancefestivalticker.KEY_SYNC_REQUEST", pushMethod);
 
		body.put("notification", notification);
		body.put("data", data);
 
/**
		{
		   "notification": {
		      "title": "JSA Notification",
		      "body": "Happy Message!"
		   },
		   "data": {
		      "Key-1": "JSA Data 1",
		      "Key-2": "JSA Data 2"
		   },
		   "to": "/topics/JavaSampleApproach",
		   "priority": "high"
		}
*/
 
		HttpEntity<String> request = new HttpEntity<>(body.toString());
 
		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();
 
		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
 
		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
}

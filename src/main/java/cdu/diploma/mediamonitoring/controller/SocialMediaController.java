package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.service.TwitterService;
import cdu.diploma.mediamonitoring.service.YTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/dashboard")
public class SocialMediaController {
    private final YTService ytService;
    private final TwitterService twitterService;

    @Autowired
    public SocialMediaController(YTService ytService, TwitterService twitterService) {
        this.ytService = ytService;
        this.twitterService = twitterService;
    }

    @RequestMapping("/get-yt-data")
    public ResponseEntity getVideoData(@RequestBody String[] keys) throws IOException {
        ytService.getVideoData(keys);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/get-tw-data")
    public ResponseEntity getTwitterData(@RequestBody String[] keys) throws IOException {
        twitterService.collectDataForModel(keys);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

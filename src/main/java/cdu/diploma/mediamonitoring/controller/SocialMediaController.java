package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.service.RedditService;
import cdu.diploma.mediamonitoring.service.TwitterService;
import cdu.diploma.mediamonitoring.service.YTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class SocialMediaController {
    private final YTService ytService;
    private final TwitterService twitterService;
    private final RedditService redditService;

    @Autowired
    public SocialMediaController(YTService ytService, TwitterService twitterService, RedditService redditService) {
        this.ytService = ytService;
        this.twitterService = twitterService;
        this.redditService = redditService;
    }

    @RequestMapping("/get-yt-data")
    public ResponseEntity getVideoData(@RequestBody String[] keys) throws IOException {
        ytService.getVideoData(keys, new SocialMediaPlatform());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/get-tw-data")
    public ResponseEntity getTwitterData(@RequestBody String[] keys) throws IOException {
        twitterService.collectDataForModel(keys, new SocialMediaPlatform());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/get-rd-data")
    public List<String> getRedditData(@RequestBody String[] keys) throws Exception {

        return redditService.searchReddit(keys, new SocialMediaPlatform());
    }
}

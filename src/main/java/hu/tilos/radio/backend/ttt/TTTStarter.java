package hu.tilos.radio.backend.ttt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@RestController
@ComponentScan
@EnableEurekaClient
public class TTTStarter {

    @Autowired
    TTTService tttService;


    @RequestMapping("/api/v1/ttt/business")
    public Object[] home() {
        return tttService.getAllBusiness();
    }

    @ResponseBody
    @RequestMapping(value = "/api/v1/ttt/business/{id}/photo.jpg", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("id") String id) {
        return tttService.getImage(id);

    }

    public static void main(String[] args) {
        SpringApplication.run(TTTStarter.class, args);
    }
}

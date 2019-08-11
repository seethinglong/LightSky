package tachyonis.space;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tachyonis.space.model.Location;
import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.*;
import java.util.Arrays;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Service
public class TachyonApp {
	private static final Logger log = LoggerFactory.getLogger(TachyonApp.class);
    //Scheduled Jobs
	//https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ScheduledExecutorService.html
	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(1);

	private static TachyonService ts = new TachyonService();
	private static TachyonController tc = new TachyonController(ts);
	private final int HTTP_CONNECT_TIMEOUT = 1000;
	private final int HTTP_READ_TIMEOUT = 1000;

	public void ScheudleTask() {
		int interval = 5 ;
		int duration = 60 * 60 ;
		final Runnable task1 = new Runnable() {
			public void run() {
				log.info("===== Tachyon Schedules Task Every " + interval + "seconds ==========");
				//darksky();
				//tc.getAll();
			}
		};
		final ScheduledFuture<?> Handle =
				scheduler.scheduleAtFixedRate(task1, interval , interval , SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() { Handle.cancel(true); }
		}, duration , SECONDS);

		int housekeep = 10 ;
		final Runnable task2 = new Runnable() {
			public void run() {
				log.info("===== Tachyon HouseKeep Every " + housekeep + "seconds ==========");
				//tc.HouseKeepMongoDB();
				tc.FindMongoCollection();
			}
	    };
		final ScheduledFuture<?> HouseKeep =
				scheduler.scheduleAtFixedRate(task2, housekeep , housekeep , SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() { HouseKeep.cancel(true); }
		}, duration , SECONDS);
	}
	private ClientHttpRequestFactory TachyonRest() {
		HttpComponentsClientHttpRequestFactory c = new HttpComponentsClientHttpRequestFactory();
		c.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
		c.setReadTimeout(HTTP_READ_TIMEOUT);
		return c;
	}
	public void darksky(){
		RestTemplate TachyonServiceRest = new RestTemplate();
		Location location = TachyonServiceRest.getForObject(
				"https://api.darksky.net/forecast/6a3b5656878a2c807e2b0175cde1462b/-5.888,107.065", Location.class);
		log.info(location.toString());
		location = TachyonServiceRest.getForObject(
				"https://api.darksky.net/forecast/6a3b5656878a2c807e2b0175cde1462b/42.805,140.687", Location.class);
		log.info(location.toString());
		location = TachyonServiceRest.getForObject(
				"https://api.darksky.net/forecast/6a3b5656878a2c807e2b0175cde1462b/37.8267,-122.4233", Location.class);
		log.info(location.toString());
	}



	public static void main(String args[]) {
		SpringApplication.run(TachyonApp.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			ScheudleTask();
		};
	}
}
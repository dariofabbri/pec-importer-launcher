package it.corteconti.silea.pecimporterlauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Runner implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(Runner.class);
	
	private final RestTemplate restTemplate;

    public Runner(RestTemplateBuilder restTemplateBuilder) {
    	
        this.restTemplate = restTemplateBuilder
        		.setConnectTimeout(60000)
        		.setReadTimeout(120000)
        		.build();
    }

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		ResponseEntity<String> result = null;
		int i = 1;
		
		String url = "http://sclbackendprod.azurewebsites.net/PecImporterService/start";
		
		do {
			
			LOG.info("About to perform call #" + i);
			
			long start = System.currentTimeMillis();
			try {
				result = restTemplate.exchange(
						url, HttpMethod.GET, null, String.class);
			} catch(Exception e) {
				LOG.error("Exception caught while executing REST call.", e);
				continue;
			}
			long end = System.currentTimeMillis();
		
			LOG.info("Call returned with code: " + 
					result.getStatusCodeValue() +
					" after " +
					(end - start) +
					" ms");
			LOG.info(result.getBody());
			
			Thread.sleep(5000);
			
			i += 1;
			
		} while(result.getStatusCodeValue() == 200);
	}
}

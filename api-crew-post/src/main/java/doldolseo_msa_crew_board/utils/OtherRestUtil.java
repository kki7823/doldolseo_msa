package doldolseo_msa_crew_board.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OtherRestUtil {
    private final RestTemplate restTemplate = new RestTemplate();

    public void crew_UpdatePoint(String url, Integer crewPoint) {
        HttpHeaders headers = new HttpHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("crewPoint", crewPoint);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, String.class);
    }
}

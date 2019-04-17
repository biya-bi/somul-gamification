/**
 * 
 */
package microservices.book.gamification.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;

/**
 * This implementation of MultiplicationResultAttemptClient interface connects
 * to the Multiplication microservice via REST.
 * 
 * @author biya-bi
 *
 */
@Component
class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {
	private final RestTemplate restTemplate;
	private final String multiplicationHost;

	@Autowired
	MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate,
			@Value("${multiplicationHost}") final String multiplicationHost) {
		this.restTemplate = restTemplate;
		this.multiplicationHost = multiplicationHost;
	}

	@Override
	@HystrixCommand(fallbackMethod  = "defaultResult")
	public MultiplicationResultAttempt retrieveMultiplicationResultAttemptbyId(
			final Long multiplicationResultAttemptId) {
		return restTemplate.getForObject(multiplicationHost + "/results/" + multiplicationResultAttemptId,
				MultiplicationResultAttempt.class);
	}

	@SuppressWarnings("unused")
	private MultiplicationResultAttempt defaultResult(final Long multiplicationResultAttemptId) {
		return new MultiplicationResultAttempt("fakeAlias", 10, 10, 100, true);
	}
}

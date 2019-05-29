/**
 * 
 */
package microservices.book.gamification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.service.GameService;

/**
 * @author biya-bi
 *
 */
@RestController
@RequestMapping("/scores")
class ScoreController {
	private final GameService gameService;

	ScoreController(final GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/{attemptId}")
	ScoreCard getScoreForAttempt(@PathVariable("attemptId") final Long attemptId) {
		return gameService.getScoreForAttempt(attemptId);
	}
}

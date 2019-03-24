/**
 * 
 */
package microservices.book.gamification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.service.GameService;

/**
 * This class implements a REST API for the Gamification User Statistics
 * service.
 * 
 * @author biya-bi
 */
@RestController
@RequestMapping("/stats")
class UserStatsController {
	private final GameService gameService;

	@Autowired
	UserStatsController(final GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping
	GameStats getStatsForUser(@RequestParam("userId") final Long userId) {
		return gameService.retrieveStatsForUser(userId);
	}
}

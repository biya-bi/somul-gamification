/**
 * 
 */
package microservices.book.gamification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.service.LeaderBoardService;

/**
 * This class implements a REST API for the Gamification LeaderBoard service.
 * 
 * @author biya-bi
 */
@RestController
@RequestMapping("/leaders")
class LeaderBoardController {
	private final LeaderBoardService leaderBoardService;

	@Autowired
	LeaderBoardController(final LeaderBoardService leaderBoardService) {
		this.leaderBoardService = leaderBoardService;
	}

	@GetMapping
	List<LeaderBoardRow> getLeaderBoard() {
		return leaderBoardService.getCurrentLeaderBoard();
	}
}
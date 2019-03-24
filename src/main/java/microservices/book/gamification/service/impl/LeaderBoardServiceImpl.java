/**
 * 
 */
package microservices.book.gamification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.LeaderBoardService;

/**
 * @author biya-bi
 *
 */
@Service
@Slf4j
class LeaderBoardServiceImpl implements LeaderBoardService {
	private final ScoreCardRepository scoreCardRepository;

	@Autowired
	LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
		Assert.notNull(scoreCardRepository, "The scoreCardRepository must not be null.");
		this.scoreCardRepository = scoreCardRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see microservices.book.gamification.service.LeaderBoardService#
	 * getCurrentLeaderBoard()
	 */
	@Override
	public List<LeaderBoardRow> getCurrentLeaderBoard() {
		final List<LeaderBoardRow> leaderBoardRows = scoreCardRepository.findFirst10();
		log.info("Returning {} LeaderBoardRow object(s).", leaderBoardRows.size());
		return leaderBoardRows;
	}

}

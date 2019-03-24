/**
 * 
 */
package microservices.book.gamification.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;

/**
 * @author biya-bi
 *
 */
public class LeaderBoardServiceImplTest {

	private LeaderBoardServiceImpl leaderBoardServiceImpl;

	@Mock
	private ScoreCardRepository scoreCardRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		leaderBoardServiceImpl = new LeaderBoardServiceImpl(scoreCardRepository);
	}

	@Test
	public void getCurrentLeaderBoard_ThereAre20LeaderBoardRows_Return10LeaderBoardRowsWithHighestScores() {
		getCurrentLeaderBoard(20);
	}

	@Test
	public void getCurrentLeaderBoard_ThereAre4LeaderBoardRows_Return4LeaderBoardRowsWithHighestScores() {
		getCurrentLeaderBoard(4);
	}

	@Test
	public void getCurrentLeaderBoard_ThereAreNoLeaderBoardRows_ReturnNoLeaderBoardRows() {
		getCurrentLeaderBoard(0);
	}

	private void getCurrentLeaderBoard(final long leaderBoardCount) {
		final List<LeaderBoardRow> expectedLeaderBoardRows = leaderBoardCount <= 0 ? Collections.emptyList()
				: LongStream.range(1, leaderBoardCount + 1).mapToObj(x -> new LeaderBoardRow(x, x * 10))
						.sorted((x, y) -> y.getTotalScore().compareTo(x.getTotalScore())).limit(10)
						.collect(Collectors.toList());

		// Given
		BDDMockito.given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoardRows);

		// When
		final List<LeaderBoardRow> actualLeaderBoardRows = leaderBoardServiceImpl.getCurrentLeaderBoard();

		// Then
		Mockito.verify(scoreCardRepository).findFirst10();
		Assertions.assertThat(actualLeaderBoardRows).isEqualTo(expectedLeaderBoardRows);
	}
}

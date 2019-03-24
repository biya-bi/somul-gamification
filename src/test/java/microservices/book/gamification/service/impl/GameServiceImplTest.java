/**
 * 
 */
package microservices.book.gamification.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.Builder;
import lombok.Getter;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;

/**
 * @author biya-bi
 *
 */
public class GameServiceImplTest {

	private GameServiceImpl gameServiceImpl;

	@Mock
	private ScoreCardRepository scoreCardRepository;

	@Mock
	private BadgeCardRepository badgeCardRepository;

	@Mock
	private MultiplicationResultAttemptClient attemptClient;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		gameServiceImpl = new GameServiceImpl(scoreCardRepository, badgeCardRepository, attemptClient);
	}

	@Test
	public void newAttemptForUser_FirstAttemptIsCorrect_ReturnGameStatsWithFirstWonBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 10;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = 30;
		final int multiplicationFactorB = 40;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays
				.asList(new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_FirstAttemptIsNotCorrect_ReturnGameStatsWithNoBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 0;
		final Long attemptId = 1L;
		final int totalScore = 0;
		final boolean correct = false;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = 30;
		final int multiplicationFactorB = 40;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Collections.emptyList();

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(badgeTimestamp)
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_AttemptIsCorrectAndTotalScoreIsBetween100And499_ReturnGameStatsWithBronzeBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 110;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = 30;
		final int multiplicationFactorB = 40;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays.asList(
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.BRONZE_MULTIPLICATOR));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_AttemptIsCorrectAndTotalScoreIsBetween500And998_ReturnGameStatsWithSilverBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 510;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = 30;
		final int multiplicationFactorB = 40;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays.asList(
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.BRONZE_MULTIPLICATOR),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.SILVER_MULTIPLICATOR));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_AttemptIsCorrectAndTotalScoreIs999_ReturnGameStatsWithGoldBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 999;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = 30;
		final int multiplicationFactorB = 40;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays.asList(
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.BRONZE_MULTIPLICATOR),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.SILVER_MULTIPLICATOR),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.GOLD_MULTIPLICATOR));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_AttemptIsCorrectAndTotalScoreIsLessThan100AndMultiplicationFactorAIsEqualToLuckyNumber_ReturnGameStatsWithLuckyNumberBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 10;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = GameServiceImpl.LUCKY_NUMBER;
		final int multiplicationFactorB = 40;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays.asList(
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.LUCKY_NUMBER));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_AttemptIsCorrectAndTotalScoreIsLessThan100AndMultiplicationFactorBIsEqualToLuckyNumber_ReturnGameStatsWithLuckyNumberBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 10;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = 30;
		final int multiplicationFactorB = GameServiceImpl.LUCKY_NUMBER;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays.asList(
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.LUCKY_NUMBER));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void newAttemptForUser_AttemptIsCorrectAndTotalScoreIsLessThan100AndBothMultiplicationFactorsAreEqualToLuckyNumber_ReturnGameStatsWithLuckyNumberBadge() {
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int gameScore = 10;
		final Long attemptId = 1L;
		final int totalScore = 10;
		final boolean correct = true;
		final long badgeTimestamp = System.currentTimeMillis();
		final String userAlias = "john_doe";
		final int multiplicationFactorA = GameServiceImpl.LUCKY_NUMBER;
		final int multiplicationFactorB = GameServiceImpl.LUCKY_NUMBER;
		final List<BadgeCard> badgeCardsBeforeAttempt = Collections.emptyList();
		final List<BadgeCard> badgeCardsAfterAttempt = Arrays.asList(
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.FIRST_WON),
				new BadgeCard(badgeId, userId, badgeTimestamp, Badge.LUCKY_NUMBER));

		AttemptArg arg = new AttemptArg.AttemptArgBuilder().badgeId(badgeId).userId(userId).attemptId(attemptId)
				.gameScore(gameScore).totalScore(totalScore).correct(correct).badgeTimestamp(System.currentTimeMillis())
				.badgeCardsBeforeAttempt(badgeCardsBeforeAttempt).badgeCardsAfterAttempt(badgeCardsAfterAttempt)
				.userAlias(userAlias).multiplicationFactorA(multiplicationFactorA)
				.multiplicationFactorB(multiplicationFactorB).build();

		newAttemptForUser(arg);
	}

	@Test
	public void retrieveStatsForUser_UserHasMadeAttempts_ReturnGameStats() {
		// Given
		final Long badgeId = 1L;
		final Long userId = 1L;
		final int score = 10;
		final long badgeTimestamp = System.currentTimeMillis();
		final Badge badge = Badge.FIRST_WON;
		final List<BadgeCard> badgeCards = Arrays.asList(new BadgeCard(badgeId, userId, badgeTimestamp, badge));

		BDDMockito.given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(score);
		BDDMockito.given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(badgeCards);

		// When
		final GameStats gameStats = gameServiceImpl.retrieveStatsForUser(userId);

		// Then
		Assertions.assertThat(gameStats.getBadges())
				.isEqualTo(badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
		Assertions.assertThat(gameStats.getUserId()).isEqualTo(userId);
		Assertions.assertThat(gameStats.getScore()).isEqualTo(score);
	}

	private void newAttemptForUser(final AttemptArg arg) {
		// Given
		final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(arg.getUserAlias(),
				arg.getMultiplicationFactorA(), arg.getMultiplicationFactorB(), arg.getResultAttempt(),
				arg.isCorrect());
		final ScoreCard scoreCard = new ScoreCard(arg.getUserId(), arg.getAttemptId());

		BDDMockito.given(scoreCardRepository.save(scoreCard)).willReturn(scoreCard);
		BDDMockito.given(scoreCardRepository.getTotalScoreForUser(arg.getUserId())).willReturn(arg.getTotalScore());
		BDDMockito.given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(arg.getUserId()))
				.willReturn(Arrays.asList(scoreCard));
		BDDMockito.given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(arg.getUserId()))
				.willReturn(arg.getBadgeCardsBeforeAttempt());
		BDDMockito.given(attemptClient.retrieveMultiplicationResultAttemptbyId(arg.getAttemptId())).willReturn(attempt);

		// When
		final GameStats gameStats = gameServiceImpl.newAttemptForUser(arg.getUserId(), arg.getAttemptId(),
				arg.isCorrect());

		// Then
		Assertions.assertThat(gameStats.getBadges()).isEqualTo(
				arg.getBadgeCardsAfterAttempt().stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
		Assertions.assertThat(gameStats.getUserId()).isEqualTo(arg.getUserId());
		Assertions.assertThat(gameStats.getScore()).isEqualTo(arg.getGameScore());
	}

	@Builder
	@Getter
	private static class AttemptArg {
		private Long badgeId;
		private Long userId;
		private Long attemptId;
		private int gameScore;
		private int totalScore;
		private boolean correct;
		private long badgeTimestamp;
		private List<BadgeCard> badgeCardsBeforeAttempt;
		private List<BadgeCard> badgeCardsAfterAttempt;
		private String userAlias;
		private int multiplicationFactorA;
		private int multiplicationFactorB;

		public int getResultAttempt() {
			final int product = multiplicationFactorA * multiplicationFactorB;
			return correct ? product : -product;
		}
	}
}

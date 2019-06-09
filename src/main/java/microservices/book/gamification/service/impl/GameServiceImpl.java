/**
 * 
 */
package microservices.book.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.GameService;

/**
 * @author biya-bi
 *
 */
@Service
@Slf4j
class GameServiceImpl implements GameService {
	public static final int LUCKY_NUMBER = 42;

	private final ScoreCardRepository scoreCardRepository;
	private final BadgeCardRepository badgeCardRepository;
	private final MultiplicationResultAttemptClient attemptClient;

	@Autowired
	GameServiceImpl(ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository,
			MultiplicationResultAttemptClient attemptClient) {
		Assert.notNull(scoreCardRepository, "The scoreCardRepository must not be null.");
		Assert.notNull(badgeCardRepository, "The badgeCardRepository must not be null.");
		Assert.notNull(attemptClient, "The attemptClient must not be null.");
		this.scoreCardRepository = scoreCardRepository;
		this.badgeCardRepository = badgeCardRepository;
		this.attemptClient = attemptClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * microservices.book.gamification.service.GameService#newAttemptForUser(java.
	 * lang.Long, java.lang.Long, boolean)
	 */
	@Override
	public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
		// For the first version we'll give points only if it's correct
		if (correct) {
			ScoreCard scoreCard = new ScoreCard(userId, attemptId);
			scoreCardRepository.save(scoreCard);
			log.info("User with ID {} scored {} points for attempt with ID {}", userId, scoreCard.getScore(),
					attemptId);
			List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
			return new GameStats(userId, scoreCard.getScore(),
					badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
		}
		return GameStats.emptyStats(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * microservices.book.gamification.service.GameService#retrieveStatsForUser(java
	 * .lang.Long)
	 */
	@Override
	public GameStats retrieveStatsForUser(Long userId) {
		Integer score = scoreCardRepository.getTotalScoreForUser(userId);

		List<Badge> badges = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId).stream()
				.map(BadgeCard::getBadge).collect(Collectors.toList());

		return new GameStats(userId, score, badges);
	}

	/**
	 * Checks the total score and the different score cards obtained to give new
	 * badges in case their conditions are met.
	 */
	private List<BadgeCard> processForBadges(final Long userId, final Long attemptId) {
		List<BadgeCard> badgeCards = new ArrayList<>();
		Integer totalScore = scoreCardRepository.getTotalScoreForUser(userId);
		log.info("New score for user with ID {} is {}", userId, totalScore);
		List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
		List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
		// Badges depending on score
		checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
				.ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
				.ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
				.ifPresent(badgeCards::add);
		// First won badge
		if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
			BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
			badgeCards.add(0, firstWonBadge);
		}
		// Lucky number badge
		if (!containsBadge(badgeCardList, Badge.LUCKY_NUMBER)) {
			MultiplicationResultAttempt attempt = attemptClient.retrieveMultiplicationResultAttemptbyId(attemptId);
			if (LUCKY_NUMBER == attempt.getMultiplicationFactorA()
					|| LUCKY_NUMBER == attempt.getMultiplicationFactorB()) {
				BadgeCard luckyNumberBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
				badgeCards.add(luckyNumberBadge);
			}
		}
		return badgeCards;
	}

	/**
	 * Convenience method to check the current score against the different
	 * thresholds to gain badges. It also assigns badge to user if the conditions
	 * are met.
	 */
	private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards, final Badge badge,
			final int score, final int scoreThreshold, final Long userId) {
		if (score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
			return Optional.of(giveBadgeToUser(badge, userId));
		}
		return Optional.empty();
	}

	/**
	 * Checks if the passed list of badges includes the one being checked
	 */
	private boolean containsBadge(final List<BadgeCard> badgeCards, final Badge badge) {
		return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
	}

	/**
	 * Assigns a new badge to the given user
	 */
	private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
		BadgeCard badgeCard = new BadgeCard(userId, badge);
		badgeCardRepository.save(badgeCard);
		log.info("User with ID {} won a new badge: {}", userId, badge);
		return badgeCard;
	}

	@Override
	public ScoreCard getScoreForAttempt(Long attemptId) {
		return scoreCardRepository.findByAttemptId(attemptId);
	}

}

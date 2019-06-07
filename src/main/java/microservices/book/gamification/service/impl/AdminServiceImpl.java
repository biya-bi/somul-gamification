/**
 * 
 */
package microservices.book.gamification.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.AdminService;

/**
 * @author @author biya-bi
 */
@Profile("test")
@Service
class AdminServiceImpl implements AdminService {

	private BadgeCardRepository badgeCardRepository;
	private ScoreCardRepository scoreCardRepository;

	@Autowired
	AdminServiceImpl(final BadgeCardRepository badgeCardRepository, final ScoreCardRepository scoreCardRepository) {
		this.badgeCardRepository = badgeCardRepository;
		this.scoreCardRepository = scoreCardRepository;
	}

	@Override
	public void deleteDatabaseContents() {
		scoreCardRepository.deleteAll();
		badgeCardRepository.deleteAll();
	}
}
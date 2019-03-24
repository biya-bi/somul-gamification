/**
 * 
 */
package microservices.book.gamification.event;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Event that models the fact that a multiplication has been solved in the
 * system. Provides some context information about the multiplication.
 * 
 * @author biya-bi
 *
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MultiplicationSolvedEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5653629641114806956L;
	private final Long multiplicationResultAttemptId;
	private final Long userId;
	private final boolean correct;

	// Empty constructor for JSON/JPA
	public MultiplicationSolvedEvent() {
		this(0L, 0L, false);
	}
}

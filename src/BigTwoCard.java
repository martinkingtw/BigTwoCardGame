/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two
 * card game.
 * @author King Min Hao
 *
 */
public class BigTwoCard extends Card {
	/**
	 * Creates and returns an instance of the BigTwoCard class.
	 * 
	 * @param suit
	 *            an int value between 0 and 3 representing the suit of a card:
	 *            <p>
	 *            0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank
	 *            an int value between 0 and 12 representing the rank of a card:
	 *            <p>
	 *            0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '10', 10 = 'J', 11
	 *            = 'Q', 12 = 'K'
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	/**
	 * Compares this card with the specified card for order according to the rule of Big Two.
	 * 
	 * @param card
	 *            the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is
	 *         less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		if ((this.rank+11)%13 > (card.rank+11)%13) {
			return 1;
		} else if ((this.rank+11)%13 < (card.rank+11)%13) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}

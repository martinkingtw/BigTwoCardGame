/**
 * The StraightFlush class is a subclass of the Hand class, and are used to model a hand of straight flush in a
 * Big Two card game.
 * @author King Min Hao
 *
 */
public class StraightFlush extends Hand {

	/**
	 * A constructor for building a straight flush hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid straight flush.
	 * @return The boolean value of whether this is a valid straight flush.
	 */
	public boolean isValid() {
		this.sort();
		if(this.size() == 5) {
			if(this.getCard(4).getSuit() == this.getCard(3).getSuit() && 
			this.getCard(3).getSuit() == this.getCard(2).getSuit() &&
			this.getCard(2).getSuit() == this.getCard(1).getSuit() &&
			this.getCard(1).getSuit() == this.getCard(0).getSuit()) {
				if(this.getCard(4).getRank() == 1) {
					if(this.getCard(4).getRank() - this.getCard(3).getRank() == 1 && 
					this.getCard(3).getRank() - this.getCard(2).getRank() == -12 &&
					this.getCard(2).getRank() - this.getCard(1).getRank() == 1 &&
					this.getCard(1).getRank() - this.getCard(0).getRank() == 1) {
						return true;
					} else {
						return false;
					}
				}else if(this.getCard(4).getRank() == 0) {
					if(this.getCard(4).getRank() - this.getCard(3).getRank() == -12 && 
					this.getCard(3).getRank() - this.getCard(2).getRank() == 1 &&
					this.getCard(2).getRank() - this.getCard(1).getRank() == 1 &&
					this.getCard(1).getRank() - this.getCard(0).getRank() == 1) {
						return true;
					} else {
						return false;
					}
				}else {
					if(this.getCard(4).getRank() - this.getCard(3).getRank() == 1 && 
					this.getCard(3).getRank() - this.getCard(2).getRank() == 1 &&
					this.getCard(2).getRank() - this.getCard(1).getRank() == 1 &&
					this.getCard(1).getRank() - this.getCard(0).getRank() == 1) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * A method for returning a string specifying the type of this hand.
	 * @return A string specifying the type of this hand.
	 */
	public String getType() {
		if(this.isValid()) {
			return "StraightFlush";
		}else {
			return null;
		}
	}

}

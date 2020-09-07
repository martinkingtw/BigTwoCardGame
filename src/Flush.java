/**
 * The Flush class is a subclass of the Hand class, and are used to model a hand of flush in a
 * Big Two card game.
 * @author King Min Hao
 *
 */
public class Flush extends Hand {

	/**
	 * A constructor for building a flush hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid flush.
	 * @return The boolean value of whether this is a valid flush.
	 */
	public boolean isValid() {
		if(this.size() == 5) {
			if(this.getCard(4).getSuit() == this.getCard(3).getSuit() && 
			this.getCard(3).getSuit() == this.getCard(2).getSuit() &&
			this.getCard(2).getSuit() == this.getCard(1).getSuit() &&
			this.getCard(1).getSuit() == this.getCard(0).getSuit()) {
				return true;
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
			return "Flush";
		}else {
			return null;
		}
	}

}

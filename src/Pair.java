/**
 * The Pair class is a subclass of the Hand class, and are used to model a hand of pair in a
 * Big Two card game.
 * @author King Min Hao
 *
 */
public class Pair extends Hand {
	/**
	 * A constructor for building a pair hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid pair.
	 * @return The boolean value of whether this is a valid pair.
	 */
	public boolean isValid() {
		if(this.size() == 2) {
			if(this.getCard(0).getRank() == this.getCard(1).getRank()) {
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
			return "Pair";
		}else {
			return null;
		}
	}

}

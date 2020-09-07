/**
 * The Single class is a subclass of the Hand class, and are used to model a hand of single in a
 * Big Two card game.
 * @author King Min Hao
 *
 */
public class Single extends Hand {
	/**
	 * A constructor for building a single hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid single.
	 * @return The boolean value of whether this is a valid single.
	 */
	public boolean isValid() {
		if(this.size() == 1)
			return true;
		else
			return false;
	}

	/**
	 * A method for returning a string specifying the type of this hand.
	 * @return A string specifying the type of this hand.
	 */
	public String getType() {
		if(this.isValid()) {
			return "Single";
		}else {
			return null;
		}
	}

}

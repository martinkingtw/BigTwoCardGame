/**
 * The Triple class is a subclass of the Hand class, and are used to model a hand of triple in a
 * Big Two card game.
 * @author King Min Hao
 *
 */
public class Triple extends Hand {
	/**
	 * A constructor for building a triple hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid triple.
	 * @return The boolean value of whether this is a valid triple.
	 */
	public boolean isValid() {
		if(this.size() == 3) {
			if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()) {
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
			return "Triple";
		}else {
			return null;
		}
	}

}

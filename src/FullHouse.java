/**
 * The FullHouse class is a subclass of the Hand class, and are used to model a hand of full house in a
 * Big Two card game.
 * @author King Min Hao
 *
 */
public class FullHouse extends Hand {

	/**
	 * A constructor for building a full house hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid full house.
	 * @return The boolean value of whether this is a valid full house.
	 */
	public boolean isValid() {
		this.sort();
		if(this.size() == 5) {
			if(this.getCard(4).getRank() == this.getCard(3).getRank() && 
			this.getCard(2).getRank() == this.getCard(1).getRank() &&
			this.getCard(1).getRank() == this.getCard(0).getRank()) {
				return true;
			} else if(this.getCard(4).getRank() == this.getCard(3).getRank() && 
			this.getCard(3).getRank() == this.getCard(2).getRank() &&
			this.getCard(1).getRank() == this.getCard(0).getRank()) {
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
			return "FullHouse";
		}else {
			return null;
		}
	}

}

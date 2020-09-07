/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards.
 * @author King Min Hao
 *
 */
abstract class Hand extends CardList {
	private CardGamePlayer player;
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player The specified player who are playing the cards.
	 * @param cards The cards which the player are playing.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		this.removeAllCards();
		for(int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * A method for retrieving the player of this hand.
	 * @return The player of this hand.
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	/**
	 * A method for retrieving the top card of this hand.
	 * @return The top card of this hand.
	 */
	public Card getTopCard() {
		this.sort();
		if(this.size() == 5) {
			if(this.getType() == "Straight" || this.getType() == "Flush" || this.getType() == "StraightFlush") {
				return this.getCard(4);
			}else if(this.getType() == "FullHouse"){
				if(this.getCard(1).getRank() != this.getCard(2).getRank()) {
					return this.getCard(4);
				}else {
					return this.getCard(2);
				}
			}else {
				if(this.getCard(0).getRank() != this.getCard(1).getRank()) {
					return this.getCard(4);
				}else {
					return this.getCard(3);
				}
			}
		} else {
			return this.getCard(this.size()-1);
		}
	}
	/**
	 * A method for checking if this hand beats a specified hand.
	 * @param hand The hand which the current hand is playing against.
	 * @return The boolean value of whether this hand beats the specified hand.
	 */
	public boolean beats(Hand hand) {
		if(this.size() != hand.size()) {
			return false;
		} else {
			int thisPriority = -1, handPriority = -1;
			if(this.getType() == "Straight")
				thisPriority = 0;
			if(hand.getType() == "Straight")
				handPriority = 0;
			if(this.getType() == "Flush")
				thisPriority = 1;
			if(hand.getType() == "Flush")
				handPriority = 1;
			if(this.getType() == "FullHouse")
				thisPriority = 2;
			if(hand.getType() == "FullHouse")
				handPriority = 2;
			if(this.getType() == "Quad")
				thisPriority = 3;
			if(hand.getType() == "Quad")
				handPriority = 3;
			if(this.getType() == "StraightFlush")
				thisPriority = 4;
			if(hand.getType() == "StraightFlush")
				handPriority = 4;
			if(thisPriority > handPriority) {
				return true;
			} else if(thisPriority == 1 && handPriority == 1) {
				if(this.getTopCard().getSuit() > hand.getTopCard().getSuit()) {
					return true;
				}else if(this.getTopCard().getSuit() == hand.getTopCard().getSuit()) {
					if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			} else if(thisPriority == handPriority) {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1)
					return true;
				else
					return false;
			} else {
				return false;
			}
		}
	}
	/**
	 * A method for checking if this is a valid hand.
	 * @return The boolean value of whether this is a valid hand.
	 */
	abstract boolean isValid();
	/**
	 * A method for returning a string specifying the type of this hand.
	 * @return A string specifying the type of this hand.
	 */
	abstract String getType();
}

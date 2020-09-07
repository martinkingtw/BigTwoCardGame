import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface.
 * It is used to model a Big Two card game that supports 4 players playing over the Internet. 
 * @author King Min Hao
 */
public class BigTwoClient implements CardGame, NetworkGame{
	//An integer specifying the number of players.
	private int numOfPlayers;
	//A deck of cards.
	private Deck deck;
	//A list of players.
	private ArrayList<CardGamePlayer> playerList;
	//A list of hands played on the table.
	private ArrayList<Hand> handsOnTable;
	//An integer specifying the playerID of the local player.
	private int playerID;
	//A String specifying the name of the local player.
	private String playerName;
	//A String specifying the IP address of the game server.
	private String serverIP;
	//An int specifying the TCP port of the game server.
	private int serverPort;
	//A socket connection to the game server.
	private Socket sock;
	//An OOPS for sending messages to the server.
	private ObjectOutputStream oos;
	//A int specifying the index of the player for the current turn.
	private int currentIdx;
	//A table building the GUI for the game and handles all user actions.
	private BigTwoTable table;
	//A boolean value checking whether it is the first hand.
	private boolean firstHand = true;
	//A boolean value checking whether it is the start of the game.
	private boolean startGame = true;
	//A integer checking the number of pass.
	private int passTime = 0;
	//A boolean value checking whether it is a legal move.
	private boolean notALegalMove;
	//An ObjectInputStream object
	private ObjectInputStream ois;
	
	/**
	 * A constructor for creating a Big Two client.
	 *  You should (i)create 4 players and add them to the list of players;
	 *  (ii)create a Big Two table which builds the GUI for the game and handles user actions; and
	 *  (iii)make a connection to the game server by calling the makeConnection() method from the NetworkGame interface.
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		serverIP = "127.0.0.1";
		serverPort = 2396;
		for(int i = 0; i < 4; i++) {
			CardGamePlayer player = new CardGamePlayer();
			playerList.add(player);
		}
		table = new BigTwoTable(this);
		table.disable();
		
	}

	@Override
	synchronized public int getPlayerID() {
		// TODO Auto-generated method stub
		return this.playerID;
	}

	@Override
	synchronized public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID = playerID;
	}

	@Override
	synchronized public String getPlayerName() {
		// TODO Auto-generated method stub
		return this.playerName;
	}

	@Override
	synchronized public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName = playerName;
	}

	@Override
	synchronized public String getServerIP() {
		// TODO Auto-generated method stub
		return this.serverIP;
	}

	@Override
	synchronized public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP = serverIP;
	}

	@Override
	synchronized public int getServerPort() {
		// TODO Auto-generated method stub
		return this.serverPort;
	}

	@Override
	synchronized public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort = serverPort;
	}

	@Override
	synchronized public void makeConnection() {
		// TODO Auto-generated method stub
		try {
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			Thread thread = new Thread(new ServerHandler());
			thread.start();
			GameMessage msg = new CardGameMessage(1, -1, getPlayerName());
			sendMessage(msg);
			msg = new CardGameMessage(4, -1, null);
			sendMessage(msg);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	synchronized public void parseMessage(GameMessage message) {
		// TODO Auto-generated method stub
		if(message.getType() == 0) {
			//playerList
			setPlayerID(message.getPlayerID());
			for(int i = 0; i < 4; i++) {
				if(((String[])message.getData())[i] != null) {
					playerList.get(i).setName(((String[])message.getData())[i]);
					numOfPlayers++;
				}
			}
		}else if(message.getType() == 1) {
			//join
			playerList.set(message.getPlayerID(), new CardGamePlayer((String)message.getData()));
		}else if(message.getType() == 2) {
			//full
			table.printMsg("Sorry! We're full right now. Please come back later.");
		}else if(message.getType() == 3) {
			//quit
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " leaves the game.");
			table.printMsg("The game ends now.");
			playerList.get(message.getPlayerID()).setName(null);
			table.repaint();
			sendMessage(new CardGameMessage(4, -1, null));
			table.disable();
		}else if(message.getType() == 4) {
			//ready
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready.");
		}else if(message.getType() == 5) {
			//start
			deck = (Deck)message.getData();
			table.putOnNames();
			this.start(deck);
			table.repaint();
		}else if(message.getType() == 6) {
			//move
			checkMove(message.getPlayerID(), (int[])message.getData());
		}else if(message.getType() == 7) {
			//msg
			table.printChat((String)message.getData());
		}
	}

	@Override
	synchronized public void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
		try {
			oos.writeObject(message);
			oos.flush();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	synchronized public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return this.numOfPlayers;
	}

	@Override
	synchronized public Deck getDeck() {
		// TODO Auto-generated method stub
		return this.deck;
	}

	@Override
	synchronized public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return this.playerList;
	}

	@Override
	synchronized public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return this.handsOnTable;
	}

	@Override
	synchronized public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return this.currentIdx;
	}

	@Override
	synchronized public void start(Deck deck) {
		// TODO Auto-generated method stub
		table.reset();
		this.deck = deck;
		this.handsOnTable.clear();
		for(int i = 0; i < 4; i++) {
			playerList.get(i).removeAllCards();
		}
		for(int i = 0;i < 13; i++) {
			for(int j = 0; j < 4; j++) {
				playerList.get(j).addCard(deck.getCard(i*4+j));
			}
		}
		for(int i = 0; i < 4; i++) {
			if(playerList.get(i).getCardsInHand().contains(new BigTwoCard(0, 2))) {
				currentIdx = i;
				break;
			}
		}
		for(int i = 0; i < 4; i++) {
			playerList.get(i).sortCardsInHand();
		}
		this.firstHand = true;
		this.startGame = true;
		this.passTime = 0;
		table.resetSelected();
		table.setActivePlayer(currentIdx);
		table.printMsg(playerList.get(currentIdx).getName() + "'s turn:");
	}

	@Override
	synchronized public void makeMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		sendMessage(new CardGameMessage(6, -1, cardIdx));
	}

	@Override
	synchronized public void checkMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		if(firstHand == true) {
			CardList gettingHand = playerList.get(playerID).play(cardIdx);
			if(gettingHand != null && startGame == true && !(gettingHand.contains(new Card(0, 2)))) {
				table.printMsg("Not a legal move!!!");
				notALegalMove = true;
			}else {
				if(gettingHand == null) {
					table.printMsg("Not a legal move!!!");
					notALegalMove = true;
				}else {
					Hand playingHand = composeHand(playerList.get(playerID), gettingHand);
					if(playingHand == null) {
							table.printMsg("Not a legal move!!!");
							notALegalMove = true;
					}else {
						handsOnTable.add(playingHand);
						playerList.get(playerID).removeCards(playingHand);
						String string = "";
						for(int i = 0; i < playingHand.size(); i++) {
							string = string + "[" + playingHand.getCard(i) + "]";
						}
						table.printMsg("{" + playingHand.getType() + "} " + string);
						firstHand = false;
						startGame = false;
						notALegalMove = false;
					}
				}
			}
		}else {
			CardList gettingHand = playerList.get(playerID).play(cardIdx);
			if(gettingHand == null) {
				table.printMsg("{pass}");
				passTime++;
				if(passTime == 3) {
					firstHand = true;
					passTime = 0;
				}
				notALegalMove = false;
			}else {
				Hand playingHand = composeHand(playerList.get(playerID), gettingHand);
				if(playingHand == null || !(playingHand.beats(handsOnTable.get(handsOnTable.size() - 1)))) {
					table.printMsg("Not a legal move!!!");
					notALegalMove = true;
				}else {
					handsOnTable.add(playingHand);
					playerList.get(playerID).removeCards(playingHand);
					String string = "";
					for(int i = 0; i < playingHand.size(); i++) {
						string = string + "[" + playingHand.getCard(i) + "]";
					}
					table.printMsg("{" + playingHand.getType() + "} " + string);
					passTime = 0;
					notALegalMove = false;
					}
				}
		}
		if(!endOfGame()) {
			if(notALegalMove) {
				
			}else {
				currentIdx = (playerID + 1) % 4;
				table.printMsg("");
				table.printMsg(playerList.get(currentIdx).getName() + "'s turn:");
				table.setChanged(true);
			}
			table.setActivePlayer(currentIdx);
			table.resetSelected();
			table.repaint();
		}else {
			table.printMsg("");
			table.printMsg("Game ends");
			String endMsg = new String("Game ends");
			endMsg += "\n";
			for(int i = 0; i < 4; i++) {
				if(playerList.get(i).getNumOfCards() == 0) {
					endMsg += ("Player " + i + " wins the game.");
					endMsg += "\n";
				}else {
					endMsg += ("Player " + i + " has " + playerList.get(i).getNumOfCards() + " cards in hand.");
					endMsg += "\n";
				}
			}
			JOptionPane.showMessageDialog(null, endMsg, "Results", JOptionPane.OK_OPTION);
			sendMessage(new CardGameMessage(4,-1,null));
		}
	}

	@Override
	synchronized public boolean endOfGame() {
		// TODO Auto-generated method stub
		if(playerList.get(currentIdx).getNumOfCards() == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * An inner class that implements the Runnable interface.
	 * Its job includes (1)makeConnection() method for receiving messages from the game server.
	 * (2)parseMessage() method should be called to parse the message accordingly.
	 * @author King Min Hao
	 *
	 */
	class ServerHandler implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			GameMessage msg;
			try {
				while((msg = (GameMessage) ois.readObject()) != null) {
					parseMessage(msg);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	/**
	 * A method for creating an instance of BigTwoClient.
	 * @param args The argument being passed on.
	 */
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
		String name = JOptionPane.showInputDialog("Please enter your name");
		client.setPlayerName(name);
		client.makeConnection();

	}
	/**
	 * A method for returning a valid hand from the specified list of cards of the player.
	 * Returns null if no valid hand can be composed from the specified list of cards.
	 * @param player The player called the method.
	 * @param cards The cards being passed in to be composed.
	 * @return either a hand if it could be composed, or null if it can't.
	 */
	synchronized public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand beExamined = new Single(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new Pair(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new Triple(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new StraightFlush(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new Straight(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new Flush(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new FullHouse(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		beExamined = new Quad(player, cards);
		if(beExamined.isValid()) {
			return beExamined;
		}
		return null;
	}
	/**
	 * A method to disconnect from the server.
	 */
	synchronized public void disconnect(){
		if(this.sock != null){
			try {
				this.sock.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

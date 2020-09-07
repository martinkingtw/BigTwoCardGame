import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build the GUI for the BigTwo card game and handle all user
 * actions.
 * @author King Min Hao
 *
 */
public class BigTwoTable implements CardGameTable {
	//A chat text area
	private JTextArea chatTextArea;
	//A chat input area
	private JTextField chatInputArea;
	//A card game associates with this table.
	private BigTwoClient game;
	//A boolean array indicating which cards are being selected.
	private boolean[] selected;
	//An integer specifying the index of the active player.
	private int activePlayer;
	//The main window of the application
	private JFrame frame;
	//A panel for showing the cards of each player and the cards played on the table.
	private JPanel bigTwoPanel;
	//A ¡§Play¡¨ button for the active player to play the selected cards.
	private JButton playButton;
	//A ¡§Pass¡¨ button for the active player to pass his/her turn to the next player.
	private JButton passButton;
	//A text area for showing the current game status as well as end of game messages.
	private JTextArea msgArea;
	//A 2D array storing the images for the faces of the cards.
	private Image[][] cardImages;
	//An image for the backs of the cards.
	private Image cardBackImage;
	//An array storing the images for the avatars.
	private Image[] avatars;
	//The Maximum number of cards a player can hold
	private final static int MAX_CARD_NUM = 13;
	//The players' cards x coordinates.
	private int[] cardX;
	//The players' cards y coordinates.
	private int[] cardY;
	//A JLabel of the player who played last hand.
	private JLabel lastHandLabel;
	//A boolean value checking whether lastHandNum have changed.
	private boolean changed = true;
	//A JMenu item to set up the game.
	private JMenu menu;
	
	/**
	 * A constructor for creating a BigTwoTable.
	 * @param game A reference to a CardGame associates with this table.
	 */
	public BigTwoTable(BigTwoClient game) {
		this.game = game;
		this.selected = new boolean[MAX_CARD_NUM];
		this.initialize();
		this.cardImages = new Image[4][13];
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				this.cardImages[i][j] = new ImageIcon("images/" + (i+j*4) + ".jpg").getImage().getScaledInstance(80, 120, Image.SCALE_DEFAULT);
			}
		}
		this.cardBackImage = new ImageIcon("images/backOfCards.jpg").getImage();
		cardBackImage = cardBackImage.getScaledInstance(80, 120, Image.SCALE_DEFAULT);
		
		this.avatars = new Image[4];
		for(int i = 0; i < 4; i++) {
			this.avatars[i] = new ImageIcon("images/avatar" + i + ".jpg").getImage().getScaledInstance(100, 150, Image.SCALE_DEFAULT);
		}
		this.frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.bigTwoPanel = new BigTwoPanel();
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		
		this.playButton = new JButton("Play");
		this.passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		playButton.setPreferredSize(new Dimension(80, 40));
		passButton.setPreferredSize(new Dimension(80, 40));
		JPanel playPassPanel = new JPanel();
		playPassPanel.add(playButton);
		playPassPanel.add(passButton);
		frame.add(playPassPanel, BorderLayout.SOUTH);
		
		JPanel areaPanel = new JPanel();
		areaPanel.setLayout(new BoxLayout(areaPanel, BoxLayout.Y_AXIS));
		this.msgArea = new JTextArea(26, 80);
		JScrollPane scroller = new JScrollPane(msgArea);
		msgArea.setEditable(false);
		msgArea.setLineWrap(true);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.chatTextArea = new JTextArea(26, 80);
		this.chatInputArea = new JTextField();
		JLabel chatLabel = new JLabel("Message Service");
		chatLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		chatInputArea.addActionListener(new ChatInputListener());
		JScrollPane chatscroll = new JScrollPane(chatTextArea);
		chatTextArea.setEditable(false);
		chatTextArea.setLineWrap(true);
		chatscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		areaPanel.add(scroller);
		areaPanel.add(chatscroll);
		areaPanel.add(chatLabel);
		areaPanel.add(chatInputArea);
		frame.add(areaPanel, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		this.menu = new JMenu("Game");
		JMenuItem jMenuQuit = new JMenuItem("Quit");
		JMenuItem jMenuConnect = new JMenuItem("Connect");
		jMenuQuit.addActionListener(new QuitMenuItemListener());
		jMenuConnect.addActionListener(new ConnectMenuItemListener());
		menu.add(jMenuConnect);
		menu.add(jMenuQuit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		this.lastHandLabel = new JLabel("");
		lastHandLabel.setBounds(10, 800, 300, 50);
		bigTwoPanel.add(lastHandLabel);
				
		frame.setSize(2000, 1200);
		frame.setVisible(true);
		
	}
	
	/**
	 * A method for setting the index of the active player.
	 * @param activePlayer The current player.
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}
	/**
	 * A method for getting an array of indices of the cards selected.
	 * @return the cards that are bring selected.
	 */
	public int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}
		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		resetSelected();
		return cardIdx;
	}
	/**
	 * A method for resetting the list of selected cards. 
	 */
	public void resetSelected() {
		selected = new boolean[MAX_CARD_NUM];
	}
	/**
	 * A method for repainting the GUI.
	 */
	public void repaint() {
		frame.repaint();
		if(activePlayer == game.getPlayerID()){
			enable();
		}else{
			disable();
		}
		if(changed) {
			bigTwoPanel.remove(lastHandLabel);
			lastHandLabel = new JLabel("Played by " + game.getPlayerList().get((activePlayer+3)%4).getName());
			lastHandLabel.setBounds(10, 800, 300, 50);
			bigTwoPanel.add(lastHandLabel);
			changed = false;
		}
	}
	/**
	 * A method for printing the specified string to the message area of the GUI.
	 * @param msg The message that are being printed out.
	 */
	public void printMsg(String msg) {
		msgArea.append(msg+"\n");
	}
	/**
	 * A method for clearing the message area of the GUI.
	 */
	public void clearMsgArea() {
		msgArea.setText(null);
	}
	/**
	 * A method for resetting the GUI.
	 * (i) reset the list of selected cards using resetSelected() method from the CardGameTable interface; 
	 * (ii) clear the message  area  using  the clearMsgArea() method  from  the  CardGameTable  interface; and
	 * (iii) enable user interactions using the enable() method from the CardGameTable interface.
	 */
	public void reset() {
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
		this.repaint();
	}
	/**
	 * A method for enabling user interactions with the GUI.
	 * (i) enable  the  ¡§Play¡¨  button  and  ¡§Pass¡¨  button  (i.e.,  making  them  clickable); and
	 * (ii) enable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	/**
	 * A method for disabling user interactions with the GUI.
	 * (i) disable the ¡§Play¡¨ button and ¡§Pass¡¨ button (i.e., making them not clickable); and  
	 * (ii) disable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	/**
	 * A method to initialize the cards x-y coordinates.
	 */
	public void initialize() {
		cardX = new int[MAX_CARD_NUM];
		cardY = new int[4];
		for(int i = 0; i < MAX_CARD_NUM; i++) {
			cardX[i] = 150 + 40 * i;
		}
		for(int i = 0; i < 4; i++) {
			cardY[i] = 50 + 200 * i;
		}
	}
	/**
	 * A method to put on names.
	 */
	public void putOnNames() {
		for(int i = 0; i < 4; i++) {
			JLabel label = new JLabel(game.getPlayerList().get(i).getName());
			label.setBounds(10, 200*i, 50, 50);
			bigTwoPanel.add(label);
		}
	}
	/**
	 * A method to print chat.
	 */
	public void printChat(String s) {
		chatTextArea.append(s+"\n");
	}
	/**
	 * A method to set the boolean changed.
	 */
	public void setChanged(boolean b) {
		this.changed = b;
	}
	
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener interface.
	 * Overrides the paintComponent() method inherited from the JPanel class to draw the card game table.
	 * @author King Min Hao
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		public BigTwoPanel() {
			this.setLayout(null);
			this.setSize(1077, 1054);
			this.setBackground(Color.WHITE);
			this.addMouseListener(this);
			for(int i = 0; i < 4; i++) {
				ImageIcon icon = new ImageIcon(avatars[i]);
				JLabel label = new JLabel();
				label.setIcon(icon);
				label.setBounds(10, 50+200*i, 100, 150);
				this.add(label);
			}
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for(int i = 0; i < 4; i++) {
				CardGamePlayer player = game.getPlayerList().get(i);
				for(int j = 0; j < player.getNumOfCards(); j++) {
					if(i == game.getPlayerID()) {
						if(selected[j]) {
							Card card = player.getCardsInHand().getCard(j);
							g.drawImage(cardImages[card.getSuit()][card.getRank()], cardX[j], cardY[i]-25, this);
						}else {
							Card card = player.getCardsInHand().getCard(j);
							g.drawImage(cardImages[card.getSuit()][card.getRank()], cardX[j], cardY[i], this);
						}
					}else {
						g.drawImage(cardBackImage, cardX[j], cardY[i], this);
					}
				}
				g.drawLine(0, 5+200*(i+1), 1077, 5+200*(i+1));
			}
			ArrayList<Hand> handsOnTable = game.getHandsOnTable();
			if(game.getHandsOnTable().isEmpty()) {
				
			}else {
				Hand lastHand = handsOnTable.get(handsOnTable.size()-1);
				for(int i = 0; i < lastHand.size(); i++) {
					Card card = lastHand.getCard(i);
					g.drawImage(cardImages[card.getSuit()][card.getRank()], cardX[i], 850, this);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int mouseX = e.getX();
			int mouseXN = mouseX;
			int mouseY = e.getY();
			//System.out.println(mouseY);
			mouseX -= 150;
			mouseXN -= 150;
			mouseX /= 40;
			mouseXN /= 40;
			if(mouseX >= 0 && mouseX <= game.getPlayerList().get(activePlayer).getNumOfCards()) {
				if(mouseX == game.getPlayerList().get(activePlayer).getNumOfCards()) {
					mouseX -= 1;
				}
				if(selected[mouseX]) {
					if(mouseY >= cardY[activePlayer]-25 && mouseY <= cardY[activePlayer]+95) {
						selected[mouseX] = false;
					}
					if(mouseX > 0 && mouseXN != game.getPlayerList().get(activePlayer).getNumOfCards()) {
						if(!selected[mouseX-1]) {
							if(mouseY > cardY[activePlayer]+95 && mouseY <= cardY[activePlayer]+120) {
								selected[mouseX-1] = true;
							}
						}
					}
				}else {
					if(mouseY >= cardY[activePlayer] && mouseY <= cardY[activePlayer]+120) {
						selected[mouseX] = true;
					}
				}
			}
			
			
			this.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the ¡§Play¡¨ button. 
	 * When the ¡§Play¡¨ button is clicked, you should call the makeMove() method of your CardGame object to make a move.
	 * @author King Min Hao
	 **/
	class PlayButtonListener implements ActionListener {
		/**
		 * Handle button clicking event for the "Play" button.
		 * @param event The event that the player performed.
		 */
		public void actionPerformed(ActionEvent event) {
			int[] passOn = getSelected();
			game.makeMove(activePlayer, passOn);
		}
	}
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the ¡§Pass¡¨ button.
	 * When the ¡§Pass¡¨ button is clicked, you should call the makeMove() method of your CardGame object to make a move.
	 * @author King Min Hao
	 *
	 */
	class PassButtonListener implements ActionListener {
		/**
		 * Handle button-click events for the ¡§Pass¡¨ button.
		 * @param event The action that the player performed.
		 */
		public void actionPerformed(ActionEvent event) {
			game.makeMove(activePlayer, null);
		}
	}
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for ¡§Quit¡¨ item.
	 * When the ¡§Quit¡¨ menu item is selected, you should terminate your application. ( System.exit() )
	 * @author King Min Hao
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * Handle menu-item-click events for ¡§Quit¡¨ item.
		 * @param event The action that the player performed.
		 */
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for ¡§Connect¡¨ item.
	 * When the ¡§Connect¡¨ menu item is selected, you should connect your application to the server.
	 * @author King Min Hao
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {
		/**
		 * Handle menu-item-click events for ¡§Quit¡¨ item.
		 * @param event The action that the player performed.
		 */
		public void actionPerformed(ActionEvent event) {
			game.disconnect();
			game.makeConnection();
		}
	}
	/**
	 * @author King Min Hao
	 * Check if the chat box typing area has received the "Enter" key
	 */
	public class ChatInputListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			/*
			 * send message to chatDisplayArea here
			*/
			String chatOutput = chatInputArea.getText();
			game.sendMessage(new CardGameMessage(7,-1,chatOutput));
			chatInputArea.setText(null);
		}
		
	}
}

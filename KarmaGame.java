import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class KarmaGame
{
    public List<Player> players;
    public Deck drawPile;
    public Deck discardPile;

    int stalemateCounter = 0;
    int lastDiscardSize = -1;
    
    public KarmaGame()
    {
        this.players = new ArrayList<>();
        this.drawPile = new Deck(); 
        this.discardPile = new Deck();

        BufferedReader r = new BufferedReader(
            new InputStreamReader(System.in));

        try
        {
            // SETUP
            drawPile.initPopulate(2);
            drawPile.shuffle(104);

            addPlayers(r);
            dealFaceDown(players, drawPile);
            dealStartSix(players, drawPile);

            // GAMEPLAY
            // 1: Playing until drawPile == 0
  
            int activePlayers = players.size();


            System.out.println("Do you wish to autoplay (y) or play (n)?");
            String input = r.readLine();
            boolean autoPlay = false;

            if (input.toLowerCase().equals("y"))
                autoPlay = true;

            while (activePlayers > 1)
            {
                for (Player player : players)
                {
                    // Skip players who are finished
                    if (player.hand.size() == 0 &&
                        player.faceUp.size() == 0 &&
                        player.faceDown.size() == 0)
                    {
                        continue;
                    }

                    // Determine hand type
                    HandType handType;
                    if (player.hand.size() > 0)
                        handType = HandType.HAND;
                    else if (player.faceUp.size() > 0)
                        handType = HandType.FACE_UP;
                    else
                        handType = HandType.FACE_DOWN;

                    playCard(player, discardPile, drawPile, handType, autoPlay);

                    // Stalemate detection
                    if (discardPile.getSize() == lastDiscardSize) {
                        stalemateCounter++;
                        if (stalemateCounter > 20) {
                            System.out.println("STALEMATE DETECTED");
                            activePlayers = 1;
                            break;
                        }
                    } else {
                        stalemateCounter = 0;
                    }
                    lastDiscardSize = discardPile.getSize();

                    if (player.hand.size() == 0 &&
                        player.faceUp.size() == 0 &&
                        player.faceDown.size() == 0)
                    {
                        activePlayers--;
                        System.out.println(player.getName() + " has finished!");
                    }
                }
            }

            for (Player player : players)
            {
                if (player.faceDown.size() != 0)
                {
                    System.out.println(player.getName() + " LOST !\n GAME END");
                }
            }
        } catch (IOException e)
        {
            System.err.println(e);
        }
    }

    private void playCard(Player player, Deck discardPile, Deck drawPile, HandType handType, boolean autoPlay) throws IOException
    {
        List<Card> playerCards = player.getCards(handType); 
        List<Card> legalCards = new ArrayList<>(getLegalCards(playerCards, discardPile));
        
        // if no legal cards, player picks up discardPile
        if (legalCards.size() == 0)
        {
            List<Card> cardsToPickUp = discardPile.pickUpPile();
            player.hand.addAll(cardsToPickUp);    
            //System.out.println(player.getName() + " cannot play, picking up: " + cardsToPickUp.size() + " cards\n");
        } 
        else
        {
            // player picks a card
            System.out.println("Player: " + player.getName() + ". Legal Cards from (" + handType.name() + "):");
            int cardIndex = player.pickCard(legalCards, autoPlay);
            Card picked = legalCards.get(cardIndex);
           
            playerCards.remove(picked);
            // Add picked card to discard
            discardPile.addCard(picked);

            if (picked.getRank() == Rank.TEN)
            {
                //System.out.println("discard pile size was: " + discardPile.getSize());
                discardPile.clear();
                //System.out.println("discard pile size is: " + discardPile.getSize());
            }

            // Only draw cards to replenish hand if they played from their hand
            if (handType == HandType.HAND)
            {
                int available = drawPile.getSize();
                // Use player.hand.size() directly to get current size after removal
                int toDraw = Math.min(3 - player.hand.size(), available);

                if (toDraw > 0) {
                   player.hand.addAll(drawPile.drawCardsFromPile(toDraw));
                }
            }
        }
    }

    private Card getMostRecentValidCard(Deck discardPile)
    {
        List<Card> discardCards = discardPile.getCards();

        for (int i = discardCards.size() - 1; i >= 0; i--)
        {
            if (discardCards.get(i).getRank() != Rank.EIGHT)
                return discardCards.get(i);
        }
        
        Card defaultCard = new Card(Rank.TWO, Suit.CLUB);
        return defaultCard;
    }

    private List<Card> getLegalCards(List<Card> playerCards, Deck discardPile)
    {
        if (discardPile.isEmpty())
            return playerCards;
        
        Card discardTop = discardPile.getTopCard();
        if (discardTop.getRank() == Rank.EIGHT)
        {
            discardTop = getMostRecentValidCard(discardPile);
        }

        System.out.println("DISCARD PILE TOP CARD = " + discardTop + "\n");

        List<Card> legalCards = new ArrayList<Card>();

        for (Card card : playerCards)
        {
            if (isLegalPlay(card, discardTop))
            {
                legalCards.add(card);
            }
        }

        return legalCards;
    }

    private boolean isLegalPlay(Card toCheck, Card discardTop)
    {
        Rank toCheckRank = toCheck.getRank();
        Rank discardTopRank = discardTop.getRank();

        if (discardTopRank == Rank.SEVEN)
        {
            return toCheckRank.getValue() <= discardTopRank.getValue();
        }

        if (toCheckRank == Rank.TWO ||
            toCheckRank == Rank.EIGHT ||
            toCheckRank == Rank.TEN)
        {
            return true;
        }

        if (toCheckRank.getValue() >= discardTopRank.getValue())
        {
            return true;
        }
        return false;
    }

    private void dealFaceDown(List<Player> players, Deck deck) {
        for (Player player : players) {
            List<Card> cardsToDeal = deck.drawCardsFromPile(3);
            player.dealCards(player.faceDown, cardsToDeal);
        }
    }

    private void dealStartSix(List<Player> players, Deck deck) {
        for (Player player : players) {
            List<Card> faceUpCards = deck.drawCardsFromPile(3);
            List<Card> handCards = deck.drawCardsFromPile(3);
            player.dealCards(player.faceUp, faceUpCards);
            player.dealCards(player.hand, handCards);
        }
    }
    
    private void addPlayers(BufferedReader r) throws IOException
    {
        int playerCount = 0;

        boolean exit = false;
        System.out.println("Karma requires 2-6 players");

        while (!exit)
        {
            int playerId = playerCount + 1;

            System.out.print(playerId + ": Player name = ");
            String input = r.readLine();
            playerCount += 1;

            Player newPlayer = new Player(playerId, input);
            players.add(newPlayer);

            if (playerCount == 6)
            {
                System.out.println("Max players in game");
                exit = true; 
            }
            else if (playerCount >= 2)
            {
                System.out.print("Stop adding players (q), or add another player (y): ");
                input = r.readLine();
                
                if (!input.toLowerCase().equals("y"))
                    exit = true;
            }
        }

    }
    
}


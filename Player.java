import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

enum HandType {
    HAND,
    FACE_UP,    
    FACE_DOWN,
};

public class Player
{
    public List<Card> hand;
    public List<Card> faceUp;
    public List<Card> faceDown;

    private int id;
    private String name;

    public Player(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.hand = new ArrayList<Card>();
        this.faceUp = new ArrayList<Card>();
        this.faceDown = new ArrayList<Card>();
    }

    public List<Card> getCards(HandType handType)
    {
        switch(handType)
        {   
            case FACE_UP:
                return faceUp;
            case FACE_DOWN:
                return faceDown;
            default:
                return hand;
        }
    }

    public void removeCard(int index, List<Card> cardsToRemove)
    {
        cardsToRemove.remove(index);
    }

    public int pickCard(List<Card> cardsToTake, boolean autoPlay) throws IOException
    {
        BufferedReader r = new BufferedReader(
            new InputStreamReader(System.in));
        displayHand(cardsToTake);

        int index = 0;
        if (autoPlay)
        {
            index = 1;
        } else {
            System.out.println("Enter the index number of the card, you wish to play");
            index = Integer.parseInt(r.readLine());
        }
        System.out.println("");

        return (index - 1);
    }

    public void dealCard(List<Card> cardsToAdd, Card drawCard)
    {
        cardsToAdd.add(drawCard);
    }    

    public void dealCards(List<Card> cardsToAdd, List<Card> drawCards)
    {
        cardsToAdd.addAll(drawCards);
    }

    public void displayHand(List<Card> handToDisplay)
    {
        String displayString = "";
        int pos = 1;
        for (Card card : handToDisplay)
        {
            displayString += "\t" + pos + ": " + card.toString() + "\n";
            pos += 1;
        }
        System.out.println(displayString);
    }

    public String getName()
    {
        return name;
    }
}

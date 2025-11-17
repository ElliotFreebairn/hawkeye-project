import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;
import java.util.Random;

public class Deck
{
    private List<Card> cards;

    public Deck()
    {
        cards = new ArrayList<Card>();
    }

    public void clear()
    {
        cards.clear();
    }

    public List<Card> pickUpPile()
    {
        List<Card> newCards = new ArrayList<Card>(cards);
        cards.clear();
        return newCards;
    }

    public Card getTopCard()
    {
        return cards.get(getSize() - 1);
    }

    public boolean isEmpty()
    {
        return cards.size() == 0;
    }

    public int getSize()
    {
        return cards.size();
    }

    public void addCard(Card card)
    {
        cards.add(card);
    }   

    public void display()
    {
        for (Card card : cards)
        {
            System.out.println(card);
        }
    }

    public Card drawCardFromPile()
    {
        if (cards.size() == 0)
            return null;
        
        Card card = cards.remove(0);
        return card;
    }

    public List<Card> drawCardsFromPile(int n) {
        List<Card> drawn = new ArrayList<>();
        for (int i = 0; i < n && !cards.isEmpty(); i++) {
            drawn.add(cards.remove(cards.size() - 1)); // remove from end
        }
        return drawn;
    }

    
    public void shuffle(int n)
    {
        // how to shuffle the cards?
        Random rand = new Random();

        for (int i = 0; i < n; i++)
        {
            int r = i + rand.nextInt(cards.size() - i);

            Card temp = cards.get(r);
            cards.set(r, cards.get(i));
            cards.set(i, temp);
        }
    }

    public List<Card> getCards()
    {
        return cards;
    }

    public void initPopulate(int nDecks)
    {
        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < nDecks; i++)
        {
            for (Suit suit : Suit.values())
            {
                cards = Stream.concat(cards.stream(), generate_rank(suit).stream()).collect(Collectors.toList());
            }
        }
        this.cards = cards;
    }

    private List<Card> generate_rank(Suit suit)
    {
        List<Card> cards = new ArrayList<Card>();
        for (Rank rank : Rank.values())
        {   
            cards.add(new Card(rank, suit));
        }   
        return cards;
    }

    public void displayCards()
    {
        for (Card card : cards)
        {
            System.out.println(card);
        }
    }
}

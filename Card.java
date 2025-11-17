enum Rank 
{
    TWO(2),
    THREE(3), FOUR(4),
    FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8),
    NINE(9), TEN(10),
    JACK(11), QUEEN(12),
    KING(13), ACE(14);

    private int value;

    private Rank(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
};

enum Suit
{
    HEART,
    DIAMOND,
    CLUB,
    SPADE
};

public class Card
{
    private Rank rank;
    private Suit suit;

    Card(Rank rank, Suit suit)
    {
        setRank(rank);
        setSuit(suit);
    }

    public Card clone()
    {
        return new Card(getRank(), getSuit());
    }
    
    // setters
    public void setRank(Rank rank)
    {
        this.rank = rank;
    }
    public void setSuit(Suit suit)
    {
        this.suit = suit;
    }

    // getters
    public Rank getRank()
    {
        return rank;
    }
    public Suit getSuit()
    {
        return suit;
    }

    public String toString()
    {
        return "Card {Rank = " + rank.name() + " Suit = " + suit.name() + "}";
    }
}

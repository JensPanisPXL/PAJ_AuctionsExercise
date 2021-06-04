package be.pxl.auctions.model;

import java.time.LocalDate;

public final class BidBuilder {
    private long id;
    private double amount;
    private LocalDate date;
    private Auction auction;
    private User user;

    private BidBuilder() {
    }

    public static BidBuilder aBid() {
        return new BidBuilder();
    }

    public BidBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public BidBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public BidBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public BidBuilder withAuction(Auction auction) {
        this.auction = auction;
        return this;
    }

    public BidBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Bid build() {
        Bid bid = new Bid();
        bid.setId(id);
        bid.setAmount(amount);
        bid.setDate(date);
        bid.setAuction(auction);
        bid.setUser(user);
        return bid;
    }
}

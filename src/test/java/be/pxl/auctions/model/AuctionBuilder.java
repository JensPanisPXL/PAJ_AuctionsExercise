package be.pxl.auctions.model;

import java.time.LocalDate;

public final class AuctionBuilder {
    public static final String DESCRIPTION = "AuctionLand";
    public static final LocalDate END_DATE = LocalDate.now().minusDays(5);

    private long id;
    private String description = DESCRIPTION;
    private LocalDate endDate = END_DATE;

    private AuctionBuilder() {
    }

    public static AuctionBuilder anAuction() {
        return new AuctionBuilder();
    }

    public AuctionBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public AuctionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AuctionBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Auction build() {
        Auction auction = new Auction();
        auction.setId(id);
        auction.setDescription(description);
        auction.setEndDate(endDate);
        return auction;
    }
}

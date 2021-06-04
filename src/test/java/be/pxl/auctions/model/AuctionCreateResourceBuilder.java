package be.pxl.auctions.model;

import be.pxl.auctions.rest.resource.AuctionCreateResource;

public final class AuctionCreateResourceBuilder {
    public static final String DESCRIPTION = "AuctionLand";
    public static final String ENDDATE = "23/03/2030";

    private String description = DESCRIPTION;
    private String endDate = ENDDATE;

    private AuctionCreateResourceBuilder() {
    }

    public static AuctionCreateResourceBuilder anAuctionCreateResource() {
        return new AuctionCreateResourceBuilder();
    }

    public AuctionCreateResourceBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AuctionCreateResourceBuilder withEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public AuctionCreateResource build() {
        AuctionCreateResource auctionCreateResource = new AuctionCreateResource();
        auctionCreateResource.setDescription(description);
        auctionCreateResource.setEndDate(endDate);
        return auctionCreateResource;
    }
}

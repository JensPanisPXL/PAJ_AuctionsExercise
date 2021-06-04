package be.pxl.auctions.rest.resource;

import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuctionDTO {
    private long id;
    private String description;
    private LocalDate endDate;
    /*public List<Bid> bids = new ArrayList<>();
    private int numberOfBids;
    private Bid highestBid;
    private User user;*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /*public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public int getNumberOfBids() {
        return numberOfBids;
    }

    public void setNumberOfBids(int numberOfBids) {
        this.numberOfBids = numberOfBids;
    }

    public Bid getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Bid highestBid) {
        this.highestBid = highestBid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/
}

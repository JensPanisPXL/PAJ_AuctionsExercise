package be.pxl.auctions.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@NamedQuery(name = "findAllAuctions", query = "SELECT a FROM Auction a")
public class Auction {
    @Id
    @GeneratedValue
    private long id;
    private String description;
    private LocalDate endDate;
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    public List<Bid> bids = new ArrayList<>();

    public Auction() {
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

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

    public List<Bid> getBids() {
        return bids;
    }

    public boolean isFinished() {
        return LocalDate.now().isAfter(endDate);
    }

    public Bid findHighestBid() {
        if (getBids().size() > 0) {
            return getBids().stream().max(Comparator.comparingDouble(Bid::getAmount)).get();
        } else {
            return null;
        }
    }
}

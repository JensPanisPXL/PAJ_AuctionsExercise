package be.pxl.auctions.dao;

import be.pxl.auctions.model.Auction;
import java.util.List;
import java.util.Optional;


public interface AuctionDao {
    Auction saveAuction(Auction auction);
    Optional<Auction> findAuctionById(long auction);
    List<Auction> findAllAuctions();
}

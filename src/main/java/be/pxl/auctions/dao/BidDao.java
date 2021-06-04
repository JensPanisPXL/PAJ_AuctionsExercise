package be.pxl.auctions.dao;

import be.pxl.auctions.model.Bid;

public interface BidDao {
    Bid saveBid(Bid bid);
}

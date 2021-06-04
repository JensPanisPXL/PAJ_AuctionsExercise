package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.BidDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.EmailValidator;
import be.pxl.auctions.util.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidDao bidDao;
    @Autowired
    private AuctionDao auctionDao;
    @Autowired
    private UserDao userDao;

    public BidDTO createBid(BidCreateResource bidInfo, long auctionId) {
        if (StringUtils.isBlank(bidInfo.getEmail())) {
            throw new RequiredFieldException("email");
        }
        if (!EmailValidator.isValid(bidInfo.getEmail())) {
            throw new InvalidEmailException(bidInfo.getEmail());
        }
        if (bidInfo.getPrice() == null) {
            throw new RequiredFieldException("price");
        }
        Optional<User> existingUser = userDao.findUserByEmail(bidInfo.getEmail());
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("No user found with email:" + bidInfo.getEmail());
        }
        Optional<Auction> existingAuction = auctionDao.findAuctionById(auctionId);
        if (existingAuction.isEmpty()) {
            throw new AuctionNotFoundException("No auction found with id:" + auctionId);
        }
        if (existingAuction.get().findHighestBid().getAmount() > bidInfo.getPrice()) {
            throw new InvalidBidException("Bid is lower then current price, the current price is: " + existingAuction.get().findHighestBid().getAmount());
        }
        if (existingAuction.get().getEndDate().isBefore(LocalDate.now())) {
            throw new InvalidDateException("The auction has already closed!");
        }
        if (existingUser.get().equals(existingAuction.get().findHighestBid().getUser())) {
            throw new InvalidBidException("You already have the highest bid!");
        }
        Bid bid = mapToBid(bidInfo, auctionId);
        return mapToBidDTO(bidDao.saveBid(bid));
    }

    private BidDTO mapToBidDTO(Bid saveBid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(saveBid.getId());
        bidDTO.setAmount(saveBid.getAmount());
        bidDTO.setDate(saveBid.getDate());
        bidDTO.setUser(mapUserDTO(saveBid.getUser()));
        return bidDTO;
    }

    private UserDTO mapUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setAge(user.getAge());
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        return userDTO;
    }

    private Bid mapToBid(BidCreateResource bidInfo, long auctionId) {
        Bid bid = new Bid();
        bid.setAmount(bidInfo.getPrice());
        bid.setUser(userDao.findUserByEmail(bidInfo.getEmail()).get());
        bid.setAuction(auctionDao.findAuctionById(auctionId).get());
        bid.setDate(LocalDate.now());
        return bid;
    }

}

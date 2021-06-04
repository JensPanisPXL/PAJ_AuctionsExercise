package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    @Autowired
    private AuctionDao auctionDao;

    public List<AuctionDTO> getAllAuctions() {
        return auctionDao.findAllAuctions().stream().map(this::mapToAuctionDTO).collect(Collectors.toList());
    }

    public AuctionDTO getAuctionById(long auctionId) {
        return auctionDao.findAuctionById(auctionId).map(this::mapToAuctionDTO).orElseThrow(() -> new AuctionNotFoundException("Unable to find User with id [" + auctionId + "]"));
    }

    public AuctionDTO createAuction(AuctionCreateResource auctionInfo) {
        if (StringUtils.isBlank(auctionInfo.getDescription())) {
            throw new RequiredFieldException("Description");
        }
        if (auctionInfo.getEndDate() == null) {
            throw new RequiredFieldException("DateOfBirth");
        }
        Auction auction = mapToAuction(auctionInfo);
        if (auction.getEndDate().isBefore(LocalDate.now())) {
            throw new InvalidDateException("End date cannot be in the past.");
        }
        return mapToAuctionDTO(auctionDao.saveAuction(auction));
    }

    private AuctionDTO mapToAuctionDTO(Auction auction) {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(auction.getId());
        auctionDTO.setEndDate(auction.getEndDate());
        auctionDTO.setDescription(auction.getDescription());
            auctionDTO.setHighestBid(mapToBidDTO(auction.findHighestBid()));
            for (Bid b: auction.getBids()){
                BidDTO bidDTO = mapToBidDTO(b);
                auctionDTO.bids.add(bidDTO);
            }
        auctionDTO.setNumberOfBids(auction.getBids().size());
        return auctionDTO;
    }

    private BidDTO mapToBidDTO(Bid highestBid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setUser(mapUserDTO(highestBid.getUser()));
        bidDTO.setAmount(highestBid.getAmount());
        bidDTO.setId(highestBid.getId());
        bidDTO.setDate(highestBid.getDate());
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

    private Auction mapToAuction(AuctionCreateResource auctionInfo) {
        Auction auction = new Auction();
        auction.setDescription(auctionInfo.getDescription());
        try {
            auction.setEndDate(LocalDate.parse(auctionInfo.getEndDate(), DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("[" + auction.getEndDate() + "] is not a valid date. Excepted format: dd/mm/yyyy");
        }
        return auction;
    }
}

# auction-house-application
A simple auctionhouse application similar to that of a stock trading platform


An auction has bids, and a minimum bid price with a total quantity
of bids available.

The users can place bids on the auction itself and specify a price not less 
than the minimum bid price, as well as a quantity. 

Auction bids are sorted bt bid price, that highest price bids will take
the greatest priority as to maximize profits. 

When the auction is closed, the auction will take all the highest priced 
bids, until there are no bids remaining. 


Users are broken down into regular users and admins

Admins:
    -Can create,delete,update, and read all auctionUser data
    -admins cannot participate in the auction directly
Users:
    -can create an auction for peer users to bid, however 
    cannot bid themselves on their own auction 
    -

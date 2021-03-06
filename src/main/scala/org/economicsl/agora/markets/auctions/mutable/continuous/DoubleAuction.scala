/*
Copyright 2016 ScalABM

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.economicsl.agora.markets.auctions.mutable.continuous

import org.economicsl.agora.markets.Fill
import org.economicsl.agora.markets.auctions.matching.FindBestPricedOrder
import org.economicsl.agora.markets.auctions.mutable.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.agora.markets.tradables.orders.Persistent
import org.economicsl.agora.markets.tradables.orders.ask.LimitAskOrder
import org.economicsl.agora.markets.tradables.orders.bid.LimitBidOrder
import org.economicsl.agora.markets.tradables.{Price, Quantity, Tradable}


class DoubleAuction(askOrderPricingRule: (LimitAskOrder with Quantity, LimitBidOrder with Persistent with Quantity) => Price,
                    bidOrderPricingRule: (LimitBidOrder with Quantity, LimitAskOrder with Persistent with Quantity) => Price,
                    val tradable: Tradable)
  extends TwoSidedPostedPriceAuction[LimitAskOrder with Quantity, SortedAskOrderBook[LimitAskOrder with Persistent with Quantity],
                                     LimitBidOrder with Quantity, SortedBidOrderBook[LimitBidOrder with Persistent with Quantity]] {

  /** Fill an incoming `LimitAskOrder`.
    *
    * @param order a `LimitAskOrder` instance.
    * @return
    */
  final def fill(order: LimitAskOrder with Quantity): Option[Fill] = {
    val result = buyerPostedPriceAuction.fill(order)
    result match {
      case Some(fills) => result
      case None => order match {
        case unfilledOrder: LimitAskOrder with Persistent => place(unfilledOrder); result
        case _ => result
      }
    }
  }

  /** Fill an incoming `LimitBidOrder`.
    *
    * @param order a `LimitBidOrder` instance.
    * @return
    */
  final def fill(order: LimitBidOrder with Quantity): Option[Fill] = {
    val result = sellerPostedPriceAuction.fill(order)
    result match {
      case Some(fills) => result
      case None => order match {
        case unfilledOrder: LimitBidOrder with Persistent => place(unfilledOrder); result
        case _ => None
      }
    }
  }

  /** An instance of `BuyerPostedPriceAuction` used to fill incoming `AskOrder` instances. */
  protected val buyerPostedPriceAuction = {
    val bidOrderBook = SortedBidOrderBook[LimitBidOrder with Persistent with Quantity](tradable)
    val askOrderMatchingRule = FindBestPricedOrder[LimitAskOrder with Quantity, LimitBidOrder with Persistent with Quantity]()
    BuyerPostedPriceAuction(bidOrderBook, askOrderMatchingRule, askOrderPricingRule)
  }

  /** An instance of `SellerPostedPriceAuction` used to fill incoming `BidOrder` instances. */
  protected val sellerPostedPriceAuction = {
    val askOrderBook = SortedAskOrderBook[LimitAskOrder with Persistent with Quantity](tradable)
    val bidOrderMatchingRule = FindBestPricedOrder[LimitBidOrder with Quantity, LimitAskOrder with Persistent with Quantity]()
    SellerPostedPriceAuction(askOrderBook, bidOrderMatchingRule, bidOrderPricingRule)
  }

}

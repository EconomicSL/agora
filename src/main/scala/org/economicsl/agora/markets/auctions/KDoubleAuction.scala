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
package org.economicsl.agora.markets.auctions

import org.economicsl.agora.markets.Fill
import org.economicsl.agora.markets.auctions.orderbooks.OrderBook
import org.economicsl.agora.markets.auctions.pricing.WeightedAveragePricingRule
import org.economicsl.agora.markets.tradables.Tradable
import org.economicsl.agora.markets.tradables.orders.Persistent
import org.economicsl.agora.markets.tradables.orders.ask.LimitAskOrder
import org.economicsl.agora.markets.tradables.orders.bid.LimitBidOrder


class KDoubleAuction[AB <: OrderBook[LimitAskOrder with Persistent, AB], BB <: OrderBook[LimitBidOrder with Persistent, BB]]
                    (askOrderBook: AB,
                     askOrderMatchingRule: (LimitAskOrder, BB) => Option[(LimitAskOrder, LimitBidOrder with Persistent)],
                     bidOrderBook: BB,
                     bidOrderMatchingRule: (LimitBidOrder, AB) => Option[(LimitBidOrder, LimitAskOrder with Persistent)],
                     val k: Double,
                     val tradable: Tradable)
  extends ContinuousDoubleAuction[LimitAskOrder, AB, LimitBidOrder, BB](askOrderBook, askOrderMatchingRule, WeightedAveragePricingRule(1-k),
                                                                        bidOrderBook, bidOrderMatchingRule, WeightedAveragePricingRule(k)) {

  require(0 <= k && k <= 1, "The value of k must be in the unit interval (i.e., [0, 1]).")

  /** Fill an incoming `LimitAskOrder`.
    *
    * @param order a `LimitAskOrder` instance.
    * @return
    */
  final def fill(order: LimitAskOrder): Option[Fill] = askOrderMatchingRule(order, bidOrderBook) match {
    case Some((askOrder, matchedBidOrder)) =>
      cancel(matchedBidOrder.uuid) // SIDE EFFECT!
      val price = askOrderPricingRule(askOrder, matchedBidOrder)
      val quantity = math.min(askOrder.quantity, matchedBidOrder.quantity) // not dealing with residual orders!
      Some(new Fill(matchedBidOrder.issuer, askOrder.issuer, price, quantity, tradable))
    case None => order match {
      case unfilledOrder: LimitAskOrder with Persistent => place(unfilledOrder.uuid -> unfilledOrder); None
      case _ => None
    }
  }

  /** Fill an incoming `LimitBidOrder`.
    *
    * @param order a `LimitBidOrder` instance.
    * @return
    */
  final def fill(order: LimitBidOrder): Option[Fill] = bidOrderMatchingRule(order, askOrderBook) match {
    case Some((bidOrder, matchedAskOrder)) =>
      cancel(matchedAskOrder.uuid) // SIDE EFFECT!
      val price = bidOrderPricingRule(bidOrder, matchedAskOrder)
      val quantity = math.min(matchedAskOrder.quantity, bidOrder.quantity) // not dealing with residual orders!
      Some(new Fill(bidOrder.issuer, matchedAskOrder.issuer, price, quantity, tradable))
    case None => order match {
      case unfilledOrder: LimitBidOrder with Persistent => place(unfilledOrder.uuid -> unfilledOrder); None
      case _ => None
    }
  }

}


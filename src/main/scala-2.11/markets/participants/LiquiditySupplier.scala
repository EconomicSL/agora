/*
Copyright 2015 David R. Pugh, J. Doyne Farmer, and Dan F. Tang

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
package markets.participants

import markets.orders.limit.{LimitAskOrder, LimitBidOrder}
import markets.participants.strategies.LimitOrderTradingStrategy
import markets.tradables.Tradable


trait LiquiditySupplier extends MarketParticipant {

  def limitOrderTradingStrategy: LimitOrderTradingStrategy

  override def receive: Receive = {
    case SubmitLimitAskOrder =>
      limitOrderTradingStrategy.askOrderStrategy(tickers) match {
        case Some((price, quantity, tradable)) =>
          val limitAskOrder = generateLimitAskOrder(price, quantity, tradable)
          submit(limitAskOrder)
        case None =>  // no feasible askOrderStrategy!
      }
    case SubmitLimitBidOrder =>
      limitOrderTradingStrategy.bidOrderStrategy(tickers) match {
        case Some((price, quantity, tradable)) =>
          val limitBidOrder = generateLimitBidOrder(price, quantity, tradable)
          submit(limitBidOrder)
        case None =>  // no feasible bidOrderStrategy!
      }
    case message => super.receive(message)
  }

  private[this] def generateLimitAskOrder(price: Long, quantity: Long, tradable: Tradable) = {
    LimitAskOrder(self, price, quantity, timestamp(), tradable, uuid())
  }

  private[this] def generateLimitBidOrder(price: Long, quantity: Long, tradable: Tradable) = {
    LimitBidOrder(self, price, quantity, timestamp(), tradable, uuid())
  }
}
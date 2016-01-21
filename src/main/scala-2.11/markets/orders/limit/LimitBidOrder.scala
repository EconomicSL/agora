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
package markets.orders.limit

import akka.actor.ActorRef

import java.util.UUID

import markets.orders.{AskOrder, BidOrder}
import markets.orders.market.MarketAskOrder
import markets.tradables.Tradable


case class LimitBidOrder(issuer: ActorRef,
                         price: Long,
                         quantity: Long,
                         timestamp: Long,
                         tradable: Tradable,
                         uuid: UUID) extends LimitOrderLike with BidOrder {

  def crosses(order: AskOrder): Boolean = order match {
    case _: MarketAskOrder => true
    case _: LimitAskOrder => if (order.price < this.price) true else false
  }

  def split(residualQuantity: Long): (LimitBidOrder, LimitBidOrder) = {
    val filledQuantity = quantity - residualQuantity
    (this.copy(quantity = filledQuantity), this.copy(quantity = residualQuantity))
  }

}

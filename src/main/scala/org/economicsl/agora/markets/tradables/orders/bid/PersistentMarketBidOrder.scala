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
package org.economicsl.agora.markets.tradables.orders.bid

import java.util.UUID

import org.economicsl.agora.markets.tradables.orders.Persistent
import org.economicsl.agora.markets.tradables.orders.ask.{AskOrder, LimitAskOrder}
import org.economicsl.agora.markets.tradables.Tradable


/** Trait defining a `MarketBidOrder` that can be stored in a `BidOrderBook`. */
trait PersistentMarketBidOrder extends MarketBidOrder with Persistent


/** Companion object for the `PersistentMarketBidOrder` trait.
  *
  * Provides constructors for the default implementations of a `PersistentMarketBidOrder`.
  */
object PersistentMarketBidOrder {

  /** Creates an instance of a `PersistentMarketBidOrder`.
    *
    * @param issuer the `UUID` of the actor that issued the `PersistentMarketBidOrder`.
    * @param quantity the number of units of the `tradable` for which the `PersistentMarketBidOrder` was issued.
    * @param timestamp the time at which the `PersistentMarketBidOrder` was issued.
    * @param tradable the `Tradable` for which the `PersistentMarketBidOrder` was issued.
    * @param uuid the `UUID` of the `PersistentMarketBidOrder`.
    * @return an instance of a `PersistentMarketBidOrder`.
    */
  def apply(issuer: UUID, quantity: Long, timestamp: Long, tradable: Tradable, uuid: UUID): PersistentMarketBidOrder = {
    DefaultImpl(issuer, quantity, timestamp, tradable, uuid)
  }


  /** Class providing a default implementation of a `PersistentMarketBidOrder` designed for use in securities market simulations.
    *
    * @param issuer the `UUID` of the actor that issued the `PersistentMarketBidOrder`.
    * @param quantity the number of units of the `tradable` for which the `PersistentMarketBidOrder` was issued.
    * @param timestamp the time at which the `PersistentMarketBidOrder` was issued.
    * @param tradable the `Tradable` for which the `PersistentMarketBidOrder` was issued.
    * @param uuid the `UUID` of the `PersistentMarketBidOrder`.
    * @return an instance of a `PersistentMarketBidOrder`.
    */
  private[this] case class DefaultImpl(issuer: UUID, quantity: Long, timestamp: Long, tradable: Tradable, uuid: UUID)
    extends PersistentMarketBidOrder {

    val priceCriteria: (AskOrder with Persistent) => Boolean = {
      case order: LimitAskOrder with Persistent => limit >= order.limit
      case _ => false
    }

    val isAcceptable: (AskOrder with Persistent) => Boolean = {
      order => (order.tradable == tradable) && priceCriteria(order)
    }

  }

}
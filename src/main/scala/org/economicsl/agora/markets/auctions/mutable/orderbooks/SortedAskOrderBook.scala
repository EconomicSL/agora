package org.economicsl.agora.markets.auctions.mutable.orderbooks

import java.util.UUID

import org.economicsl.agora.markets.tradables.orders.ask.AskOrder
import org.economicsl.agora.markets.tradables.Tradable
import org.economicsl.agora.markets.tradables.orders.Persistent

import scala.collection.mutable


class SortedAskOrderBook[A <: AskOrder with Persistent](val tradable: Tradable)(implicit ordering: Ordering[A])
  extends AskOrderBook[A] with SortedOrders[A] {

  protected[orderbooks] val existingOrders = mutable.HashMap.empty[UUID, A]

  protected[orderbooks] val sortedOrders = mutable.TreeSet.empty[A](ordering)

}


object SortedAskOrderBook {

  /** Create an `AskOrderBook` instance for a particular `Tradable`.
    *
    * @param tradable all `AskOrder` instances contained in the `AskOrderBook` should be for the same `Tradable`.
    * @tparam A type of `AskOrder` stored in the order book.
    */
  def apply[A <: AskOrder with Persistent](tradable: Tradable)(implicit ordering: Ordering[A]): SortedAskOrderBook[A] = {
    new SortedAskOrderBook[A](tradable)(ordering)
  }

}
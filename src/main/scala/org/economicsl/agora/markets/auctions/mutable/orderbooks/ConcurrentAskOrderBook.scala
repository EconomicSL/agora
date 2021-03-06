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
package org.economicsl.agora.markets.auctions.mutable.orderbooks

import java.util.UUID

import org.economicsl.agora.markets.tradables.Tradable
import org.economicsl.agora.markets.tradables.orders.Persistent
import org.economicsl.agora.markets.tradables.orders.ask.AskOrder

import scala.collection.concurrent


/** Class implementing a mutable, thread-safe `AskOrderBook`.
  *
  * @param tradable all `AskOrder with Persistent` instances must be for the same `Tradable`.
  * @tparam A the type of `AskOrder with Persistent` stored in the `AskOrderBook`.
  */
case class ConcurrentAskOrderBook[A <: AskOrder with Persistent](tradable: Tradable) extends AskOrderBook[A] {

  /* underlying collection used to store `Order` instances. */
  protected[orderbooks] val existingOrders = concurrent.TrieMap.empty[UUID, A]

}

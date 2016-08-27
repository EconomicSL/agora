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
package markets.orderbooks

import markets.orders.Order
import markets.tradables.Tradable


/** Abstract class defining the interface for a `SortedOrderBook`.
  *
  * @param tradable all `Orders` contained in a `SortedOrderBook` should be for the same `Tradable`.
  * @tparam A the type of `Order` stored in a `SortedOrderBook`.
  */
abstract class AbstractSortedOrderBook[A <: Order](tradable: Tradable)
  extends AbstractOrderBook[A](tradable) {

  /** Return the head `Order` of the `OrderBook`.
    *
    * @return `None` if the `OrderBook` is empty; `Some(order)` otherwise.
    */
  def headOption: Option[A] = sortedOrders.headOption

  /* Underlying sorted collection of `Order` instances; protected at package-level for testing. */
  protected[orderbooks] def sortedOrders: collection.SortedSet[A]

}


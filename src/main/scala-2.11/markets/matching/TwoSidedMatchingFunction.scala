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
package markets.matching

import java.util.UUID

import markets.orderbooks.OrderBook
import markets.tradables.orders.Order


/** Trait defining the interface for a `TwoSidedMatchingFunction`. */
trait TwoSidedMatchingFunction[O1 <: Order, O2 <: Order] extends MatchingFunction[O1, O2]{

  /** Matches a given `Order` with an existing `Order` from an `OrderBook`.
    *
    * @param order the `Order` that needs a match.
    * @param orderBook an `OrderBook` containing potential matches for the `order`.
    * @return `None` if no suitable `Order` is found in the `orderBook`; `Some(order)` otherwise.
    */
  def apply(order: O1, orderBook: OrderBook[O2, collection.GenMap[UUID, O2]]): Option[(O1, O2)]

}

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
package markets.clearing.engines

import markets.clearing.strategies.BestLimitPriceFormationStrategy
import markets.orders.filled.FilledOrderLike
import markets.orders.{AskOrderLike, BidOrderLike, OrderLike}

import scala.collection.immutable


class BrokenMatchingEngine extends MatchingEngineLike with BestLimitPriceFormationStrategy {

  var askOrderBook: immutable.Iterable[AskOrderLike] = immutable.List[AskOrderLike]()

  var bidOrderBook: immutable.Iterable[BidOrderLike] = immutable.List[BidOrderLike]()

  var referencePrice: Long = 1

  /** A `BrokenMatchingEngine` always fails to fill orders. */
  def fillIncomingOrder(order: OrderLike): Option[immutable.Seq[FilledOrderLike]] = {
    None
  }

}
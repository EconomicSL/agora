/*
Copyright 2016 David R. Pugh

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
package markets.engines

import markets.orders.{AskOrder, BidOrder, Order}

import scala.collection.immutable.Queue


trait GenericMatchingEngine[+CC1 <: Iterable[AskOrder], +CC2 <: Iterable[BidOrder]] {

  def askOrderBook: GenericAskOrderBook[CC1]

  def bidOrderBook: GenericBidOrderBook[CC2]

  /** Find a match for the incoming order.
    *
    * @param incoming the order to be matched.
    * @return a collection of matches.
    * @note Depending on size of the incoming order and the state of the market when the order is
    *       received, a single incoming order may generate several matches.
    */
  def findMatch(incoming: Order): Option[Queue[Matching]]

  /** Remove and return a specific order one of the order books.
    *
    * @return if the order can not be found in the order book, `None`; else `Some(residualOrder)`.
    * @note Removal of the order is a side effect.
    */
  def pop(order: Order): Option[Order] = order match {
    case order: AskOrder => askOrderBook.pop(order)
    case order: BidOrder => bidOrderBook.pop(order)
  }

}

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
package markets.engines.immutable

import markets.orders.{AskOrder, BidOrder}

import scala.collection.immutable.TreeSet


/** Immutable implementation of a Continuous Double Auction (CDA) Matching Engine.
  *
  * @param askOrdering
  * @param bidOrdering
  * @param initialPrice
  */
class ImmutableTreeSetCDAMatchingEngine(val askOrdering: Ordering[AskOrder],
                                        val bidOrdering: Ordering[BidOrder],
                                        val initialPrice: Long)
  extends GenericImmutableCDAMatchingEngine[TreeSet[AskOrder], TreeSet[BidOrder]] {

  val askOrderBook = new ImmutableTreeSetAskOrderBook()(askOrdering)

  val bidOrderBook = new ImmutableTreeSetBidOrderBook()(bidOrdering)

}


object ImmutableTreeSetCDAMatchingEngine {

  def apply(askOrdering: Ordering[AskOrder],
            bidOrdering: Ordering[BidOrder],
            initialPrice: Long): ImmutableTreeSetCDAMatchingEngine = {
    new ImmutableTreeSetCDAMatchingEngine(askOrdering, bidOrdering, initialPrice)
  }

}
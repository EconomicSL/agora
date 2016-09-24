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
package markets.orders


/** Trait representing an order to buy a `Tradable` object. */
trait BidOrder extends Order with Price with Quantity {

  /** Determines whether the `BidOrder` crosses a particular `AskOrder`.
    *
    * @return true if the `AskOrder` crosses the `BidOrder`; false otherwise.
    * @note This partial function is only defined for ask orders for the same `Tradable` as the
    *       `BidOrder` and will generate a `MatchError` if called with an ask order for any other
    *       `Tradable`.
    */
  def crosses: PartialFunction[AskOrder, Boolean] = {
    case order: AskOrder if this.tradable == order.tradable => this.price >= order.price
  }

}


/** Companion object for the `BidOrder` trait.
  *
  * The companion object provides various orderings for `BidOrder` instances.
  */
object BidOrder {

  /** By default, instances of `BidOrder` are ordered based on `price` from highest to lowest */
  implicit def ordering[O <: BidOrder]: Ordering[O] = Price.ordering.reverse

  /** The highest priority `BidOrder` is the one with the highest `price`. */
  def priority[O <: BidOrder]: Ordering[O] = Price.ordering

}
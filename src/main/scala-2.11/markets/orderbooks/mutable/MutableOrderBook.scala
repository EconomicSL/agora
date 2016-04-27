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
package markets.orderbooks.mutable

import markets.orderbooks.OrderBook
import markets.orders.Order

import scala.collection.mutable


/** Base trait for all order books.
  *
  * An order book is a collection of orders (typically either ask or bid orders).
  *
  * @tparam A the type of orders stored in the order book.
  * @tparam B the type of underlying collection used to store the orders.
  */
trait MutableOrderBook[A <: Order, B <: mutable.Iterable[A]] extends OrderBook[A, B] {

  protected val backingStore: B

}


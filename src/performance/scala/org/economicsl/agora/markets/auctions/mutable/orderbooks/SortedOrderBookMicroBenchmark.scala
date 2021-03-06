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

import org.economicsl.agora.markets.tradables.orders.ask.{AskOrder, LimitAskOrder}
import org.economicsl.agora.OrderGenerator
import org.economicsl.agora.markets.tradables.orders.Persistent
import org.scalameter.api._
import org.scalameter.{Bench, Gen}


/** Performance tests for the `SortedOrderBook` class. */
object SortedOrderBookMicroBenchmark extends Bench.OnlineRegressionReport with OrderGenerator {

  val sizes = Gen.exponential("Number of existing orders")(factor=10, until=1000000, from=10)

  /** Generates a collection of `SortedOrderBook` instances of increasing size. */
  val orderBooks = for { size <- sizes } yield {
    val orderBook = SortedOrderBook[LimitAskOrder with Persistent](validTradable)
    val orders = for { i <- 1 to size } yield orderGenerator.nextAskOrder(0.5, validTradable)
    orders.foreach( order => orderBook.add(order) )
    orderBook
  }

  performance of "OrderBook" config (
    reports.resultDir -> "target/benchmarks/markets/engines/orderbooks/OrderBook",
    exec.benchRuns -> 200,
    exec.independentSamples -> 20,
    exec.jvmflags -> List("-Xmx2G")
    ) in {

    /** Adding an `Order` to a `SortedOrderBook` should be an `O(log n)` operation. */
    measure method "add" in {
      using(orderBooks) in {
        orderBook =>
          val newOrder = orderGenerator.nextAskOrder(0.5, validTradable)
          orderBook.add(newOrder)
      }
    }

    /** Filtering a `SortedOrderBook` should be an `O(n)` operation. */
    measure method "filter" in {
      using(orderBooks) in {
        orderBook => orderBook.filter(order => order.isInstanceOf[LimitAskOrder])
      }
    }

    /** Finding an `Order` in a `SortedOrderBook` should be an `O(n)` operation. */
    measure method "find" in {
      using(orderBooks) in {
        orderBook => orderBook.find(order => order.isInstanceOf[LimitAskOrder])
      }
    }

    /** Removing an `Order` from a `SortedOrderBook` should be an `O(log n)` operation. */
    measure method "remove(order)" in {
      using(orderBooks) in {
        orderBook =>
          val (uuid, _) = orderBook.existingOrders.head
          orderBook.remove(uuid)
      }
    }

    /** Removing the head `Order` from a `SortedOrderBook` should be an `O(log n)` operation. */
    measure method "remove()" in {
      using(orderBooks) in {
        orderBook => orderBook.remove()
      }
    }

  }

}

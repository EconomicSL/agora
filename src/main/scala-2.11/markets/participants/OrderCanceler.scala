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
package markets.participants

import markets.participants.strategies.OrderCancellationStrategy
import markets.{Cancel, Canceled}
import markets.orders.Order


/** Mixin Trait providing behavior necessary to cancel outstanding orders. */
trait OrderCanceler extends MarketParticipant {

  def orderCancellationStrategy: OrderCancellationStrategy

  override def receive: Receive = {
    case Canceled(order, _, _) =>
      outstandingOrders -= order
    case SubmitOrderCancellation =>
      val canceledOrder = orderCancellationStrategy.cancelOneOf(outstandingOrders)
      canceledOrder match {
        case Some(order) =>
          val orderCancellation = generateOrderCancellation(order)
          submit(orderCancellation)
        case None =>  // no outstanding orders to cancel!
      }
    case message => super.receive(message)
  }

  private[this] def submit(orderCancellation: Cancel): Unit = {
    val market = markets(orderCancellation.order.tradable)
    market tell(orderCancellation, self)
  }

  private[this] def generateOrderCancellation(order: Order): Cancel = {
    Cancel(order, timestamp(), uuid())
  }

}
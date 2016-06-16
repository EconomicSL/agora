package markets.engines.orderbooks

import markets.MarketsTestKit
import markets.orders.BidOrder
import markets.orders.orderings.bid.BidPriceOrdering
import markets.tradables.Tradable
import org.scalatest.{FeatureSpecLike, Matchers}

import scala.util.Random


class SortedBidOrderBookSpec extends SortedOrderBookSpec[BidOrder]("SortedOrderBook[BidOrder]")
  with FeatureSpecLike
  with Matchers
  with MarketsTestKit {

  def prng: Random = new Random()

  def orderBookFactory(tradable: Tradable) = {
    SortedOrderBook[BidOrder](validTradable)(BidPriceOrdering)
  }

  /** Generate a random `Order`.
    *
    * @param marketOrderProbability probability of generating a `MarketOrder`.
    * @param minimumPrice           lower bound on the price for a `LimitOrder`.
    * @param maximumPrice           upper bound on the price for a `LimitOrder`.
    * @param minimumQuantity        lower bound on the `Order` quantity.
    * @param maximumQuantity        upper bound on the `Order` quantity.
    * @param timestamp              a timestamp for the `Order`.
    * @param tradable               the `Order` validTradable.
    * @return either `LimitOrder` or `MarketOrder`, depending.
    */
  def generateRandomOrder(marketOrderProbability: Double,
                          minimumPrice: Long,
                          maximumPrice: Long,
                          minimumQuantity: Long,
                          maximumQuantity: Long,
                          timestamp: Long,
                          tradable: Tradable): BidOrder = {
    randomBidOrder(marketOrderProbability, minimumPrice, maximumPrice, minimumQuantity,
      maximumQuantity, timestamp, tradable)
  }

}
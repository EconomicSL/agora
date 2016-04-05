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
package markets.participants.strategies

import akka.agent.Agent

import markets.tickers.Tick
import markets.tradables.Tradable

import scala.collection.mutable


trait TradingStrategy[T] {

  def askOrderStrategy(tickers: mutable.Map[Tradable, Agent[Tick]]): Option[T]

  def askQuantity(ticker: Agent[Tick], tradable: Tradable): Long

  def bidOrderStrategy(tickers: mutable.Map[Tradable, Agent[Tick]]): Option[T]

  def bidQuantity(ticker: Agent[Tick], tradable: Tradable): Long

  def chooseOneOf(tickers: mutable.Map[Tradable, Agent[Tick]]): Option[(Tradable, Agent[Tick])]

}

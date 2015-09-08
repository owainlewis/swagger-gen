package com.monitise.mcp.service.models

import spray.json.DefaultJsonProtocol

case class FetchSucceeded(card: CardOut)

object FetchSucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(FetchSucceeded.apply)
}

case class Failed(message: String, errorDetail: String)

object Failed extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat2(Failed.apply)
}

case class ExpiryDate(expiryMonth: String, expiryYear: String)

object ExpiryDate extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat2(ExpiryDate.apply)
}

case class CardOut(cardId: String, defaultCard: boolean, lastFourDigits: String, expiryDate: ExpiryDate, billingAddress: BillingAddress, cardholderName: String, nickname: String)

object CardOut extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat7(CardOut.apply)
}

case class Rejected(rejections: Seq[RejectionInfo])

object Rejected extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(Rejected.apply)
}

case class EditStarted(card: CardEdit)

object EditStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(EditStarted.apply)
}

case class DeleteStarted(cardId: String)

object DeleteStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(DeleteStarted.apply)
}

case class HistoryStarted(limit: Int)

object HistoryStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(HistoryStarted.apply)
}

case class AddStarted(card: CardAdd)

object AddStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(AddStarted.apply)
}

case object DefaultSucceeded

object DefaultSucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(DefaultSucceeded.apply)
}

case class EditSucceeded(card: CardOut)

object EditSucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(EditSucceeded.apply)
}

case object DefaultAccepted

object DefaultAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(DefaultAccepted.apply)
}

case object HistoryAccepted

object HistoryAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(HistoryAccepted.apply)
}

case class HistoryItem(timestamp: String, action: String)

object HistoryItem extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat2(HistoryItem.apply)
}

case class CardAdd(pan: String, expiryDate: ExpiryDate, billingAddress: BillingAddress, cardholderName: String, nickname: String)

object CardAdd extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat5(CardAdd.apply)
}

case class DefaultStarted(cardId: String)

object DefaultStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(DefaultStarted.apply)
}

case class CardEdit(cardId: String, expiryDate: ExpiryDate, billingAddress: BillingAddress, cardholderName: String, nickname: String)

object CardEdit extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat5(CardEdit.apply)
}

case object DeleteSucceeded

object DeleteSucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(DeleteSucceeded.apply)
}

case class FetchStarted(cardId: String)

object FetchStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(FetchStarted.apply)
}

case object FetchAccepted

object FetchAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(FetchAccepted.apply)
}

case class RejectionInfo(source: String, message: String)

object RejectionInfo extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat2(RejectionInfo.apply)
}

case class BillingAddress(addressLine1: String, addressLine2: String, locality: String, region: String, postalCode: String, countryCode: String)

object BillingAddress extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat6(BillingAddress.apply)
}

case class EditAccepted(card: CardEdit)

object EditAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(EditAccepted.apply)
}

case object FetchListAccepted

object FetchListAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(FetchListAccepted.apply)
}

case class FetchListSucceeded(cards: Seq[CardOut])

object FetchListSucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(FetchListSucceeded.apply)
}

case object DeleteAccepted

object DeleteAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(DeleteAccepted.apply)
}

case class AddSucceeded(card: CardOut)

object AddSucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(AddSucceeded.apply)
}

case object FetchListStarted

object FetchListStarted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(FetchListStarted.apply)
}

case class AddAccepted(card: CardAdd)

object AddAccepted extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(AddAccepted.apply)
}

case class HistorySucceeded(history: Seq[HistoryItem])

object HistorySucceeded extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1(HistorySucceeded.apply)
}


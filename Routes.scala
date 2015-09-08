package com.monitise.mcp.service

trait GeneratedRoutes
  extends HttpService
  with MCPSprayDirectives
  with TokenisationDirectives {
  this: RouteUtilities =>

  lazy val allGeneratedRoutes = 

  val getCards =
    path(cards) { 
      get {
        processIAM(ConsumerCategory, None, None, ROLES, SECURITY) { msg =>
          tokeniseEntity(lensFor[MODEL]) { body =>
            complete(getCards_impl(msg, body))
          }
        }
      }
    }

  def getCards(msg: Message[Nothing], add: MODEL): Future[HttpResponse]

  val addCard =
    path(cards) { 
      post {
        processIAM(ConsumerCategory, None, None, ROLES, SECURITY) { msg =>
          tokeniseEntity(lensFor[MODEL]) { body =>
            complete(addCard_impl(msg, body))
          }
        }
      }
    }

  def addCard(msg: Message[Nothing], add: MODEL): Future[HttpResponse]

  val getCard =
    path(cards / Segment) { cardId
      get {
        processIAM(ConsumerCategory, None, None, ROLES, SECURITY) { msg =>
          tokeniseEntity(lensFor[MODEL]) { body =>
            complete(getCard_impl(msg, body))
          }
        }
      }
    }

  def getCard(msg: Message[Nothing], add: MODEL): Future[HttpResponse]

  val editCard =
    path(cards / Segment) { cardId
      put {
        processIAM(ConsumerCategory, None, None, ROLES, SECURITY) { msg =>
          tokeniseEntity(lensFor[MODEL]) { body =>
            complete(editCard_impl(msg, body))
          }
        }
      }
    }

  def editCard(msg: Message[Nothing], add: MODEL): Future[HttpResponse]

  val deleteCard =
    path(cards / Segment) { cardId
      delete {
        processIAM(ConsumerCategory, None, None, ROLES, SECURITY) { msg =>
          tokeniseEntity(lensFor[MODEL]) { body =>
            complete(deleteCard_impl(msg, body))
          }
        }
      }
    }

  def deleteCard(msg: Message[Nothing], add: MODEL): Future[HttpResponse]

  val setDefault =
    path(cards / default / Segment) { cardId
      put {
        processIAM(ConsumerCategory, None, None, ROLES, SECURITY) { msg =>
          tokeniseEntity(lensFor[MODEL]) { body =>
            complete(setDefault_impl(msg, body))
          }
        }
      }
    }

  def setDefault(msg: Message[Nothing], add: MODEL): Future[HttpResponse]

}


package elastic

import zio._
import zio.console._
import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.http.{ ElasticClient, ElasticProperties }
import com.sksamuel.elastic4s.http.Response

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import com.sksamuel.elastic4s.http.bulk.BulkResponse
import com.sksamuel.elastic4s.http.search.SearchResponse

object EsZioApp extends App {

  def run(args: List[String]) =
    eff.foldM(_ => ZIO.dieMessage("Failed"), _ => ZIO.succeed(0))

  val eff = {
    // you must import the DSL to use the syntax helpers
    import com.sksamuel.elastic4s.http.ElasticDsl._

    val client = ElasticClient(ElasticProperties("http://localhost:9200"))

    val wrQuery = client.execute {
      bulk(
        indexInto("myindex" / "mytype").fields("country" -> "Mongolia", "capital" -> "Ulaanbaatar"),
        indexInto("myindex" / "mytype").fields("country" -> "Namibia", "capital"  -> "Windhoek")
      ).refresh(RefreshPolicy.WaitFor)
    }

    val rdQuery: Future[Response[SearchResponse]] = client.execute {
      search("myindex").matchQuery("capital", "ulaanbaatar")
    }

    def write(query: Future[Response[BulkResponse]])(implicit ec: ExecutionContext): Task[Response[BulkResponse]] =
      ZIO.fromFuture(ec => query)

    def read(query: Future[Response[SearchResponse]])(implicit ec: ExecutionContext): Task[Response[SearchResponse]] =
      ZIO.fromFuture(ec => query)

    // def read()
    val prog = for {
      _    <- write(wrQuery)
      resp <- read(rdQuery)
      _    <- putStrLn("Elastic Response") *> putStrLn(resp.result.hits.hits.head.sourceAsString)
      _    = client.close()
    } yield resp
    prog
  }
}

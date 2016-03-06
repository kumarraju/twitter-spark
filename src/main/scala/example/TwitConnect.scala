package example
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import TwitterConnection._
/**
 * @author Kumar
 */
object TwitConnect {
  def main(args: Array[String]) {

    // Checkpoint directory
    val checkpointDir = TwitterConnection.getCheckpointDirectory()

    // Configure Twitter credentials
    val apiKey = "yourapikey"
    val apiSecret = "apiSecret"
    val accessToken = "accessToken"
    val accessTokenSecret = "accessTokenSecret"
    TwitterConnection.configureTwitterCredentials(apiKey, apiSecret, accessToken, accessTokenSecret)

    val ssc = new StreamingContext(new SparkConf(), Seconds(1))
    val tweets = TwitterUtils.createStream(ssc, None)
    val statuses = tweets.map(status => status.getText())
    //statuses.print()
    ssc.checkpoint(checkpointDir)
    //Get the stream of hashtags from the stream of tweets
    val words = statuses.flatMap(status => status.split(" "))
    val hashtags = words.filter(word => word.startsWith("#"))
    //hashtags.print()
    //Count the hashtags over a 1 minute window
    val counts = hashtags.map(tag => (tag, 1))
      .reduceByKeyAndWindow(_ + _, _ - _, Seconds(60), Seconds(1))
    //Find the top 10 hashtags based on their counts
    val sortedCounts = counts.map { case (tag, count) => (count, tag) }
      .transform(rdd => rdd.sortByKey(false))
    sortedCounts.foreach(rdd =>
      println("\nTop 10 hashtags:\n" + rdd.take(10).mkString("\n")))
    ssc.start()
    ssc.awaitTermination()

  }
}


# twitter-spark
twitter twits analysis using apache spark streaming

1. Open twitter api page 
      https://apps.twitter.com/
2. create an application and fill the neccessary detials.
3. Once you have created the application, Click on API key button.
4. Edit TwitConnect.scala file and replace the following below entries wiht your detials. 
      apiKey = "yourapikey"
     apiSecret = "apiSecret"
     accessToken = "accessToken"
     accessTokenSecret = "accessTokenSecret"
5. Finally, the programe can be executed using given below commnad
     spark-submit --class example.TwitConnect Twits-0.0.1-SNAPSHOT-jar-with-dependencies.jar
    

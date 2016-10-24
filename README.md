# tweetmap
COMS 6998 Cloud Computing & Big Data HW1 TWEETMAP
============

This is a maven project with tomcat. I used servlet, ajax, javascript, JEST api, twitter4j api and googlemap api.

URL:
tweet.usjmkfet5p.us-west-2.elasticbeanstalk.com

JEST API is elastic search for java, to use it, you can either run elastic search on localhost or aws elastic search
Your elastic search url is http://localhost:9200 or AWS ES endpoint

The website is running twitter4j streaming api to upload tweets with locations to AWS elastic search in the back end.
The website refreshes the location markers every 10 seconds to show new tweets.

Problem Description
--------------------

Lets say a bot or a bad user is sending too much of HTTP traffic to your site. He is sending many http requests through some scripts or programs that eats up your server resources and bandwidth.

Solution
----------

The solution is being inspired from Java's own garbage collection. There are three LRUs, young, old and permanent LRU. Any request comes to your site, extract the user ip and user identifier and pass that to this library. Library will keep track of all new users in the young LRU. If the user is coming too often, then he will be promoted to yound generation and if he is coming really too often which is not possible by a normal human to come to your site, then he will be promoted to permanent LRU.

The best way to use this library is, develop a filter which extracts user ip and user identification and passes to TrackUser.trackUserIp(String ip, String userId) method. For any incoming user also check, if he is already blocked by using TrackUser.isThisAOffendingUser(String ip, String userId).

The threshold of how many requests user should do to promote him to Old generation from Young generation or exactly how many requests you should receive to promote him from old generation to permanent generation can be configured.

Check the test cases to know more about it.

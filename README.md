# TweetTrove
Twitter Android App

This is an Android application for displaying the authenticated user's timeline and letting him/her view images embedded within tweets. This app also supports retweeting and favoriting.


Time spent: 24 hours spent in total

## Completed required user stories:

* [X] User can sign in to Twitter using OAuth login (2 points)
* [X] User can view the tweets from their home timeline
        User should be displayed the username, name, and body for each tweet (2 points)
        User should be displayed the relative timestamp for each tweet "8m", "7h" (1 point)
        User can view more tweets as they scroll with infinite pagination (1 point)
* [X] User can compose a new tweet
        User can click a “Compose” icon in the Action Bar on the top right (1 point)
        User can then enter a new tweet and post this to twitter (2 points)
        User is taken back to home timeline with new tweet visible in timeline (1 point)

## All the following addditional stories have been implemented:

* [X] While composing a tweet, user can see a character counter with characters remaining for tweet out of 140 (1 point)
* [X] Links in tweets are clickable and will launch the web browser (see autolink) (1 point)
* [X] User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh) (1 point)
* [X] User can open the twitter app offline and see last loaded tweets
        Tweets are persisted into sqlite and can be displayed from the local DB (2 points)
* [X] User can tap a tweet to display a "detailed" view of that tweet (2 points)
* [X] User can select "reply" from detail view to respond to a tweet (1 point)
* [X] Improve the user interface and theme the app to feel "twitter branded" (1 to 5 points)
* [X] Stretch: User can see embedded image media within the tweet detail view (1 point)

* [X] Stretch: Compose activity is replaced with a modal overlay (2 points)
* [X] Stretch: Use Parcelable instead of Serializable using the popular Parceler library. (1 point)
* [X] Stretch: Apply the popular Butterknife annotation library to reduce view boilerplate. (1 point)
* [X] Stretch: Leverage RecyclerView as a replacement for the ListView and ArrayAdapter for all lists of tweets. (2 points)
* [X] Stretch: Move the "Compose" action to a FloatingActionButton instead of on the AppBar. (1 point)

* [X] Stretch: Leverage the data binding support module to bind data into one or more activity or fragment layout templates. (1 point)
* [X] Stretch: Replace Picasso with Glide for more efficient image rendering. (1 point)


## Extras completed:

* [X] Applied animation on the recycler view
* [X] USed Card View to improve the UI
* [X] Added support for retweeting and favoriting
* [X] Added Bottom Sheet for composing a new tweet
* [X] Used Rounded corners on images with Glide

Note:

I tried three different ways to get the embedded video to play but wasn't able to. My parsing logic seems correct and so I'm guessing that using the video player from within an adapter is not straight-forward. 


Here's a walkthrough of implemented user stories:

<img src='https://github.com/mhareendra/TweetTrove/blob/master/TT1.gif' width=' '/>


https://vimeo.com/177973475


## License

    Copyright [2016] [Hareendra Manuru]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

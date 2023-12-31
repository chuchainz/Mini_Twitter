﻿# Mini_Twitter

Program features:

1. There is a centralized admin control panel to create users and user groups.
2. A user has 1) an unique ID; 2) a list of user IDs that are following this user (followers); 3)
a list of user IDs being followed by this user (followings); 4) a news feed list containing a
list of Twitter messages.
3. A user group has an unique ID, which can be used to group users. A user group can
contain any number of users. The same user can only be included in one group. Of
course, a user group can contain other user groups recursively. There is always a root
group called Root to include everything.
4. Users can choose to follow other users (not user groups) by providing the target user ID.
Unfollow is not required.
5. Users can also post a short Tweet message (a String), so that all the followers can see
this message in their news feed lists. Of course, the user can also see his or her own
posted messages.
6. A few analysis features are needed in the admin control panel: 1) output the total
number of users; 2) output the total number of groups; 3) output the total number of
Tweet messages in all the users’ news feed; 4) output the percentage of the positive
Tweet messages in all the users’ news feed (the message containing positive words,
such as good, great, excellent, etc.) Free free to decide the positive words

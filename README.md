# Use Case

<img src = "https://github.com/junha3284/WalkingSchoolBus/blob/master/UseCaseDiagram.png" alt = "user case diagram" >

## Walking Group: Iteration 1

1. Craete Account, Log-in, Log-out,
    - A new user (a parent or a child) launches app and signs up using their name, email, and sets a password.
    Email addreses are used to uniquely identify accounts.
    - After logging in, if user closes app and relaunches it then the app remembers log-in info and automatically logs in.
    - Once logged in, a user can log-out. 
    While logged out, the user will not automatically be re-logged in if the app is restarted.

2. Manage Account Monitoring
The app supports one user (likely a parent) "monitoring" the account of another user (likely a child/student).
A user may have 0 or more users monitoring them, and they may monitor 0 or more other users.
    - User can list the users whom they monitor, and who monitors them.
    Should show at least the name and email of the other account.
    - User can remove anyone from the list of users whom they monitor, and who monitors them.
    - User can add another user to the set of users they monitor (add their child).
    The child account must already exist and is identified by its email address.
    - User can add another user to the set of users who monitor them (add their parent).
    The parent account must already exist and is identified by its email address.

3. Create, View, and Join a Walking Group
    - User can see on a map, centred on their GPS coordinates, walking groups around them.
    - User can create a new walking group by specifying a meeting place for the group and the destination.
     User gives the group a name once they create it.
    - User can select an existing walking group from the map and join it.
     User can join either themselves, or any user they monitor (their child) to the group.
    - User can leave any group they are part of, or remove a user they monitor (their child) from the group.
     User may be a member of any number of groups.

General Notes:
- UI must be in a separated package from the data "Model".
- Use a singleton pattern to allow all parts of the UI to access the model.
- From any screen, the Android back button must do the "reasonable" thing.
- Strings shown to the user must be in string.xml

----------------------------------------------------------------------------------------------------

## Walking Group: Iteration 2

1. User Information:
    - As a user of the system, I am able to see and edit my own information, all of which is optional, which includes: 
	    - name, birth year & birth month, address, 
	    - home phone, cell phone
	    - email, 
	    - current grade and teacher's name (which is meaningful only if I'm a student)
	    - emergency contact info (free text which may include person's name, relationship to 
	      user, email, phone numbers, etc. This is only meaningful if I'm a student.)

    - As a parent of a child using the system, I am able to see and edit the information of anyone I monitor (my children), which includes the same type of information as entered about me (see above)
	

2. Walking Groups
    - As a group leader, when I select a walking group I lead, I can see the names of users who are members in the walking group, plus for each of those members I can see more in-depth info on all users who monitor them. I use this to remember the names of kids in my walking group, plus look up contact info of their parents whenever I need to contact them (such as for a problem related to their child, or planning a weekend BBQ with all members of the walking group and their parents).
    - As a member of a walking group, when I select the group I am in I can see the names of users who are members in the walking group, plus for each of those members I can see more in-depth info on all users who monitor them. I use this to remember the names of other kids in my walking group, plus look up contact info of their parents whenever there is a problem and I need to contact them.
    - As a parent with children in walking groups, when I select the group which my child is a member of, I can see the names of users who are members in the walking group, plus for each of those members I can see more in-depth info on all users who monitor them. I use this to know the names of other kids my child is walking with, plus look up contact info of their parents whenever there is a problem and I need to contact them.

3. On Walk Features
    - As a member of a group, or a leader of a group, when I'm walking with the group I am able to start the app uploading my GPS location to the server. It uploads my location every 30 seconds so that those who monitor me (or who monitor members of my group if I am the leader) may know where I am. I am able to stop the uploading once I get to school, and the uploading automatically stops 10 minutes after I have reached school.
    - As a parent with a child in a walking group, I use the in-app Parent's Dashboard feature, which shows a map, to see the last reported location of my child and the leader as they walk with the group. The parent's dashboard also allows me to see all my children (users I monitor) at once so I can, at a glance, know they are safe and walking it to school as expected. 
    - As a parent using the parent's dashboard, for each user that I see on the map it indicates how long it has been since that user's latest location was updated. This helps me know if location data is still being received from the users.

4. In-App Messages
    - As a user, I am able to view any messages which have been sent to me. I can see which messages are read vs unread. From Parent's Dashboard I can always see how many new messages are waiting for me, and easily view such messages. App checks for new (or unread) messages every minute. This allows me to receive messages from my children, children in my group, or the leader of a group I'm a member of.
    - As the leader of a walking group, I am able able to send out broadcast messages to all members of my group, and those who monitor members of my group. These messages are just plain text. I may use this before we meet up to announce I am unable to go today (such as sick), or a reminder of something (such as bring an umbrella for the rain expected later). While on the walk I may use this to notify all parents of members of my group that there is an issue, such as we were unable to get to class due to police incident, or being chased by a dog.
    - As a child who walks with a walking group, when I am walking I am able to press a panic button and optionally enter a message (plain text) to indicate that I am having a problem on the walk (likely an emergency). This will notify all users who monitor me (my parents), plus the group leader of any groups I am in. It allows me to quickly report a problem so that I may receive help from them, or to let them know about an emergency. 
    - As a child who walks with a walking group, when I am walking I am able to send a message (plain text) to all users who monitor me (my parents) and the group leader of any groups I am in. This allows me to let them know of something such as I'm running late, the group never showed up, or some other non-emergency situation. This allows me to use the app to communicate with my parents, but not send an emergency panic message.
    
----------------------------------------------------------------------------------------------------

## Walking Group: Iteration 3

1. Gamification
    - As a student, I want the app to reward me for how often I walk with my group. Each time I complete a 
      walk with my group I earn one or more points (each team decides how to reward users with points). 
    - As a student (and possibly a group leader), I want to earn points each time I walk with my group. In 
      the app I can prominently see my current points that I can spend, and I can spend my points on in-app 
      rewards. In the app I can go look at my currently 'purchased' rewards, and up-coming rewards that I 
      could buy. Things I might want to spend points on include:
         * app customization, such as new backgrounds, new colour schemes, new in-app icons,
         * buying stickers in game to drag onto a virtual sticker book,
         * avatar icons and glorious titles like "dragon slayer" or "butterfly catcher",
         * or other ideas! 

   
    - As a user, I can view a leader board showing the total number of points each user has earned over the 
  lifetime of using the app (not just the number of points they currently have to spend). The leader board 
  shows the first name and first letter of the last name for each user, along with how many points the 
  user has earned over their lifetime of using the app. The leader board shows all users (not just ones in 
  my walking group), and is sorted from highest-points to lowest points. It is fine if the leader board 
  only shows the top 100 users (or so) if that makes more sense from an app design point of view.

    - As a developer, I can use the provided web-page to manually award points to users for testing (or TA 
  marking!) -- DONE FOR YOU!



2. Permissions
    - As a parent I want to approve or deny requests: 
       * to set my child as the leader of a walking group (likely when my child creates a group).
   

    - As a user, I can see all permission requests which have ever been sent to me. For each permission 
      request I can see the details of what action was being requested, and the status of that request 
      (approved, rejected, or pending). I can see which users approved or rejected the request.

In detail, when an API call is made to the server which would trigger an action which now requires 
permission, the server will create one Permission record.

    - A Permission record stores:
       - who requested the action
       - what action is being requested (X joining group Y, X being monitored by Y, etc).
       - status: accepted, rejected, pending
       - which users were asked to give permission for the action,
       - which users have accepted or rejected the request
       
    - A Permission record is status is:
       pending: if it is not yet accepted or rejected.
       rejected: if any of the users who are asked to give permission reject the request.
       accepted: if, for each set of users who need to grant permission, one user of that set grants 
       permission.
       
    - A Permission record may have multiple sets of users needing to give permission. For example, if 
      child A requests to monitor child B, then:
       - B will have to approve it, and
       - one of A's 'parents' will have to approve it, and
       - one of B's 'parents' will have to approve it.
    - Each user can see a list of Permission records which for which they were asked to give permission.
    - Once one user has accepted or rejected a request, no other users in that same user-set may accept 
      or reject the request. Though, they may still see the request for their reference.
    - In cases where nobody need be asked for permission (i.e., the current user, who initiated the 
      action, is sufficient for granting all required permissions) then the server completes the requested 
      change without generating a permission request or asking anyone (even the current user) for 
      permission.

   
   


EDGE CASES:
* Q. What happens to pending requests once the last user is removed from monitoring a user?
-> The list of who can approve/reject a request is fixed at the time the request is made. 
   Therefore, the user will still see the requests made before they stopped monitoring the user.
  
* Q. Won't this make it hard to test?
-> Yes!
   So there will be a special permission-override header on the HTTP messages to the server which tells 
   the server not to ask users for permission. This permission-override will be enabled by default so 
   that existing app functionality does not break. When your app wants to begin triggering permission 
   requests, you can explicitly disable the override by sending the required header to enable 
   permissions. 

   (Note that such a header would normally never be allowed on a production server; this is only for our 
   testing. Expected use of this override header is that, once your app supports permissions, you then 
   start sending the header on every message to the server to enable permissions. Your final submission 
   for iteration 3 must send the "enable permissions" header on every message.)
   
   
   
   
   
   
   
   
Permissions removed from this iteration:
- As a parent I want to approve or deny requests: 
   * for my child to join a walking group
   * to begin monitoring my child,
   * to remove a person from the set of people monitoring my child,
     (exception for a 'parent' to remove themselves from monitoring my child; the 'parent' can do this 
     themselves without additional permission.)
   * where my child would begin monitoring another user
   
- As a user, I want to protect my privacy so only people I trust can monitor me. I want to be able to 
  approve or deny requests:
   * to monitor me,
   * to add me to a walking group,
   * for me to monitor someone else.
   
- As a group leader I want to ensure that I have control over who join my group. I want to be able to 
  approve or deny requests to join my group.

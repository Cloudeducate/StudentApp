# StudentApp
Student app for school

## Pages/Views ##
- Login Page for Students/Parents
- Assignment Submssion
- Profile
- Grade Book
- Results
- Attendance
 
## API Request URL ##
### Login ###
POST Request: 

#### Headers ####
- X-Student-App: true

#### Parameters ####
- username
- password
- action: 'logmein'

On successful request you will receive json data 
```
/auth/login.json
```

### After Login ###
Foreach Request send the headers
- X-App: student
- X-Access-Token: {$token_stored}

To get successful response 


### Student Dashboard (after login) ###
```
/student.json
```

### Update Profile ###
POST Request parameter
- password
- action = saveUser
```
/student/profile.json
```

### View Assignments  ###
$course_id (optional parameter)

Can also send POST Request to get assignments for any particular course
Parameters
- course: {$course_id}

Otherwise assignments for all courses will be displayed
See the response

```
/student/assignments/{$course_id}.json
```
#### Viewing Assignment Response ####
```
/public/assets/uploads/assignments/filename
```

### View Assignment Result  ###
- $assignment_id: Id of the assignment whose result is to be viewed

This will return the Submission Model for the student who submitted the assignment

Important data:
- grade: Will contain the grade for the submission on a scale of 1 to 5 (best)
- remarks: Remarks given by the teacher
- live: (boolean) 1 => 'accepted', 0 => 'rejected'

```
/assignments/result/{$assignment_id}.json
```

### Submit Assignment ###
POST Request parameters
- response (Containing the response file) (Max file size for upload is 6MB)

GET Request JSON Response
- assignment: contain details about the assignment
- success: Only if the assignment is previously submitted by the student
- error: Only if last date of submission is over

```
/student/submitAssignment/{$assignment_id}.json
```

###  Result ###
POST Request parameter
- course (Containing the course_id of the subject for which result is needed)

Send a GET Request to the page to see the resposne
- $course_id (optional) Id of the course whose result you want to display
```
/student/result/{$course_id}.json
```

### Courses ###
Will give the courses student is studying currently
```
/student/courses.json
```

### Attendance ###
Send a get request to see the response

```
/student/attendance.json
```

### Performance ###
This API will give weekly performance analysis under 'performance' key contain
- course: Name of the course
- teacher: Teacher who is teaching the course
- tracking: [Array] of objects containing grade for each week

```
/student/performance/{$course_id}.json
```

### Events ###
Send a get request to see the response
```
/events/all.json
```

### Finding Conversations ###
This will find the conversations in which the student is there
- conversations: key will contain the different conversations
```
/conversation/find.json
```

### Message Conversation ###
Send a POST request with
- action: sendMessage
- message: Message user want to send in the conversation

```
/conversation/message/{$conversation_id}.json
```

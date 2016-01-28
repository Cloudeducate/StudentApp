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
http://cloudeducate.com/auth/login.json
```

### After Login ###
Foreach Request send the headers
- X-App: student
- X-Access-Token: {$token_stored}

To get successful response 


### Student Dashboard (after login) ###
```
http://cloudeducate.com/student.json
```

### Update Profile ###
POST Request parameter
- password
- action = saveUser
```
http://cloudeducate.com/student/profile.json
```

### View Assignments  ###
$course_id (optional parameter)

Can also send POST Request to get assignments for any particular course
Parameters
- course: {$course_id}

Otherwise assignments for all courses will be displayed
See the response

```
http://cloudeducate.com/student/assignments/{$course_id}.json
```
#### Viewing Assignment Response ####
```
http://cloudeducate.com/public/assets/uploads/assignments/filename
```

### View Assignment Result  ###
- $assignment_id: Id of the assignment whose result is to be viewed

This will return the Submission Model for the student who submitted the assignment

Important data:
- grade: Will contain the grade for the submission on a scale of 1 to 5 (best)
- remarks: Remarks given by the teacher
- live: (boolean) 1 => 'accepted', 0 => 'rejected'

```
http://cloudeducate.com/assignments/result/{$assignment_id}.json
```

### Submit Assignment ###
POST Request parameters
- response (Containing the response file) (Max file size for upload is 6MB)

GET Request JSON Response
- assignment: contain details about the assignment
- success: Only if the assignment is previously submitted by the student
- error: Only if last date of submission is over

```
http://cloudeducate.com/student/submitAssignment/{$assignment_id}.json
```

###  Result ###
POST Request parameter
- course (Containing the course_id of the subject for which result is needed)

Send a GET Request to the page to see the resposne
```
http://cloudeducate.com/student/result.json
```


### Courses ###
Will give the courses student is studying currently
```
http://cloudeducate.com/student/courses.json
```

### Attendance ###
Send a get request to see the response

```
http://cloudeducate.com/student/attendance.json
```

### Events ###
Send a get request to see the response
```
http://cloudeducate.com/events/all.json
```

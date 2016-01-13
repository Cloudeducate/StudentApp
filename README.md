# StudentApp
Student app for school

## Pages/Views ##
- Login Page for Students/Parents
- Assignment Submssion
- Profile
- Grade Book
- Results
 
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

### Student Dashboard (after login) ###
```
http://cloudeducate.com/student.json
```

### Update Profile ###
POST Request parameter
- name
- phone
- address
- city
- action = saveUser
```
http://cloudeducate.com/student/profile.json
```

### View Assignments  ###

```
http://cloudeducate.com/student/assignments.json
```

### Submit Assignment ###
POST Request parameters
- response (Containing the response file)
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

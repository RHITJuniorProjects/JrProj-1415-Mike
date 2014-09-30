from firebase import firebase
firebase = firebase.FirebaseApplication('https://henry371.firebaseio.com', None)
result = firebase.get('/user', None)
print result

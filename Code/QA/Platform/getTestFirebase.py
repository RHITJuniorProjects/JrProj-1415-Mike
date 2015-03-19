from firebase import firebase
import time
import smtplib

firebase = firebase.FirebaseApplication('https://henry-test.firebaseio.com/', None)
result = firebase.get('/default_categories', None)
f = open("C:\Python34\Scripts\getScriptOutput.txt", "a")
f.write("Monitor test for Firebase on: " + time.strftime("%m/%d/%Y") + " at " + time.strftime("%H:%M:%S") + "\n")
if (str(result) == "None"):
	f.write("Error monitoring the firebase: query came up empty.\n")
	msg = "The Monitor just detected that the firebase isn't running correctly."
	server = smtplib.SMTP('smtp.gmail.com:587')
	server.starttls()
	server.login("rhfirebase@gmail.com","rhfirebaset")
		
	server.sendmail("rhfirebase@gmail.com", "davidliu541557@gmail.com", msg)
	server.sendmail("rhfirebase@gmail.com", "watersdr@rose-hulman.edu", msg)
	server.sendmail("rhfirebase@gmail.com", "hullzr@rose-hulman.edu", msg)
	server.sendmail("rhfirebase@gmail.com", "maderwc@rose-hulman.edu", msg)

	server.quit()
else:
	f.write(str(result))
	f.write('\n')
f.close()
	


#valid login check
#android is checking to make sure password is longer than 4 but
#they seem to be the only ones checking for that
#so before I ran this script, I had to alter their valid login check
click("1413941604021.png")
type("test2@test.com" + Key.TAB + "test")
click("1415303013465.png")
wait(10)
if exists("1415306687711.png"):
    print("Login Successful")
else:
    print("ERROR: Login Unsuccessful")

#logout sheck
click("1415306104147.png")
click("1415306124687.png")
wait(5)
if exists("1415306242470.png"):
    print("Logout successful")
else:
    print("ERROR: Logout Unsuccessful")

#check login with invalid user
click("1413955218009.png")
type("foo@foo.com" + Key.TAB + "foo11")

click("1415303013465.png") 
#sign in button
wait(5)

if exists("1415306435661.png"):
    print("Invalid credentials check successful")
else:
    print("ERROR: Invalid credentials check unsuccessful")
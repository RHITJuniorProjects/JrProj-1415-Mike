#valid login check
click("1413941604021.png")
type("t@g.com" + Key.TAB + "test1")
click("1413955651324.png")
sleep(10)
if exists("1413943908924.png"):
    print("Login Successful")
else:
    print("ERROR: Login Unsuccessful")

#logout sheck
click("1413954886338.png")
click("1413954932916.png")
if exists("1413955008480.png"):
    print("Logout successful")
else:
    print("ERROR: Logout Unsuccessful")

#check login with invalid user
click("1413955218009.png")
type("foo@foo.com" + Key.TAB + "foo11")
click("1413955301527.png")

if exists("1413955388815.png"):
    print("Invalid credentials check successful")
else:
    print("ERROR: Invalid credentials check unsuccessful")
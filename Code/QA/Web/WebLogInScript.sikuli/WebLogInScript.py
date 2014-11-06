#valid login check
click("1413862843952.png")
wait(.5)        
click("1413862727594.png")
type("henry-test.firebaseapp.com" + Key.ENTER)
click("1415306981594.png")
wait(5)
click("1415307005957.png")
type("test2@test.com" + Key.TAB + "test")
click("1415307023207.png")
if exists("1413863276230.png"):
    print("Log in successful")
else:
    print("ERROR: Log in unsuccessful")

#logout check
click("1413863573148.png")
if exists("1415306981594.png"):
    print("Logout successful")
else:
    print("ERROR: Logout unsuccessful")

#invalid login check
click("1413862843952.png")
wait(.5)        
click("1413862727594.png")
type("henry-test.firebaseapp.com" + Key.ENTER)
click("1415306981594.png")
wait(5)
click("1415307005957.png")
type("foo@foo.com" + Key.TAB + "foofoofoo")
click("1415307023207.png")
wait(1)
if exists("1415307378784.png"):
    print("Invalid credentials check successful")
else:
    print("ERROR: Invalid credentials check unsuccessful")